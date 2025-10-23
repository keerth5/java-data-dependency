package com.example.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test file for sql-java-017: MaxPoolSizeConfiguration
 * Detects maximum pool size configuration
 */
public class MaxPoolSizeConfigurationExample {
    
    public void maxPoolSizeWithHikariCP() throws SQLException {
        // Maximum pool size with HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // HikariCP maximum pool size configuration
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setInitializationFailTimeout(1);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithDBCP() throws SQLException {
        // Maximum pool size with DBCP
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // DBCP maximum pool size configuration
        dataSource.setMaxTotal(25);
        dataSource.setMaxIdle(15);
        dataSource.setMinIdle(5);
        dataSource.setInitialSize(5);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithC3P0() throws SQLException {
        // Maximum pool size with C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            
            // C3P0 maximum pool size configuration
            dataSource.setMaxPoolSize(30);
            dataSource.setMinPoolSize(5);
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
    
    public void maxPoolSizeWithTomcatPool() throws SQLException {
        // Maximum pool size with Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Tomcat JDBC Pool maximum pool size configuration
        dataSource.setMaxActive(35);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(5);
        dataSource.setInitialSize(5);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithSpringBoot() throws SQLException {
        // Maximum pool size with Spring Boot configuration
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        
        // Spring Boot maximum pool size settings
        config.setMaximumPoolSize(10); // spring.datasource.hikari.maximum-pool-size
        config.setMinimumIdle(2); // spring.datasource.hikari.minimum-idle
        config.setConnectionTimeout(20000); // spring.datasource.hikari.connection-timeout
        config.setIdleTimeout(300000); // spring.datasource.hikari.idle-timeout
        config.setMaxLifetime(1200000); // spring.datasource.hikari.max-lifetime
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithProperties() throws SQLException {
        // Maximum pool size with properties configuration
        java.util.Properties props = new java.util.Properties();
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        props.setProperty("url", "jdbc:mysql://localhost:3306/testdb");
        props.setProperty("username", "user");
        props.setProperty("password", "password");
        props.setProperty("maximumPoolSize", "40");
        props.setProperty("minimumIdle", "8");
        props.setProperty("initializationFailTimeout", "1");
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setJdbcUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));
        dataSource.setDriverClassName(props.getProperty("driverClassName"));
        dataSource.setMaximumPoolSize(Integer.parseInt(props.getProperty("maximumPoolSize")));
        dataSource.setMinimumIdle(Integer.parseInt(props.getProperty("minimumIdle")));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithEnvironmentVariables() throws SQLException {
        // Maximum pool size with environment variables
        String maxPoolSize = System.getProperty("db.max.pool.size", "20");
        String minIdle = System.getProperty("db.min.idle", "5");
        
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        config.setMinimumIdle(Integer.parseInt(minIdle));
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithConfigurationFile() throws SQLException {
        // Maximum pool size with configuration file
        java.util.Properties config = new java.util.Properties();
        try {
            config.load(getClass().getClassLoader().getResourceAsStream("pool-size.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String maxPoolSize = config.getProperty("pool.max.size", "25");
        String minIdle = config.getProperty("pool.min.idle", "5");
        String initialSize = config.getProperty("pool.initial.size", "5");
        
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaxTotal(Integer.parseInt(maxPoolSize));
        dataSource.setMinIdle(Integer.parseInt(minIdle));
        dataSource.setInitialSize(Integer.parseInt(initialSize));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithJNDI() throws SQLException {
        // Maximum pool size with JNDI DataSource
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
            
            // JNDI DataSource with configured maximum pool size
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void maxPoolSizeWithCustomDataSource() throws SQLException {
        // Maximum pool size with custom DataSource
        CustomDataSource dataSource = new CustomDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setMaxPoolSize(50);
        dataSource.setMinPoolSize(10);
        dataSource.setInitialSize(10);
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void maxPoolSizeWithDifferentEnvironments() throws SQLException {
        // Maximum pool size for different environments
        String environment = System.getProperty("env", "development");
        int maxPoolSize;
        int minIdle;
        
        switch (environment) {
            case "production":
                maxPoolSize = 100;
                minIdle = 20;
                break;
            case "staging":
                maxPoolSize = 50;
                minIdle = 10;
                break;
            case "testing":
                maxPoolSize = 20;
                minIdle = 5;
                break;
            default: // development
                maxPoolSize = 10;
                minIdle = 2;
        }
        
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void maxPoolSizeWithMonitoring() throws SQLException {
        // Maximum pool size with monitoring
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(30);
        config.setMinimumIdle(5);
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
    
    public void maxPoolSizeWithValidation() throws SQLException {
        // Maximum pool size with validation
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // Pool size configuration with validation
        dataSource.setMaxTotal(40);
        dataSource.setMaxIdle(20);
        dataSource.setMinIdle(5);
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
    
    // Custom DataSource class for demonstration
    private static class CustomDataSource {
        private String url;
        private String username;
        private String password;
        private int maxPoolSize;
        private int minPoolSize;
        private int initialSize;
        
        public void setUrl(String url) { this.url = url; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setMaxPoolSize(int maxPoolSize) { this.maxPoolSize = maxPoolSize; }
        public void setMinPoolSize(int minPoolSize) { this.minPoolSize = minPoolSize; }
        public void setInitialSize(int initialSize) { this.initialSize = initialSize; }
        
        public Connection getConnection() throws SQLException {
            return java.sql.DriverManager.getConnection(url, username, password);
        }
    }
}
