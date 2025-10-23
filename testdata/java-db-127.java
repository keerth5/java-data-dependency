// @testdata/AuditTrailImplementationTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑127: AuditTrailImplementation
 */
public class AuditTrailImplementationTest {

    // Violation: logging database operations for audit trail
    public void updateRecordWithAudit(String user, int recordId, String newValue) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String dbUser = "dbadmin";
        String dbPassword = "adminpassword";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            conn.createStatement().executeUpdate("UPDATE users SET name='" + newValue + "' WHERE id=" + recordId);
            // Audit trail logging
            System.out.println("AUDIT: User " + user + " updated record ID " + recordId);
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: logging delete operations for audit purposes
    public void deleteRecordWithAudit(String user, int recordId) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String dbUser = "dbadmin";
        String dbPassword = "adminpassword";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            conn.createStatement().executeUpdate("DELETE FROM users WHERE id=" + recordId);
            // Audit trail
            System.out.println("AUDIT: User " + user + " deleted record ID " + recordId);
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: performing database operations without audit logging
    public void updateRecordNoAudit(int recordId, String newValue) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.createStatement().executeUpdate("UPDATE users SET name='" + newValue + "' WHERE id=" + recordId);
            System.out.println("Record updated (no audit).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: inserting data without audit trail
    public void insertRecordNoAudit(String name) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.createStatement().executeUpdate("INSERT INTO users(name) VALUES('" + name + "')");
            System.out.println("Record inserted (no audit).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
