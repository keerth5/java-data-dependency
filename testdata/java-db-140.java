// @testdata/LocalDateMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Sample code to test rule sql‑java‑140: LocalDateMapping
 */
public class LocalDateMappingTest {

    // Violation: direct concatenation of LocalDate in SQL string
    public void insertWithLocalDateConcat(LocalDate localDate, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO holidays(holiday_date, description) VALUES('" + localDate + "', '" + description + "')";
            stmt.executeUpdate(sql); // LocalDate -> DATE mapping via concatenation
            System.out.println("Inserted record with LocalDate concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating DATE column using string concatenation
    public void updateWithLocalDateConcat(LocalDate newDate, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE holidays SET holiday_date='" + newDate + "' WHERE id=" + id;
            stmt.executeUpdate(sql); // LocalDate -> DATE mapping via concatenation
            System.out.println("Updated DATE column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setDate() and conversion
    public void insertWithPreparedStatement(LocalDate localDate, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO holidays(holiday_date, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(localDate)); // proper mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating DATE column using PreparedStatement
    public void updateWithPreparedStatement(LocalDate newDate, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE holidays SET holiday_date=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(newDate)); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated DATE column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
