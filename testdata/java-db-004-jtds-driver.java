package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test file for sql-java-004: JtdsDriverUsage
 * Detects jTDS JDBC driver usage for SQL Server
 */
public class JtdsDriverExample {
    
    public void jtdsDriverUsage() throws SQLException {
        // jTDS driver usage for SQL Server
        String url = "jdbc:jtds:sqlserver://localhost:1433/TestDB";
        String username = "sa";
        String password = "password";
        
        // Load jTDS driver
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void jtdsDriverWithProperties() throws SQLException {
        // jTDS driver with connection properties
        String url = "jdbc:jtds:sqlserver://server:1433/MyDB";
        java.util.Properties props = new java.util.Properties();
        props.setProperty("user", "admin");
        props.setProperty("password", "secret");
        props.setProperty("domain", "MYDOMAIN");
        props.setProperty("useNTLMv2", "true");
        props.setProperty("ssl", "require");
        
        Connection conn = DriverManager.getConnection(url, props);
        conn.close();
    }
    
    public void jtdsDriverDifferentFormats() throws SQLException {
        // Different jTDS connection string formats
        
        // Basic format
        String basicUrl = "jdbc:jtds:sqlserver://localhost:1433/AdventureWorks";
        Connection basicConn = DriverManager.getConnection(basicUrl, "sa", "password");
        basicConn.close();
        
        // With instance name
        String instanceUrl = "jdbc:jtds:sqlserver://localhost:1433/TestDB;instance=SQLEXPRESS";
        Connection instanceConn = DriverManager.getConnection(instanceUrl, "user", "pass");
        instanceConn.close();
        
        // With domain authentication
        String domainUrl = "jdbc:jtds:sqlserver://server:1433/DB;domain=COMPANY;useNTLMv2=true";
        Connection domainConn = DriverManager.getConnection(domainUrl, "username", "password");
        domainConn.close();
    }
    
    public void jtdsDriverWithSSL() throws SQLException {
        // jTDS driver with SSL configuration
        String urlSSL = "jdbc:jtds:sqlserver://secure-server:1433/ProductionDB;ssl=require;sslProtocol=TLSv1.2";
        Connection sslConn = DriverManager.getConnection(urlSSL, "admin", "securepass");
        sslConn.close();
        
        // jTDS with SSL and certificate validation
        String urlSSLCert = "jdbc:jtds:sqlserver://server:1433/DB;ssl=require;sslProtocol=TLSv1.2;certificate=server.crt";
        Connection certConn = DriverManager.getConnection(urlSSLCert, "user", "pass");
        certConn.close();
    }
    
    public void jtdsDriverWithNTLM() throws SQLException {
        // jTDS driver with NTLM authentication
        String urlNTLM = "jdbc:jtds:sqlserver://server:1433/Database;domain=WORKGROUP;useNTLMv2=true";
        Connection ntlmConn = DriverManager.getConnection(urlNTLM, "username", "password");
        ntlmConn.close();
        
        // jTDS with NTLM and specific authentication
        String urlNTLMv2 = "jdbc:jtds:sqlserver://server:1433/DB;domain=DOMAIN;useNTLMv2=true;authentication=NTLM";
        Connection ntlmv2Conn = DriverManager.getConnection(urlNTLMv2, "user", "pass");
        ntlmv2Conn.close();
    }
    
    public void jtdsDriverWithAdvancedOptions() throws SQLException {
        // jTDS driver with advanced connection options
        String urlAdvanced = "jdbc:jtds:sqlserver://localhost:1433/TestDB;" +
                           "sendStringParametersAsUnicode=false;" +
                           "prepareSQL=3;" +
                           "lobBuffer=32768;" +
                           "loginTimeout=30;" +
                           "socketTimeout=60";
        
        Connection advancedConn = DriverManager.getConnection(urlAdvanced, "sa", "password");
        advancedConn.close();
    }
    
    public void jtdsDriverInDataSource() throws SQLException {
        // jTDS driver used with DataSource
        net.sourceforge.jtds.jdbcx.JtdsDataSource dataSource = 
            new net.sourceforge.jtds.jdbcx.JtdsDataSource();
        
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("TestDB");
        dataSource.setUser("sa");
        dataSource.setPassword("password");
        dataSource.setDomain("WORKGROUP");
        dataSource.setUseNTLMV2(true);
        dataSource.setSsl("require");
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void jtdsDriverClassLoading() {
        // Explicit jTDS driver class loading
        try {
            // Load the jTDS driver class
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            
            // Alternative way to load jTDS driver
            net.sourceforge.jtds.jdbc.Driver driver = 
                new net.sourceforge.jtds.jdbc.Driver();
            
            System.out.println("jTDS driver loaded successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("jTDS driver not found: " + e.getMessage());
        }
    }
    
    public void jtdsDriverWithSybase() throws SQLException {
        // jTDS driver can also be used for Sybase
        String sybaseUrl = "jdbc:jtds:sybase://localhost:5000/MySybaseDB";
        Connection sybaseConn = DriverManager.getConnection(sybaseUrl, "sa", "password");
        sybaseConn.close();
        
        // jTDS Sybase with properties
        String sybaseUrlProps = "jdbc:jtds:sybase://server:5000/DB;charset=utf8;prepareSQL=3";
        Connection sybasePropsConn = DriverManager.getConnection(sybaseUrlProps, "user", "pass");
        sybasePropsConn.close();
    }
}
