// @testdata/KerberosAuthenticationUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑123: KerberosAuthenticationUsage
 */
public class KerberosAuthenticationUsageTest {

    // Violation: using Kerberos authentication via Properties
    public void connectWithKerberos() {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        Properties props = new Properties();
        props.setProperty("user", "dbuser");
        props.setProperty("jaasApplicationName", "pgjdbc"); // triggers Kerberos auth
        props.setProperty("kerberosServerName", "postgres");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected using Kerberos authentication.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: using Kerberos with integrated security in SQL Server
    public void connectWithKerberosSqlServer() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;integratedSecurity=true;authenticationScheme=JavaKerberos;";
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to SQL Server using Kerberos authentication.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using standard username/password
    public void connectWithSqlCredentials() {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using standard SQL credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: credentials from environment variables
    public void connectWithEnvCredentials() {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using environment variable credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
