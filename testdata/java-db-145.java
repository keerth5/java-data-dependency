// @testdata/ByteArrayToSqlVarbinaryMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑145: ByteArrayToSqlVarbinaryMapping
 */
public class ByteArrayToSqlVarbinaryMappingTest {

    // Violation: direct concatenation of byte[] in SQL string
    public void insertWithByteArrayConcat(byte[] data, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Unsafe: concatenating byte[] directly (will call toString(), not proper storage)
            String sql = "INSERT INTO files(file_data, description) VALUES('" + data + "', '" + description + "')";
            stmt.executeUpdate(sql); 
            System.out.println("Inserted record with byte[] concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating VARBINARY column using string concatenation
    public void updateWithByteArrayConcat(byte[] newData, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE files SET file_data='" + newData + "' WHERE id=" + id;
            stmt.executeUpdate(sql); 
            System.out.println("Updated VARBINARY column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setBytes()
    public void insertWithPreparedStatement(byte[] data, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO files(file_data, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, data); // proper byte[] -> VARBINARY mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating VARBINARY column using PreparedStatement
    public void updateWithPreparedStatement(byte[] newData, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE files SET file_data=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, newData); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated VARBINARY column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
