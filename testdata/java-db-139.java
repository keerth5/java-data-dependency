// @testdata/LocalDateTimeMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Sample code to test rule sql‑java‑139: LocalDateTimeMapping
 */
public class LocalDateTimeMappingTest {

    // Violation: direct concatenation of LocalDateTime in SQL string
    public void insertWithLocalDateTimeConcat(LocalDateTime ldt, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO events(event_time, description) VALUES('" + ldt + "', '" + description + "')";
            stmt.executeUpdate(sql); // LocalDateTime -> SQL mapping via concatenation
            System.out.println("Inserted record with LocalDateTime concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating SQL datetime column using string concatenation
    public void updateWithLocalDateTimeConcat(LocalDateTime newLdt, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE events SET event_time='" + newLdt + "' WHERE id=" + id;
            stmt.executeUpdate(sql); // LocalDateTime -> SQL mapping via concatenation
            System.out.println("Updated SQL datetime column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setTimestamp() and conversion
    public void insertWithPreparedStatement(LocalDateTime ldt, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO events(event_time, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(ldt)); // proper mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating SQL datetime column using PreparedStatement
    public void updateWithPreparedStatement(LocalDateTime newLdt, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE events SET event_time=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(newLdt)); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated SQL datetime column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
