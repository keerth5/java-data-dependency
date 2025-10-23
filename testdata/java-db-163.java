// @testdata/SavepointUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Sample code to test rule sql‑java‑163: SavepointUsage
 */
public class SavepointUsageTest {

    // Violation: using a savepoint for partial rollback
    public void savepointViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
                 PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO users(name) VALUES('Bob')")) {

                stmt1.executeUpdate();

                Savepoint sp1 = conn.setSavepoint("Savepoint1"); // Violation
                stmt2.executeUpdate();

                conn.rollback(sp1); // Partial rollback
                conn.commit();
                System.out.println("Used savepoint for partial rollback (violation).");

            } catch (SQLException e) {
                conn.rollback();
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

    // Non-violation: no savepoints, standard transaction
    public void standardTransactionNonViolation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Charlie')")) {

            conn.setAutoCommit(false);
            stmt.executeUpdate();
            conn.commit();
            System.out.println("Standard transaction, no savepoints (non-violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: using framework-managed transaction
    public void springManagedTransactionNonViolation(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Spring-managed transaction (non-violation).");
    }

    public static void main(String[] args) {
        SavepointUsageTest test = new SavepointUsageTest();
        test.savepointViolation();             // Violation
        test.standardTransactionNonViolation(); // Non-violation
    }
}
