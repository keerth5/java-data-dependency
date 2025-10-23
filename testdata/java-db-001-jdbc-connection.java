package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Test file for sql-java-001: JdbcConnectionUsage
 * Detects java.sql.Connection interface usage
 */
public class JdbcConnectionExample {
    
    private Connection connection;
    
    public void establishConnection() throws SQLException {
        // Direct Connection interface usage
        String url = "jdbc:mysql://localhost:3306/testdb";
        String username = "user";
        String password = "password";
        
        // Using java.sql.Connection interface
        connection = DriverManager.getConnection(url, username, password);
        
        // Connection interface methods
        boolean isValid = connection.isValid(5);
        boolean isClosed = connection.isClosed();
        boolean isReadOnly = connection.isReadOnly();
        
        // Connection metadata
        String catalog = connection.getCatalog();
        String schema = connection.getSchema();
        int transactionIsolation = connection.getTransactionIsolation();
    }
    
    public void performDatabaseOperations() throws SQLException {
        // Using Connection interface for statements
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        
        // Connection interface for prepared statements
        java.sql.PreparedStatement pstmt = connection.prepareStatement(
            "INSERT INTO users (name, email) VALUES (?, ?)");
        
        // Connection interface for callable statements
        java.sql.CallableStatement cstmt = connection.prepareCall(
            "{call get_user_by_id(?)}");
        
        // Connection interface transaction methods
        connection.setAutoCommit(false);
        connection.commit();
        connection.rollback();
        connection.setSavepoint();
        
        // Connection interface for batch operations
        connection.setAutoCommit(false);
        stmt.addBatch("INSERT INTO users VALUES (1, 'John')");
        stmt.addBatch("INSERT INTO users VALUES (2, 'Jane')");
        stmt.executeBatch();
        connection.commit();
    }
    
    public void connectionProperties() throws SQLException {
        // Connection interface property methods
        connection.setReadOnly(true);
        connection.setCatalog("production_db");
        connection.setSchema("public");
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        
        // Connection interface type map
        java.util.Map<String, Class<?>> typeMap = connection.getTypeMap();
        connection.setTypeMap(typeMap);
        
        // Connection interface client info
        java.util.Properties clientInfo = connection.getClientInfo();
        connection.setClientInfo(clientInfo);
    }
    
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    // Method returning Connection interface
    public Connection getConnection() {
        return connection;
    }
    
    // Method accepting Connection interface parameter
    public void processConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
            // Process connection
        }
    }
}
