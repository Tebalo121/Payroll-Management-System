package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private StackPane contentPane;

    // Home Section
    @FXML private VBox homeContent;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Admin");
        showHome();
    }

    private void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }

    @FXML
    private void showHome() {
        setContent(homeContent);
    }

    @FXML
    private void showEmployeeManagement() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/employee_management.fxml"));
            setContent(root);
        } catch (IOException e) {
            showError("Failed to load Employee Management UI.");
        }
    }

    @FXML
    private void showSalaryCalculation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/salary_calculation.fxml"));
            setContent(root);
        } catch (IOException e) {
            showError("Failed to load Salary Calculation UI.");
        }
    }

    @FXML
    private void showPayslipGenerator() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/payslip_generator.fxml"));
            setContent(root);
        } catch (IOException e) {
            showError("Failed to load Payslip Generator UI.");
        }
    }

    @FXML
    private void showReports() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/payroll_reports.fxml"));
            setContent(root);
        } catch (IOException e) {
            showError("Failed to load Payroll Reports UI.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/login.fxml"));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showError("Logout failed: unable to return to login screen.");
        }
    }

    private void showError(String message) {
        System.out.println("ERROR: " + message);
    }
}