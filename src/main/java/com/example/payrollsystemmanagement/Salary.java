package com.example.payrollsystemmanagement;

import javafx.beans.property.*;

public class Salary {

    private final IntegerProperty salaryId;
    private final StringProperty employeeId;
    private final IntegerProperty overtimeHours;
    private final DoubleProperty overtimePay;
    private final DoubleProperty deductions;
    private final DoubleProperty grossSalary;
    private final DoubleProperty netSalary;
    private final StringProperty salaryMonth;

    public Salary(int salaryId, String employeeId, int overtimeHours, double overtimePay,
                  double deductions, double grossSalary, double netSalary, String salaryMonth) {
        this.salaryId = new SimpleIntegerProperty(salaryId);
        this.employeeId = new SimpleStringProperty(employeeId);
        this.overtimeHours = new SimpleIntegerProperty(overtimeHours);
        this.overtimePay = new SimpleDoubleProperty(overtimePay);
        this.deductions = new SimpleDoubleProperty(deductions);
        this.grossSalary = new SimpleDoubleProperty(grossSalary);
        this.netSalary = new SimpleDoubleProperty(netSalary);
        this.salaryMonth = new SimpleStringProperty(salaryMonth);
    }

    // Property Getters
    public IntegerProperty salaryIdProperty() { return salaryId; }
    public StringProperty employeeIdProperty() { return employeeId; }
    public IntegerProperty overtimeHoursProperty() { return overtimeHours; }
    public DoubleProperty overtimePayProperty() { return overtimePay; }
    public DoubleProperty deductionsProperty() { return deductions; }
    public DoubleProperty grossSalaryProperty() { return grossSalary; }
    public DoubleProperty netSalaryProperty() { return netSalary; }
    public StringProperty salaryMonthProperty() { return salaryMonth; }

    // Value Getters and Setters
    public int getSalaryId() { return salaryId.get(); }
    public void setSalaryId(int id) { this.salaryId.set(id); }

    public String getEmployeeId() { return employeeId.get(); }
    public void setEmployeeId(String id) { this.employeeId.set(id); }

    public int getOvertimeHours() { return overtimeHours.get(); }
    public void setOvertimeHours(int hours) { this.overtimeHours.set(hours); }

    public double getOvertimePay() { return overtimePay.get(); }
    public void setOvertimePay(double pay) { this.overtimePay.set(pay); }

    public double getDeductions() { return deductions.get(); }
    public void setDeductions(double d) { this.deductions.set(d); }

    public double getGrossSalary() { return grossSalary.get(); }
    public void setGrossSalary(double gross) { this.grossSalary.set(gross); }

    public double getNetSalary() { return netSalary.get(); }
    public void setNetSalary(double net) { this.netSalary.set(net); }

    public String getSalaryMonth() { return salaryMonth.get(); }
    public void setSalaryMonth(String month) { this.salaryMonth.set(month); }
}