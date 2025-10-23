// @testdata/DataTypeConversionUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑150: DataTypeConversionUsage
 */
public class DataTypeConversionUsageTest {

    // Violation: explicit type casting in SQL concatenation
    public void insertWithExplicitCast(Object value, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Unsafe: explicit cast inside concatenation
            String sql = "INSERT INTO numbers(num_value, id) VALUES(" + (Integer) value + ", " + id + ")";
            stmt.executeUpdate(sql); 
            System.out.println("Inserted record with explicit cast (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: using parseInt or parseDouble directly in SQL string
    public void updateWithParseConversion(String valueStr, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            int value = Integer.parseInt(valueStr); // explicit conversion
            String sql = "UPDATE numbers SET num_value=" + value + " WHERE id=" + id;
            stmt.executeUpdate(sql);
            System.out.println("Updated record with parseInt conversion (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with proper type mapping
    public void insertWithPreparedStatement(int value, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO numbers(num_value, id) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, value); // safe assignment without explicit cast
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating using PreparedStatement with proper type mapping
    public void updateWithPreparedStatement(int value, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE numbers SET num_value=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, value); // safe assignment
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
