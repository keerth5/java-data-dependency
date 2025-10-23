package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test file for sql-java-011: ConnectionStringHardcoding
 * Detects hardcoded database URLs in Java code
 */
public class HardcodedConnectionExample {
    
    public void hardcodedConnectionStrings() throws SQLException {
        // Hardcoded database URLs - security risk
        String mysqlUrl = "jdbc:mysql://localhost:3306/production_db";
        String postgresUrl = "jdbc:postgresql://prod-server:5432/customer_data";
        String oracleUrl = "jdbc:oracle:thin:@prod-oracle:1521:PROD";
        String sqlserverUrl = "jdbc:sqlserver://sql-prod:1433;databaseName=SalesDB";
        
        // Hardcoded credentials - major security risk
        String username = "admin";
        String password = "SuperSecretPassword123!";
        
        Connection conn1 = DriverManager.getConnection(mysqlUrl, username, password);
        Connection conn2 = DriverManager.getConnection(postgresUrl, "postgres", "postgres123");
        Connection conn3 = DriverManager.getConnection(oracleUrl, "system", "oracle_password");
        Connection conn4 = DriverManager.getConnection(sqlserverUrl, "sa", "SqlServer@2024");
        
        conn1.close();
        conn2.close();
        conn3.close();
        conn4.close();
    }
    
    public void hardcodedConnectionWithInlineCredentials() throws SQLException {
        // Hardcoded URLs with inline credentials
        String urlWithCreds1 = "jdbc:mysql://user:password@localhost:3306/mydb";
        String urlWithCreds2 = "jdbc:postgresql://admin:secret@prod-server:5432/database";
        String urlWithCreds3 = "jdbc:oracle:thin:scott/tiger@localhost:1521:xe";
        String urlWithCreds4 = "jdbc:sqlserver://sa:password123@sql-server:1433;databaseName=TestDB";
        
        Connection conn1 = DriverManager.getConnection(urlWithCreds1);
        Connection conn2 = DriverManager.getConnection(urlWithCreds2);
        Connection conn3 = DriverManager.getConnection(urlWithCreds3);
        Connection conn4 = DriverManager.getConnection(urlWithCreds4);
        
        conn1.close();
        conn2.close();
        conn3.close();
        conn4.close();
    }
    
    public void hardcodedConnectionInConstructor() throws SQLException {
        // Hardcoded connection in constructor
        String dbUrl = "jdbc:mysql://production-server:3306/application_db";
        String dbUser = "app_user";
        String dbPass = "ApplicationPassword2024";
        
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        conn.close();
    }
    
    public void hardcodedConnectionInStaticBlock() throws SQLException {
        // Hardcoded connection in static initialization
        static {
            try {
                String staticUrl = "jdbc:postgresql://static-server:5432/static_db";
                String staticUser = "static_user";
                String staticPass = "StaticPassword123";
                
                Connection staticConn = DriverManager.getConnection(staticUrl, staticUser, staticPass);
                staticConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void hardcodedConnectionInMethod() throws SQLException {
        // Hardcoded connection in method
        String methodUrl = "jdbc:oracle:thin:@method-server:1521:ORCL";
        String methodUser = "method_user";
        String methodPass = "MethodPassword456";
        
        Connection methodConn = DriverManager.getConnection(methodUrl, methodUser, methodPass);
        methodConn.close();
    }
    
    public void hardcodedConnectionWithProperties() throws SQLException {
        // Hardcoded connection with hardcoded properties
        java.util.Properties props = new java.util.Properties();
        props.setProperty("user", "hardcoded_user");
        props.setProperty("password", "HardcodedPassword789");
        props.setProperty("serverTimezone", "UTC");
        props.setProperty("useSSL", "true");
        
        String hardcodedUrl = "jdbc:mysql://hardcoded-server:3306/hardcoded_db";
        Connection conn = DriverManager.getConnection(hardcodedUrl, props);
        conn.close();
    }
    
    public void hardcodedConnectionInField() throws SQLException {
        // Hardcoded connection strings as class fields
        private static final String DB_URL = "jdbc:sqlserver://field-server:1433;databaseName=FieldDB";
        private static final String DB_USER = "field_user";
        private static final String DB_PASSWORD = "FieldPassword2024";
        
        Connection fieldConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        fieldConn.close();
    }
    
    public void hardcodedConnectionInArray() throws SQLException {
        // Hardcoded connection strings in arrays
        String[] urls = {
            "jdbc:mysql://array-server1:3306/array_db1",
            "jdbc:mysql://array-server2:3306/array_db2",
            "jdbc:postgresql://array-server3:5432/array_db3"
        };
        
        String[] users = {"array_user1", "array_user2", "array_user3"};
        String[] passwords = {"ArrayPass1", "ArrayPass2", "ArrayPass3"};
        
        for (int i = 0; i < urls.length; i++) {
            Connection conn = DriverManager.getConnection(urls[i], users[i], passwords[i]);
            conn.close();
        }
    }
    
    public void hardcodedConnectionInStringBuilder() throws SQLException {
        // Hardcoded connection strings built with StringBuilder
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("jdbc:mysql://");
        urlBuilder.append("builder-server");
        urlBuilder.append(":3306/");
        urlBuilder.append("builder_database");
        
        String builtUrl = urlBuilder.toString();
        String builtUser = "builder_user";
        String builtPass = "BuilderPassword123";
        
        Connection conn = DriverManager.getConnection(builtUrl, builtUser, builtPass);
        conn.close();
    }
    
    public void hardcodedConnectionInConcatenation() throws SQLException {
        // Hardcoded connection strings with concatenation
        String host = "concatenation-server";
        String port = "3306";
        String database = "concatenation_db";
        String user = "concat_user";
        String pass = "ConcatPassword456";
        
        String concatUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        Connection conn = DriverManager.getConnection(concatUrl, user, pass);
        conn.close();
    }
    
    public void hardcodedConnectionInSwitch() throws SQLException {
        // Hardcoded connection strings in switch statement
        String environment = "production";
        String url, user, password;
        
        switch (environment) {
            case "production":
                url = "jdbc:mysql://prod-server:3306/prod_db";
                user = "prod_user";
                password = "ProdPassword789";
                break;
            case "staging":
                url = "jdbc:mysql://staging-server:3306/staging_db";
                user = "staging_user";
                password = "StagingPassword123";
                break;
            default:
                url = "jdbc:mysql://dev-server:3306/dev_db";
                user = "dev_user";
                password = "DevPassword456";
        }
        
        Connection conn = DriverManager.getConnection(url, user, password);
        conn.close();
    }
}
