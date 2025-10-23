// @testdata/WindowsAuthenticationUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑122: WindowsAuthenticationUsage
 */
public class WindowsAuthenticationUsageTest {

    // Violation: using Windows Integrated Authentication
    public void connectWithWindowsAuth() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;integratedSecurity=true;";
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected using Windows Authentication.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: using Windows Authentication via Properties
    public void connectWithWindowsAuthProperties() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB";
        Properties props = new Properties();
        props.setProperty("integratedSecurity", "true");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected using Windows Authentication via Properties.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using standard SQL Server username/password
    public void connectWithSqlCredentials() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using standard SQL Server credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using environment variables for credentials (no Windows auth)
    public void connectWithEnvCredentials() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;";
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using credentials from environment variables.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
