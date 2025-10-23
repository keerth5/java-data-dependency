package com.example.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Test file for sql-java-005: DataSourceUsage
 * Detects javax.sql.DataSource interface usage
 */
public class DataSourceExample {
    
    private DataSource dataSource;
    
    public void basicDataSourceUsage() throws SQLException {
        // Basic DataSource interface usage
        Connection conn = dataSource.getConnection();
        
        // DataSource interface methods
        Connection connWithUser = dataSource.getConnection("username", "password");
        
        // Using DataSource connection
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                System.out.println("User: " + rs.getString("name"));
            }
        }
    }
    
    public void dataSourceWithConnectionPooling() throws SQLException {
        // DataSource with connection pooling features
        Connection conn = dataSource.getConnection();
        
        // Connection pooling properties
        if (conn instanceof javax.sql.PooledConnection) {
            javax.sql.PooledConnection pooledConn = (javax.sql.PooledConnection) conn;
            // Handle pooled connection
        }
        
        // Connection wrapper for pooling
        if (conn instanceof javax.sql.ConnectionPoolDataSource) {
            javax.sql.ConnectionPoolDataSource poolDataSource = 
                (javax.ConnectionPoolDataSource) dataSource;
            javax.sql.PooledConnection pooledConnection = poolDataSource.getPooledConnection();
        }
        
        conn.close();
    }
    
    public void dataSourceWithXA() throws SQLException {
        // DataSource with XA transaction support
        if (dataSource instanceof javax.sql.XADataSource) {
            javax.sql.XADataSource xaDataSource = (javax.sql.XADataSource) dataSource;
            javax.sql.XAConnection xaConnection = xaDataSource.getXAConnection();
            
            // XA transaction support
            javax.transaction.xa.XAResource xaResource = xaConnection.getXAResource();
            Connection connection = xaConnection.getConnection();
            
            connection.close();
            xaConnection.close();
        }
    }
    
    public void dataSourceWithCommonDBCP() throws SQLException {
        // DataSource implementation with Apache DBCP
        org.apache.commons.dbcp2.BasicDataSource dbcpDataSource = 
            new org.apache.commons.dbcp2.BasicDataSource();
        
        dbcpDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dbcpDataSource.setUsername("user");
        dbcpDataSource.setPassword("password");
        dbcpDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // DBCP DataSource configuration
        dbcpDataSource.setInitialSize(5);
        dbcpDataSource.setMaxTotal(20);
        dbcpDataSource.setMaxIdle(10);
        dbcpDataSource.setMinIdle(5);
        dbcpDataSource.setMaxWaitMillis(30000);
        
        Connection conn = dbcpDataSource.getConnection();
        conn.close();
        dbcpDataSource.close();
    }
    
    public void dataSourceWithHikariCP() throws SQLException {
        // DataSource implementation with HikariCP
        com.zaxxer.hikari.HikariDataSource hikariDataSource = 
            new com.zaxxer.hikari.HikariDataSource();
        
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        hikariDataSource.setUsername("user");
        hikariDataSource.setPassword("password");
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // HikariCP DataSource configuration
        hikariDataSource.setMaximumPoolSize(20);
        hikariDataSource.setMinimumIdle(5);
        hikariDataSource.setConnectionTimeout(30000);
        hikariDataSource.setIdleTimeout(600000);
        hikariDataSource.setMaxLifetime(1800000);
        
        Connection conn = hikariDataSource.getConnection();
        conn.close();
        hikariDataSource.close();
    }
    
    public void dataSourceWithC3P0() throws SQLException {
        // DataSource implementation with C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource c3p0DataSource = 
            new com.mchange.v2.c3p0.ComboPooledDataSource();
        
        try {
            c3p0DataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            c3p0DataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            c3p0DataSource.setUser("user");
            c3p0DataSource.setPassword("password");
            
            // C3P0 DataSource configuration
            c3p0DataSource.setMinPoolSize(5);
            c3p0DataSource.setMaxPoolSize(20);
            c3p0DataSource.setAcquireIncrement(5);
            c3p0DataSource.setMaxIdleTime(300);
            c3p0DataSource.setMaxConnectionAge(3600);
            
            Connection conn = c3p0DataSource.getConnection();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c3p0DataSource.close();
        }
    }
    
    public void dataSourceWithTomcatJdbc() throws SQLException {
        // DataSource implementation with Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource = 
            new org.apache.tomcat.jdbc.pool.DataSource();
        
        tomcatDataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        tomcatDataSource.setUsername("user");
        tomcatDataSource.setPassword("password");
        tomcatDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        // Tomcat JDBC Pool DataSource configuration
        tomcatDataSource.setInitialSize(5);
        tomcatDataSource.setMaxActive(20);
        tomcatDataSource.setMaxIdle(10);
        tomcatDataSource.setMinIdle(5);
        tomcatDataSource.setMaxWait(30000);
        
        Connection conn = tomcatDataSource.getConnection();
        conn.close();
        tomcatDataSource.close();
    }
    
    public void dataSourceMethodParameter(DataSource ds) throws SQLException {
        // Method accepting DataSource as parameter
        Connection conn = ds.getConnection();
        conn.close();
    }
    
    public DataSource getDataSource() {
        // Method returning DataSource
        return dataSource;
    }
    
    public void dataSourceWithJNDI() throws Exception {
        // DataSource obtained from JNDI
        javax.naming.Context ctx = new javax.naming.InitialContext();
        DataSource jndiDataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
        
        Connection conn = jndiDataSource.getConnection();
        conn.close();
    }
}
