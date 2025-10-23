// @testdata/LdapAuthenticationUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑124: LdapAuthenticationUsage
 */
public class LdapAuthenticationUsageTest {

    // Violation: using LDAP authentication via Properties
    public void connectWithLdapAuth() {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        Properties props = new Properties();
        props.setProperty("user", "ldapuser");
        props.setProperty("password", "ldappassword");
        props.setProperty("authenticationMechanism", "LDAP");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected using LDAP authentication.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: using LDAP with SQL Server authentication
    public void connectWithLdapSqlServer() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;authentication=ActiveDirectoryLDAP;";
        String user = "ldapuser";
        String password = "ldappassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to SQL Server using LDAP authentication.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using standard SQL credentials
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

    // Non-violation: using environment variables for credentials
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
