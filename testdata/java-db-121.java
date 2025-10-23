// @testdata/DatabaseAuthenticationUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑121: DatabaseAuthenticationUsage
 */
public class DatabaseAuthenticationUsageTest {

    // Violation: using default username/password (weak authentication)
    public void connectWithDefaultCredentials() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "root"; // default weak password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using default weak credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: plaintext credentials without secure authentication
    public void connectPlainCredentials() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "admin";
        String password = "admin123"; // hardcoded or plaintext

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using plaintext credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using encrypted password or secure credential storage
    public void connectWithSecureAuthentication() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        Properties props = new Properties();
        props.setProperty("user", System.getenv("DB_USER"));
        props.setProperty("password", System.getenv("DB_PASSWORD")); // secure retrieval
        props.setProperty("useSSL", "true");
        props.setProperty("requireSSL", "true");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected using secure authentication mechanism.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using Kerberos authentication (example)
    public void connectWithKerberosAuthentication() {
        String url = "jdbc:postgresql://localhost:5432/testdb?jaasApplicationName=pgjdbc";
        Properties props = new Properties();
        props.setProperty("kerberosServerName", "postgres");
        props.setProperty("user", "dbuser");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected using Kerberos authentication.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
