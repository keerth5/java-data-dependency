package com.example.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test file for sql-java-019: ConnectionValidationConfiguration
 * Detects connection validation settings
 */
public class ConnectionValidationConfigurationExample {
    
    public void connectionValidationWithHikariCP() throws SQLException {
        // Connection validation with HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // HikariCP connection validation configuration
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5000);
        config.setLeakDetectionThreshold(60000);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithDBCP() throws SQLException {
        // Connection validation with DBCP
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // DBCP connection validation configuration
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setValidationQueryTimeout(5);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        dataSource.setNumTestsPerEvictionRun(3);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithC3P0() throws SQLException {
        // Connection validation with C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            
            // C3P0 connection validation configuration
            dataSource.setTestConnectionOnCheckout(true);
            dataSource.setTestConnectionOnCheckin(false);
            dataSource.setIdleConnectionTestPeriod(300);
            dataSource.setPreferredTestQuery("SELECT 1");
            dataSource.setConnectionTesterClassName("com.mchange.v2.c3p0.impl.DefaultConnectionTester");
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
    
    public void connectionValidationWithTomcatPool() throws SQLException {
        // Connection validation with Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Tomcat JDBC Pool connection validation configuration
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setValidationQueryTimeout(5);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationInterval(30000);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        dataSource.setMaxEvictableIdleTimeMillis(300000);
        dataSource.setNumTestsPerEvictionRun(3);
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithSpringBoot() throws SQLException {
        // Connection validation with Spring Boot configuration
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        
        // Spring Boot connection validation settings
        config.setConnectionTestQuery("SELECT 1"); // spring.datasource.hikari.connection-test-query
        config.setValidationTimeout(5000); // spring.datasource.hikari.validation-timeout
        config.setLeakDetectionThreshold(60000); // spring.datasource.hikari.leak-detection-threshold
        config.setConnectionTimeout(20000); // spring.datasource.hikari.connection-timeout
        config.setIdleTimeout(300000); // spring.datasource.hikari.idle-timeout
        config.setMaxLifetime(1200000); // spring.datasource.hikari.max-lifetime
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithProperties() throws SQLException {
        // Connection validation with properties configuration
        java.util.Properties props = new java.util.Properties();
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        props.setProperty("url", "jdbc:mysql://localhost:3306/testdb");
        props.setProperty("username", "user");
        props.setProperty("password", "password");
        props.setProperty("validationQuery", "SELECT 1");
        props.setProperty("testOnBorrow", "true");
        props.setProperty("testWhileIdle", "true");
        props.setProperty("timeBetweenEvictionRunsMillis", "30000");
        props.setProperty("minEvictableIdleTimeMillis", "60000");
        
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));
        dataSource.setDriverClassName(props.getProperty("driverClassName"));
        dataSource.setValidationQuery(props.getProperty("validationQuery"));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(props.getProperty("testOnBorrow")));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(props.getProperty("testWhileIdle")));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(props.getProperty("timeBetweenEvictionRunsMillis")));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(props.getProperty("minEvictableIdleTimeMillis")));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithEnvironmentVariables() throws SQLException {
        // Connection validation with environment variables
        String validationQuery = System.getProperty("db.validation.query", "SELECT 1");
        String testOnBorrow = System.getProperty("db.test.on.borrow", "true");
        String testWhileIdle = System.getProperty("db.test.while.idle", "true");
        
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("org.postgresql.Driver");
        config.setConnectionTestQuery(validationQuery);
        config.setValidationTimeout(5000);
        config.setLeakDetectionThreshold(60000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithConfigurationFile() throws SQLException {
        // Connection validation with configuration file
        java.util.Properties config = new java.util.Properties();
        try {
            config.load(getClass().getClassLoader().getResourceAsStream("connection-validation.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String validationQuery = config.getProperty("validation.query", "SELECT 1");
        String testOnBorrow = config.getProperty("test.on.borrow", "true");
        String testWhileIdle = config.getProperty("test.while.idle", "true");
        String validationTimeout = config.getProperty("validation.timeout", "5000");
        
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setValidationQuery(validationQuery);
        dataSource.setValidationQueryTimeout(Integer.parseInt(validationTimeout));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithJNDI() throws SQLException {
        // Connection validation with JNDI DataSource
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
            
            // JNDI DataSource with configured connection validation
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void connectionValidationWithCustomDataSource() throws SQLException {
        // Connection validation with custom DataSource
        CustomDataSource dataSource = new CustomDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationTimeout(5000);
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void connectionValidationWithDifferentDatabases() throws SQLException {
        // Connection validation for different databases
        com.zaxxer.hikari.HikariConfig mysqlConfig = new com.zaxxer.hikari.HikariConfig();
        mysqlConfig.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        mysqlConfig.setUsername("user");
        mysqlConfig.setPassword("password");
        mysqlConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        mysqlConfig.setConnectionTestQuery("SELECT 1");
        mysqlConfig.setValidationTimeout(5000);
        
        com.zaxxer.hikari.HikariConfig postgresConfig = new com.zaxxer.hikari.HikariConfig();
        postgresConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb");
        postgresConfig.setUsername("user");
        postgresConfig.setPassword("password");
        postgresConfig.setDriverClassName("org.postgresql.Driver");
        postgresConfig.setConnectionTestQuery("SELECT 1");
        postgresConfig.setValidationTimeout(5000);
        
        com.zaxxer.hikari.HikariConfig oracleConfig = new com.zaxxer.hikari.HikariConfig();
        oracleConfig.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        oracleConfig.setUsername("user");
        oracleConfig.setPassword("password");
        oracleConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        oracleConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
        oracleConfig.setValidationTimeout(5000);
        
        com.zaxxer.hikari.HikariDataSource mysqlDS = new com.zaxxer.hikari.HikariDataSource(mysqlConfig);
        com.zaxxer.hikari.HikariDataSource postgresDS = new com.zaxxer.hikari.HikariDataSource(postgresConfig);
        com.zaxxer.hikari.HikariDataSource oracleDS = new com.zaxxer.hikari.HikariDataSource(oracleConfig);
        
        Connection mysqlConn = mysqlDS.getConnection();
        Connection postgresConn = postgresDS.getConnection();
        Connection oracleConn = oracleDS.getConnection();
        
        mysqlConn.close();
        postgresConn.close();
        oracleConn.close();
        
        mysqlDS.close();
        postgresDS.close();
        oracleDS.close();
    }
    
    public void connectionValidationWithHealthChecks() throws SQLException {
        // Connection validation with health checks
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Health check configuration
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(3000);
        config.setLeakDetectionThreshold(30000);
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1200000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        
        // Perform health check
        Connection conn = dataSource.getConnection();
        boolean isValid = conn.isValid(5);
        System.out.println("Connection is valid: " + isValid);
        
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithPerformanceOptimization() throws SQLException {
        // Connection validation with performance optimization
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        // Performance optimized validation
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setValidationQueryTimeout(2);
        dataSource.setTestOnBorrow(false); // Disable for performance
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000); // Less frequent
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setNumTestsPerEvictionRun(1); // Test fewer connections
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void connectionValidationWithReliabilityOptimization() throws SQLException {
        // Connection validation with reliability optimization
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Reliability optimized validation
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        config.setValidationTimeout(10000); // Longer timeout for reliability
        config.setLeakDetectionThreshold(30000); // Shorter for faster detection
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    // Custom DataSource class for demonstration
    private static class CustomDataSource {
        private String url;
        private String username;
        private String password;
        private String validationQuery;
        private boolean testOnBorrow;
        private boolean testWhileIdle;
        private int validationTimeout;
        
        public void setUrl(String url) { this.url = url; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
        public void setValidationQuery(String validationQuery) { this.validationQuery = validationQuery; }
        public void setTestOnBorrow(boolean testOnBorrow) { this.testOnBorrow = testOnBorrow; }
        public void setTestWhileIdle(boolean testWhileIdle) { this.testWhileIdle = testWhileIdle; }
        public void setValidationTimeout(int validationTimeout) { this.validationTimeout = validationTimeout; }
        
        public Connection getConnection() throws SQLException {
            return java.sql.DriverManager.getConnection(url, username, password);
        }
    }
}
