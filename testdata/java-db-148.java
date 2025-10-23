// @testdata/NullValueHandlingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑148: NullValueHandling
 */
public class NullValueHandlingTest {

    // Violation: direct concatenation of potentially null Java variable
    public void insertWithNullConcat(String name, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Unsafe: concatenating variables that may be null
            String sql = "INSERT INTO users(name, description) VALUES('" + name + "', '" + description + "')";
            stmt.executeUpdate(sql); 
            System.out.println("Inserted record with null concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating a column using concatenation without null check
    public void updateWithNullConcat(String newName, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE users SET name='" + newName + "' WHERE id=" + id;
            stmt.executeUpdate(sql); 
            System.out.println("Updated column with null concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with proper null handling
    public void insertWithPreparedStatement(String name, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(name, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (name != null) {
                pstmt.setString(1, name);
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            if (description != null) {
                pstmt.setString(2, description);
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement with null handling (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating a column with proper null handling
    public void updateWithPreparedStatement(String newName, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE users SET name=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (newName != null) {
                pstmt.setString(1, newName);
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated column using PreparedStatement with null handling (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
