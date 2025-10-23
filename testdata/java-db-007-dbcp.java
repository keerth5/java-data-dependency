package com.example.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-007: DbcpUsage
 * Detects Apache DBCP connection pool usage
 */
public class DbcpExample {
    
    private BasicDataSource basicDataSource;
    
    public void basicDBCPUsage() throws SQLException {
        // Basic Apache DBCP usage
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        basicDataSource.setUsername("user");
        basicDataSource.setPassword("password");
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithProperties() throws Exception {
        // DBCP with properties configuration
        Properties props = new Properties();
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        props.setProperty("url", "jdbc:mysql://localhost:3306/testdb");
        props.setProperty("username", "user");
        props.setProperty("password", "password");
        
        basicDataSource = BasicDataSourceFactory.createDataSource(props);
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpConfiguration() throws SQLException {
        // DBCP detailed configuration
        basicDataSource = new BasicDataSource();
        
        // Basic connection settings
        basicDataSource.setUrl("jdbc:postgresql://localhost:5432/mydb");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("postgres");
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        
        // Pool size configuration
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxTotal(20);
        basicDataSource.setMaxIdle(10);
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxWaitMillis(30000);
        
        // Connection validation
        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setValidationQueryTimeout(5);
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setTestOnReturn(false);
        basicDataSource.setTestWhileIdle(true);
        basicDataSource.setTimeBetweenEvictionRunsMillis(30000);
        basicDataSource.setMinEvictableIdleTimeMillis(60000);
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithConnectionFactory() throws SQLException {
        // DBCP with custom connection factory
        String connectionUri = "jdbc:mysql://localhost:3306/testdb?user=root&password=password";
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectionUri, null);
        
        PoolableConnectionFactory poolableConnectionFactory = 
            new PoolableConnectionFactory(connectionFactory, null);
        
        GenericObjectPool<org.apache.commons.dbcp2.PoolableConnection> connectionPool = 
            new GenericObjectPool<>(poolableConnectionFactory);
        
        poolableConnectionFactory.setPool(connectionPool);
        
        basicDataSource = new BasicDataSource();
        basicDataSource.setConnectionPool(connectionPool);
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithAdvancedConfiguration() throws SQLException {
        // DBCP with advanced configuration
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        basicDataSource.setUsername("system");
        basicDataSource.setPassword("oracle");
        basicDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Advanced pool configuration
        basicDataSource.setInitialSize(3);
        basicDataSource.setMaxTotal(15);
        basicDataSource.setMaxIdle(8);
        basicDataSource.setMinIdle(3);
        basicDataSource.setMaxWaitMillis(20000);
        
        // Connection lifecycle configuration
        basicDataSource.setMaxConnLifetimeMillis(1800000);
        basicDataSource.setMaxConnLifetimeMillis(1800000);
        basicDataSource.setLogAbandoned(true);
        basicDataSource.setRemoveAbandonedOnBorrow(true);
        basicDataSource.setRemoveAbandonedOnMaintenance(true);
        basicDataSource.setRemoveAbandonedTimeout(300);
        
        // Validation configuration
        basicDataSource.setValidationQuery("SELECT 1 FROM DUAL");
        basicDataSource.setValidationQueryTimeout(3);
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setTestOnReturn(false);
        basicDataSource.setTestWhileIdle(true);
        basicDataSource.setTimeBetweenEvictionRunsMillis(30000);
        basicDataSource.setMinEvictableIdleTimeMillis(60000);
        basicDataSource.setNumTestsPerEvictionRun(3);
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithJNDI() throws SQLException {
        // DBCP with JNDI DataSource
        basicDataSource = new BasicDataSource();
        basicDataSource.setDataSourceJNDI("java:/comp/env/jdbc/MyDataSource");
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithConnectionProperties() throws SQLException {
        // DBCP with connection properties
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=TestDB");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("password");
        basicDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Connection properties
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("encrypt", "true");
        connectionProperties.setProperty("trustServerCertificate", "true");
        connectionProperties.setProperty("loginTimeout", "30");
        basicDataSource.setConnectionProperties(connectionProperties);
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithMonitoring() throws SQLException {
        // DBCP monitoring and statistics
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:h2:mem:testdb");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");
        basicDataSource.setDriverClassName("org.h2.Driver");
        
        // Enable monitoring
        basicDataSource.setLogAbandoned(true);
        basicDataSource.setAbandonedUsageTracking(true);
        
        Connection conn = basicDataSource.getConnection();
        
        // Get pool statistics
        int numActive = basicDataSource.getNumActive();
        int numIdle = basicDataSource.getNumIdle();
        int maxTotal = basicDataSource.getMaxTotal();
        
        System.out.println("Active: " + numActive + ", Idle: " + numIdle + ", Max: " + maxTotal);
        
        conn.close();
        basicDataSource.close();
    }
    
    public void dbcpWithTryWithResources() {
        // DBCP with try-with-resources
        try (BasicDataSource ds = new BasicDataSource()) {
            ds.setUrl("jdbc:sqlite:test.db");
            ds.setUsername("");
            ds.setPassword("");
            ds.setMaxTotal(5);
            
            try (Connection conn = ds.getConnection()) {
                // Use connection
                System.out.println("DBCP connection established");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void dbcpMethodParameter(BasicDataSource dbcpDataSource) throws SQLException {
        // Method accepting BasicDataSource as parameter
        Connection conn = dbcpDataSource.getConnection();
        conn.close();
    }
    
    public BasicDataSource getDbcpDataSource() {
        // Method returning BasicDataSource
        return basicDataSource;
    }
    
    public void dbcpWithSpringBoot() throws SQLException {
        // DBCP configuration for Spring Boot
        basicDataSource = new BasicDataSource();
        
        // Spring Boot style configuration
        basicDataSource.setUrl("jdbc:h2:mem:testdb");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");
        basicDataSource.setDriverClassName("org.h2.Driver");
        
        // Spring Boot optimized settings
        basicDataSource.setInitialSize(2);
        basicDataSource.setMaxTotal(10);
        basicDataSource.setMaxIdle(5);
        basicDataSource.setMinIdle(2);
        basicDataSource.setMaxWaitMillis(30000);
        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setTestWhileIdle(true);
        
        Connection conn = basicDataSource.getConnection();
        conn.close();
        basicDataSource.close();
    }
}
