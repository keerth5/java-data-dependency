package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test file for sql-java-003: SqlServerJdbcDriverUsage
 * Detects SQL Server JDBC driver usage (com.microsoft.sqlserver.jdbc)
 */
public class SqlServerDriverExample {
    
    public void sqlServerDriverUsage() throws SQLException {
        // Microsoft SQL Server JDBC driver usage
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;encrypt=true;trustServerCertificate=true";
        String username = "sa";
        String password = "YourStrong@Passw0rd";
        
        // Load Microsoft SQL Server driver
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void sqlServerDriverWithProperties() throws SQLException {
        // SQL Server driver with connection properties
        String url = "jdbc:sqlserver://server:1433;databaseName=MyDB";
        java.util.Properties props = new java.util.Properties();
        props.setProperty("user", "admin");
        props.setProperty("password", "password123");
        props.setProperty("encrypt", "true");
        props.setProperty("trustServerCertificate", "true");
        props.setProperty("loginTimeout", "30");
        
        Connection conn = DriverManager.getConnection(url, props);
        conn.close();
    }
    
    public void sqlServerDriverDifferentVersions() throws SQLException {
        // Different versions of Microsoft SQL Server driver
        
        // SQL Server 2019+ driver
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // Connection with SQL Server 2019+ features
        String url2019 = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;encrypt=true;trustServerCertificate=true;authenticationScheme=JavaKerberos";
        Connection conn2019 = DriverManager.getConnection(url2019, "user", "pass");
        conn2019.close();
        
        // Connection with Always Encrypted
        String urlEncrypted = "jdbc:sqlserver://localhost:1433;databaseName=SecureDB;columnEncryptionSetting=Enabled";
        Connection connEncrypted = DriverManager.getConnection(urlEncrypted, "user", "pass");
        connEncrypted.close();
    }
    
    public void sqlServerDriverWithAuthentication() throws SQLException {
        // SQL Server driver with different authentication methods
        
        // Windows Authentication
        String urlWindowsAuth = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;integratedSecurity=true";
        Connection connWindows = DriverManager.getConnection(urlWindowsAuth);
        connWindows.close();
        
        // SQL Server Authentication
        String urlSqlAuth = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;user=sa;password=password";
        Connection connSql = DriverManager.getConnection(urlSqlAuth);
        connSql.close();
        
        // Azure SQL Database
        String urlAzure = "jdbc:sqlserver://myserver.database.windows.net:1433;databaseName=MyDB;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
        Connection connAzure = DriverManager.getConnection(urlAzure, "user@myserver", "password");
        connAzure.close();
    }
    
    public void sqlServerDriverConnectionString() throws SQLException {
        // Various SQL Server connection string formats
        
        // Basic connection string
        String basicUrl = "jdbc:sqlserver://localhost:1433;databaseName=Northwind";
        Connection basicConn = DriverManager.getConnection(basicUrl, "sa", "password");
        basicConn.close();
        
        // Connection string with multiple properties
        String complexUrl = "jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;encrypt=true;trustServerCertificate=true;loginTimeout=30;sendStringParametersAsUnicode=false";
        Connection complexConn = DriverManager.getConnection(complexUrl, "user", "pass");
        complexConn.close();
        
        // Connection string with instance name
        String instanceUrl = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=TestDB";
        Connection instanceConn = DriverManager.getConnection(instanceUrl, "sa", "password");
        instanceConn.close();
    }
    
    public void sqlServerDriverInDataSource() throws SQLException {
        // SQL Server driver used with DataSource
        com.microsoft.sqlserver.jdbc.SQLServerDataSource dataSource = 
            new com.microsoft.sqlserver.jdbc.SQLServerDataSource();
        
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("TestDB");
        dataSource.setUser("sa");
        dataSource.setPassword("password");
        dataSource.setEncrypt(true);
        dataSource.setTrustServerCertificate(true);
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void sqlServerDriverClassLoading() {
        // Explicit SQL Server driver class loading
        try {
            // Load the driver class
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Alternative way to load driver
            com.microsoft.sqlserver.jdbc.SQLServerDriver driver = 
                new com.microsoft.sqlserver.jdbc.SQLServerDriver();
            
            System.out.println("SQL Server driver loaded successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server driver not found: " + e.getMessage());
        }
    }
}
