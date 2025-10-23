// @testdata/ConnectionRollbackUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑162: ConnectionRollbackUsage
 */
public class ConnectionRollbackUsageTest {

    // Violation: manual rollback on exception
    public void manualRollbackViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false); // Manual transaction management

            try (PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
                 PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO users(name) VALUES('Bob')")) {

                stmt1.executeUpdate();
                stmt2.executeUpdate();
                conn.commit();
                System.out.println("Transaction committed.");

            } catch (SQLException e) {
                conn.rollback(); // Violation
                System.out.println("Manual rollback used (violation).");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Non-violation: relying on auto-commit (no manual rollback)
    public void autoCommitNonViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Charlie')")) {

            // Default auto-commit handles rollback automatically
            stmt.executeUpdate();
            System.out.println("Auto-commit used (non-violation).");

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
        ConnectionRollbackUsageTest test = new ConnectionRollbackUsageTest();
        test.manualRollbackViolation();       // Violation
        test.autoCommitNonViolation();        // Non-violation
    }
}
