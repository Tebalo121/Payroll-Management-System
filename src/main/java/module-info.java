module com.example.payrollsystemmanagement {  // Changed module name
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.payrollsystemmanagement to javafx.fxml;
    opens com.example.payrollsystemmanagement.models to javafx.fxml;

    exports com.example.payrollsystemmanagement;
    exports com.example.payrollsystemmanagement.models;
}