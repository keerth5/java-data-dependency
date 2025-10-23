// @testdata/PasswordEncryptionUsageTest.java
package com.example.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Sample code to test rule sql‑java‑118: PasswordEncryptionUsage
 */
public class PasswordEncryptionUsageTest {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // Violation: storing plaintext password
    public void storePlainPassword(String username, String password) {
        String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // plaintext storage
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: store password hashed with SHA-256
    public void storeHashedPassword(String username, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: store password encrypted (example with Base64 for demo)
    public void storeEncryptedPassword(String username, String password) {
        String encryptedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, encryptedPassword);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Utility method to hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found");
        }
    }
}
