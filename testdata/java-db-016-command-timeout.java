package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-016: CommandTimeoutConfiguration
 * Detects query timeout settings
 */
public class CommandTimeoutConfigurationExample {
    
    public void basicCommandTimeoutConfiguration() throws SQLException {
        // Basic command timeout configuration
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "user";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        
        // Set query timeout on Statement
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // 30 seconds
        
        // Set query timeout on PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        pstmt.setQueryTimeout(60); // 60 seconds
        
        stmt.close();
        pstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithStatement() throws SQLException {
        // Command timeout with Statement
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(45); // 45 seconds timeout
        
        // Execute query with timeout
        java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM large_table");
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithPreparedStatement() throws SQLException {
        // Command timeout with PreparedStatement
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE status = ? AND created_date > ?");
        pstmt.setQueryTimeout(90); // 90 seconds timeout
        
        pstmt.setString(1, "active");
        pstmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        
        java.sql.ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithCallableStatement() throws SQLException {
        // Command timeout with CallableStatement
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        
        java.sql.CallableStatement cstmt = conn.prepareCall("{call long_running_procedure(?)}");
        cstmt.setQueryTimeout(120); // 2 minutes timeout
        
        cstmt.setString(1, "parameter_value");
        cstmt.execute();
        
        cstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithHikariCP() throws SQLException {
        // Command timeout with HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // HikariCP doesn't have direct query timeout, but we can set it on statements
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30);
        
        stmt.close();
        conn.close();
        dataSource.close();
    }
    
    public void commandTimeoutWithDBCP() throws SQLException {
        // Command timeout with DBCP
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM complex_view");
        pstmt.setQueryTimeout(60);
        
        java.sql.ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
        dataSource.close();
    }
    
    public void commandTimeoutWithC3P0() throws SQLException {
        // Command timeout with C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(45);
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }
    
    public void commandTimeoutWithTomcatPool() throws SQLException {
        // Command timeout with Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM large_table WHERE condition = ?");
        pstmt.setQueryTimeout(90);
        
        pstmt.setString(1, "value");
        java.sql.ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
        dataSource.close();
    }
    
    public void commandTimeoutWithSQLServer() throws SQLException {
        // Command timeout with SQL Server
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB";
        String username = "sa";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        
        // SQL Server specific query timeout
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30);
        
        // SQL Server stored procedure with timeout
        java.sql.CallableStatement cstmt = conn.prepareCall("{call sp_long_running_procedure}");
        cstmt.setQueryTimeout(120);
        
        stmt.close();
        cstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithOracle() throws SQLException {
        // Command timeout with Oracle
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "oracle";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        
        // Oracle query timeout
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(60);
        
        // Oracle PL/SQL block with timeout
        PreparedStatement pstmt = conn.prepareStatement("BEGIN long_running_plsql_block; END;");
        pstmt.setQueryTimeout(180);
        
        stmt.close();
        pstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithMySQL() throws SQLException {
        // Command timeout with MySQL
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "root";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        
        // MySQL query timeout
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30);
        
        // MySQL stored procedure with timeout
        java.sql.CallableStatement cstmt = conn.prepareCall("{call long_running_procedure()}");
        cstmt.setQueryTimeout(90);
        
        stmt.close();
        cstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithPostgreSQL() throws SQLException {
        // Command timeout with PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String username = "postgres";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        
        // PostgreSQL query timeout
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(45);
        
        // PostgreSQL function with timeout
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM long_running_function()");
        pstmt.setQueryTimeout(120);
        
        stmt.close();
        pstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithBatchOperations() throws SQLException {
        // Command timeout with batch operations
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO batch_table (name, value) VALUES (?, ?)");
        pstmt.setQueryTimeout(60); // Set timeout for batch operations
        
        // Add batch statements
        for (int i = 0; i < 1000; i++) {
            pstmt.setString(1, "name" + i);
            pstmt.setInt(2, i);
            pstmt.addBatch();
        }
        
        // Execute batch with timeout
        int[] results = pstmt.executeBatch();
        
        pstmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithTransaction() throws SQLException {
        // Command timeout with transaction
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);
            
            // Multiple statements in transaction with timeout
            stmt.executeUpdate("UPDATE table1 SET status = 'processing'");
            stmt.executeUpdate("UPDATE table2 SET processed = true");
            stmt.executeUpdate("INSERT INTO log_table (action) VALUES ('batch_processed')");
            
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    
    public void commandTimeoutWithSpringBoot() throws SQLException {
        // Command timeout with Spring Boot configuration
        String springDatasourceUrl = "jdbc:mysql://localhost:3306/testdb";
        String springDatasourceUsername = "user";
        String springDatasourcePassword = "password";
        
        Connection conn = DriverManager.getConnection(springDatasourceUrl, springDatasourceUsername, springDatasourcePassword);
        
        // Spring Boot query timeout configuration
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(30); // From spring.datasource.hikari.connection-timeout
        
        stmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithJNDI() throws SQLException {
        // Command timeout with JNDI DataSource
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
            
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM complex_query");
            pstmt.setQueryTimeout(60);
            
            java.sql.ResultSet rs = pstmt.executeQuery();
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void commandTimeoutWithEnvironmentVariables() throws SQLException {
        // Command timeout with environment variables
        String queryTimeout = System.getProperty("db.query.timeout", "30");
        int timeoutSeconds = Integer.parseInt(queryTimeout);
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(timeoutSeconds);
        
        stmt.close();
        conn.close();
    }
    
    public void commandTimeoutWithConfigurationFile() throws SQLException {
        // Command timeout with configuration file
        java.util.Properties config = new java.util.Properties();
        try {
            config.load(getClass().getClassLoader().getResourceAsStream("query-timeout.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String queryTimeout = config.getProperty("query.timeout", "30");
        int timeoutSeconds = Integer.parseInt(queryTimeout);
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users");
        pstmt.setQueryTimeout(timeoutSeconds);
        
        java.sql.ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
}
