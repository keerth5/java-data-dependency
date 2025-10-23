// @testdata/ResourceCleanupUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑107: ResourceCleanupUsage
 */
public class ResourceCleanupUsageTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Non-violation: proper try-with-resources usage
    public void addUser(String name, String email) {
        String sql = "INSERT INTO users(name, email) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }

    // Violation: not using try-with-resources, potential resource leak
    public void updateUser(String email, long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("UPDATE users SET email=? WHERE id=?");
            stmt.setString(1, email);
            stmt.setLong(2, id);
            stmt.executeUpdate();
            // Missing proper closure of resources
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        } finally {
            // Improper cleanup: may miss closing if exception occurs before stmt creation
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // Non-violation: try-with-resources for query
    public void printUsers() {
        String sql = "SELECT id, name, email FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("User: " + rs.getLong("id") + ", " + rs.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("SQL exception during query: " + e.getMessage());
        }
    }

    // Violation: manual resource cleanup without try-with-resources
    public void deleteUser(long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception during delete: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
