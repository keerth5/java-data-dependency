package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-025: GetUtcDateFunctionUsage
 * Detects "GETUTCDATE()" SQL Server function usage
 */
public class GetUtcDateFunctionUsageExample {
    
    public void basicGetUtcDateFunctionUsage() throws SQLException {
        // Basic GETUTCDATE() function usage
        String selectWithGetUtcDate = "SELECT name, email, GETUTCDATE() as utc_time FROM users";
        String insertWithGetUtcDate = "INSERT INTO audit_log (action, utc_timestamp) VALUES ('LOGIN', GETUTCDATE())";
        String updateWithGetUtcDate = "UPDATE users SET last_login_utc = GETUTCDATE() WHERE id = 1";
        String whereWithGetUtcDate = "SELECT * FROM orders WHERE order_date_utc > GETUTCDATE() - 30";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithGetUtcDate);
        rs1.close();
        
        int result1 = stmt.executeUpdate(insertWithGetUtcDate);
        int result2 = stmt.executeUpdate(updateWithGetUtcDate);
        
        ResultSet rs2 = stmt.executeQuery(whereWithGetUtcDate);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionInSelectStatements() throws SQLException {
        // GETUTCDATE() function in SELECT statements
        String selectCurrentUtcTime = "SELECT GETUTCDATE() as current_utc_datetime";
        String selectWithDateDiff = "SELECT name, DATEDIFF(day, created_date_utc, GETUTCDATE()) as days_since_created FROM users";
        String selectWithDateAdd = "SELECT name, DATEADD(day, 30, GETUTCDATE()) as utc_expiry_date FROM users";
        String selectWithFormat = "SELECT name, FORMAT(GETUTCDATE(), 'yyyy-MM-dd HH:mm:ss') as formatted_utc_date FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectCurrentUtcTime);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithDateDiff);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithDateAdd);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithFormat);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionInInsertStatements() throws SQLException {
        // GETUTCDATE() function in INSERT statements
        String insertUser = "INSERT INTO users (name, email, created_date_utc) VALUES ('John', 'john@example.com', GETUTCDATE())";
        String insertOrder = "INSERT INTO orders (user_id, product_name, order_date_utc, created_date_utc) VALUES (1, 'Laptop', GETUTCDATE(), GETUTCDATE())";
        String insertLog = "INSERT INTO audit_log (action, table_name, utc_timestamp, created_date_utc) VALUES ('INSERT', 'users', GETUTCDATE(), GETUTCDATE())";
        String insertWithDefault = "INSERT INTO products (name, price, created_date_utc) VALUES ('Tablet', 299.99, GETUTCDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertUser);
        int result2 = stmt.executeUpdate(insertOrder);
        int result3 = stmt.executeUpdate(insertLog);
        int result4 = stmt.executeUpdate(insertWithDefault);
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionInUpdateStatements() throws SQLException {
        // GETUTCDATE() function in UPDATE statements
        String updateLastLogin = "UPDATE users SET last_login_utc = GETUTCDATE() WHERE id = ?";
        String updateModifiedDate = "UPDATE products SET modified_date_utc = GETUTCDATE(), modified_by = 'system' WHERE product_id = ?";
        String updateWithCondition = "UPDATE orders SET status = 'processed', processed_date_utc = GETUTCDATE() WHERE order_date_utc < GETUTCDATE() - 1";
        String updateMultipleFields = "UPDATE users SET last_activity_utc = GETUTCDATE(), login_count = login_count + 1 WHERE id = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(updateLastLogin);
        pstmt1.setInt(1, 1);
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(updateModifiedDate);
        pstmt2.setInt(1, 100);
        pstmt2.executeUpdate();
        pstmt2.close();
        
        Statement stmt = conn.createStatement();
        int result1 = stmt.executeUpdate(updateWithCondition);
        stmt.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(updateMultipleFields);
        pstmt3.setInt(1, 2);
        pstmt3.executeUpdate();
        pstmt3.close();
        
        conn.close();
    }
    
    public void getUtcDateFunctionInWhereClauses() throws SQLException {
        // GETUTCDATE() function in WHERE clauses
        String whereRecentOrders = "SELECT * FROM orders WHERE order_date_utc > GETUTCDATE() - 7";
        String whereActiveUsers = "SELECT * FROM users WHERE last_login_utc > GETUTCDATE() - 30";
        String whereExpiredProducts = "SELECT * FROM products WHERE expiry_date_utc < GETUTCDATE()";
        String whereTodayOrders = "SELECT * FROM orders WHERE CAST(order_date_utc AS DATE) = CAST(GETUTCDATE() AS DATE)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereRecentOrders);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereActiveUsers);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereExpiredProducts);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereTodayOrders);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionWithDateArithmetic() throws SQLException {
        // GETUTCDATE() function with date arithmetic
        String dateAdd = "SELECT DATEADD(day, 30, GETUTCDATE()) as utc_future_date";
        String dateSub = "SELECT DATEADD(day, -30, GETUTCDATE()) as utc_past_date";
        String dateDiff = "SELECT DATEDIFF(day, created_date_utc, GETUTCDATE()) as days_old FROM users";
        String datePart = "SELECT DATEPART(year, GETUTCDATE()) as utc_year, DATEPART(month, GETUTCDATE()) as utc_month";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(dateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateSub);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(dateDiff);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(datePart);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionWithTimezoneConversion() throws SQLException {
        // GETUTCDATE() function with timezone conversion
        String utcToLocal = "SELECT SWITCHOFFSET(GETUTCDATE(), '+05:30') as local_time";
        String utcToEst = "SELECT SWITCHOFFSET(GETUTCDATE(), '-05:00') as est_time";
        String utcToPst = "SELECT SWITCHOFFSET(GETUTCDATE(), '-08:00') as pst_time";
        String utcToGmt = "SELECT GETUTCDATE() as gmt_time";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(utcToLocal);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(utcToEst);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(utcToPst);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(utcToGmt);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionWithStringFunctions() throws SQLException {
        // GETUTCDATE() function with string functions
        String formatUtcDate = "SELECT FORMAT(GETUTCDATE(), 'yyyy-MM-dd') as utc_date_only";
        String convertUtcDate = "SELECT CONVERT(varchar, GETUTCDATE(), 120) as utc_iso_format";
        String castUtcDate = "SELECT CAST(GETUTCDATE() AS DATE) as utc_date_part";
        String stringUtcDate = "SELECT CONVERT(varchar, GETUTCDATE(), 101) as utc_us_format";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(formatUtcDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(convertUtcDate);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(castUtcDate);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(stringUtcDate);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionInStoredProcedures() throws SQLException {
        // GETUTCDATE() function in stored procedure calls
        String callProcedure = "{call sp_update_user_last_login_utc(?, GETUTCDATE())}";
        String callProcedureWithDate = "{call sp_create_audit_log_utc(?, ?, GETUTCDATE())}";
        String callProcedureComplex = "{call sp_process_orders_utc(GETUTCDATE(), GETUTCDATE() - 1)}";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(callProcedure);
        cstmt1.setInt(1, 1);
        cstmt1.execute();
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(callProcedureWithDate);
        cstmt2.setString(1, "LOGIN");
        cstmt2.setString(2, "users");
        cstmt2.execute();
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(callProcedureComplex);
        cstmt3.execute();
        cstmt3.close();
        
        conn.close();
    }
    
    public void getUtcDateFunctionInSubqueries() throws SQLException {
        // GETUTCDATE() function in subqueries
        String subqueryWithGetUtcDate = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE order_date_utc > GETUTCDATE() - 30)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND order_date_utc > GETUTCDATE() - 7) as recent_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND order_date_utc > GETUTCDATE() - 1)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithGetUtcDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionInCaseStatements() throws SQLException {
        // GETUTCDATE() function in CASE statements
        String caseWithGetUtcDate = "SELECT name, CASE WHEN last_login_utc > GETUTCDATE() - 7 THEN 'Active' WHEN last_login_utc > GETUTCDATE() - 30 THEN 'Inactive' ELSE 'Dormant' END as status FROM users";
        String caseWithDateDiff = "SELECT name, CASE WHEN DATEDIFF(day, created_date_utc, GETUTCDATE()) < 30 THEN 'New' WHEN DATEDIFF(day, created_date_utc, GETUTCDATE()) < 365 THEN 'Regular' ELSE 'Old' END as user_type FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithGetUtcDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithDateDiff);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionInMethods() throws SQLException {
        // GETUTCDATE() function in methods
        String userQuery = getUserQueryWithGetUtcDate();
        String orderQuery = getOrderQueryWithGetUtcDate();
        String logQuery = getLogQueryWithGetUtcDate();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(userQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderQuery);
        rs2.close();
        
        int result = stmt.executeUpdate(logQuery);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserQueryWithGetUtcDate() {
        return "SELECT name, email, DATEDIFF(day, created_date_utc, GETUTCDATE()) as days_since_registration FROM users WHERE last_login_utc > GETUTCDATE() - 30";
    }
    
    private String getOrderQueryWithGetUtcDate() {
        return "SELECT * FROM orders WHERE order_date_utc BETWEEN GETUTCDATE() - 7 AND GETUTCDATE() ORDER BY order_date_utc DESC";
    }
    
    private String getLogQueryWithGetUtcDate() {
        return "INSERT INTO audit_log (action, table_name, utc_timestamp) VALUES ('QUERY_EXECUTED', 'users', GETUTCDATE())";
    }
    
    public void getUtcDateFunctionWithDynamicSQL() throws SQLException {
        // GETUTCDATE() function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE";
        String dateCondition = " last_login_utc > GETUTCDATE() - 30";
        String statusCondition = " AND status = 'active'";
        
        String dynamicQuery = baseQuery + dateCondition + statusCondition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionWithBatchOperations() throws SQLException {
        // GETUTCDATE() function with batch operations
        String updateQuery = "UPDATE users SET last_activity_utc = GETUTCDATE() WHERE id = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(updateQuery);
        
        // Add multiple updates to batch
        pstmt.setInt(1, 1);
        pstmt.addBatch();
        
        pstmt.setInt(1, 2);
        pstmt.addBatch();
        
        pstmt.setInt(1, 3);
        pstmt.addBatch();
        
        // Execute batch
        int[] results = pstmt.executeBatch();
        
        pstmt.close();
        conn.close();
    }
    
    public void getUtcDateFunctionWithTransaction() throws SQLException {
        // GETUTCDATE() function with transaction
        String updateUser = "UPDATE users SET last_login_utc = GETUTCDATE() WHERE id = 1";
        String insertLog = "INSERT INTO login_log (user_id, login_time_utc) VALUES (1, GETUTCDATE())";
        String updateStats = "UPDATE user_stats SET login_count = login_count + 1, last_login_utc = GETUTCDATE() WHERE user_id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            int result1 = stmt.executeUpdate(updateUser);
            int result2 = stmt.executeUpdate(insertLog);
            int result3 = stmt.executeUpdate(updateStats);
            
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    
    public void getUtcDateFunctionForGlobalDeployment() throws SQLException {
        // GETUTCDATE() function for global deployment scenarios
        String globalUserQuery = "SELECT name, email, GETUTCDATE() as server_time, SWITCHOFFSET(GETUTCDATE(), '+00:00') as utc_time FROM users";
        String globalOrderQuery = "SELECT * FROM orders WHERE order_date_utc BETWEEN GETUTCDATE() - 1 AND GETUTCDATE()";
        String globalLogQuery = "INSERT INTO global_audit_log (action, server_time_utc, timezone) VALUES ('GLOBAL_ACTION', GETUTCDATE(), 'UTC')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(globalUserQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(globalOrderQuery);
        rs2.close();
        
        int result = stmt.executeUpdate(globalLogQuery);
        
        stmt.close();
        conn.close();
    }
}
