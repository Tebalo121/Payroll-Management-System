package com.example.payrollsystemmanagement;

import javafx.fxml.FXML;
import javafx.scene.chart.*;

public class payroll_reportsController {

    @FXML private BarChart<String, Number> barChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private PieChart pieChart;

    @FXML
    public void initialize() {
        setupBarChart();
        setupLineChart();
        setupPieChart();
    }

    private void setupBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Departments");
        series.getData().add(new XYChart.Data<>("HR", 30000));
        series.getData().add(new XYChart.Data<>("IT", 45000));
        series.getData().add(new XYChart.Data<>("Sales", 35000));
        barChart.getData().add(series);
    }

    private void setupLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2025");
        series.getData().add(new XYChart.Data<>("Jan", 120000));
        series.getData().add(new XYChart.Data<>("Feb", 100000));
        series.getData().add(new XYChart.Data<>("Mar", 140000));
        series.getData().add(new XYChart.Data<>("Apr", 110000));
        lineChart.getData().add(series);
    }

    private void setupPieChart() {
        pieChart.getData().addAll(
                new PieChart.Data("IT", 40),
                new PieChart.Data("HR", 25),
                new PieChart.Data("Sales", 35)
        );
    }
}
