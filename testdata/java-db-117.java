// @testdata/DatabaseCredentialsHardcodingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑117: DatabaseCredentialsHardcoding
 */
public class DatabaseCredentialsHardcodingTest {

    // Violation: hardcoded credentials directly in code
    private static final String URL_HARDCODED = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER_HARDCODED = "root";
    private static final String PASSWORD_HARDCODED = "password123";

    public void connectWithHardcodedCredentials() {
        try (Connection conn = DriverManager.getConnection(URL_HARDCODED, USER_HARDCODED, PASSWORD_HARDCODED)) {
            System.out.println("Connected using hardcoded credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: credentials obtained from environment variables
    public void connectWithEnvCredentials() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using environment credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: credentials obtained from external configuration
    public void connectWithConfigCredentials() {
        Config config = Config.loadFromFile("dbconfig.properties");
        String url = config.getUrl();
        String user = config.getUser();
        String password = config.getPassword();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected using config credentials.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Simulated external config class
    static class Config {
        private String url;
        private String user;
        private String password;

        public static Config loadFromFile(String path) {
            Config c = new Config();
            c.url = "jdbc:mysql://localhost:3306/testdb"; // loaded from file
            c.user = "configUser";
            c.password = "configPassword";
            return c;
        }

        public String getUrl() { return url; }
        public String getUser() { return user; }
        public String getPassword() { return password; }
    }
}
