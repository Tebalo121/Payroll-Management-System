package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private StackPane contentPane;

    public void setWelcomeMessage(String username) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + username + "!");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showProfile() {
        showAlert("Info", "Profile view will be shown here.");
    }

    @FXML
    private void showAttendance() {
        showAlert("Info", "Attendance view will be shown here.");
    }

    @FXML
    private void showPayrollInfo() {
        showAlert("Info", "Payroll info will be shown here.");
    }

    @FXML
    private void showNotifications() {
        showAlert("Info", "Notifications will be shown here.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
