// sql-java-088: SqlExceptionGetSqlStateUsage
// Detects SQLException.getSQLState() usage
// This file tests detection of SQLSTATE code handling

package com.example.database;

import java.sql.*;

public class SqlExceptionSqlStateExample {
    
    // VIOLATION: Using SQLException.getSQLState()
    public void handleSqlState() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/mydb");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            System.err.println("SQL State: " + sqlState);
        }
    }
    
    // VIOLATION: Checking specific SQLSTATE codes
    public void checkSpecificSqlState() {
        try {
            performDatabaseOperation();
        } catch (SQLException ex) {
            String state = ex.getSQLState();
            if ("23505".equals(state)) {
                System.err.println("Unique violation");
            } else if ("23503".equals(state)) {
                System.err.println("Foreign key violation");
            } else if ("08001".equals(state)) {
                System.err.println("Connection error");
            }
        }
    }
    
    // VIOLATION: Using getSQLState() for error categorization
    public void categorizeError() {
        try {
            executeQuery("SELECT * FROM orders");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if (sqlState != null && sqlState.startsWith("23")) {
                handleIntegrityConstraintViolation(e);
            } else if (sqlState != null && sqlState.startsWith("08")) {
                handleConnectionException(e);
            } else if (sqlState != null && sqlState.startsWith("42")) {
                handleSyntaxError(e);
            }
        }
    }
    
    // VIOLATION: Logging SQL state
    public void logSqlState(Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id = ?");
            ps.setInt(1, 100);
            ps.executeUpdate();
        } catch (SQLException sqlEx) {
            System.err.println("SQLSTATE: " + sqlEx.getSQLState());
            System.err.println("Error Message: " + sqlEx.getMessage());
        }
    }
    
    // VIOLATION: SQL state in custom exception handling
    public void mapSqlStateToException() throws ApplicationException {
        try {
            Connection connection = getConnection();
            connection.createStatement().execute("UPDATE inventory SET quantity = 0");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            throw new ApplicationException("Database error with SQLSTATE: " + sqlState, e);
        }
    }
    
    // VIOLATION: Using getSQLState() with switch statement
    public void switchOnSqlState() {
        try {
            performUpdate();
        } catch (SQLException ex) {
            String state = ex.getSQLState();
            switch (state) {
                case "23000":
                    System.err.println("Integrity constraint violation");
                    break;
                case "42000":
                    System.err.println("Syntax error or access rule violation");
                    break;
                case "08000":
                    System.err.println("Connection exception");
                    break;
                default:
                    System.err.println("Unknown SQL state: " + state);
            }
        }
    }
    
    // VIOLATION: Retry logic based on SQL state
    public boolean retryBasedOnSqlState(String query) {
        try {
            executeQuery(query);
            return true;
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            // Retry on connection errors
            if (sqlState != null && sqlState.startsWith("08")) {
                return retryQuery(query);
            }
            return false;
        }
    }
    
    // VIOLATION: Combining getSQLState() with getErrorCode()
    public void comprehensiveErrorInfo() {
        try {
            executeDatabaseOperation();
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            int errorCode = e.getErrorCode();
            System.err.println("SQLSTATE: " + sqlState + ", Error Code: " + errorCode);
        }
    }
    
    // VIOLATION: SQL state null check and handling
    public void handleSqlStateWithNullCheck() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE temp_table (id INT)");
        } catch (SQLException ex) {
            String sqlState = ex.getSQLState();
            if (sqlState == null) {
                System.err.println("No SQL state available");
            } else {
                System.err.println("SQL State: " + sqlState);
            }
        }
    }
    
    // VIOLATION: Using getSQLState() in exception chain iteration
    public void iterateExceptionChain(SQLException ex) {
        SQLException current = ex;
        while (current != null) {
            System.err.println("SQLSTATE: " + current.getSQLState());
            System.err.println("Message: " + current.getMessage());
            current = current.getNextException();
        }
    }
    
    // VIOLATION: Pattern matching with SQL state prefix
    public void matchSqlStatePattern() {
        try {
            performBatchUpdate();
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if (sqlState != null) {
                if (sqlState.matches("23.*")) {
                    System.err.println("Integrity constraint violation class");
                } else if (sqlState.matches("08.*")) {
                    System.err.println("Connection exception class");
                }
            }
        }
    }
    
    // VIOLATION: Storing SQL state for later analysis
    public String captureSqlState() {
        try {
            executeComplexQuery();
            return "SUCCESS";
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            logError("Operation failed with SQLSTATE: " + sqlState);
            return sqlState;
        }
    }
    
    // NON-VIOLATION: Catching SQLException without using getSQLState()
    public void catchWithoutSqlState() {
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    
    // NON-VIOLATION: Using only getErrorCode()
    public void useErrorCodeOnly() {
        try {
            executeQuery("SELECT * FROM customers");
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            System.err.println("Error code: " + errorCode);
        }
    }
    
    // NON-VIOLATION: Generic exception handling
    public void handleGenericException() {
        try {
            performOperation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // NON-VIOLATION: Throwing SQLException without inspection
    public void throwSqlException() throws SQLException {
        Connection conn = getConnection();
        conn.createStatement().executeUpdate("UPDATE accounts SET balance = 0");
    }
    
    // Helper methods
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost/db");
    }
    private void performDatabaseOperation() throws SQLException { }
    private void executeQuery(String sql) throws SQLException { }
    private void handleIntegrityConstraintViolation(SQLException e) { }
    private void handleConnectionException(SQLException e) { }
    private void handleSyntaxError(SQLException e) { }
    private void performUpdate() throws SQLException { }
    private boolean retryQuery(String query) { return false; }
    private void executeDatabaseOperation() throws SQLException { }
    private void performBatchUpdate() throws SQLException { }
    private void executeComplexQuery() throws SQLException { }
    private void logError(String msg) { }
    private void performOperation() throws Exception { }
}

class ApplicationException extends Exception {
    ApplicationException(String msg, Throwable cause) { super(msg, cause); }
}

