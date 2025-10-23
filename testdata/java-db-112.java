// @testdata/StringConcatenationSqlRiskTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑112: StringConcatenationSqlRisk
 */
public class StringConcatenationSqlRiskTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Violation: string concatenation in SELECT query
    public void getUserByName(String username) {
        String sql = "SELECT * FROM users WHERE name = '" + username + "'";
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

    // Non-violation: using parameterized query prevents injection
    public void getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("User: " + rs.getString("name") + ", " + rs.getString("email"));
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }

    // Violation: string concatenation in UPDATE query
    public void updateUserEmail(String username, String newEmail) {
        String sql = "UPDATE users SET email = '" + newEmail + "' WHERE name = '" + username + "'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("SQL exception during update: " + e.getMessage());
        }
    }

    // Non-violation: parameterized UPDATE query
    public void safeUpdateUserEmail(String username, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newEmail);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception during safe update: " + e.getMessage());
        }
    }
}
