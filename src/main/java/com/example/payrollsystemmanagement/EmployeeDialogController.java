package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmployeeDialogController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField depField;
    @FXML private TextField posField;
    @FXML private TextField salaryField;
    @FXML private TextField hoursField;

    private Employee resultEmployee;
    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public Employee getResultEmployee() {
        return resultEmployee;
    }

    @FXML
    private void handleSave() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            String department = depField.getText();
            String position = posField.getText();
            double salary = Double.parseDouble(salaryField.getText());
            int hours = Integer.parseInt(hoursField.getText());

            resultEmployee = new Employee(id, name, department, position, salary, hours);
            dialogStage.close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values for salary and hours.").showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        resultEmployee = null;
        dialogStage.close();
    }

    public void prefillForm(Employee employee) {
        idField.setText(employee.getId());
        nameField.setText(employee.getName());
        depField.setText(employee.getDepartment());
        posField.setText(employee.getPosition());
        salaryField.setText(String.valueOf(employee.getSalary()));
        hoursField.setText(String.valueOf(employee.getWorkingHours()));
    }
}