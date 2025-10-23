// @testdata/SqlTypeEnumUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Sample code to test rule sql‑java‑152: SqlTypeEnumUsage
 */
public class SqlTypeEnumUsageTest {

    // Violation: using java.sql.Types enum explicitly
    public void insertUsingSqlTypesEnum(String name, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(name, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (name != null) {
                pstmt.setString(1, name);
            } else {
                // Violation: using Types.VARCHAR explicitly
                pstmt.setNull(1, Types.VARCHAR);
            }

            if (description != null) {
                pstmt.setString(2, description);
            } else {
                // Violation: using Types.VARCHAR explicitly
                pstmt.setNull(2, Types.VARCHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Inserted record using java.sql.Types enum (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using type-safe setters only
    public void insertUsingTypeSafeSetters(String name, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO users(name, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);        // safe, no explicit Types enum
            pstmt.setString(2, description); // safe, no explicit Types enum

            pstmt.executeUpdate();
            System.out.println("Inserted record using type-safe setters (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
