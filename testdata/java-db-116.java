// @testdata/SqlEscapingUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑116: SqlEscapingUsage
 */
public class SqlEscapingUsageTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Violation: no escaping, direct string concatenation
    public void addUserUnsafe(String username) {
        String sql = "INSERT INTO users(name) VALUES('" + username + "')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: escape single quotes manually
    public void addUserEscaped(String username) {
        if (username != null) {
            username = username.replace("'", "''"); // simple SQL escaping
        }

        String sql = "INSERT INTO users(name) VALUES('" + username + "')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using parameterized PreparedStatement
    public void addUserSafe(String username) {
        String sql = "INSERT INTO users(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: dynamic update query with no escaping
    public void updateUserEmailUnsafe(String username, String email) {
        String sql = "UPDATE users SET email = '" + email + "' WHERE name = '" + username + "'";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: parameterized update using PreparedStatement
    public void updateUserEmailSafe(String username, String email) {
        String sql = "UPDATE users SET email = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
