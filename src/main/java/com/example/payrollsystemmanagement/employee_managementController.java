package com.example.payrollsystemmanagement;

import com.example.payrollsystemmanagement.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class employee_managementController {

    @FXML private TableView<Employee> view;
    @FXML private TableColumn<Employee, String> empid;
    @FXML private TableColumn<Employee, String> namec;
    @FXML private TableColumn<Employee, String> Department;
    @FXML private TableColumn<Employee, String> pss;
    @FXML private TableColumn<Employee, Double> salary;
    @FXML private TableColumn<Employee, Integer> hrs;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        empid.setCellValueFactory(cell -> cell.getValue().idProperty());
        namec.setCellValueFactory(cell -> cell.getValue().nameProperty());
        Department.setCellValueFactory(cell -> cell.getValue().departmentProperty());
        pss.setCellValueFactory(cell -> cell.getValue().positionProperty());
        salary.setCellValueFactory(cell -> cell.getValue().salaryProperty().asObject());
        hrs.setCellValueFactory(cell -> cell.getValue().workingHoursProperty().asObject());

        view.setItems(employeeList);
        loadEmployees();
    }

    @FXML
    private void handleAdd() {
        Employee newEmployee = showEmployeeDialog(null);
        if (newEmployee != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO employees (employee_id, name, department, position, salary, working_hours) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newEmployee.getId());
                stmt.setString(2, newEmployee.getName());
                stmt.setString(3, newEmployee.getDepartment());
                stmt.setString(4, newEmployee.getPosition());
                stmt.setDouble(5, newEmployee.getSalary());
                stmt.setInt(6, newEmployee.getWorkingHours());
                stmt.executeUpdate();
                loadEmployees();
            } catch (SQLException e) {
                showError("Error adding employee: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdate() {
        Employee selected = view.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an employee to update.");
            return;
        }

        Employee updated = showEmployeeDialog(selected);
        if (updated != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "UPDATE employees SET name=?, department=?, position=?, salary=?, working_hours=? WHERE employee_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, updated.getName());
                stmt.setString(2, updated.getDepartment());
                stmt.setString(3, updated.getPosition());
                stmt.setDouble(4, updated.getSalary());
                stmt.setInt(5, updated.getWorkingHours());
                stmt.setString(6, updated.getId());
                stmt.executeUpdate();
                loadEmployees();
            } catch (SQLException e) {
                showError("Error updating employee: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDelete() {
        Employee selected = view.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an employee to delete");
            return;
        }

        // Confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Employee");
        confirmation.setContentText("Are you sure you want to delete " + selected.getName() + "?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    // Start transaction
                    conn.setAutoCommit(false);

                    try {
                        // First delete related salary records
                        String deleteSalarySQL = "DELETE FROM salary WHERE employee_id = ?";
                        try (PreparedStatement salaryStmt = conn.prepareStatement(deleteSalarySQL)) {
                            salaryStmt.setString(1, selected.getId());
                            salaryStmt.executeUpdate();
                        }

                        // Then delete employee
                        String deleteEmployeeSQL = "DELETE FROM employees WHERE employee_id = ?";
                        try (PreparedStatement employeeStmt = conn.prepareStatement(deleteEmployeeSQL)) {
                            employeeStmt.setString(1, selected.getId());
                            int affectedRows = employeeStmt.executeUpdate();

                            if (affectedRows > 0) {
                                conn.commit();
                                employeeList.remove(selected);
                                showAlert("Success", "Employee deleted successfully");
                            } else {
                                conn.rollback();
                                showAlert("Error", "Failed to delete employee");
                            }
                        }
                    } catch (SQLException e) {
                        conn.rollback();
                        showAlert("Database Error", "Error deleting employee: " + e.getMessage());
                    } finally {
                        conn.setAutoCommit(true);
                    }
                } catch (SQLException e) {
                    showAlert("Database Error", "Could not connect to database: " + e.getMessage());
                }
            }
        });


        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM employees WHERE employee_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selected.getId());
            stmt.executeUpdate();
            loadEmployees();
        } catch (SQLException e) {
            showError("Error deleting employee: " + e.getMessage());
        }
    }

    private void showAlert(String noSelection, String s) {
    }

    private void loadEmployees() {
        employeeList.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            while (rs.next()) {
                employeeList.add(new Employee(
                        rs.getString("employee_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getInt("working_hours")
                ));
            }
        } catch (SQLException e) {
            showError("Error loading employees: " + e.getMessage());
        }
    }

    private Employee showEmployeeDialog(Employee prefill) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/payrollsystemmanagement/employee_dialog.fxml"));
            Parent root = loader.load();

            EmployeeDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(prefill == null ? "Add Employee" : "Edit Employee");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            controller.setDialogStage(dialogStage);

            if (prefill != null) {
                controller.prefillForm(prefill);
            }

            dialogStage.showAndWait();
            return controller.getResultEmployee();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
    }
}