package com.example.payrollsystemmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

public class AdminHomeDashboardController {

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label pendingRequestsLabel;

    @FXML
    private TableView<String> homeActivityTable;

    @FXML
    private TableColumn<String, String> activityColumn;

    @FXML
    public void initialize() {

        activityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        // Set example data
        setTotalUsers(125);
        setPendingRequests(8);

        ObservableList<String> sampleActivities = FXCollections.observableArrayList(
                "User Tebalo Ts'ehla  added a new employee record",
                "Payroll processed for March 2025",
                "Employee Jane Smith updated profile",
                "System backup completed successfully",
                "New report generated: Monthly Payroll Summary"
        );
        setRecentActivities(sampleActivities);
    }

    public void setTotalUsers(int totalUsers) {
        totalUsersLabel.setText(String.valueOf(totalUsers));
    }

    public void setPendingRequests(int pendingRequests) {
        pendingRequestsLabel.setText(String.valueOf(pendingRequests));
    }

    public void setRecentActivities(ObservableList<String> activities) {
        homeActivityTable.setItems(activities);
    }
}
