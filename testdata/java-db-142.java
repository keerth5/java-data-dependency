// @testdata/BooleanToSqlBitMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑142: BooleanToSqlBitMapping
 */
public class BooleanToSqlBitMappingTest {

    // Violation: direct concatenation of Boolean in SQL string
    public void insertWithBooleanConcat(Boolean flag, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO features(is_enabled, description) VALUES(" + flag + ", '" + description + "')";
            stmt.executeUpdate(sql); // Boolean -> BIT mapping via concatenation
            System.out.println("Inserted record with Boolean concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating BIT column using string concatenation
    public void updateWithBooleanConcat(Boolean newFlag, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE features SET is_enabled=" + newFlag + " WHERE id=" + id;
            stmt.executeUpdate(sql); // Boolean -> BIT mapping via concatenation
            System.out.println("Updated BIT column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setBoolean()
    public void insertWithPreparedStatement(Boolean flag, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO features(is_enabled, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, flag); // proper Boolean -> BIT mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating BIT column using PreparedStatement
    public void updateWithPreparedStatement(Boolean newFlag, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE features SET is_enabled=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, newFlag); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated BIT column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
