// sql-java-089: SqlExceptionGetMessageUsage
// Detects SQLException.getMessage() usage
// This file tests detection of error message retrieval

package com.example.database;

import java.sql.*;
import java.util.logging.Logger;

public class SqlExceptionMessageExample {
    
    private static final Logger logger = Logger.getLogger(SqlExceptionMessageExample.class.getName());
    
    // VIOLATION: Using SQLException.getMessage()
    public void handleErrorMessage() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO products (id, name) VALUES (1, 'Product1')");
        } catch (SQLException e) {
            String message = e.getMessage();
            System.err.println("Error message: " + message);
        }
    }
    
    // VIOLATION: Logging error message
    public void logErrorMessage() {
        try {
            performDatabaseOperation();
        } catch (SQLException ex) {
            String errorMsg = ex.getMessage();
            logger.severe("Database operation failed: " + errorMsg);
        }
    }
    
    // VIOLATION: Using getMessage() in exception wrapping
    public void wrapExceptionWithMessage() throws ApplicationException {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET active = false");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationException("Database error: " + e.getMessage(), e);
        }
    }
    
    // VIOLATION: Displaying error message to user
    public String getUserFriendlyError() {
        try {
            executeQuery("SELECT * FROM orders WHERE status = 'PENDING'");
            return "Success";
        } catch (SQLException e) {
            String message = e.getMessage();
            return "Operation failed: " + message;
        }
    }
    
    // VIOLATION: getMessage() with conditional logic
    public void handleMessageContent() {
        try {
            performUpdate();
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            if (msg.contains("duplicate")) {
                handleDuplicateEntry();
            } else if (msg.contains("foreign key")) {
                handleForeignKeyViolation();
            } else {
                System.err.println("Unknown error: " + msg);
            }
        }
    }
    
    // VIOLATION: Combining getMessage() with other exception details
    public void comprehensiveLogging() {
        try {
            executeBatchOperation();
        } catch (SQLException e) {
            String message = e.getMessage();
            String sqlState = e.getSQLState();
            int errorCode = e.getErrorCode();
            logger.warning("SQL Error - Message: " + message + 
                          ", State: " + sqlState + 
                          ", Code: " + errorCode);
        }
    }
    
    // VIOLATION: Using getMessage() in exception chain
    public void iterateExceptionMessages(SQLException ex) {
        SQLException current = ex;
        int count = 1;
        while (current != null) {
            String msg = current.getMessage();
            System.err.println("Exception " + count + ": " + msg);
            current = current.getNextException();
            count++;
        }
    }
    
    // VIOLATION: getMessage() for debugging
    public void debugWithMessage() {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE temp_data");
        } catch (SQLException ex) {
            String debugMsg = ex.getMessage();
            System.out.println("DEBUG: " + debugMsg);
        }
    }
    
    // VIOLATION: Storing message in database or log file
    public void persistErrorMessage() {
        try {
            performComplexOperation();
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            saveErrorToLog(errorMessage, e.getSQLState());
        }
    }
    
    // VIOLATION: Using getMessage() in alert/notification
    public void sendAlert() {
        try {
            executeImportantQuery();
        } catch (SQLException ex) {
            String message = ex.getMessage();
            sendNotification("Database Error", message);
        }
    }
    
    // VIOLATION: getMessage() with StringBuilder
    public String buildErrorReport() {
        StringBuilder report = new StringBuilder();
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            report.append("Error: ");
            report.append(e.getMessage());
            report.append("\nTime: ").append(System.currentTimeMillis());
        }
        return report.toString();
    }
    
    // VIOLATION: Using getMessage() for response construction
    public Response createErrorResponse() {
        try {
            processTransaction();
            return new Response(200, "Success");
        } catch (SQLException e) {
            String msg = e.getMessage();
            return new Response(500, "Database error: " + msg);
        }
    }
    
    // VIOLATION: getMessage() in custom logging method
    public void customLog() {
        try {
            Connection conn = getConnection();
            conn.createStatement().execute("TRUNCATE TABLE audit_log");
        } catch (SQLException ex) {
            logToFile("SQL Exception occurred: " + ex.getMessage());
        }
    }
    
    // VIOLATION: Pattern matching on message content
    public void parseErrorMessage() {
        try {
            executeUpdate("UPDATE accounts SET balance = -100");
        } catch (SQLException e) {
            String message = e.getMessage();
            if (message.matches(".*timeout.*")) {
                handleTimeout();
            } else if (message.matches(".*connection.*")) {
                handleConnectionError();
            }
        }
    }
    
    // NON-VIOLATION: Catching SQLException without using getMessage()
    public void catchWithoutMessage() {
        try {
            performDatabaseOperation();
        } catch (SQLException e) {
            e.printStackTrace(); // Uses toString(), not explicitly getMessage()
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
    public void handleGenerically() {
        try {
            performOperation();
        } catch (Exception e) {
            System.err.println("An error occurred");
        }
    }
    
    // NON-VIOLATION: Throwing SQLException without inspection
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
    private void performUpdate() throws SQLException { }
    private void handleDuplicateEntry() { }
    private void handleForeignKeyViolation() { }
    private void executeBatchOperation() throws SQLException { }
    private void performComplexOperation() throws SQLException { }
    private void saveErrorToLog(String msg, String state) { }
    private void executeImportantQuery() throws SQLException { }
    private void sendNotification(String title, String msg) { }
    private void processTransaction() throws SQLException { }
    private void logToFile(String msg) { }
    private void executeUpdate(String sql) throws SQLException { }
    private void handleTimeout() { }
    private void handleConnectionError() { }
    private void performOperation() throws Exception { }
}

class ApplicationException extends Exception {
    ApplicationException(String msg, Throwable cause) { super(msg, cause); }
}

class Response {
    int code;
    String message;
    Response(int code, String message) { 
        this.code = code; 
        this.message = message; 
    }
}

