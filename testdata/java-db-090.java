// sql-java-090: SqlExceptionChainHandling
// Detects SQLException chain iteration
// This file tests detection of exception chain traversal patterns

package com.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlExceptionChainExample {
    
    // VIOLATION: Iterating through SQLException chain with while loop
    public void iterateExceptionChain() {
        try {
            performDatabaseOperation();
        } catch (SQLException ex) {
            SQLException current = ex;
            while (current != null) {
                System.err.println("Error: " + current.getMessage());
                current = current.getNextException();
            }
        }
    }
    
    // VIOLATION: Using getNextException() to traverse chain
    public void traverseChain() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeBatch();
        } catch (SQLException e) {
            SQLException nextEx = e.getNextException();
            while (nextEx != null) {
                System.err.println("Chained exception: " + nextEx.getMessage());
                nextEx = nextEx.getNextException();
            }
        }
    }
    
    // VIOLATION: Comprehensive chain handling with all error details
    public void handleCompleteChain() {
        try {
            executeBatchUpdate();
        } catch (SQLException ex) {
            SQLException exception = ex;
            int count = 1;
            while (exception != null) {
                System.err.println("Exception " + count);
                System.err.println("  Message: " + exception.getMessage());
                System.err.println("  SQLState: " + exception.getSQLState());
                System.err.println("  ErrorCode: " + exception.getErrorCode());
                exception = exception.getNextException();
                count++;
            }
        }
    }
    
    // VIOLATION: Collecting all exceptions in chain to list
    public List<String> collectChainMessages() {
        List<String> messages = new ArrayList<>();
        try {
            performBatchOperation();
        } catch (SQLException e) {
            SQLException current = e;
            while (current != null) {
                messages.add(current.getMessage());
                current = current.getNextException();
            }
        }
        return messages;
    }
    
    // VIOLATION: Logging entire exception chain
    public void logExceptionChain() {
        try {
            executeMultipleStatements();
        } catch (SQLException sqlException) {
            SQLException ex = sqlException;
            do {
                logError("SQL Error: " + ex.getMessage() + 
                        " (State: " + ex.getSQLState() + ")");
                ex = ex.getNextException();
            } while (ex != null);
        }
    }
    
    // VIOLATION: Counting exceptions in chain
    public int countChainedExceptions(SQLException ex) {
        int count = 0;
        SQLException current = ex;
        while (current != null) {
            count++;
            current = current.getNextException();
        }
        return count;
    }
    
    // VIOLATION: Finding specific error in chain
    public boolean hasSpecificErrorInChain(SQLException ex, String errorPattern) {
        SQLException current = ex;
        while (current != null) {
            if (current.getMessage().contains(errorPattern)) {
                return true;
            }
            current = current.getNextException();
        }
        return false;
    }
    
    // VIOLATION: Processing batch update exceptions
    public void handleBatchUpdateException() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.addBatch("INSERT INTO users VALUES (1, 'User1')");
            stmt.addBatch("INSERT INTO users VALUES (2, 'User2')");
            stmt.addBatch("INSERT INTO users VALUES (1, 'Duplicate')"); // Will fail
            stmt.executeBatch();
        } catch (BatchUpdateException batchEx) {
            SQLException ex = batchEx;
            while (ex != null) {
                System.err.println("Batch error: " + ex.getMessage());
                ex = ex.getNextException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // VIOLATION: Building error report from chain
    public String buildChainReport() {
        StringBuilder report = new StringBuilder();
        try {
            performComplexTransaction();
        } catch (SQLException e) {
            report.append("Exception Chain:\n");
            SQLException current = e;
            int index = 0;
            while (current != null) {
                report.append(index++).append(". ")
                      .append(current.getMessage()).append("\n");
                current = current.getNextException();
            }
        }
        return report.toString();
    }
    
    // VIOLATION: Rethrowing with chain information
    public void rethrowWithChainInfo() throws DatabaseException {
        try {
            executeOperation();
        } catch (SQLException ex) {
            StringBuilder chainMsg = new StringBuilder("Multiple errors occurred: ");
            SQLException current = ex;
            while (current != null) {
                chainMsg.append(current.getMessage()).append("; ");
                current = current.getNextException();
            }
            throw new DatabaseException(chainMsg.toString(), ex);
        }
    }
    
    // VIOLATION: Checking for specific SQL state in chain
    public boolean hasSqlStateInChain(SQLException ex, String targetState) {
        SQLException current = ex;
        while (current != null) {
            if (targetState.equals(current.getSQLState())) {
                return true;
            }
            current = current.getNextException();
        }
        return false;
    }
    
    // VIOLATION: For loop style chain iteration
    public void forLoopChainIteration() {
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            for (SQLException ex = e; ex != null; ex = ex.getNextException()) {
                System.err.println("Exception: " + ex.getMessage());
            }
        }
    }
    
    // VIOLATION: Recursive chain handling
    public void handleChainRecursively(SQLException ex) {
        if (ex != null) {
            System.err.println("Error: " + ex.getMessage());
            SQLException next = ex.getNextException();
            if (next != null) {
                handleChainRecursively(next);
            }
        }
    }
    
    // VIOLATION: Extracting all error codes from chain
    public List<Integer> extractErrorCodes(SQLException ex) {
        List<Integer> errorCodes = new ArrayList<>();
        SQLException current = ex;
        while (current != null) {
            errorCodes.add(current.getErrorCode());
            current = current.getNextException();
        }
        return errorCodes;
    }
    
    // NON-VIOLATION: Handling SQLException without chain traversal
    public void handleWithoutChain() {
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    
    // NON-VIOLATION: Only using first exception
    public void useFirstExceptionOnly() {
        try {
            executeQuery("SELECT * FROM products");
        } catch (SQLException e) {
            String message = e.getMessage();
            String sqlState = e.getSQLState();
            System.err.println("Error: " + message + " (" + sqlState + ")");
        }
    }
    
    // NON-VIOLATION: Printing stack trace (doesn't explicitly traverse chain)
    public void printStackTraceOnly() {
        try {
            performUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // NON-VIOLATION: Rethrowing exception without chain inspection
    public void rethrowException() throws SQLException {
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            throw new SQLException("Operation failed", e);
        }
    }
    
    // NON-VIOLATION: Generic exception handling
    public void handleGenericException() {
        try {
            performOperation();
        } catch (Exception e) {
            System.err.println("Error occurred: " + e);
        }
    }
    
    // Helper methods
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/db");
    }
    private void performDatabaseOperation() throws SQLException { }
    private void executeBatchUpdate() throws SQLException { }
    private void performBatchOperation() throws SQLException { }
    private void executeMultipleStatements() throws SQLException { }
    private void logError(String msg) { }
    private void performComplexTransaction() throws SQLException { }
    private void executeOperation() throws SQLException { }
    private void executeQuery(String sql) throws SQLException { }
    private void performUpdate() throws SQLException { }
    private void performOperation() throws Exception { }
}

class DatabaseException extends Exception {
    DatabaseException(String msg, Throwable cause) { super(msg, cause); }
}

