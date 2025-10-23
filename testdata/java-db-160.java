// @testdata/ConnectionAutoCommitUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑160: ConnectionAutoCommitUsage
 */
public class ConnectionAutoCommitUsageTest {

    // Violation: manually disabling auto-commit
    public void disableAutoCommitViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')")) {

            conn.setAutoCommit(false); // Violation
            stmt.executeUpdate();
            conn.commit();
            System.out.println("Manual setAutoCommit(false) used (violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Violation: explicitly enabling auto-commit
    public void enableAutoCommitViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Bob')")) {

            conn.setAutoCommit(true); // Violation
            stmt.executeUpdate();
            System.out.println("Explicit setAutoCommit(true) used (violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: relying on default auto-commit
    public void defaultAutoCommitNonViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Charlie')")) {

            // Default auto-commit, no setAutoCommit() call
            stmt.executeUpdate();
            System.out.println("Default auto-commit used (non-violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: using framework-managed transaction
    public void springManagedTransactionNonViolation(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Spring-managed transaction used (non-violation).");
    }

    public static void main(String[] args) {
        ConnectionAutoCommitUsageTest test = new ConnectionAutoCommitUsageTest();
        test.disableAutoCommitViolation();     // Violation
        test.enableAutoCommitViolation();      // Violation
        test.defaultAutoCommitNonViolation();  // Non-violation
    }
}
