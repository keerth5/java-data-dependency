// @testdata/ByteToSqlTinyIntMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑143: ByteToSqlTinyIntMapping
 */
public class ByteToSqlTinyIntMappingTest {

    // Violation: direct concatenation of Byte in SQL string
    public void insertWithByteConcat(Byte value, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO small_numbers(value, description) VALUES(" + value + ", '" + description + "')";
            stmt.executeUpdate(sql); // Byte -> TINYINT mapping via concatenation
            System.out.println("Inserted record with Byte concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating TINYINT column using string concatenation
    public void updateWithByteConcat(Byte newValue, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE small_numbers SET value=" + newValue + " WHERE id=" + id;
            stmt.executeUpdate(sql); // Byte -> TINYINT mapping via concatenation
            System.out.println("Updated TINYINT column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setByte()
    public void insertWithPreparedStatement(Byte value, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO small_numbers(value, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setByte(1, value); // proper Byte -> TINYINT mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating TINYINT column using PreparedStatement
    public void updateWithPreparedStatement(Byte newValue, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE small_numbers SET value=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setByte(1, newValue); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated TINYINT column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
