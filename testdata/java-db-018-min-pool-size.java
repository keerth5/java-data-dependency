package com.example.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test file for sql-java-018: MinPoolSizeConfiguration
 * Detects minimum pool size configuration
 */
public class MinPoolSizeConfigurationExample {
    
    public void minPoolSizeWithHikariCP() throws SQLException {
        // Minimum pool size with HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // HikariCP minimum pool size configuration
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        config.setInitializationFailTimeout(1);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithDBCP() throws SQLException {
        // Minimum pool size with DBCP
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // DBCP minimum pool size configuration
        dataSource.setMinIdle(8);
        dataSource.setMaxTotal(25);
        dataSource.setMaxIdle(15);
        dataSource.setInitialSize(5);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithC3P0() throws SQLException {
        // Minimum pool size with C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            
            // C3P0 minimum pool size configuration
            dataSource.setMinPoolSize(10);
            dataSource.setMaxPoolSize(30);
            dataSource.setInitialPoolSize(5);
            dataSource.setAcquireIncrement(5);
            dataSource.setMaxStatements(50);
            dataSource.setMaxStatementsPerConnection(5);
            
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }
    
    public void minPoolSizeWithTomcatPool() throws SQLException {
        // Minimum pool size with Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Tomcat JDBC Pool minimum pool size configuration
        dataSource.setMinIdle(6);
        dataSource.setMaxActive(35);
        dataSource.setMaxIdle(20);
        dataSource.setInitialSize(5);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithSpringBoot() throws SQLException {
        // Minimum pool size with Spring Boot configuration
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        
        // Spring Boot minimum pool size settings
        config.setMinimumIdle(2); // spring.datasource.hikari.minimum-idle
        config.setMaximumPoolSize(10); // spring.datasource.hikari.maximum-pool-size
        config.setConnectionTimeout(20000); // spring.datasource.hikari.connection-timeout
        config.setIdleTimeout(300000); // spring.datasource.hikari.idle-timeout
        config.setMaxLifetime(1200000); // spring.datasource.hikari.max-lifetime
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithProperties() throws SQLException {
        // Minimum pool size with properties configuration
        java.util.Properties props = new java.util.Properties();
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        props.setProperty("url", "jdbc:mysql://localhost:3306/testdb");
        props.setProperty("username", "user");
        props.setProperty("password", "password");
        props.setProperty("minimumIdle", "8");
        props.setProperty("maximumPoolSize", "40");
        props.setProperty("initializationFailTimeout", "1");
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setJdbcUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));
        dataSource.setDriverClassName(props.getProperty("driverClassName"));
        dataSource.setMinimumIdle(Integer.parseInt(props.getProperty("minimumIdle")));
        dataSource.setMaximumPoolSize(Integer.parseInt(props.getProperty("maximumPoolSize")));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithEnvironmentVariables() throws SQLException {
        // Minimum pool size with environment variables
        String minIdle = System.getProperty("db.min.idle", "5");
        String maxPoolSize = System.getProperty("db.max.pool.size", "20");
        
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("org.postgresql.Driver");
        config.setMinimumIdle(Integer.parseInt(minIdle));
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithConfigurationFile() throws SQLException {
        // Minimum pool size with configuration file
        java.util.Properties config = new java.util.Properties();
        try {
            config.load(getClass().getClassLoader().getResourceAsStream("min-pool-size.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String minIdle = config.getProperty("pool.min.idle", "5");
        String maxPoolSize = config.getProperty("pool.max.size", "25");
        String initialSize = config.getProperty("pool.initial.size", "5");
        
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMinIdle(Integer.parseInt(minIdle));
        dataSource.setMaxTotal(Integer.parseInt(maxPoolSize));
        dataSource.setInitialSize(Integer.parseInt(initialSize));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithJNDI() throws SQLException {
        // Minimum pool size with JNDI DataSource
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
            
            // JNDI DataSource with configured minimum pool size
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void minPoolSizeWithCustomDataSource() throws SQLException {
        // Minimum pool size with custom DataSource
        CustomDataSource dataSource = new CustomDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setMinPoolSize(10);
        dataSource.setMaxPoolSize(50);
        dataSource.setInitialSize(10);
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void minPoolSizeWithDifferentEnvironments() throws SQLException {
        // Minimum pool size for different environments
        String environment = System.getProperty("env", "development");
        int minIdle;
        int maxPoolSize;
        
        switch (environment) {
            case "production":
                minIdle = 20;
                maxPoolSize = 100;
                break;
            case "staging":
                minIdle = 10;
                maxPoolSize = 50;
                break;
            case "testing":
                minIdle = 5;
                maxPoolSize = 20;
                break;
            default: // development
                minIdle = 2;
                maxPoolSize = 10;
        }
        
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithMonitoring() throws SQLException {
        // Minimum pool size with monitoring
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMinimumIdle(8);
        config.setMaximumPoolSize(30);
        config.setLeakDetectionThreshold(60000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        
        // Get pool metrics
        com.zaxxer.hikari.HikariPoolMXBean poolBean = dataSource.getHikariPoolMXBean();
        int activeConnections = poolBean.getActiveConnections();
        int idleConnections = poolBean.getIdleConnections();
        int totalConnections = poolBean.getTotalConnections();
        
        System.out.println("Active: " + activeConnections + ", Idle: " + idleConnections + ", Total: " + totalConnections);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithValidation() throws SQLException {
        // Minimum pool size with validation
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // Pool size configuration with validation
        dataSource.setMinIdle(8);
        dataSource.setMaxTotal(40);
        dataSource.setMaxIdle(20);
        dataSource.setInitialSize(5);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithStartupPerformance() throws SQLException {
        // Minimum pool size for startup performance
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Optimized for startup performance
        config.setMinimumIdle(1); // Start with minimal connections
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void minPoolSizeWithResourceOptimization() throws SQLException {
        // Minimum pool size for resource optimization
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Resource optimized configuration
        dataSource.setMinIdle(3); // Minimal idle connections
        dataSource.setMaxTotal(15);
        dataSource.setMaxIdle(8);
        dataSource.setInitialSize(3);
        dataSource.setMaxWaitMillis(30000);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    // Custom DataSource class for demonstration
    private static class CustomDataSource {
        private String url;
        private String username;
        private String password;
        private int minPoolSize;
        private int maxPoolSize;
        private int initialSize;
        
        public void setUrl(String url) { this.url = url; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setMinPoolSize(int minPoolSize) { this.minPoolSize = minPoolSize; }
        public void setMaxPoolSize(int maxPoolSize) { this.maxPoolSize = maxPoolSize; }
        public void setInitialSize(int initialSize) { this.initialSize = initialSize; }
        
        public Connection getConnection() throws SQLException {
            return java.sql.DriverManager.getConnection(url, username, password);
        }
    }
}
