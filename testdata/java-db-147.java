// @testdata/BlobToSqlImageMappingTest.java
package com.example.test;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.ByteArrayInputStream;

/**
 * Sample code to test rule sql‑java‑147: BlobToSqlImageMapping
 */
public class BlobToSqlImageMappingTest {

    // Violation: direct concatenation of Blob in SQL string
    public void insertWithBlobConcat(Blob blobData, String description) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Unsafe: concatenating Blob directly (calls toString(), not proper storage)
            String sql = "INSERT INTO images(image_data, description) VALUES('" + blobData + "', '" + description + "')";
            stmt.executeUpdate(sql); 
            System.out.println("Inserted record with Blob concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating IMAGE column using string concatenation
    public void updateWithBlobConcat(Blob newBlob, int id) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE images SET image_data='" + newBlob + "' WHERE id=" + id;
            stmt.executeUpdate(sql); 
            System.out.println("Updated IMAGE column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setBlob()
    public void insertWithPreparedStatement(byte[] data, String description) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO images(image_data, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Blob blob = conn.createBlob();
            blob.setBytes(1, data);
            pstmt.setBlob(1, blob); // proper Blob -> IMAGE mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating IMAGE column using PreparedStatement
    public void updateWithPreparedStatement(byte[] newData, int id) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE images SET image_data=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Blob blob = conn.createBlob();
            blob.setBytes(1, newData);
            pstmt.setBlob(1, blob); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated IMAGE column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
