package com.example.payrollsystemmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class PayslipGeneratorController {

    @FXML private ChoiceBox<String> cbox;
    @FXML private Label empIdLabel;
    @FXML private Label grossLabel;
    @FXML private Label deductionsLabel;
    @FXML private Label netLabel;

    private final ObservableList<String> employeeNames = FXCollections.observableArrayList();

    private int currentEmployeeId;
    private double currentSalary;
    private int currentWorkingHours;
    private int overtimeHours = 10; // You can modify this value or get it from the user

    @FXML
    public void initialize() {
        loadEmployeeNames();

        cbox.setOnAction(event -> {
            String selected = cbox.getValue();
            if (selected != null) {
                loadPayslipData(selected);
            }
        });
    }

    private void loadEmployeeNames() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM employees");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employeeNames.add(rs.getString("name"));
            }
            cbox.setItems(employeeNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPayslipData(String name) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT employee_id, salary, working_hours FROM employees WHERE name = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentEmployeeId = rs.getInt("employee_id");
                    currentSalary = rs.getDouble("salary");
                    currentWorkingHours = rs.getInt("working_hours");

                    double hourlyRate = currentSalary / currentWorkingHours;
                    double overtimePay = overtimeHours * hourlyRate * 1.5;
                    double gross = currentSalary + overtimePay;
                    double tax = gross * 0.10;
                    double insurance = gross * 0.03;
                    double deductions = tax + insurance;
                    double net = gross - deductions;

                    // Display
                    empIdLabel.setText(String.valueOf(currentEmployeeId));
                    grossLabel.setText(String.format("R%.2f", gross));
                    deductionsLabel.setText(String.format("R%.2f", deductions));
                    netLabel.setText(String.format("R%.2f", net));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrint() {
        if (empIdLabel.getText().isEmpty()) {
            showAlert("Error", "Please select an employee.");
            return;
        }

        try {
            double hourlyRate = currentSalary / currentWorkingHours;
            double overtimePay = overtimeHours * hourlyRate * 1.5;
            double gross = currentSalary + overtimePay;
            double tax = gross * 0.10;
            double insurance = gross * 0.03;
            double deductions = tax + insurance;
            double net = gross - deductions;

            String month = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO salary (employee_id, overtime_hours, overtime_pay, tax_deductions, insurance_deductions, gross_salary, net_salary, salary_month) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, currentEmployeeId);
                    stmt.setInt(2, overtimeHours);
                    stmt.setDouble(3, overtimePay);
                    stmt.setDouble(4, tax);
                    stmt.setDouble(5, insurance);
                    stmt.setDouble(6, gross);
                    stmt.setDouble(7, net);
                    stmt.setString(8, month);

                    stmt.executeUpdate();
                }

                showAlert("Success", "Payslip saved to 'salary' table.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save payslip: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
