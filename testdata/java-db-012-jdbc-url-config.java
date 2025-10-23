package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test file for sql-java-012: JdbcUrlConfiguration
 * Detects JDBC URL configuration patterns
 */
public class JdbcUrlConfigurationExample {
    
    public void jdbcUrlConfigurationPatterns() throws SQLException {
        // JDBC URL configuration patterns
        
        // MySQL JDBC URL patterns
        String mysqlUrl1 = "jdbc:mysql://localhost:3306/testdb";
        String mysqlUrl2 = "jdbc:mysql://server:3306/database?useSSL=true&serverTimezone=UTC";
        String mysqlUrl3 = "jdbc:mysql://host:port/dbname?autoReconnect=true&useUnicode=true";
        
        // PostgreSQL JDBC URL patterns
        String postgresUrl1 = "jdbc:postgresql://localhost:5432/mydb";
        String postgresUrl2 = "jdbc:postgresql://server:5432/database?ssl=true&sslmode=require";
        String postgresUrl3 = "jdbc:postgresql://host:port/dbname?application_name=MyApp";
        
        // Oracle JDBC URL patterns
        String oracleUrl1 = "jdbc:oracle:thin:@localhost:1521:xe";
        String oracleUrl2 = "jdbc:oracle:thin:@server:1521:ORCL";
        String oracleUrl3 = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=host)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=service)))";
        
        // SQL Server JDBC URL patterns
        String sqlserverUrl1 = "jdbc:sqlserver://localhost:1433;databaseName=TestDB";
        String sqlserverUrl2 = "jdbc:sqlserver://server:1433;databaseName=DB;encrypt=true";
        String sqlserverUrl3 = "jdbc:sqlserver://host:1433;databaseName=Database;trustServerCertificate=true";
        
        Connection conn1 = DriverManager.getConnection(mysqlUrl1, "user", "pass");
        Connection conn2 = DriverManager.getConnection(postgresUrl1, "user", "pass");
        Connection conn3 = DriverManager.getConnection(oracleUrl1, "user", "pass");
        Connection conn4 = DriverManager.getConnection(sqlserverUrl1, "user", "pass");
        
        conn1.close();
        conn2.close();
        conn3.close();
        conn4.close();
    }
    
    public void jdbcUrlWithConnectionProperties() throws SQLException {
        // JDBC URL with connection properties
        Properties props = new Properties();
        props.setProperty("user", "username");
        props.setProperty("password", "password");
        props.setProperty("useSSL", "true");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("autoReconnect", "true");
        
        String jdbcUrl = "jdbc:mysql://localhost:3306/testdb";
        Connection conn = DriverManager.getConnection(jdbcUrl, props);
        conn.close();
    }
    
    public void jdbcUrlConfigurationInDataSource() throws SQLException {
        // JDBC URL configuration in DataSource
        org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/mydb");
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void jdbcUrlConfigurationInHikariCP() throws SQLException {
        // JDBC URL configuration in HikariCP
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
        config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
        config.setUsername("system");
        config.setPassword("oracle");
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource(config);
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void jdbcUrlConfigurationInC3P0() throws SQLException {
        // JDBC URL configuration in C3P0
        com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSource.setJdbcUrl("jdbc:sqlserver://localhost:1433;databaseName=TestDB");
            dataSource.setUser("sa");
            dataSource.setPassword("password");
            
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }
    
    public void jdbcUrlConfigurationInTomcatPool() throws SQLException {
        // JDBC URL configuration in Tomcat JDBC Pool
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setDriverClassName("org.h2.Driver");
        
        Connection conn = dataSource.getConnection();
        conn.close();
        dataSource.close();
    }
    
    public void jdbcUrlConfigurationWithEnvironment() throws SQLException {
        // JDBC URL configuration based on environment
        String environment = System.getProperty("env", "development");
        String jdbcUrl;
        
        if ("production".equals(environment)) {
            jdbcUrl = "jdbc:mysql://prod-server:3306/production_db";
        } else if ("staging".equals(environment)) {
            jdbcUrl = "jdbc:mysql://staging-server:3306/staging_db";
        } else {
            jdbcUrl = "jdbc:mysql://localhost:3306/development_db";
        }
        
        Connection conn = DriverManager.getConnection(jdbcUrl, "user", "pass");
        conn.close();
    }
    
    public void jdbcUrlConfigurationWithSystemProperties() throws SQLException {
        // JDBC URL configuration using system properties
        String host = System.getProperty("db.host", "localhost");
        String port = System.getProperty("db.port", "3306");
        String database = System.getProperty("db.name", "testdb");
        
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        Connection conn = DriverManager.getConnection(jdbcUrl, "user", "pass");
        conn.close();
    }
    
    public void jdbcUrlConfigurationInMethod() throws SQLException {
        // JDBC URL configuration in method
        String jdbcUrl = buildJdbcUrl("mysql", "localhost", 3306, "testdb");
        Connection conn = DriverManager.getConnection(jdbcUrl, "user", "pass");
        conn.close();
    }
    
    private String buildJdbcUrl(String driver, String host, int port, String database) {
        return "jdbc:" + driver + "://" + host + ":" + port + "/" + database;
    }
    
    public void jdbcUrlConfigurationWithBuilder() throws SQLException {
        // JDBC URL configuration using builder pattern
        JdbcUrlBuilder builder = new JdbcUrlBuilder()
            .driver("postgresql")
            .host("localhost")
            .port(5432)
            .database("mydb")
            .addParameter("ssl", "true")
            .addParameter("sslmode", "require");
        
        String jdbcUrl = builder.build();
        Connection conn = DriverManager.getConnection(jdbcUrl, "user", "pass");
        conn.close();
    }
    
    public void jdbcUrlConfigurationInSpringBoot() throws SQLException {
        // JDBC URL configuration for Spring Boot
        String springBootUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        Connection conn = DriverManager.getConnection(springBootUrl, "sa", "");
        conn.close();
    }
    
    // Helper class for JDBC URL building
    private static class JdbcUrlBuilder {
        private String driver;
        private String host;
        private int port;
        private String database;
        private java.util.Map<String, String> parameters = new java.util.HashMap<>();
        
        public JdbcUrlBuilder driver(String driver) {
            this.driver = driver;
            return this;
        }
        
        public JdbcUrlBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public JdbcUrlBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public JdbcUrlBuilder database(String database) {
            this.database = database;
            return this;
        }
        
        public JdbcUrlBuilder addParameter(String key, String value) {
            this.parameters.put(key, value);
            return this;
        }
        
        public String build() {
            StringBuilder url = new StringBuilder();
            url.append("jdbc:").append(driver).append("://").append(host).append(":").append(port).append("/").append(database);
            
            if (!parameters.isEmpty()) {
                url.append("?");
                boolean first = true;
                for (java.util.Map.Entry<String, String> entry : parameters.entrySet()) {
                    if (!first) {
                        url.append("&");
                    }
                    url.append(entry.getKey()).append("=").append(entry.getValue());
                    first = false;
                }
            }
            
            return url.toString();
        }
    }
}
