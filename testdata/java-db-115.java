// @testdata/UserInputValidationTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑115: UserInputValidation
 */
public class UserInputValidationTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Violation: no input validation before inserting user
    public void addUser(String name, String email) {
        String sql = "INSERT INTO users(name,email) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }

    // Non-violation: validate input before inserting user
    public void addValidatedUser(String name, String email) {
        if (name == null || name.isEmpty() || email == null || !email.contains("@")) {
            System.out.println("Invalid input, aborting insert.");
            return;
        }

        String sql = "INSERT INTO users(name,email) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }

    // Violation: input not validated in update
    public void updateUserEmail(String username, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newEmail);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception during update: " + e.getMessage());
        }
    }

    // Non-violation: validate input before update
    public void safeUpdateUserEmail(String username, String newEmail) {
        if (username == null || username.isEmpty() || newEmail == null || !newEmail.contains("@")) {
            System.out.println("Invalid input, aborting update.");
            return;
        }

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
