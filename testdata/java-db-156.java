// @testdata/EnumToSqlMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑156: EnumToSqlMapping
 */
public class EnumToSqlMappingTest {

    public enum Status {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    // Violation: directly storing Enum.name() in SQL without mapping strategy
    public void insertWithEnumDirectly(Status status) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(status) VALUES('" + status.name() + "')";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
            System.out.println("Inserted enum directly (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with explicit string conversion
    public void insertWithPreparedStatement(Status status) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(status) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Explicit mapping: Enum converted to string before binding
            pstmt.setString(1, status.name());
            pstmt.executeUpdate();
            System.out.println("Inserted enum using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: reading Enum via ResultSet without mapping
    public void readEnumDirectly() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "SELECT status FROM users";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Violation: storing DB string directly into Enum
                Status status = Status.valueOf(rs.getString("status"));
                System.out.println("Read enum directly (violation): " + status);
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: explicit mapping using a helper method
    public void readEnumWithMapping() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "SELECT status FROM users";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String statusStr = rs.getString("status");
                Status status = mapToStatus(statusStr); // Non-violation: mapping helper
                System.out.println("Read enum with mapping (non-violation): " + status);
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    private Status mapToStatus(String value) {
        // Custom mapping logic
        if ("ACTIVE".equals(value)) return Status.ACTIVE;
        if ("INACTIVE".equals(value)) return Status.INACTIVE;
        return Status.PENDING;
    }
}
