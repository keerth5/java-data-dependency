package com.example.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-013: PropertiesFileConfiguration
 * Detects database configuration in properties files
 */
public class PropertiesFileConfigurationExample {
    
    public void loadDatabasePropertiesFromFile() throws SQLException, IOException {
        // Load database configuration from properties file
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties");
        props.load(input);
        
        String jdbcUrl = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");
        String driver = props.getProperty("db.driver");
        
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    public void loadDatabasePropertiesFromSystemProperties() throws SQLException {
        // Load database configuration from system properties
        Properties systemProps = System.getProperties();
        
        String jdbcUrl = systemProps.getProperty("database.url");
        String username = systemProps.getProperty("database.username");
        String password = systemProps.getProperty("database.password");
        
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    public void loadDatabasePropertiesFromFileSystem() throws SQLException, IOException {
        // Load database configuration from file system
        Properties props = new Properties();
        java.io.FileInputStream fis = new java.io.FileInputStream("config/database.properties");
        props.load(fis);
        fis.close();
        
        String jdbcUrl = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    public void loadDatabasePropertiesWithDefaultValues() throws SQLException, IOException {
        // Load database configuration with default values
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
        props.load(input);
        
        String jdbcUrl = props.getProperty("db.connection.url", "jdbc:mysql://localhost:3306/defaultdb");
        String username = props.getProperty("db.connection.username", "defaultuser");
        String password = props.getProperty("db.connection.password", "defaultpass");
        String driver = props.getProperty("db.connection.driver", "com.mysql.cj.jdbc.Driver");
        
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    public void loadDatabasePropertiesForDifferentEnvironments() throws SQLException, IOException {
        // Load database configuration for different environments
        String environment = System.getProperty("env", "development");
        String propertiesFile = "database-" + environment + ".properties";
        
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        props.load(input);
        
        String jdbcUrl = props.getProperty("database.url");
        String username = props.getProperty("database.user");
        String password = props.getProperty("database.pass");
        
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    public void loadDatabasePropertiesWithConnectionPool() throws SQLException, IOException {
        // Load database configuration for connection pool
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("connection-pool.properties");
        props.load(input);
        
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl(props.getProperty("pool.url"));
        dataSource.setUsername(props.getProperty("pool.username"));
        dataSource.setPassword(props.getProperty("pool.password"));
        dataSource.setDriverClassName(props.getProperty("pool.driver"));
        dataSource.setInitialSize(Integer.parseInt(props.getProperty("pool.initialSize", "5")));
        dataSource.setMaxTotal(Integer.parseInt(props.getProperty("pool.maxTotal", "20")));
        dataSource.setMaxIdle(Integer.parseInt(props.getProperty("pool.maxIdle", "10")));
        dataSource.setMinIdle(Integer.parseInt(props.getProperty("pool.minIdle", "5")));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void loadDatabasePropertiesWithHikariCP() throws SQLException, IOException {
        // Load database configuration for HikariCP
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("hikari.properties");
        props.load(input);
        
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl(props.getProperty("hikari.jdbcUrl"));
        config.setUsername(props.getProperty("hikari.username"));
        config.setPassword(props.getProperty("hikari.password"));
        config.setDriverClassName(props.getProperty("hikari.driverClassName"));
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximumPoolSize", "20")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimumIdle", "5")));
        config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connectionTimeout", "30000")));
        config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idleTimeout", "600000")));
        config.setMaxLifetime(Long.parseLong(props.getProperty("hikari.maxLifetime", "1800000")));
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void loadDatabasePropertiesWithC3P0() throws SQLException, IOException {
        // Load database configuration for C3P0
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("c3p0.properties");
        props.load(input);
        
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass(props.getProperty("c3p0.driverClass"));
            dataSource.setJdbcUrl(props.getProperty("c3p0.jdbcUrl"));
            dataSource.setUser(props.getProperty("c3p0.user"));
            dataSource.setPassword(props.getProperty("c3p0.password"));
            dataSource.setMinPoolSize(Integer.parseInt(props.getProperty("c3p0.minPoolSize", "5")));
            dataSource.setMaxPoolSize(Integer.parseInt(props.getProperty("c3p0.maxPoolSize", "20")));
            dataSource.setAcquireIncrement(Integer.parseInt(props.getProperty("c3p0.acquireIncrement", "5")));
            dataSource.setCheckoutTimeout(Integer.parseInt(props.getProperty("c3p0.checkoutTimeout", "30000")));
            
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }
    
    public void loadDatabasePropertiesWithTomcatPool() throws SQLException, IOException {
        // Load database configuration for Tomcat JDBC Pool
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("tomcat-pool.properties");
        props.load(input);
        
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(props.getProperty("tomcat.url"));
        dataSource.setUsername(props.getProperty("tomcat.username"));
        dataSource.setPassword(props.getProperty("tomcat.password"));
        dataSource.setDriverClassName(props.getProperty("tomcat.driverClassName"));
        dataSource.setInitialSize(Integer.parseInt(props.getProperty("tomcat.initialSize", "5")));
        dataSource.setMaxActive(Integer.parseInt(props.getProperty("tomcat.maxActive", "20")));
        dataSource.setMaxIdle(Integer.parseInt(props.getProperty("tomcat.maxIdle", "10")));
        dataSource.setMinIdle(Integer.parseInt(props.getProperty("tomcat.minIdle", "5")));
        dataSource.setMaxWait(Integer.parseInt(props.getProperty("tomcat.maxWait", "30000")));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void loadDatabasePropertiesWithSpringBoot() throws SQLException, IOException {
        // Load database configuration for Spring Boot
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");
        props.load(input);
        
        String springDatasourceUrl = props.getProperty("spring.datasource.url");
        String springDatasourceUsername = props.getProperty("spring.datasource.username");
        String springDatasourcePassword = props.getProperty("spring.datasource.password");
        String springDatasourceDriver = props.getProperty("spring.datasource.driver-class-name");
        
        Class.forName(springDatasourceDriver);
        Connection conn = DriverManager.getConnection(springDatasourceUrl, springDatasourceUsername, springDatasourcePassword);
        conn.close();
    }
    
    public void loadDatabasePropertiesWithCustomPrefix() throws SQLException, IOException {
        // Load database configuration with custom prefix
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("custom-db.properties");
        props.load(input);
        
        String customUrl = props.getProperty("custom.database.connection.url");
        String customUser = props.getProperty("custom.database.connection.user");
        String customPass = props.getProperty("custom.database.connection.pass");
        String customDriver = props.getProperty("custom.database.connection.driver");
        
        Class.forName(customDriver);
        Connection conn = DriverManager.getConnection(customUrl, customUser, customPass);
        conn.close();
    }
    
    public void loadDatabasePropertiesWithEncryption() throws SQLException, IOException {
        // Load database configuration with encrypted properties
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("encrypted-db.properties");
        props.load(input);
        
        String encryptedUrl = props.getProperty("encrypted.db.url");
        String encryptedUsername = props.getProperty("encrypted.db.username");
        String encryptedPassword = props.getProperty("encrypted.db.password");
        
        // Decrypt properties (simplified example)
        String decryptedUrl = decrypt(encryptedUrl);
        String decryptedUsername = decrypt(encryptedUsername);
        String decryptedPassword = decrypt(encryptedPassword);
        
        Connection conn = DriverManager.getConnection(decryptedUrl, decryptedUsername, decryptedPassword);
        conn.close();
    }
    
    private String decrypt(String encryptedValue) {
        // Simplified decryption method
        return encryptedValue; // In real implementation, this would decrypt the value
    }
    
    public void loadDatabasePropertiesWithValidation() throws SQLException, IOException {
        // Load database configuration with validation
        Properties props = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("validated-db.properties");
        props.load(input);
        
        String jdbcUrl = validateProperty(props, "db.url");
        String username = validateProperty(props, "db.username");
        String password = validateProperty(props, "db.password");
        String driver = validateProperty(props, "db.driver");
        
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    private String validateProperty(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Required property " + key + " is missing or empty");
        }
        return value;
    }
}
