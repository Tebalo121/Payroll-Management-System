package com.example.payrollsystemmanagement;

import com.example.payrollsystemmanagement.DatabaseConnection;
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

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button registerButton;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        // Only Admin and Employee roles as requested
        roleComboBox.getItems().addAll("Admin", "Employee");
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        // Validation checks
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long");
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Error", "Please enter a valid email address");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if username or email already exists
            if (userExists(conn, "username", username)) {
                showAlert("Error", "Username already exists!");
                return;
            }

            if (userExists(conn, "email", email)) {
                showAlert("Error", "Email already exists!");
                return;
            }

            // Insert new user
            String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password); // In production, hash this password
                stmt.setString(4, role);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Success", "Registration successful!");
                    goToLogin();
                } else {
                    showAlert("Error", "Registration failed!");
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean userExists(Connection conn, String column, String value) throws SQLException {
        String sql = "SELECT " + column + " FROM users WHERE " + column + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @FXML
    public void goToLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/payrollsystemmanagement/login.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}