package com.example.payrollsystemmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;

public class SalaryCalculationController {

    @FXML private ChoiceBox<String> choose;
    @FXML private TextField employeeField;
    @FXML private TextField basicSalaryField;
    @FXML private TextField workingHoursField;
    @FXML private TextField overtimeHoursField;
    @FXML private Label grossSalaryLabel;
    @FXML private Label deductionsLabel;
    @FXML private Label netSalaryLabel;

    private final ObservableList<String> employeeNames = FXCollections.observableArrayList();

    // For saving
    private String selectedEmployeeName = "";
    private double grossSalary = 0;
    private double totalDeductions = 0;
    private double netSalary = 0;
    private int overtimeHours = 0;

    @FXML
    public void initialize() {
        loadEmployeeNames();

        choose.setOnAction(event -> {
            selectedEmployeeName = choose.getValue();
            if (selectedEmployeeName != null) {
                loadEmployeeDetails(selectedEmployeeName);
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
            choose.setItems(employeeNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeDetails(String name) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT employee_id, salary, working_hours FROM employees WHERE name = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    employeeField.setText(rs.getString("employee_id"));
                    basicSalaryField.setText(String.valueOf(rs.getDouble("salary")));
                    workingHoursField.setText(String.valueOf(rs.getInt("working_hours")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCalculate(ActionEvent event) {
        try {
            double basic = Double.parseDouble(basicSalaryField.getText());
            int hours = Integer.parseInt(workingHoursField.getText());
            overtimeHours = Integer.parseInt(overtimeHoursField.getText());

            double hourlyRate = basic / hours;
            double overtimePay = overtimeHours * hourlyRate * 1.5;

            grossSalary = basic + overtimePay;
            double tax = grossSalary * 0.10;
            double insurance = grossSalary * 0.03;
            totalDeductions = tax + insurance;
            netSalary = grossSalary - totalDeductions;

            grossSalaryLabel.setText(String.format("Gross Salary: R%.2f", grossSalary));
            deductionsLabel.setText(String.format("Deductions: R%.2f", totalDeductions));
            netSalaryLabel.setText(String.format("Net Salary: R%.2f", netSalary));

        } catch (NumberFormatException e) {
            grossSalaryLabel.setText("Invalid input.");
            deductionsLabel.setText("");
            netSalaryLabel.setText("");
        }
    }

    @FXML
    private void saveToTextFile() {
        try {
            String empId = employeeField.getText();
            String basic = basicSalaryField.getText();
            String hours = workingHoursField.getText();

            // Define folder location
            String userDir = System.getProperty("user.home");
            File folder = new File(userDir, "payslips");
            if (!folder.exists()) folder.mkdirs();

            // File name
            File file = new File(folder, "Payslip_" + empId + ".txt");

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("====== EMPLOYEE PAYSLIP ======");
                writer.println("Employee ID: " + empId);
                writer.println("Employee Name: " + selectedEmployeeName);
                writer.println("Basic Salary: " + basic);
                writer.println("Working Hours: " + hours);
                writer.println("Overtime Hours: " + overtimeHours);
                writer.println("-----------------------------");
                writer.println("Gross Salary: R" + String.format("%.2f", grossSalary));
                writer.println("Deductions: R" + String.format("%.2f", totalDeductions));
                writer.println("Net Salary: R" + String.format("%.2f", netSalary));
                writer.println("-----------------------------");
                writer.println("Generated on: " + LocalDateTime.now());
            }

            showAlert("Success", "Payslip saved to:\n" + file.getAbsolutePath());
        } catch (Exception e) {
            showAlert("Error", "Failed to save payslip:\n" + e.getMessage());
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
