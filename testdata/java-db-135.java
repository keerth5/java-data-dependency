// @testdata/DoubleToSqlFloatMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑135: DoubleToSqlFloatMapping
 */
public class DoubleToSqlFloatMappingTest {

    // Violation: direct concatenation of Double value in SQL string
    public void insertWithDoubleConcat(double value, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO measurements(value, description) VALUES(" + value + ", '" + description + "')";
            stmt.executeUpdate(sql); // Double -> FLOAT mapping via concatenation
            System.out.println("Inserted record with Double concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating FLOAT column using string concatenation
    public void updateWithDoubleConcat(double newValue, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE measurements SET value=" + newValue + " WHERE id=" + id;
            stmt.executeUpdate(sql); // Double -> FLOAT mapping via concatenation
            System.out.println("Updated FLOAT column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setDouble()
    public void insertWithPreparedStatement(double value, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO measurements(value, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, value); // proper Double -> FLOAT mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating FLOAT column using PreparedStatement
    public void updateWithPreparedStatement(double newValue, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE measurements SET value=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newValue); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated FLOAT column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
