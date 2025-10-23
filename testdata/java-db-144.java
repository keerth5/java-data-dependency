// @testdata/UuidToSqlUniqueIdentifierMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Sample code to test rule sql‑java‑144: UuidToSqlUniqueIdentifierMapping
 */
public class UuidToSqlUniqueIdentifierMappingTest {

    // Violation: direct concatenation of UUID in SQL string
    public void insertWithUuidConcat(UUID id, String description) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO users(user_id, description) VALUES('" + id + "', '" + description + "')";
            stmt.executeUpdate(sql); // UUID -> UNIQUEIDENTIFIER mapping via concatenation
            System.out.println("Inserted record with UUID concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating UNIQUEIDENTIFIER column using string concatenation
    public void updateWithUuidConcat(UUID newId, int id) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE users SET user_id='" + newId + "' WHERE id=" + id;
            stmt.executeUpdate(sql); // UUID -> UNIQUEIDENTIFIER mapping via concatenation
            System.out.println("Updated UNIQUEIDENTIFIER column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setObject()
    public void insertWithPreparedStatement(UUID id, String description) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(user_id, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id); // proper UUID -> UNIQUEIDENTIFIER mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating UNIQUEIDENTIFIER column using PreparedStatement
    public void updateWithPreparedStatement(UUID newId, int id) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE users SET user_id=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, newId); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated UNIQUEIDENTIFIER column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
