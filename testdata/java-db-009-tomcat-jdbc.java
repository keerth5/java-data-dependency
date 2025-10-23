package com.example.database;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-009: TomcatJdbcPoolUsage
 * Detects Tomcat JDBC Pool usage
 */
public class TomcatJdbcPoolExample {
    
    private DataSource tomcatDataSource;
    
    public void basicTomcatJdbcPoolUsage() throws SQLException {
        // Basic Tomcat JDBC Pool usage
        tomcatDataSource = new DataSource();
        tomcatDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        tomcatDataSource.setUsername("user");
        tomcatDataSource.setPassword("password");
        tomcatDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithPoolProperties() throws SQLException {
        // Tomcat JDBC Pool with PoolProperties
        PoolProperties poolProps = new PoolProperties();
        poolProps.setUrl("jdbc:postgresql://localhost:5432/mydb");
        poolProps.setUsername("postgres");
        poolProps.setPassword("postgres");
        poolProps.setDriverClassName("org.postgresql.Driver");
        
        tomcatDataSource = new DataSource(poolProps);
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolConfiguration() throws SQLException {
        // Tomcat JDBC Pool detailed configuration
        tomcatDataSource = new DataSource();
        
        // Basic connection settings
        tomcatDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        tomcatDataSource.setUsername("user");
        tomcatDataSource.setPassword("password");
        tomcatDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Pool size configuration
        tomcatDataSource.setInitialSize(5);
        tomcatDataSource.setMaxActive(20);
        tomcatDataSource.setMaxIdle(10);
        tomcatDataSource.setMinIdle(5);
        tomcatDataSource.setMaxWait(30000);
        
        // Connection lifecycle configuration
        tomcatDataSource.setMaxAge(1800000);
        tomcatDataSource.setTimeBetweenEvictionRunsMillis(30000);
        tomcatDataSource.setMinEvictableIdleTimeMillis(60000);
        tomcatDataSource.setMaxEvictableIdleTimeMillis(300000);
        tomcatDataSource.setNumTestsPerEvictionRun(3);
        
        // Connection validation
        tomcatDataSource.setValidationQuery("SELECT 1");
        tomcatDataSource.setValidationQueryTimeout(5);
        tomcatDataSource.setTestOnBorrow(true);
        tomcatDataSource.setTestOnReturn(false);
        tomcatDataSource.setTestWhileIdle(true);
        tomcatDataSource.setValidationInterval(30000);
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithAdvancedConfiguration() throws SQLException {
        // Tomcat JDBC Pool with advanced configuration
        tomcatDataSource = new DataSource();
        tomcatDataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        tomcatDataSource.setUsername("system");
        tomcatDataSource.setPassword("oracle");
        tomcatDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        // Advanced pool configuration
        tomcatDataSource.setInitialSize(3);
        tomcatDataSource.setMaxActive(15);
        tomcatDataSource.setMaxIdle(8);
        tomcatDataSource.setMinIdle(3);
        tomcatDataSource.setMaxWait(20000);
        
        // Advanced connection lifecycle
        tomcatDataSource.setMaxAge(3600000);
        tomcatDataSource.setTimeBetweenEvictionRunsMillis(30000);
        tomcatDataSource.setMinEvictableIdleTimeMillis(60000);
        tomcatDataSource.setMaxEvictableIdleTimeMillis(300000);
        tomcatDataSource.setNumTestsPerEvictionRun(5);
        
        // Advanced validation
        tomcatDataSource.setValidationQuery("SELECT 1 FROM DUAL");
        tomcatDataSource.setValidationQueryTimeout(3);
        tomcatDataSource.setTestOnBorrow(true);
        tomcatDataSource.setTestOnReturn(false);
        tomcatDataSource.setTestWhileIdle(true);
        tomcatDataSource.setValidationInterval(30000);
        
        // Connection properties
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("oracle.net.CONNECT_TIMEOUT", "10000");
        connectionProperties.setProperty("oracle.jdbc.ReadTimeout", "30000");
        tomcatDataSource.setDbProperties(connectionProperties);
        
        // Abandoned connection handling
        tomcatDataSource.setRemoveAbandoned(true);
        tomcatDataSource.setRemoveAbandonedTimeout(300);
        tomcatDataSource.setLogAbandoned(true);
        tomcatDataSource.setAbandonWhenPercentageFull(50);
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithJNDI() throws SQLException {
        // Tomcat JDBC Pool with JNDI DataSource
        tomcatDataSource = new DataSource();
        tomcatDataSource.setDataSourceJNDI("java:/comp/env/jdbc/MyDataSource");
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithConnectionProperties() throws SQLException {
        // Tomcat JDBC Pool with connection properties
        tomcatDataSource = new DataSource();
        tomcatDataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=TestDB");
        tomcatDataSource.setUsername("sa");
        tomcatDataSource.setPassword("password");
        tomcatDataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Connection properties
        Properties props = new Properties();
        props.setProperty("encrypt", "true");
        props.setProperty("trustServerCertificate", "true");
        props.setProperty("loginTimeout", "30");
        props.setProperty("sendStringParametersAsUnicode", "false");
        tomcatDataSource.setDbProperties(props);
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithMonitoring() throws SQLException {
        // Tomcat JDBC Pool monitoring and statistics
        tomcatDataSource = new DataSource();
        tomcatDataSource.setUrl("jdbc:h2:mem:testdb");
        tomcatDataSource.setUsername("sa");
        tomcatDataSource.setPassword("");
        tomcatDataSource.setDriverClassName("org.h2.Driver");
        
        // Enable monitoring
        tomcatDataSource.setLogAbandoned(true);
        tomcatDataSource.setRemoveAbandoned(true);
        tomcatDataSource.setRemoveAbandonedTimeout(300);
        
        Connection conn = tomcatDataSource.getConnection();
        
        // Get pool statistics
        int numActive = tomcatDataSource.getNumActive();
        int numIdle = tomcatDataSource.getNumIdle();
        int maxActive = tomcatDataSource.getMaxActive();
        int maxIdle = tomcatDataSource.getMaxIdle();
        
        System.out.println("Active: " + numActive + ", Idle: " + numIdle + 
                          ", MaxActive: " + maxActive + ", MaxIdle: " + maxIdle);
        
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithInterceptors() throws SQLException {
        // Tomcat JDBC Pool with interceptors
        tomcatDataSource = new DataSource();
        tomcatDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        tomcatDataSource.setUsername("user");
        tomcatDataSource.setPassword("password");
        tomcatDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Set interceptors
        tomcatDataSource.setJdbcInterceptors(
            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;" +
            "org.apache.tomcat.jdbc.pool.interceptor.QueryTimeoutInterceptor(queryTimeout=30)"
        );
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void tomcatJdbcPoolWithTryWithResources() {
        // Tomcat JDBC Pool with try-with-resources
        try (DataSource ds = new DataSource()) {
            ds.setUrl("jdbc:sqlite:test.db");
            ds.setUsername("");
            ds.setPassword("");
            ds.setMaxActive(5);
            
            try (Connection conn = ds.getConnection()) {
                // Use connection
                System.out.println("Tomcat JDBC Pool connection established");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void tomcatJdbcPoolMethodParameter(DataSource tomcatDS) throws SQLException {
        // Method accepting DataSource as parameter
        Connection conn = tomcatDS.getConnection();
        conn.close();
    }
    
    public DataSource getTomcatDataSource() {
        // Method returning DataSource
        return tomcatDataSource;
    }
    
    public void tomcatJdbcPoolWithSpringBoot() throws SQLException {
        // Tomcat JDBC Pool configuration for Spring Boot
        tomcatDataSource = new DataSource();
        
        // Spring Boot style configuration
        tomcatDataSource.setUrl("jdbc:h2:mem:testdb");
        tomcatDataSource.setUsername("sa");
        tomcatDataSource.setPassword("");
        tomcatDataSource.setDriverClassName("org.h2.Driver");
        
        // Spring Boot optimized settings
        tomcatDataSource.setInitialSize(2);
        tomcatDataSource.setMaxActive(10);
        tomcatDataSource.setMaxIdle(5);
        tomcatDataSource.setMinIdle(2);
        tomcatDataSource.setMaxWait(30000);
        tomcatDataSource.setValidationQuery("SELECT 1");
        tomcatDataSource.setTestOnBorrow(true);
        tomcatDataSource.setTestWhileIdle(true);
        tomcatDataSource.setTimeBetweenEvictionRunsMillis(30000);
        tomcatDataSource.setMinEvictableIdleTimeMillis(60000);
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
}
