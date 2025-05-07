package com.example.payrollsystemmanagement;

import javafx.beans.property.*;

public class Employee {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty department;
    private final StringProperty position;
    private final DoubleProperty salary;
    private final IntegerProperty workingHours;

    public Employee(String id, String name, String department, String position, double salary, int hours) {
        this.id = new SimpleStringProperty(id); // Fixed: Properly initialize ID
        this.name = new SimpleStringProperty(name);
        this.department = new SimpleStringProperty(department);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.workingHours = new SimpleIntegerProperty(hours);
    }

    // Property Getters
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty positionProperty() { return position; }
    public DoubleProperty salaryProperty() { return salary; }
    public IntegerProperty workingHoursProperty() { return workingHours; }

    // Value Getters and Setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getDepartment() { return department.get(); }
    public void setDepartment(String department) { this.department.set(department); }

    public String getPosition() { return position.get(); }
    public void setPosition(String position) { this.position.set(position); }

    public double getSalary() { return salary.get(); }
    public void setSalary(double salary) { this.salary.set(salary); }

    public int getWorkingHours() { return workingHours.get(); }
    public void setWorkingHours(int hours) { this.workingHours.set(hours); }
}
