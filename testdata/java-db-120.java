// @testdata/CertificateValidationUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Sample code to test rule sql‑java‑120: CertificateValidationUsage
 */
public class CertificateValidationUsageTest {

    // Violation: SSL used but certificate validation disabled
    public void connectWithoutCertificateValidation() {
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=true&verifyServerCertificate=false";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected with SSL but certificate validation disabled.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: SSL used without specifying certificate validation
    public void connectWithSslNoValidation() {
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=true";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected with SSL but certificate validation may not be enforced.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: SSL with certificate validation enabled
    public void connectWithCertificateValidation() {
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=true&verifyServerCertificate=true&requireSSL=true";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected with SSL and certificate validation enforced.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using Properties object with certificate validation
    public void connectWithPropertiesCertificateValidation() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "password");
        props.setProperty("useSSL", "true");
        props.setProperty("requireSSL", "true");
        props.setProperty("verifyServerCertificate", "true");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            System.out.println("Connected with SSL and certificate validation via Properties.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
