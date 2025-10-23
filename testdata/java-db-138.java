// @testdata/TimestampToSqlDateTimeMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Sample code to test rule sql‑java‑138: TimestampToSqlDateTimeMapping
 */
public class TimestampToSqlDateTimeMappingTest {

    // Violation: direct concatenation of Timestamp in SQL string
    public void insertWithTimestampConcat(Timestamp timestamp, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO logs(event_time, description) VALUES('" + timestamp + "', '" + description + "')";
            stmt.executeUpdate(sql); // Timestamp -> DATETIME mapping via concatenation
            System.out.println("Inserted record with Timestamp concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating DATETIME column using string concatenation
    public void updateWithTimestampConcat(Timestamp newTimestamp, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE logs SET event_time='" + newTimestamp + "' WHERE id=" + id;
            stmt.executeUpdate(sql); // Timestamp -> DATETIME mapping via concatenation
            System.out.println("Updated DATETIME column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setTimestamp()
    public void insertWithPreparedStatement(Timestamp timestamp, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO logs(event_time, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, timestamp); // proper Timestamp -> DATETIME mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating DATETIME column using PreparedStatement
    public void updateWithPreparedStatement(Timestamp newTimestamp, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE logs SET event_time=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, newTimestamp); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated DATETIME column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
