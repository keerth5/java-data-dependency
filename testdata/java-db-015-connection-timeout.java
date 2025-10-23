package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-015: ConnectionTimeoutConfiguration
 * Detects connection timeout settings
 */
public class ConnectionTimeoutConfigurationExample {
    
    public void basicConnectionTimeoutConfiguration() throws SQLException {
        // Basic connection timeout configuration
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "user";
        String password = "password";
        
        // Set connection timeout
        DriverManager.setLoginTimeout(30);
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void connectionTimeoutWithProperties() throws SQLException {
        // Connection timeout with properties
        String url = "jdbc:mysql://localhost:3306/testdb";
        Properties props = new Properties();
        props.setProperty("user", "user");
        props.setProperty("password", "password");
        props.setProperty("connectTimeout", "30000");
        props.setProperty("socketTimeout", "60000");
        props.setProperty("loginTimeout", "30");
        
        Connection conn = DriverManager.getConnection(url, props);
        conn.close();
    }
    
    public void connectionTimeoutWithHikariCP() throws SQLException {
        // Connection timeout with HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // HikariCP connection timeout settings
        config.setConnectionTimeout(30000);
        config.setValidationTimeout(5000);
        config.setLeakDetectionThreshold(60000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionTimeoutWithDBCP() throws SQLException {
        // Connection timeout with DBCP
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // DBCP connection timeout settings
        dataSource.setMaxWaitMillis(30000);
        dataSource.setValidationQueryTimeout(5);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionTimeoutWithC3P0() throws SQLException {
        // Connection timeout with C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            
            // C3P0 connection timeout settings
            dataSource.setCheckoutTimeout(30000);
            dataSource.setAcquireRetryAttempts(3);
            dataSource.setAcquireRetryDelay(1000);
            dataSource.setBreakAfterAcquireFailure(false);
            dataSource.setMaxIdleTime(300);
            dataSource.setMaxConnectionAge(3600);
            
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }
    
    public void connectionTimeoutWithTomcatPool() throws SQLException {
        // Connection timeout with Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Tomcat JDBC Pool connection timeout settings
        dataSource.setMaxWait(30000);
        dataSource.setValidationQueryTimeout(5);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        dataSource.setMaxEvictableIdleTimeMillis(300000);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionTimeoutWithSQLServer() throws SQLException {
        // Connection timeout with SQL Server
        String url = "jdbc:sqlserver://localhost:1433;databaseName=TestDB;loginTimeout=30;connectRetryCount=3;connectRetryInterval=10";
        String username = "sa";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void connectionTimeoutWithOracle() throws SQLException {
        // Connection timeout with Oracle
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        Properties props = new Properties();
        props.setProperty("user", "system");
        props.setProperty("password", "oracle");
        props.setProperty("oracle.net.CONNECT_TIMEOUT", "10000");
        props.setProperty("oracle.jdbc.ReadTimeout", "30000");
        props.setProperty("oracle.net.READ_TIMEOUT", "30000");
        
        Connection conn = DriverManager.getConnection(url, props);
        conn.close();
    }
    
    public void connectionTimeoutWithMySQL() throws SQLException {
        // Connection timeout with MySQL
        String url = "jdbc:mysql://localhost:3306/testdb?connectTimeout=30000&socketTimeout=60000&autoReconnect=true&maxReconnects=3";
        String username = "root";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void connectionTimeoutWithPostgreSQL() throws SQLException {
        // Connection timeout with PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/testdb?connectTimeout=30&socketTimeout=60&loginTimeout=30";
        String username = "postgres";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void connectionTimeoutWithH2() throws SQLException {
        // Connection timeout with H2
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        Properties props = new Properties();
        props.setProperty("user", "sa");
        props.setProperty("password", "");
        props.setProperty("LOGIN_TIMEOUT", "30");
        
        Connection conn = DriverManager.getConnection(url, props);
        conn.close();
    }
    
    public void connectionTimeoutWithSpringBoot() throws SQLException {
        // Connection timeout with Spring Boot configuration
        String springDatasourceUrl = "jdbc:mysql://localhost:3306/testdb";
        String springDatasourceUsername = "user";
        String springDatasourcePassword = "password";
        
        // Spring Boot connection timeout settings
        DriverManager.setLoginTimeout(30);
        
        Connection conn = DriverManager.getConnection(springDatasourceUrl, springDatasourceUsername, springDatasourcePassword);
        conn.close();
    }
    
    public void connectionTimeoutWithJNDI() throws SQLException {
        // Connection timeout with JNDI DataSource
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
            
            // Set connection timeout for JNDI DataSource
            DriverManager.setLoginTimeout(30);
            
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void connectionTimeoutWithCustomDataSource() throws SQLException {
        // Connection timeout with custom DataSource
        CustomDataSource dataSource = new CustomDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setConnectionTimeout(30000);
        dataSource.setLoginTimeout(30);
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void connectionTimeoutWithEnvironmentVariables() throws SQLException {
        // Connection timeout with environment variables
        String connectionTimeout = System.getProperty("db.connection.timeout", "30000");
        String loginTimeout = System.getProperty("db.login.timeout", "30");
        
        DriverManager.setLoginTimeout(Integer.parseInt(loginTimeout));
        
        String url = "jdbc:mysql://localhost:3306/testdb?connectTimeout=" + connectionTimeout;
        String username = "user";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    public void connectionTimeoutWithConfigurationFile() throws SQLException {
        // Connection timeout with configuration file
        java.util.Properties config = new java.util.Properties();
        try {
            config.load(getClass().getClassLoader().getResourceAsStream("connection-timeout.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String connectionTimeout = config.getProperty("connection.timeout", "30000");
        String loginTimeout = config.getProperty("login.timeout", "30");
        
        DriverManager.setLoginTimeout(Integer.parseInt(loginTimeout));
        
        String url = "jdbc:mysql://localhost:3306/testdb?connectTimeout=" + connectionTimeout;
        String username = "user";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.close();
    }
    
    // Custom DataSource class for demonstration
    private static class CustomDataSource {
        private String url;
        private String username;
        private String password;
        private int connectionTimeout;
        private int loginTimeout;
        
        public void setUrl(String url) { this.url = url; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        public void setLoginTimeout(int loginTimeout) { this.loginTimeout = loginTimeout; }
        
        public Connection getConnection() throws SQLException {
            DriverManager.setLoginTimeout(loginTimeout);
            return DriverManager.getConnection(url, username, password);
        }
    }
}
