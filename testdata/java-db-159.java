// @testdata/JdbcTransactionUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑159: JdbcTransactionUsage
 */
public class JdbcTransactionUsageTest {

    // Violation: manual JDBC transaction management without try/catch/finally
    public void manualTransactionViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
             PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO users(name) VALUES('Bob')")) {

            conn.setAutoCommit(false); // Violation: manual transaction
            stmt1.executeUpdate();
            stmt2.executeUpdate();
            conn.commit();
            System.out.println("Manual JDBC transaction used (violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: using auto-commit (default JDBC behavior)
    public void autoCommitNonViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Charlie')")) {

            // Default auto-commit: each statement is committed automatically
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
        JdbcTransactionUsageTest test = new JdbcTransactionUsageTest();
        test.manualTransactionViolation();      // Violation
        test.autoCommitNonViolation();          // Non-violation
    }
}
