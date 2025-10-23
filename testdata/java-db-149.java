// @testdata/OptionalTypeHandlingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Sample code to test rule sql‑java‑149: OptionalTypeHandling
 */
public class OptionalTypeHandlingTest {

    // Violation: concatenating Optional directly into SQL string
    public void insertWithOptionalConcat(Optional<String> nameOpt, Optional<String> descriptionOpt) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Unsafe: concatenating Optional directly (calls toString(), not proper null/value handling)
            String sql = "INSERT INTO users(name, description) VALUES('" + nameOpt + "', '" + descriptionOpt + "')";
            stmt.executeUpdate(sql); 
            System.out.println("Inserted record with Optional concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating a column with Optional concatenation
    public void updateWithOptionalConcat(Optional<String> newNameOpt, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE users SET name='" + newNameOpt + "' WHERE id=" + id;
            stmt.executeUpdate(sql); 
            System.out.println("Updated column with Optional concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with proper Optional handling
    public void insertWithPreparedStatement(Optional<String> nameOpt, Optional<String> descriptionOpt) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(name, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (nameOpt.isPresent()) {
                pstmt.setString(1, nameOpt.get());
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            if (descriptionOpt.isPresent()) {
                pstmt.setString(2, descriptionOpt.get());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement with Optional handling (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating a column using PreparedStatement with Optional handling
    public void updateWithPreparedStatement(Optional<String> newNameOpt, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE users SET name=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (newNameOpt.isPresent()) {
                pstmt.setString(1, newNameOpt.get());
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated column using PreparedStatement with Optional handling (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
