package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.math.BigDecimal;

/**
 * Test file for sql-java-053: CallableStatementUsage
 * Detects java.sql.CallableStatement interface usage
 */
public class CallableStatementUsageExample {
    
    public void basicCallableStatementUsage() throws SQLException {
        // Basic CallableStatement usage
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Simple stored procedure call
        String spCall = "{call GetActiveUsers}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        ResultSet rs = cstmt.executeQuery();
        rs.close();
        cstmt.close();
        
        // Stored procedure with input parameters
        String spWithInput = "{call GetUserById(?)}";
        CallableStatement cstmt2 = conn.prepareCall(spWithInput);
        cstmt2.setInt(1, 1);
        ResultSet rs2 = cstmt2.executeQuery();
        rs2.close();
        cstmt2.close();
        
        // Stored procedure with output parameters
        String spWithOutput = "{call CreateUser(?, ?, ?)}";
        CallableStatement cstmt3 = conn.prepareCall(spWithOutput);
        cstmt3.setString(1, "John Doe");
        cstmt3.setString(2, "john@example.com");
        cstmt3.registerOutParameter(3, Types.INTEGER);
        cstmt3.execute();
        int newUserId = cstmt3.getInt(3);
        cstmt3.close();
        
        conn.close();
    }
    
    public void callableStatementWithInputParameters() throws SQLException {
        // CallableStatement with input parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // String input parameter
        String spCall = "{call GetUsersByName(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        cstmt.setString(1, "John");
        ResultSet rs = cstmt.executeQuery();
        rs.close();
        cstmt.close();
        
        // Integer input parameter
        String spCall2 = "{call GetOrdersByUserId(?)}";
        CallableStatement cstmt2 = conn.prepareCall(spCall2);
        cstmt2.setInt(1, 1);
        ResultSet rs2 = cstmt2.executeQuery();
        rs2.close();
        cstmt2.close();
        
        // BigDecimal input parameter
        String spCall3 = "{call GetOrdersByAmount(?)}";
        CallableStatement cstmt3 = conn.prepareCall(spCall3);
        cstmt3.setBigDecimal(1, new BigDecimal("100.00"));
        ResultSet rs3 = cstmt3.executeQuery();
        rs3.close();
        cstmt3.close();
        
        conn.close();
    }
    
    public void callableStatementWithOutputParameters() throws SQLException {
        // CallableStatement with output parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Integer output parameter
        String spCall = "{call GetUserCount(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.execute();
        int userCount = cstmt.getInt(1);
        cstmt.close();
        
        // String output parameter
        String spCall2 = "{call GetUserName(?, ?)}";
        CallableStatement cstmt2 = conn.prepareCall(spCall2);
        cstmt2.setInt(1, 1);
        cstmt2.registerOutParameter(2, Types.VARCHAR);
        cstmt2.execute();
        String userName = cstmt2.getString(2);
        cstmt2.close();
        
        // BigDecimal output parameter
        String spCall3 = "{call GetTotalSales(?)}";
        CallableStatement cstmt3 = conn.prepareCall(spCall3);
        cstmt3.registerOutParameter(1, Types.DECIMAL);
        cstmt3.execute();
        BigDecimal totalSales = cstmt3.getBigDecimal(1);
        cstmt3.close();
        
        conn.close();
    }
    
    public void callableStatementWithInputOutputParameters() throws SQLException {
        // CallableStatement with input/output parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Input/output parameter
        String spCall = "{call ProcessOrder(?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        cstmt.setInt(1, 1); // order ID
        cstmt.registerOutParameter(2, Types.INTEGER); // result code
        cstmt.execute();
        int resultCode = cstmt.getInt(2);
        cstmt.close();
        
        // Multiple input/output parameters
        String spCall2 = "{call CalculateOrderTotal(?, ?, ?)}";
        CallableStatement cstmt2 = conn.prepareCall(spCall2);
        cstmt2.setInt(1, 1); // order ID
        cstmt2.registerOutParameter(2, Types.DECIMAL); // subtotal
        cstmt2.registerOutParameter(3, Types.DECIMAL); // tax
        cstmt2.execute();
        BigDecimal subtotal = cstmt2.getBigDecimal(2);
        BigDecimal tax = cstmt2.getBigDecimal(3);
        cstmt2.close();
        
        conn.close();
    }
    
    public void callableStatementWithReturnValue() throws SQLException {
        // CallableStatement with return value
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Stored procedure with return value
        String spCall = "{? = call GetUserCount()}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.execute();
        int userCount = cstmt.getInt(1);
        cstmt.close();
        
        // Stored procedure with return value and parameters
        String spCall2 = "{? = call GetUserCountByStatus(?)}";
        CallableStatement cstmt2 = conn.prepareCall(spCall2);
        cstmt2.registerOutParameter(1, Types.INTEGER);
        cstmt2.setString(2, "active");
        cstmt2.execute();
        int activeUserCount = cstmt2.getInt(1);
        cstmt2.close();
        
        conn.close();
    }
    
    public void callableStatementWithResultSet() throws SQLException {
        // CallableStatement with ResultSet
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Stored procedure returning ResultSet
        String spCall = "{call GetActiveUsers}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        ResultSet rs = cstmt.executeQuery();
        
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            System.out.println("User: " + name + ", Email: " + email);
        }
        
        rs.close();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithMultipleResultSets() throws SQLException {
        // CallableStatement with multiple ResultSets
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call GetUserAndOrders(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        cstmt.setInt(1, 1);
        
        boolean hasResult = cstmt.execute();
        int resultSetCount = 0;
        
        do {
            if (hasResult) {
                ResultSet rs = cstmt.getResultSet();
                resultSetCount++;
                rs.close();
            }
            hasResult = cstmt.getMoreResults();
        } while (hasResult);
        
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithBatchOperations() throws SQLException {
        // CallableStatement with batch operations
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call ProcessOrder(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        // Add multiple calls to batch
        cstmt.setInt(1, 1);
        cstmt.addBatch();
        
        cstmt.setInt(1, 2);
        cstmt.addBatch();
        
        cstmt.setInt(1, 3);
        cstmt.addBatch();
        
        // Execute batch
        int[] results = cstmt.executeBatch();
        
        // Clear batch
        cstmt.clearBatch();
        
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithTransaction() throws SQLException {
        // CallableStatement with transaction
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            
            // Call stored procedure to create user
            String createUserSP = "{call CreateUser(?, ?, ?)}";
            CallableStatement cstmt1 = conn.prepareCall(createUserSP);
            cstmt1.setString(1, "John Doe");
            cstmt1.setString(2, "john@example.com");
            cstmt1.registerOutParameter(3, Types.INTEGER);
            cstmt1.execute();
            int newUserId = cstmt1.getInt(3);
            cstmt1.close();
            
            // Call stored procedure to create order
            String createOrderSP = "{call CreateOrder(?, ?, ?)}";
            CallableStatement cstmt2 = conn.prepareCall(createOrderSP);
            cstmt2.setInt(1, newUserId);
            cstmt2.setBigDecimal(2, new BigDecimal("100.00"));
            cstmt2.registerOutParameter(3, Types.INTEGER);
            cstmt2.execute();
            int newOrderId = cstmt2.getInt(3);
            cstmt2.close();
            
            conn.commit();
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
    
    public void callableStatementWithErrorHandling() throws SQLException {
        // CallableStatement with error handling
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call GetUserById(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        try {
            cstmt.setInt(1, 1);
            ResultSet rs = cstmt.executeQuery();
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } finally {
            cstmt.close();
            conn.close();
        }
    }
    
    public void callableStatementWithConnectionProperties() throws SQLException {
        // CallableStatement with connection properties
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call GetUserById(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        // Set query timeout
        cstmt.setQueryTimeout(30);
        
        cstmt.setInt(1, 1);
        ResultSet rs = cstmt.executeQuery();
        rs.close();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithNullParameters() throws SQLException {
        // CallableStatement with null parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call UpdateUser(?, ?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        cstmt.setInt(1, 1);
        cstmt.setString(2, "John Doe");
        cstmt.setNull(3, Types.VARCHAR); // phone is null
        
        cstmt.execute();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithDateParameters() throws SQLException {
        // CallableStatement with date parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call GetOrdersByDateRange(?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        cstmt.setDate(1, new java.sql.Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000)); // 30 days ago
        cstmt.setDate(2, new java.sql.Date(System.currentTimeMillis())); // today
        
        ResultSet rs = cstmt.executeQuery();
        rs.close();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithTimestampParameters() throws SQLException {
        // CallableStatement with timestamp parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call LogUserActivity(?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        cstmt.setInt(1, 1);
        cstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
        
        cstmt.execute();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithBooleanParameters() throws SQLException {
        // CallableStatement with boolean parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call UpdateUserStatus(?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        cstmt.setInt(1, 1);
        cstmt.setBoolean(2, true);
        
        cstmt.execute();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithArrayParameters() throws SQLException {
        // CallableStatement with array parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call GetUsersByIds(?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        // Create array of integers
        java.sql.Array idArray = conn.createArrayOf("INTEGER", new Integer[]{1, 2, 3, 4, 5});
        cstmt.setArray(1, idArray);
        
        ResultSet rs = cstmt.executeQuery();
        rs.close();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithClobParameters() throws SQLException {
        // CallableStatement with CLOB parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call SaveUserNotes(?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        cstmt.setInt(1, 1);
        cstmt.setClob(2, new java.io.StringReader("This is a long text note about the user."));
        
        cstmt.execute();
        cstmt.close();
        conn.close();
    }
    
    public void callableStatementWithBlobParameters() throws SQLException {
        // CallableStatement with BLOB parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String spCall = "{call SaveUserImage(?, ?)}";
        CallableStatement cstmt = conn.prepareCall(spCall);
        
        cstmt.setInt(1, 1);
        cstmt.setBlob(2, new java.io.ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5}));
        
        cstmt.execute();
        cstmt.close();
        conn.close();
    }
}
