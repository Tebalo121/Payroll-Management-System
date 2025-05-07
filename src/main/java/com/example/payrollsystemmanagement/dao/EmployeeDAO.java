// EmployeeDAO.java
package com.example.payrollsystemmanagement.dao;

import com.example.payrollsystemmanagement.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDAO {
    public Map<String, Double> getDepartmentSalaryTotals(LocalDate start, LocalDate end) throws SQLException {
        Map<String, Double> totals = new HashMap<>();
        String query = "SELECT e.department, SUM(s.net_salary) " +
                "FROM employees e " +
                "JOIN salary s ON e.id = s.employee_id " +
                "WHERE s.month BETWEEN ? AND ? " +
                "GROUP BY e.department";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                totals.put(rs.getString(1), rs.getDouble(2));
            }
        }
        return totals;
    }
}