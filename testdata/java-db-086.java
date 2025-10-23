// sql-java-086: SqlExceptionHandling
// Detects SQLException handling in try-catch blocks
// This file tests detection of SQLException handling patterns

package com.example.database;

import java.sql.*;

public class SqlExceptionHandlingExample {
    
    // VIOLATION: Try-catch block handling SQLException
    public void basicSqlExceptionHandling() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass");
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO users VALUES (1, 'John')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // VIOLATION: Multiple catch blocks with SQLException
    public void multiCatchWithSqlException() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/mydb");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM products");
            ResultSet rs = ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("General error: " + ex.getMessage());
        }
    }
    
    // VIOLATION: Try-catch with SQLException and custom handling
    public User getUserById(long id) throws SQLException {
        User user = null;
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(rs.getLong("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch user: " + e);
            throw e;
        }
        return user;
    }
    
    // VIOLATION: Nested try-catch with SQLException
    public void nestedSqlExceptionHandling() {
        try {
            Connection conn = getConnection();
            try {
                Statement stmt = conn.createStatement();
                stmt.execute("DELETE FROM temp_data");
            } catch (SQLException inner) {
                System.err.println("Delete failed: " + inner);
            }
        } catch (SQLException outer) {
            System.err.println("Connection failed: " + outer);
        }
    }
    
    // VIOLATION: Try-with-resources catching SQLException
    public void tryWithResourcesSqlException() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    // VIOLATION: Catch SQLException and log it
    public void catchAndLogSqlException() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost;database=TestDB");
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE products SET price = 100 WHERE id = 1");
        } catch (SQLException sqlException) {
            logError("Database operation failed", sqlException);
        } finally {
            closeConnection(connection);
        }
    }
    
    // VIOLATION: Multi-catch with SQLException
    public void multiCatchIncludingSqlException() {
        try {
            performDatabaseOperation();
        } catch (SQLException | ClassNotFoundException e) {
            handleError(e);
        }
    }
    
    // VIOLATION: Catching SQLException in batch operations
    public void batchOperationWithSqlException(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.addBatch("INSERT INTO logs VALUES (1, 'Log1')");
            stmt.addBatch("INSERT INTO logs VALUES (2, 'Log2')");
            stmt.executeBatch();
        } catch (SQLException batchEx) {
            System.err.println("Batch failed");
        }
    }
    
    // VIOLATION: SQLException in transaction handling
    public void transactionWithSqlException() {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO orders VALUES (1, 'Order1')");
            stmt.executeUpdate("UPDATE inventory SET quantity = quantity - 1");
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
    
    // VIOLATION: CallableStatement with SQLException handling
    public void callableStatementException() {
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall("{call update_salary(?, ?)}")) {
            cstmt.setInt(1, 1001);
            cstmt.setDouble(2, 75000.0);
            cstmt.execute();
        } catch (SQLException sqlErr) {
            System.err.println("Stored procedure failed: " + sqlErr);
        }
    }
    
    // NON-VIOLATION: No SQLException handling (throws it)
    public void noSqlExceptionHandling() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db");
        Statement stmt = conn.createStatement();
        stmt.executeQuery("SELECT * FROM users");
    }
    
    // NON-VIOLATION: Generic Exception handling (not SQLException specific)
    public void genericExceptionHandling() {
        try {
            performOperation();
        } catch (Exception e) {
            System.err.println("Error occurred");
        }
    }
    
    // NON-VIOLATION: RuntimeException handling
    public void runtimeExceptionHandling() {
        try {
            String value = null;
            value.length();
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    // Helper methods
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass");
    }
    
    private void logError(String msg, Exception e) { }
    private void closeConnection(Connection conn) { }
    private void handleError(Exception e) { }
    private void performDatabaseOperation() throws SQLException, ClassNotFoundException { }
    private void performOperation() throws Exception { }
}

class User {
    long id;
    String name;
    User(long id, String name) { this.id = id; this.name = name; }
}

