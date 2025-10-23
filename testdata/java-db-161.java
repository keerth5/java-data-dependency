// @testdata/ConnectionCommitUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑161: ConnectionCommitUsage
 */
public class ConnectionCommitUsageTest {

    // Violation: manual commit after executing statements
    public void manualCommitViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
             PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO users(name) VALUES('Bob')")) {

            conn.setAutoCommit(false); // Manual transaction management
            stmt1.executeUpdate();
            stmt2.executeUpdate();
            conn.commit(); // Violation
            System.out.println("Manual commit used (violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: relying on default auto-commit
    public void autoCommitNonViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Charlie')")) {

            // Default auto-commit: no manual commit call
            stmt.executeUpdate();
            System.out.println("Auto-commit used (non-violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: using framework-managed transaction (Spring JdbcTemplate)
    public void springManagedTransactionNonViolation(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Spring-managed transaction used (non-violation).");
    }

    public static void main(String[] args) {
        ConnectionCommitUsageTest test = new ConnectionCommitUsageTest();
        test.manualCommitViolation();       // Violation
        test.autoCommitNonViolation();      // Non-violation
    }
}
