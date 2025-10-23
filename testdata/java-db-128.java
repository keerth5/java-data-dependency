// @testdata/DatabasePermissionValidationTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑128: DatabasePermissionValidation
 */
public class DatabasePermissionValidationTest {

    // Violation: checking user permissions before performing delete
    public void deleteRecordWithPermissionCheck(String userRole, int recordId) {
        if (!"ADMIN".equals(userRole)) { // permission validation
            System.out.println("Permission denied: only ADMIN can delete records.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/testdb";
        String dbUser = "dbadmin";
        String dbPassword = "adminpassword";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            conn.createStatement().executeUpdate("DELETE FROM users WHERE id=" + recordId);
            System.out.println("Record deleted by admin.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: validating permissions before reading sensitive data
    public void readSensitiveDataWithPermission(String userRole) {
        if (!"ADMIN".equals(userRole) && !"MANAGER".equals(userRole)) {
            System.out.println("Permission denied: insufficient privileges.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/testdb";
        String dbUser = "dbuser";
        String dbPassword = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            System.out.println("Accessing sensitive records.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: performing operations without permission validation
    public void deleteRecordNoPermission(int recordId) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String dbUser = "dbuser";
        String dbPassword = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            conn.createStatement().executeUpdate("DELETE FROM users WHERE id=" + recordId);
            System.out.println("Record deleted (no permission check).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: read data without permission logic
    public void readDataNoPermission() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            System.out.println("Reading data (no permission validation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
