package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EmployeeDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TextField nameField;
    @FXML private Label outputLabel;
    @FXML private StackPane contentPane;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome!");
    }

    @FXML
    private void handleLogout() {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/payrollsystemmanagement/login.fxml"));
            Parent loginRoot = loader.load();

            // Create a new stage for login
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();

            // Close the current dashboard window
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load login screen: " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void showPayrollInfo() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            outputLabel.setText("Please enter your name.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT s.salary_month, s.gross_salary, s.tax_deductions, s.insurance_deductions, s.net_salary " +
                    "FROM salary s " +
                    "JOIN employees e ON s.employee_id = e.employee_id " +
                    "WHERE e.name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                StringBuilder sb = new StringBuilder("Salary Info:\n");
                do {
                    sb.append("Month: ").append(rs.getString("salary_month")).append("\n")
                            .append("Gross Salary: ").append(rs.getDouble("gross_salary")).append("\n")
                            .append("Tax Deductions: ").append(rs.getDouble("tax_deductions")).append("\n")
                            .append("Insurance Deductions: ").append(rs.getDouble("insurance_deductions")).append("\n")
                            .append("Net Salary: ").append(rs.getDouble("net_salary")).append("\n\n");
                } while (rs.next());

                outputLabel.setText(sb.toString());
            } else {
                outputLabel.setText("No salary records found for: " + name);
            }
        } catch (SQLException e) {
            outputLabel.setText("Error loading salary data: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewPayslip() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            outputLabel.setText("Please enter your name.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT s.salary_month, s.net_salary " +
                    "FROM salary s " +
                    "JOIN employees e ON s.employee_id = e.employee_id " +
                    "WHERE e.name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                StringBuilder sb = new StringBuilder("Payslip Info:\n");
                do {
                    sb.append("Month: ").append(rs.getString("salary_month")).append("\n")
                            .append("Net Salary: ").append(rs.getDouble("net_salary")).append("\n\n");
                } while (rs.next());

                outputLabel.setText(sb.toString());
            } else {
                outputLabel.setText("No payslip found for: " + name);
            }
        } catch (SQLException e) {
            outputLabel.setText("Error loading payslip: " + e.getMessage());
        }
    }
}
