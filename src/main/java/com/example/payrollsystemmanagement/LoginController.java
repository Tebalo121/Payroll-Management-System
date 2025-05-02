package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    public void initialize() {
        roleComboBox.getItems().clear();
        roleComboBox.getItems().addAll("Admin", "Employee");
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert("Error", "Please fill in all login fields.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT password, role FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String userRole = rs.getString("role");

                if (!password.equals(storedPassword)) {
                    showAlert("Error", "Incorrect password.");
                    return;
                }

                if (!selectedRole.equalsIgnoreCase(userRole)) {
                    showAlert("Error", "Role mismatch. Please select the correct role.");
                    return;
                }

                // Login success
                redirectToDashboard(userRole);

            } else {
                showAlert("Error", "User not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "SQL Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("UI Error", "Failed to load dashboard: " + e.getMessage());
        }
    }

    private void redirectToDashboard(String role) throws IOException {
        String fxmlFile;
        if (role.equalsIgnoreCase("Admin")) {
            fxmlFile = "/com/example/payrollsystemmanagement/AdminDashboard.fxml";
        } else {
            fxmlFile = "/com/example/payrollsystemmanagement/EmployeeDashboard.fxml";
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(role + " Dashboard");
        stage.show();
    }

    @FXML
    private void goToRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/register.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("UI Error", "Failed to load registration page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}