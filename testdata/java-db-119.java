// @testdata/DatabaseConnectionEncryptionTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑119: DatabaseConnectionEncryption
 */
public class DatabaseConnectionEncryptionTest {

    // Violation: unencrypted database connection
    public void connectUnencrypted() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using unencrypted connection.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: encrypted connection using SSL in URL
    public void connectEncryptedSslUrl() {
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=true&requireSSL=true";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using SSL-encrypted connection.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: encrypted connection using Properties object
    public void connectEncryptedWithProperties() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "password");
        props.setProperty("useSSL", "true");
        props.setProperty("requireSSL", "true");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected using SSL-encrypted connection via Properties.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
