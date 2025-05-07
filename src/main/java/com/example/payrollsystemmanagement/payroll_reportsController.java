package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class payroll_reportsController {

    @FXML
    private BarChart<String, Number> employeeBarChart;

    @FXML
    private PieChart salaryPieChart;
    

        @FXML
        private BarChart<String, Number> salaryChart;

        @FXML
        private LineChart<String, Number> netSalaryLineChart;

        

    @FXML
    private LineChart<String, Number> salaryLineChart;

    @FXML
    public void initialize() {
        loadEmployeeBarChart();
        loadSalaryPieChart();
        loadSalaryLineChart();
    }

    private void loadSalaryLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Net Salary Trend");

        String query = "SELECT salary_month, SUM(net_salary) AS total_salary " +
                "FROM salary GROUP BY salary_month ORDER BY salary_month";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("salary_month");
                double total = rs.getDouble("total_salary");
                series.getData().add(new XYChart.Data<>(month, total));
            }

            netSalaryLineChart.getData().clear();
            netSalaryLineChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeBarChart() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name, salary FROM employees");
             ResultSet rs = stmt.executeQuery()) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Employee Salary");

            while (rs.next()) {
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");
                series.getData().add(new XYChart.Data<>(name, salary));
            }

            employeeBarChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSalaryPieChart() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT e.name, s.net_salary FROM salary s JOIN employees e ON s.employee_id = e.employee_id");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double netSalary = rs.getDouble("net_salary");
                salaryPieChart.getData().add(new PieChart.Data(name, netSalary));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNetSalaryTrend() {
        String query = "SELECT salary_month, SUM(net_salary) AS total FROM salary GROUP BY salary_month ORDER BY salary_month";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Net Salary");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("salary_month");
                double totalNet = rs.getDouble("total");
                series.getData().add(new XYChart.Data<>(month, totalNet));
            }

            netSalaryLineChart.getData().clear();
            netSalaryLineChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}