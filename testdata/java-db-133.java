// @testdata/LongToSqlBigIntMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑133: LongToSqlBigIntMapping
 */
public class LongToSqlBigIntMappingTest {

    // Violation: direct concatenation of Long value in SQL string
    public void insertWithLongConcat(long id, String name) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO users(id, name) VALUES(" + id + ", '" + name + "')"; // Long -> BIGINT mapping via concatenation
            stmt.executeUpdate(sql);
            System.out.println("Inserted record with Long concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating BIGINT column using string concatenation
    public void updateWithLongConcat(long id, long newValue) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE users SET account_balance=" + newValue + " WHERE id=" + id; // Long -> BIGINT (violation)
            stmt.executeUpdate(sql);
            System.out.println("Updated BIGINT column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setLong()
    public void insertWithPreparedStatement(long id, String name) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(id, name) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id); // proper Long -> BIGINT mapping
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating BIGINT column using PreparedStatement
    public void updateWithPreparedStatement(long id, long newValue) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE users SET account_balance=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, newValue); // proper mapping
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated BIGINT column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
