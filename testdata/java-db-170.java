// @testdata/TransactionIsolationUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑170: TransactionIsolationUsage
 */
public class TransactionIsolationUsageTest {

    // Violation: manually setting transaction isolation level
    public void isolationLevelViolation() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "dbuser", "dbpassword")) {

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // Violation

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES(?)");
            stmt.setString(1, "Alice");
            stmt.executeUpdate();

            conn.commit();
            System.out.println("Manual transaction isolation level set (violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: default isolation level (no manual setting)
    public void defaultIsolationNonViolation() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "dbuser", "dbpassword")) {

            conn.setAutoCommit(false);
            // Using default isolation level, no explicit call
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES(?)");
            stmt.setString(1, "Bob");
            stmt.executeUpdate();

            conn.commit();
            System.out.println("Default transaction isolation level (non-violation).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TransactionIsolationUsageTest test = new TransactionIsolationUsageTest();
        test.isolationLevelViolation();       // Violation
        test.defaultIsolationNonViolation();  // Non-violation
    }
}
