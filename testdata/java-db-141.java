// @testdata/LocalTimeMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Sample code to test rule sql‑java‑141: LocalTimeMapping
 */
public class LocalTimeMappingTest {

    // Violation: direct concatenation of LocalTime in SQL string
    public void insertWithLocalTimeConcat(LocalTime localTime, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO schedules(start_time, description) VALUES('" + localTime + "', '" + description + "')";
            stmt.executeUpdate(sql); // LocalTime -> TIME mapping via concatenation
            System.out.println("Inserted record with LocalTime concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating TIME column using string concatenation
    public void updateWithLocalTimeConcat(LocalTime newTime, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE schedules SET start_time='" + newTime + "' WHERE id=" + id;
            stmt.executeUpdate(sql); // LocalTime -> TIME mapping via concatenation
            System.out.println("Updated TIME column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setTime() and conversion
    public void insertWithPreparedStatement(LocalTime localTime, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO schedules(start_time, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTime(1, Time.valueOf(localTime)); // proper mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating TIME column using PreparedStatement
    public void updateWithPreparedStatement(LocalTime newTime, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE schedules SET start_time=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTime(1, Time.valueOf(newTime)); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated TIME column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
