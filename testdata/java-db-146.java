// @testdata/ClobToSqlTextMappingTest.java
package com.example.test;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.StringReader;

/**
 * Sample code to test rule sql‑java‑146: ClobToSqlTextMapping
 */
public class ClobToSqlTextMappingTest {

    // Violation: direct concatenation of Clob in SQL string
    public void insertWithClobConcat(Clob clobData, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Unsafe: concatenating Clob directly (calls toString(), not proper storage)
            String sql = "INSERT INTO documents(content, description) VALUES('" + clobData + "', '" + description + "')";
            stmt.executeUpdate(sql); 
            System.out.println("Inserted record with Clob concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: updating TEXT column using string concatenation
    public void updateWithClobConcat(Clob newClob, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "UPDATE documents SET content='" + newClob + "' WHERE id=" + id;
            stmt.executeUpdate(sql); 
            System.out.println("Updated TEXT column using concatenation (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using PreparedStatement with setClob()
    public void insertWithPreparedStatement(String textData, String description) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "INSERT INTO documents(content, description) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Clob clob = conn.createClob();
            clob.setCharacterStream(1, new StringReader(textData), textData.length());
            pstmt.setClob(1, clob); // proper Clob -> TEXT mapping
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: updating TEXT column using PreparedStatement
    public void updateWithPreparedStatement(String newText, int id) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "UPDATE documents SET content=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Clob clob = conn.createClob();
            clob.setCharacterStream(1, new StringReader(newText), newText.length());
            pstmt.setClob(1, clob); // proper mapping
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Updated TEXT column using PreparedStatement (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
