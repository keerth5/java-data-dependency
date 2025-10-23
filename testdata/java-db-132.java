// @testdata/IntegerToSqlIntMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑132: IntegerToSqlIntMapping
 */
public class IntegerToSqlIntMappingTest {

    // Violation: direct concatenation of Integer in SQL string
    public void insertWithIntegerConcat(int id, String name) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO users(id, name) VALUES(" + id + ", '" + name + "')"; // Integer -> INT mapping via concatenation
            stmt.executeUpdate(sql);
            System.out.println("Inserted record with Integer concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating INT column using string concatenation
    public void updateWithIntegerConcat(int id, int newAge) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE users SET age=" + newAge + " WHERE id=" + id; // Integer -> INT mapping (violation)
            stmt.executeUpdate(sql);
            System.out.println("Updated INT column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setInt()
    public void insertWithPreparedStatement(int id, String name) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(id, name) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // proper Integer -> INT mapping
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating INT column using PreparedStatement
    public void updateWithPreparedStatement(int id, int newAge) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE users SET age=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newAge); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated INT column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
