// @testdata/ImplicitTypeConversionTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑151: ImplicitTypeConversion
 */
public class ImplicitTypeConversionTest {

    // Violation: inserting int into varchar column via concatenation (implicit conversion)
    public void insertWithImplicitConversion(int value, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Implicit conversion: integer inserted into varchar column
            String sql = "INSERT INTO users(user_code, id) VALUES('" + value + "', " + id + ")";
            stmt.executeUpdate(sql);
            System.out.println("Inserted record with implicit conversion (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating varchar column using int directly via concatenation
    public void updateWithImplicitConversion(int newValue, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE users SET user_code='" + newValue + "' WHERE id=" + id;
            stmt.executeUpdate(sql);
            System.out.println("Updated column with implicit conversion (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with explicit type handling
    public void insertWithPreparedStatement(int value, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(user_code, id) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Explicit conversion: integer to string before binding
            pstmt.setString(1, String.valueOf(value));
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating column with explicit type binding
    public void updateWithPreparedStatement(int newValue, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE users SET user_code=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(newValue)); // explicit conversion to avoid implicit type conversion
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
