package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import java.util.Properties;

/**
 * Test file for sql-java-002: DriverManagerUsage
 * Detects DriverManager.getConnection() usage
 */
public class DriverManagerExample {
    
    public void basicDriverManagerUsage() throws SQLException {
        // Basic DriverManager.getConnection() usage
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "user";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void driverManagerWithProperties() throws SQLException {
        // DriverManager.getConnection() with Properties
        String url = "jdbc:mysql://localhost:3306/testdb";
        Properties props = new Properties();
        props.setProperty("user", "admin");
        props.setProperty("password", "secret");
        props.setProperty("useSSL", "true");
        props.setProperty("serverTimezone", "UTC");
        
        Connection conn = DriverManager.getConnection(url, props);
        conn.close();
    }
    
    public void driverManagerWithUrlOnly() throws SQLException {
        // DriverManager.getConnection() with URL containing credentials
        String url = "jdbc:mysql://user:password@localhost:3306/testdb";
        Connection conn = DriverManager.getConnection(url);
        conn.close();
    }
    
    public void driverManagerStaticMethods() {
        // DriverManager static methods
        try {
            // Register driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Get drivers
            java.util.Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                System.out.println("Driver: " + driver.getClass().getName());
            }
            
            // Get login timeout
            int loginTimeout = DriverManager.getLoginTimeout();
            DriverManager.setLoginTimeout(30);
            
            // Get log writer
            java.io.PrintWriter logWriter = DriverManager.getLogWriter();
            DriverManager.setLogWriter(logWriter);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void driverManagerInTryWithResources() {
        // DriverManager in try-with-resources
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "postgres";
        String password = "postgres";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Use connection
            System.out.println("Connection established: " + conn.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void driverManagerWithDifferentDatabases() throws SQLException {
        // DriverManager with different database URLs
        
        // MySQL
        Connection mysqlConn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/mysql_db", "root", "root");
        mysqlConn.close();
        
        // PostgreSQL
        Connection pgConn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres_db", "postgres", "postgres");
        pgConn.close();
        
        // Oracle
        Connection oracleConn = DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:xe", "system", "oracle");
        oracleConn.close();
        
        // H2
        Connection h2Conn = DriverManager.getConnection(
            "jdbc:h2:mem:testdb", "sa", "");
        h2Conn.close();
    }
    
    public void driverManagerInMethod() throws SQLException {
        // DriverManager usage in method
        Connection conn = createConnection();
        if (conn != null) {
            conn.close();
        }
    }
    
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/appdb", "appuser", "apppass");
    }
}
