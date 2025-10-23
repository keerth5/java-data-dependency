// @testdata/SecureConfigurationManagementTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑129: SecureConfigurationManagement
 */
public class SecureConfigurationManagementTest {

    // Violation: hardcoded database credentials in code
    public void connectWithHardcodedCredentials() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser"; // violation: hardcoded
        String password = "dbpassword"; // violation: hardcoded

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected with hardcoded credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: insecure properties file directly in source
    public void connectWithInsecureProperties() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        Properties props = new Properties();
        props.setProperty("user", "dbuser"); // violation
        props.setProperty("password", "dbpassword"); // violation

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected with insecure properties.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: credentials from environment variables
    public void connectWithEnvCredentials() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using environment variables (secure).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: credentials from external secured configuration service
    public void connectWithSecureConfigService() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = SecureConfig.getUser(); // retrieves securely
        String password = SecureConfig.getPassword(); // retrieves securely

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using secure configuration service.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}

// Mock secure configuration service
class SecureConfig {
    public static String getUser() {
        return System.getenv("DB_USER");
    }
    public static String getPassword() {
        return System.getenv("DB_PASSWORD");
    }
}
