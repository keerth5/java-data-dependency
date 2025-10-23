// @testdata/BigDecimalToSqlDecimalMappingTest.java
package com.example.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑136: BigDecimalToSqlDecimalMapping
 */
public class BigDecimalToSqlDecimalMappingTest {

    // Violation: direct concatenation of BigDecimal in SQL string
    public void insertWithBigDecimalConcat(BigDecimal amount, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "INSERT INTO transactions(amount, description) VALUES(" + amount + ", '" + description + "')";
            stmt.executeUpdate(sql); // BigDecimal -> DECIMAL mapping via concatenation
            System.out.println("Inserted record with BigDecimal concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating DECIMAL column using string concatenation
    public void updateWithBigDecimalConcat(BigDecimal newAmount, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE transactions SET amount=" + newAmount + " WHERE id=" + id;
            stmt.executeUpdate(sql); // BigDecimal -> DECIMAL mapping via concatenation
            System.out.println("Updated DECIMAL column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setBigDecimal()
    public void insertWithPreparedStatement(BigDecimal amount, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO transactions(amount, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, amount); // proper BigDecimal -> DECIMAL mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating DECIMAL column using PreparedStatement
    public void updateWithPreparedStatement(BigDecimal newAmount, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE transactions SET amount=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, newAmount); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated DECIMAL column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
