package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-024: GetDateFunctionUsage
 * Detects "GETDATE()" SQL Server function usage
 */
public class GetDateFunctionUsageExample {
    
    public void basicGetDateFunctionUsage() throws SQLException {
        // Basic GETDATE() function usage
        String selectWithGetDate = "SELECT name, email, GETDATE() as current_time FROM users";
        String insertWithGetDate = "INSERT INTO audit_log (action, timestamp) VALUES ('LOGIN', GETDATE())";
        String updateWithGetDate = "UPDATE users SET last_login = GETDATE() WHERE id = 1";
        String whereWithGetDate = "SELECT * FROM orders WHERE order_date > GETDATE() - 30";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithGetDate);
        rs1.close();
        
        int result1 = stmt.executeUpdate(insertWithGetDate);
        int result2 = stmt.executeUpdate(updateWithGetDate);
        
        ResultSet rs2 = stmt.executeQuery(whereWithGetDate);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getDateFunctionInSelectStatements() throws SQLException {
        // GETDATE() function in SELECT statements
        String selectCurrentTime = "SELECT GETDATE() as current_datetime";
        String selectWithDateDiff = "SELECT name, DATEDIFF(day, created_date, GETDATE()) as days_since_created FROM users";
        String selectWithDateAdd = "SELECT name, DATEADD(day, 30, GETDATE()) as expiry_date FROM users";
        String selectWithFormat = "SELECT name, FORMAT(GETDATE(), 'yyyy-MM-dd HH:mm:ss') as formatted_date FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectCurrentTime);
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
    
    public void getDateFunctionInInsertStatements() throws SQLException {
        // GETDATE() function in INSERT statements
        String insertUser = "INSERT INTO users (name, email, created_date) VALUES ('John', 'john@example.com', GETDATE())";
        String insertOrder = "INSERT INTO orders (user_id, product_name, order_date, created_date) VALUES (1, 'Laptop', GETDATE(), GETDATE())";
        String insertLog = "INSERT INTO audit_log (action, table_name, timestamp, created_date) VALUES ('INSERT', 'users', GETDATE(), GETDATE())";
        String insertWithDefault = "INSERT INTO products (name, price, created_date) VALUES ('Tablet', 299.99, GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertUser);
        int result2 = stmt.executeUpdate(insertOrder);
        int result3 = stmt.executeUpdate(insertLog);
        int result4 = stmt.executeUpdate(insertWithDefault);
        
        stmt.close();
        conn.close();
    }
    
    public void getDateFunctionInUpdateStatements() throws SQLException {
        // GETDATE() function in UPDATE statements
        String updateLastLogin = "UPDATE users SET last_login = GETDATE() WHERE id = ?";
        String updateModifiedDate = "UPDATE products SET modified_date = GETDATE(), modified_by = 'system' WHERE product_id = ?";
        String updateWithCondition = "UPDATE orders SET status = 'processed', processed_date = GETDATE() WHERE order_date < GETDATE() - 1";
        String updateMultipleFields = "UPDATE users SET last_activity = GETDATE(), login_count = login_count + 1 WHERE id = ?";
        
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
    
    public void getDateFunctionInWhereClauses() throws SQLException {
        // GETDATE() function in WHERE clauses
        String whereRecentOrders = "SELECT * FROM orders WHERE order_date > GETDATE() - 7";
        String whereActiveUsers = "SELECT * FROM users WHERE last_login > GETDATE() - 30";
        String whereExpiredProducts = "SELECT * FROM products WHERE expiry_date < GETDATE()";
        String whereTodayOrders = "SELECT * FROM orders WHERE CAST(order_date AS DATE) = CAST(GETDATE() AS DATE)";
        
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
    
    public void getDateFunctionWithDateArithmetic() throws SQLException {
        // GETDATE() function with date arithmetic
        String dateAdd = "SELECT DATEADD(day, 30, GETDATE()) as future_date";
        String dateSub = "SELECT DATEADD(day, -30, GETDATE()) as past_date";
        String dateDiff = "SELECT DATEDIFF(day, created_date, GETDATE()) as days_old FROM users";
        String datePart = "SELECT DATEPART(year, GETDATE()) as current_year, DATEPART(month, GETDATE()) as current_month";
        
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
    
    public void getDateFunctionWithStringFunctions() throws SQLException {
        // GETDATE() function with string functions
        String formatDate = "SELECT FORMAT(GETDATE(), 'yyyy-MM-dd') as date_only";
        String convertDate = "SELECT CONVERT(varchar, GETDATE(), 120) as iso_format";
        String castDate = "SELECT CAST(GETDATE() AS DATE) as date_part";
        String stringDate = "SELECT CONVERT(varchar, GETDATE(), 101) as us_format";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(formatDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(convertDate);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(castDate);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(stringDate);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getDateFunctionInStoredProcedures() throws SQLException {
        // GETDATE() function in stored procedure calls
        String callProcedure = "{call sp_update_user_last_login(?, GETDATE())}";
        String callProcedureWithDate = "{call sp_create_audit_log(?, ?, GETDATE())}";
        String callProcedureComplex = "{call sp_process_orders(GETDATE(), GETDATE() - 1)}";
        
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
    
    public void getDateFunctionInSubqueries() throws SQLException {
        // GETDATE() function in subqueries
        String subqueryWithGetDate = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE order_date > GETDATE() - 30)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND order_date > GETDATE() - 7) as recent_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND order_date > GETDATE() - 1)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithGetDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getDateFunctionInCaseStatements() throws SQLException {
        // GETDATE() function in CASE statements
        String caseWithGetDate = "SELECT name, CASE WHEN last_login > GETDATE() - 7 THEN 'Active' WHEN last_login > GETDATE() - 30 THEN 'Inactive' ELSE 'Dormant' END as status FROM users";
        String caseWithDateDiff = "SELECT name, CASE WHEN DATEDIFF(day, created_date, GETDATE()) < 30 THEN 'New' WHEN DATEDIFF(day, created_date, GETDATE()) < 365 THEN 'Regular' ELSE 'Old' END as user_type FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithGetDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithDateDiff);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void getDateFunctionInMethods() throws SQLException {
        // GETDATE() function in methods
        String userQuery = getUserQueryWithGetDate();
        String orderQuery = getOrderQueryWithGetDate();
        String logQuery = getLogQueryWithGetDate();
        
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
    
    private String getUserQueryWithGetDate() {
        return "SELECT name, email, DATEDIFF(day, created_date, GETDATE()) as days_since_registration FROM users WHERE last_login > GETDATE() - 30";
    }
    
    private String getOrderQueryWithGetDate() {
        return "SELECT * FROM orders WHERE order_date BETWEEN GETDATE() - 7 AND GETDATE() ORDER BY order_date DESC";
    }
    
    private String getLogQueryWithGetDate() {
        return "INSERT INTO audit_log (action, table_name, timestamp) VALUES ('QUERY_EXECUTED', 'users', GETDATE())";
    }
    
    public void getDateFunctionWithDynamicSQL() throws SQLException {
        // GETDATE() function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE";
        String dateCondition = " last_login > GETDATE() - 30";
        String statusCondition = " AND status = 'active'";
        
        String dynamicQuery = baseQuery + dateCondition + statusCondition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void getDateFunctionWithBatchOperations() throws SQLException {
        // GETDATE() function with batch operations
        String updateQuery = "UPDATE users SET last_activity = GETDATE() WHERE id = ?";
        
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
    
    public void getDateFunctionWithTransaction() throws SQLException {
        // GETDATE() function with transaction
        String updateUser = "UPDATE users SET last_login = GETDATE() WHERE id = 1";
        String insertLog = "INSERT INTO login_log (user_id, login_time) VALUES (1, GETDATE())";
        String updateStats = "UPDATE user_stats SET login_count = login_count + 1, last_login = GETDATE() WHERE user_id = 1";
        
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
}
