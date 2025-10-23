package com.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test file for sql-java-006: HikariCpUsage
 * Detects HikariCP connection pool usage
 */
public class HikariCpExample {
    
    private HikariDataSource hikariDataSource;
    
    public void basicHikariCPUsage() throws SQLException {
        // Basic HikariCP usage
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        hikariDataSource = new HikariDataSource(config);
        
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPWithProperties() throws SQLException {
        // HikariCP with properties file
        HikariConfig config = new HikariConfig("/hikari.properties");
        hikariDataSource = new HikariDataSource(config);
        
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPConfiguration() throws SQLException {
        // HikariCP detailed configuration
        HikariConfig config = new HikariConfig();
        
        // Basic connection settings
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setDriverClassName("org.postgresql.Driver");
        
        // Pool size configuration
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setInitializationFailTimeout(1);
        config.setIsolateInternalQueries(false);
        
        // Connection timeout configuration
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        
        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5000);
        
        hikariDataSource = new HikariDataSource(config);
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPWithDataSourceProperties() throws SQLException {
        // HikariCP with DataSource properties
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        
        // DataSource properties
        config.addDataSourceProperty("serverName", "localhost");
        config.addDataSourceProperty("port", "3306");
        config.addDataSourceProperty("databaseName", "testdb");
        config.addDataSourceProperty("user", "root");
        config.addDataSourceProperty("password", "password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        hikariDataSource = new HikariDataSource(config);
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPWithJNDI() throws SQLException {
        // HikariCP with JNDI DataSource
        HikariConfig config = new HikariConfig();
        config.setDataSourceJNDI("java:/comp/env/jdbc/MyDataSource");
        
        hikariDataSource = new HikariDataSource(config);
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPMonitoring() throws SQLException {
        // HikariCP monitoring and metrics
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUsername("user");
        config.setPassword("password");
        
        hikariDataSource = new HikariDataSource(config);
        
        // Get pool metrics
        HikariPoolMXBean poolBean = hikariDataSource.getHikariPoolMXBean();
        int activeConnections = poolBean.getActiveConnections();
        int idleConnections = poolBean.getIdleConnections();
        int totalConnections = poolBean.getTotalConnections();
        int threadsAwaitingConnection = poolBean.getThreadsAwaitingConnection();
        
        System.out.println("Active: " + activeConnections + ", Idle: " + idleConnections);
        
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPWithCustomProperties() throws SQLException {
        // HikariCP with custom properties
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setUsername("system");
        config.setPassword("oracle");
        
        // Custom pool name
        config.setPoolName("MyCustomPool");
        
        // Custom connection initialization
        config.setConnectionInitSql("SELECT 1 FROM DUAL");
        
        // Custom connection timeout
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1200000);
        
        // Custom validation
        config.setValidationTimeout(3000);
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        
        hikariDataSource = new HikariDataSource(config);
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPWithSpringBoot() throws SQLException {
        // HikariCP configuration for Spring Boot
        HikariConfig config = new HikariConfig();
        
        // Spring Boot style configuration
        config.setJdbcUrl("jdbc:h2:mem:testdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        
        // Spring Boot optimized settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1200000);
        config.setLeakDetectionThreshold(60000);
        
        hikariDataSource = new HikariDataSource(config);
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void hikariCPWithTryWithResources() {
        // HikariCP with try-with-resources
        try (HikariDataSource ds = new HikariDataSource()) {
            ds.setJdbcUrl("jdbc:sqlite:test.db");
            ds.setUsername("");
            ds.setPassword("");
            ds.setMaximumPoolSize(5);
            
            try (Connection conn = ds.getConnection()) {
                // Use connection
                System.out.println("Connection established");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void hikariCPMethodParameter(HikariDataSource hikariDS) throws SQLException {
        // Method accepting HikariDataSource as parameter
        Connection conn = hikariDS.getConnection();
        conn.close();
    }
    
    public HikariDataSource getHikariDataSource() {
        // Method returning HikariDataSource
        return hikariDataSource;
    }
}
