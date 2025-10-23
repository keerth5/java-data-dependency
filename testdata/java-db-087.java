// sql-java-087: SqlExceptionGetErrorCodeUsage
// Detects SQLException.getErrorCode() usage
// This file tests detection of vendor-specific error code handling

package com.example.database;

import java.sql.*;

public class SqlExceptionErrorCodeExample {
    
    // VIOLATION: Using SQLException.getErrorCode()
    public void handleErrorCode() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO users VALUES (1, 'John')");
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            System.err.println("Error code: " + errorCode);
        }
    }
    
    // VIOLATION: Checking specific error codes
    public void checkSpecificErrorCode() {
        try {
            performDatabaseOperation();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                System.err.println("Duplicate entry detected");
            } else if (ex.getErrorCode() == 1452) {
                System.err.println("Foreign key constraint violation");
            }
        }
    }
    
    // VIOLATION: Using getErrorCode() for retry logic
    public boolean retryOnDeadlock(String sql) {
        try {
            executeQuery(sql);
            return true;
        } catch (SQLException e) {
            int code = e.getErrorCode();
            if (code == 1213) { // MySQL deadlock
                return retryOperation(sql);
            }
            return false;
        }
    }
    
    // VIOLATION: Logging error code
    public void logErrorCode(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance = ?");
            ps.setDouble(1, 1000.0);
            ps.executeUpdate();
        } catch (SQLException sqlEx) {
            System.err.println("SQL Error Code: " + sqlEx.getErrorCode());
            System.err.println("Message: " + sqlEx.getMessage());
        }
    }
    
    // VIOLATION: Error code in exception mapping
    public void mapErrorCodeToException() throws CustomException {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM products WHERE id = 999");
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            switch (errorCode) {
                case 1451:
                    throw new CustomException("Cannot delete: foreign key constraint");
                case 1064:
                    throw new CustomException("SQL syntax error");
                default:
                    throw new CustomException("Database error: " + errorCode);
            }
        }
    }
    
    // VIOLATION: Oracle-specific error code handling
    public void handleOracleErrorCode() {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe")) {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("SELECT * FROM employees");
        } catch (SQLException oraEx) {
            int oracleCode = oraEx.getErrorCode();
            if (oracleCode == 1) {
                System.err.println("ORA-00001: Unique constraint violated");
            } else if (oracleCode == 1017) {
                System.err.println("ORA-01017: Invalid username/password");
            }
        }
    }
    
    // VIOLATION: SQL Server error code handling
    public void handleSqlServerErrorCode() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost;database=TestDB");
            executeUpdate(conn, "INSERT INTO customers VALUES (1, 'Test')");
        } catch (SQLException sqlServerEx) {
            int errorCode = sqlServerEx.getErrorCode();
            if (errorCode == 2627) {
                handleDuplicateKey();
            } else if (errorCode == 547) {
                handleConstraintViolation();
            }
        }
    }
    
    // VIOLATION: PostgreSQL error code handling
    public void handlePostgresErrorCode() {
        try {
            performPostgresOperation();
        } catch (SQLException pgEx) {
            int pgErrorCode = pgEx.getErrorCode();
            System.err.println("PostgreSQL error code: " + pgErrorCode);
            if (pgErrorCode == 0) {
                // PostgreSQL uses SQLState, but getErrorCode() might return 0
                handlePostgresError(pgEx);
            }
        }
    }
    
    // VIOLATION: Combining error code with other SQLException methods
    public void comprehensiveErrorHandling() {
        try {
            executeBatchOperation();
        } catch (SQLException e) {
            int code = e.getErrorCode();
            String state = e.getSQLState();
            String message = e.getMessage();
            System.err.println("Error [" + code + "]: " + message + " (State: " + state + ")");
        }
    }
    
    // VIOLATION: Error code in custom exception
    public void wrapWithErrorCode() throws DatabaseException {
        try {
            Connection conn = getConnection();
            conn.createStatement().execute("UPDATE orders SET status = 'SHIPPED'");
        } catch (SQLException ex) {
            throw new DatabaseException("Operation failed with code: " + ex.getErrorCode(), ex);
        }
    }
    
    // VIOLATION: Using getErrorCode in loop through chained exceptions
    public void iterateExceptionChainWithErrorCode(SQLException ex) {
        SQLException current = ex;
        while (current != null) {
            System.err.println("Error code: " + current.getErrorCode());
            System.err.println("Message: " + current.getMessage());
            current = current.getNextException();
        }
    }
    
    // NON-VIOLATION: Catching SQLException but not using getErrorCode()
    public void catchWithoutErrorCode() {
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
        }
    }
    
    // NON-VIOLATION: Using only getSQLState()
    public void useSqlStateOnly() {
        try {
            executeQuery("SELECT * FROM users");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            System.err.println("SQL State: " + sqlState);
        }
    }
    
    // NON-VIOLATION: Generic exception handling
    public void genericExceptionOnly() {
        try {
            performOperation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // NON-VIOLATION: Throwing SQLException without inspecting error code
    public void throwWithoutInspection() throws SQLException {
        Connection conn = getConnection();
        conn.createStatement().executeQuery("SELECT * FROM products");
    }
    
    // Helper methods
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/db");
    }
    private void performDatabaseOperation() throws SQLException { }
    private void executeQuery(String sql) throws SQLException { }
    private boolean retryOperation(String sql) { return false; }
    private void executeUpdate(Connection conn, String sql) throws SQLException { }
    private void handleDuplicateKey() { }
    private void handleConstraintViolation() { }
    private void performPostgresOperation() throws SQLException { }
    private void handlePostgresError(SQLException ex) { }
    private void executeBatchOperation() throws SQLException { }
    private void performOperation() throws Exception { }
}

class CustomException extends Exception {
    CustomException(String msg) { super(msg); }
}

class DatabaseException extends Exception {
    DatabaseException(String msg, Throwable cause) { super(msg, cause); }
}

