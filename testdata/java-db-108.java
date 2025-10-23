// @testdata/FinallyBlockResourceCleanupTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑108: FinallyBlockResourceCleanup
 */
public class FinallyBlockResourceCleanupTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Non-violation: proper finally block cleanup
    public void addUser(String name, String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("INSERT INTO users(name,email) VALUES(?,?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        } finally {
            // Proper cleanup in finally block
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // Violation: no finally block, resources may leak
    public void updateUser(String email, long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("UPDATE users SET email=? WHERE id=?");
            stmt.setString(1, email);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        // Violation: missing finally block for cleanup
    }

    // Non-violation: finally block with multiple resources
    public void printUsers() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.prepareStatement("SELECT id, name FROM users");
            rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("User: " + rs.getLong("id") + ", " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("SQL exception during query: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // Violation: improper cleanup, only closing connection
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
            // Violation: only closing connection, stmt not closed
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
