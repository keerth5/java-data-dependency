// @testdata/DynamicSqlSecurityTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑111: DynamicSqlSecurity
 */
public class DynamicSqlSecurityTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Violation: unsafe dynamic SQL construction with user input
    public void searchUsersByKeyword(String keyword) {
        String sql = "SELECT * FROM users WHERE name LIKE '%" + keyword + "%'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("User: " + rs.getString("name") + ", " + rs.getString("email"));
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }

    // Non-violation: using parameterized query prevents SQL injection
    public void safeSearchUsersByKeyword(String keyword) {
        String sql = "SELECT * FROM users WHERE name LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("User: " + rs.getString("name") + ", " + rs.getString("email"));
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }

    // Violation: building dynamic update statement insecurely
    public void updateUserStatus(String username, String status) {
        String sql = "UPDATE users SET status = '" + status + "' WHERE name = '" + username + "'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("SQL exception during update: " + e.getMessage());
        }
    }

    // Non-violation: parameterized update query
    public void safeUpdateUserStatus(String username, String status) {
        String sql = "UPDATE users SET status = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception during safe update: " + e.getMessage());
        }
    }
}
