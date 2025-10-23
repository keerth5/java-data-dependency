package com.example.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-008: C3p0Usage
 * Detects C3P0 connection pool usage
 */
public class C3p0Example {
    
    private ComboPooledDataSource comboPooledDataSource;
    private PooledDataSource pooledDataSource;
    
    public void basicC3P0Usage() throws SQLException {
        // Basic C3P0 usage with ComboPooledDataSource
        comboPooledDataSource = new ComboPooledDataSource();
        
        try {
            comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            comboPooledDataSource.setUser("user");
            comboPooledDataSource.setPassword("password");
            
            Connection conn = comboPooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comboPooledDataSource.close();
        }
    }
    
    public void c3p0Configuration() throws SQLException {
        // C3P0 detailed configuration
        comboPooledDataSource = new ComboPooledDataSource();
        
        try {
            // Basic connection settings
            comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            comboPooledDataSource.setUser("user");
            comboPooledDataSource.setPassword("password");
            
            // Pool size configuration
            comboPooledDataSource.setMinPoolSize(5);
            comboPooledDataSource.setMaxPoolSize(20);
            comboPooledDataSource.setInitialPoolSize(5);
            comboPooledDataSource.setAcquireIncrement(5);
            comboPooledDataSource.setMaxStatements(50);
            comboPooledDataSource.setMaxStatementsPerConnection(5);
            
            // Connection timeout configuration
            comboPooledDataSource.setCheckoutTimeout(30000);
            comboPooledDataSource.setAcquireRetryAttempts(3);
            comboPooledDataSource.setAcquireRetryDelay(1000);
            comboPooledDataSource.setBreakAfterAcquireFailure(false);
            
            // Connection lifecycle configuration
            comboPooledDataSource.setMaxIdleTime(300);
            comboPooledDataSource.setMaxConnectionAge(3600);
            comboPooledDataSource.setMaxIdleTimeExcessConnections(300);
            comboPooledDataSource.setMaxConnectionAge(3600);
            
            // Connection validation
            comboPooledDataSource.setTestConnectionOnCheckout(true);
            comboPooledDataSource.setTestConnectionOnCheckin(false);
            comboPooledDataSource.setIdleConnectionTestPeriod(300);
            comboPooledDataSource.setPreferredTestQuery("SELECT 1");
            comboPooledDataSource.setConnectionTesterClassName("com.mchange.v2.c3p0.impl.DefaultConnectionTester");
            
            Connection conn = comboPooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comboPooledDataSource.close();
        }
    }
    
    public void c3p0WithProperties() throws SQLException {
        // C3P0 with properties configuration
        Properties props = new Properties();
        props.setProperty("c3p0.driverClass", "com.mysql.cj.jdbc.Driver");
        props.setProperty("c3p0.jdbcUrl", "jdbc:mysql://localhost:3306/testdb");
        props.setProperty("c3p0.user", "user");
        props.setProperty("c3p0.password", "password");
        props.setProperty("c3p0.minPoolSize", "5");
        props.setProperty("c3p0.maxPoolSize", "20");
        props.setProperty("c3p0.acquireIncrement", "5");
        
        try {
            pooledDataSource = DataSources.pooledDataSource(
                DataSources.unpooledDataSource(
                    props.getProperty("c3p0.jdbcUrl"),
                    props.getProperty("c3p0.user"),
                    props.getProperty("c3p0.password")
                ),
                props
            );
            
            Connection conn = pooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pooledDataSource != null) {
                try {
                    DataSources.destroy(pooledDataSource);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void c3p0WithDataSource() throws SQLException {
        // C3P0 with DataSource
        try {
            javax.sql.DataSource unpooledDataSource = DataSources.unpooledDataSource(
                "jdbc:postgresql://localhost:5432/mydb", "postgres", "postgres");
            
            pooledDataSource = DataSources.pooledDataSource(unpooledDataSource);
            
            Connection conn = pooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pooledDataSource != null) {
                try {
                    DataSources.destroy(pooledDataSource);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void c3p0WithJNDI() throws SQLException {
        // C3P0 with JNDI DataSource
        comboPooledDataSource = new ComboPooledDataSource();
        
        try {
            comboPooledDataSource.setDataSourceName("java:/comp/env/jdbc/MyDataSource");
            comboPooledDataSource.setMinPoolSize(3);
            comboPooledDataSource.setMaxPoolSize(15);
            
            Connection conn = comboPooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comboPooledDataSource.close();
        }
    }
    
    public void c3p0WithAdvancedConfiguration() throws SQLException {
        // C3P0 with advanced configuration
        comboPooledDataSource = new ComboPooledDataSource();
        
        try {
            comboPooledDataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
            comboPooledDataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
            comboPooledDataSource.setUser("system");
            comboPooledDataSource.setPassword("oracle");
            
            // Advanced pool configuration
            comboPooledDataSource.setMinPoolSize(3);
            comboPooledDataSource.setMaxPoolSize(15);
            comboPooledDataSource.setInitialPoolSize(3);
            comboPooledDataSource.setAcquireIncrement(3);
            comboPooledDataSource.setMaxStatements(100);
            comboPooledDataSource.setMaxStatementsPerConnection(10);
            
            // Advanced timeout configuration
            comboPooledDataSource.setCheckoutTimeout(20000);
            comboPooledDataSource.setAcquireRetryAttempts(5);
            comboPooledDataSource.setAcquireRetryDelay(2000);
            comboPooledDataSource.setBreakAfterAcquireFailure(true);
            
            // Advanced connection lifecycle
            comboPooledDataSource.setMaxIdleTime(600);
            comboPooledDataSource.setMaxConnectionAge(7200);
            comboPooledDataSource.setMaxIdleTimeExcessConnections(600);
            comboPooledDataSource.setUnreturnedConnectionTimeout(300);
            
            // Advanced validation
            comboPooledDataSource.setTestConnectionOnCheckout(true);
            comboPooledDataSource.setTestConnectionOnCheckin(true);
            comboPooledDataSource.setIdleConnectionTestPeriod(600);
            comboPooledDataSource.setPreferredTestQuery("SELECT 1 FROM DUAL");
            comboPooledDataSource.setConnectionTesterClassName("com.mchange.v2.c3p0.impl.DefaultConnectionTester");
            
            // Statement caching
            comboPooledDataSource.setMaxStatements(200);
            comboPooledDataSource.setMaxStatementsPerConnection(20);
            comboPooledDataSource.setStatementCacheNumDeferredCloseThreads(1);
            
            Connection conn = comboPooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comboPooledDataSource.close();
        }
    }
    
    public void c3p0WithJBoss() throws SQLException {
        // C3P0 with JBoss integration
        C3P0PooledDataSource jbossDataSource = new C3P0PooledDataSource();
        
        try {
            jbossDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            jbossDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            jbossDataSource.setUser("user");
            jbossDataSource.setPassword("password");
            
            // JBoss specific configuration
            jbossDataSource.setMinPoolSize(5);
            jbossDataSource.setMaxPoolSize(20);
            jbossDataSource.setAcquireIncrement(5);
            
            Connection conn = jbossDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jbossDataSource.close();
        }
    }
    
    public void c3p0WithTryWithResources() {
        // C3P0 with try-with-resources
        try (ComboPooledDataSource ds = new ComboPooledDataSource()) {
            ds.setDriverClass("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:testdb");
            ds.setUser("sa");
            ds.setPassword("");
            ds.setMinPoolSize(2);
            ds.setMaxPoolSize(10);
            
            try (Connection conn = ds.getConnection()) {
                // Use connection
                System.out.println("C3P0 connection established");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void c3p0MethodParameter(ComboPooledDataSource c3p0DataSource) throws SQLException {
        // Method accepting ComboPooledDataSource as parameter
        Connection conn = c3p0DataSource.getConnection();
        conn.close();
    }
    
    public ComboPooledDataSource getC3P0DataSource() {
        // Method returning ComboPooledDataSource
        return comboPooledDataSource;
    }
    
    public void c3p0WithSpringBoot() throws SQLException {
        // C3P0 configuration for Spring Boot
        comboPooledDataSource = new ComboPooledDataSource();
        
        try {
            // Spring Boot style configuration
            comboPooledDataSource.setDriverClass("org.h2.Driver");
            comboPooledDataSource.setJdbcUrl("jdbc:h2:mem:testdb");
            comboPooledDataSource.setUser("sa");
            comboPooledDataSource.setPassword("");
            
            // Spring Boot optimized settings
            comboPooledDataSource.setMinPoolSize(2);
            comboPooledDataSource.setMaxPoolSize(10);
            comboPooledDataSource.setInitialPoolSize(2);
            comboPooledDataSource.setAcquireIncrement(2);
            comboPooledDataSource.setCheckoutTimeout(30000);
            comboPooledDataSource.setTestConnectionOnCheckout(true);
            comboPooledDataSource.setIdleConnectionTestPeriod(300);
            comboPooledDataSource.setPreferredTestQuery("SELECT 1");
            
            Connection conn = comboPooledDataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comboPooledDataSource.close();
        }
    }
}
