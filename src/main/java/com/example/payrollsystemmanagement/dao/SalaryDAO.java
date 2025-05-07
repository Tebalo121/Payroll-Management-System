// SalaryDAO.java
package com.example.payrollsystemmanagement.dao;

import com.example.payrollsystemmanagement.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SalaryDAO {
    public Map<String, Double> getMonthlyTotals(LocalDate start, LocalDate end) throws SQLException {
        Map<String, Double> totals = new HashMap<>();
        String query = "SELECT DATE_FORMAT(month, '%Y-%m'), SUM(net_salary) " +
                "FROM salary " +
                "WHERE month BETWEEN ? AND ? " +
                "GROUP BY DATE_FORMAT(month, '%Y-%m')";

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