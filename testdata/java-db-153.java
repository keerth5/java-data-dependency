// @testdata/PreparedStatementParameterTypingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Sample code to test rule sql‑java‑153: PreparedStatementParameterTyping
 */
public class PreparedStatementParameterTypingTest {

    // Violation: explicitly specifying parameter type using Types enum
    public void insertWithExplicitType(String name, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(name, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Violation: explicit type for parameter
            if (name != null) {
                pstmt.setString(1, name);
            } else {
                pstmt.setNull(1, Types.VARCHAR);
            }

            // Violation: explicit type for parameter
            if (description != null) {
                pstmt.setString(2, description);
            } else {
                pstmt.setNull(2, Types.VARCHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Inserted record with explicit parameter typing (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using setters without explicit type specification
    public void insertWithTypeInference(String name, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(name, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // JDBC infers type from setter, no explicit Types enum used
            pstmt.setString(1, name);
            pstmt.setString(2, description);

            pstmt.executeUpdate();
            System.out.println("Inserted record using type inference (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
