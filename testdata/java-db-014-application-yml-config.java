package com.example.database;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test file for sql-java-014: ApplicationYmlConfiguration
 * Detects database configuration in YAML files
 */
@Configuration
@PropertySource("classpath:application.yml")
public class ApplicationYmlConfigurationExample {
    
    // Spring Boot YAML configuration properties
    @Value("${spring.datasource.url}")
    private String springDatasourceUrl;
    
    @Value("${spring.datasource.username}")
    private String springDatasourceUsername;
    
    @Value("${spring.datasource.password}")
    private String springDatasourcePassword;
    
    @Value("${spring.datasource.driver-class-name}")
    private String springDatasourceDriver;
    
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int hikariMaximumPoolSize;
    
    @Value("${spring.datasource.hikari.minimum-idle}")
    private int hikariMinimumIdle;
    
    @Value("${spring.datasource.hikari.connection-timeout}")
    private long hikariConnectionTimeout;
    
    @Value("${spring.datasource.hikari.idle-timeout}")
    private long hikariIdleTimeout;
    
    @Value("${spring.datasource.hikari.max-lifetime}")
    private long hikariMaxLifetime;
    
    public void springBootYamlConfiguration() throws SQLException {
        // Spring Boot YAML configuration usage
        Class.forName(springDatasourceDriver);
        Connection conn = DriverManager.getConnection(springDatasourceUrl, springDatasourceUsername, springDatasourcePassword);
        conn.close();
    }
    
    public void springBootYamlWithHikariCP() throws SQLException {
        // Spring Boot YAML configuration with HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl(springDatasourceUrl);
        config.setUsername(springDatasourceUsername);
        config.setPassword(springDatasourcePassword);
        config.setDriverClassName(springDatasourceDriver);
        config.setMaximumPoolSize(hikariMaximumPoolSize);
        config.setMinimumIdle(hikariMinimumIdle);
        config.setConnectionTimeout(hikariConnectionTimeout);
        config.setIdleTimeout(hikariIdleTimeout);
        config.setMaxLifetime(hikariMaxLifetime);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    // ConfigurationProperties class for YAML binding
    @ConfigurationProperties(prefix = "app.database")
    public static class DatabaseConfig {
        private String url;
        private String username;
        private String password;
        private String driver;
        private int maxPoolSize;
        private int minPoolSize;
        private long connectionTimeout;
        private long idleTimeout;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getDriver() { return driver; }
        public void setDriver(String driver) { this.driver = driver; }
        
        public int getMaxPoolSize() { return maxPoolSize; }
        public void setMaxPoolSize(int maxPoolSize) { this.maxPoolSize = maxPoolSize; }
        
        public int getMinPoolSize() { return minPoolSize; }
        public void setMinPoolSize(int minPoolSize) { this.minPoolSize = minPoolSize; }
        
        public long getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(long connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        
        public long getIdleTimeout() { return idleTimeout; }
        public void setIdleTimeout(long idleTimeout) { this.idleTimeout = idleTimeout; }
    }
    
    public void yamlConfigurationWithPropertiesClass(DatabaseConfig dbConfig) throws SQLException {
        // YAML configuration using ConfigurationProperties class
        Class.forName(dbConfig.getDriver());
        Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        conn.close();
    }
    
    // Environment-specific YAML configuration
    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @Value("${spring.datasource.username}")
    private String datasourceUsername;
    
    @Value("${spring.datasource.password}")
    private String datasourcePassword;
    
    public void environmentSpecificYamlConfiguration() throws SQLException {
        // Environment-specific YAML configuration
        String environment = activeProfile;
        String jdbcUrl = datasourceUrl;
        String username = datasourceUsername;
        String password = datasourcePassword;
        
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
    // Multi-database YAML configuration
    @Value("${app.databases.primary.url}")
    private String primaryDbUrl;
    
    @Value("${app.databases.primary.username}")
    private String primaryDbUsername;
    
    @Value("${app.databases.primary.password}")
    private String primaryDbPassword;
    
    @Value("${app.databases.secondary.url}")
    private String secondaryDbUrl;
    
    @Value("${app.databases.secondary.username}")
    private String secondaryDbUsername;
    
    @Value("${app.databases.secondary.password}")
    private String secondaryDbPassword;
    
    public void multiDatabaseYamlConfiguration() throws SQLException {
        // Multi-database YAML configuration
        Connection primaryConn = DriverManager.getConnection(primaryDbUrl, primaryDbUsername, primaryDbPassword);
        Connection secondaryConn = DriverManager.getConnection(secondaryDbUrl, secondaryDbUsername, secondaryDbPassword);
        
        primaryConn.close();
        secondaryConn.close();
    }
    
    // YAML configuration with validation
    @Value("${spring.datasource.validation-query}")
    private String validationQuery;
    
    @Value("${spring.datasource.test-on-borrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.test-while-idle}")
    private boolean testWhileIdle;
    
    public void yamlConfigurationWithValidation() throws SQLException {
        // YAML configuration with validation settings
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl(springDatasourceUrl);
        dataSource.setUsername(springDatasourceUsername);
        dataSource.setPassword(springDatasourcePassword);
        dataSource.setDriverClassName(springDatasourceDriver);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestWhileIdle(testWhileIdle);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    // YAML configuration with custom properties
    @Value("${custom.database.connection.url}")
    private String customDbUrl;
    
    @Value("${custom.database.connection.username}")
    private String customDbUsername;
    
    @Value("${custom.database.connection.password}")
    private String customDbPassword;
    
    @Value("${custom.database.connection.driver}")
    private String customDbDriver;
    
    public void customYamlConfiguration() throws SQLException {
        // Custom YAML configuration
        Class.forName(customDbDriver);
        Connection conn = DriverManager.getConnection(customDbUrl, customDbUsername, customDbPassword);
        conn.close();
    }
    
    // YAML configuration with nested properties
    @Value("${database.connection.jdbc.url}")
    private String nestedJdbcUrl;
    
    @Value("${database.connection.jdbc.username}")
    private String nestedJdbcUsername;
    
    @Value("${database.connection.jdbc.password}")
    private String nestedJdbcPassword;
    
    @Value("${database.connection.pool.max-size}")
    private int nestedMaxPoolSize;
    
    @Value("${database.connection.pool.min-size}")
    private int nestedMinPoolSize;
    
    public void nestedYamlConfiguration() throws SQLException {
        // Nested YAML configuration
        Connection conn = DriverManager.getConnection(nestedJdbcUrl, nestedJdbcUsername, nestedJdbcPassword);
        conn.close();
    }
    
    // YAML configuration with arrays
    @Value("${app.databases[0].url}")
    private String firstDbUrl;
    
    @Value("${app.databases[0].username}")
    private String firstDbUsername;
    
    @Value("${app.databases[0].password}")
    private String firstDbPassword;
    
    @Value("${app.databases[1].url}")
    private String secondDbUrl;
    
    @Value("${app.databases[1].username}")
    private String secondDbUsername;
    
    @Value("${app.databases[1].password}")
    private String secondDbPassword;
    
    public void arrayYamlConfiguration() throws SQLException {
        // YAML configuration with arrays
        Connection conn1 = DriverManager.getConnection(firstDbUrl, firstDbUsername, firstDbPassword);
        Connection conn2 = DriverManager.getConnection(secondDbUrl, secondDbUsername, secondDbPassword);
        
        conn1.close();
        conn2.close();
    }
    
    // YAML configuration with conditional properties
    @Value("${spring.datasource.url:#{null}}")
    private String conditionalDatasourceUrl;
    
    @Value("${spring.datasource.username:#{null}}")
    private String conditionalDatasourceUsername;
    
    @Value("${spring.datasource.password:#{null}}")
    private String conditionalDatasourcePassword;
    
    public void conditionalYamlConfiguration() throws SQLException {
        // Conditional YAML configuration
        if (conditionalDatasourceUrl != null && conditionalDatasourceUsername != null && conditionalDatasourcePassword != null) {
            Connection conn = DriverManager.getConnection(conditionalDatasourceUrl, conditionalDatasourceUsername, conditionalDatasourcePassword);
            conn.close();
        }
    }
    
    // YAML configuration with environment variables
    @Value("${DATABASE_URL:jdbc:mysql://localhost:3306/defaultdb}")
    private String envDatabaseUrl;
    
    @Value("${DATABASE_USERNAME:defaultuser}")
    private String envDatabaseUsername;
    
    @Value("${DATABASE_PASSWORD:defaultpass}")
    private String envDatabasePassword;
    
    public void environmentVariableYamlConfiguration() throws SQLException {
        // YAML configuration with environment variable fallbacks
        Connection conn = DriverManager.getConnection(envDatabaseUrl, envDatabaseUsername, envDatabasePassword);
        conn.close();
    }
}
