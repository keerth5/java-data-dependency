// @testdata/BatchUpdateExceptionHandlingTest.java
package com.example.test;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchUpdateExceptionHandlingTest {

    public void executeBatchUpdate() {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "password";

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            pstmt.setString(1, "Alice");
            pstmt.setString(2, "alice@example.com");
            pstmt.addBatch();

            pstmt.setString(1, "Bob");
            pstmt.setString(2, "bob@example.com");
            pstmt.addBatch();

            pstmt.setString(1, "Charlie");
            pstmt.setString(2, "charlie@example.com");
            pstmt.addBatch();

            pstmt.executeBatch();
            conn.commit();

        } catch (BatchUpdateException e) {
            // Violation: inadequate handling, just printing the exception
            System.out.println("Batch update failed: " + e.getMessage());

        } catch (SQLException e) {
            // Non-violation: proper handling with detailed logging
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeBatchUpdateWithDetailedHandling() {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "password";

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            pstmt.setString(1, "Alice");
            pstmt.setString(2, "alice@example.com");
            pstmt.addBatch();

            pstmt.setString(1, "Bob");
            pstmt.setString(2, "bob@example.com");
            pstmt.addBatch();

            pstmt.setString(1, "Charlie");
            pstmt.setString(2, "charlie@example.com");
            pstmt.addBatch();

            pstmt.executeBatch();
            conn.commit();

        } catch (BatchUpdateException e) {
            // Non-violation: detailed handling, logging update counts and continuing
            int[] updateCounts = e.getUpdateCounts();
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                    System.err.println("Failed to execute batch item at index " + i);
                } else {
                    System.out.println("Update count for batch item " + i + ": " + updateCounts[i]);
                }
            }
            // Optionally, retry or skip the failed batch item
            // retryBatchItem(batchItem);
            // or
            // skipBatchItem(batchItem);
        } catch (SQLException e) {
            // Non-violation: proper handling with detailed logging
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
