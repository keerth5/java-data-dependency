// @testdata/DatabaseConnectionObfuscationTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Sample code to test rule sql‑java‑130: DatabaseConnectionObfuscation
 */
public class DatabaseConnectionObfuscationTest {

    // Violation: using Base64 obfuscation for connection string
    public void connectWithBase64Obfuscation() {
        String encodedUrl = "amRiYzpteXNxbDovL2xvY2FsaG9zdDozMzA2L3Rlc3RkYg=="; // "jdbc:mysql://localhost:3306/testdb"
        byte[] decodedBytes = Base64.getDecoder().decode(encodedUrl);
        String url = new String(decodedBytes);

        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using obfuscated connection string (violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: concatenated or encoded parts for connection string
    public void connectWithConcatenatedObfuscation() {
        String host = "localhost";
        String port = "3306";
        String db = "testdb";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db; // considered as simple obfuscation pattern in some rules

        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using concatenated connection string (potential violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using plain connection string directly
    public void connectWithPlainString() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using plain connection string (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using connection from secure configuration service
    public void connectWithSecureConfig() {
        String url = SecureConfig.getJdbcUrl();
        String user = SecureConfig.getUser();
        String password = SecureConfig.getPassword();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using secure configuration service (non-violation).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}

// Mock secure configuration service
class SecureConfig {
    public static String getJdbcUrl() {
        return "jdbc:mysql://localhost:3306/testdb";
    }
    public static String getUser() {
        return System.getenv("DB_USER");
    }
    public static String getPassword() {
        return System.getenv("DB_PASSWORD");
    }
}
