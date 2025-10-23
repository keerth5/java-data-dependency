package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-041: DateDiffFunctionUsage
 * Detects "DATEDIFF(" SQL Server date function usage
 */
public class DateDiffFunctionUsageExample {
    
    public void basicDateDiffFunctionUsage() throws SQLException {
        // Basic DATEDIFF function usage
        String dateDiffDays = "SELECT DATEDIFF(day, order_date, GETDATE()) as days_since_order FROM orders";
        String dateDiffMonths = "SELECT DATEDIFF(month, created_date, GETDATE()) as months_since_creation FROM users";
        String dateDiffYears = "SELECT DATEDIFF(year, hire_date, GETDATE()) as years_of_service FROM employees";
        String dateDiffHours = "SELECT DATEDIFF(hour, last_login, GETDATE()) as hours_since_login FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(dateDiffDays);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateDiffMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(dateDiffYears);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(dateDiffHours);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionWithDifferentDateParts() throws SQLException {
        // DATEDIFF function with different date parts
        String dateDiffMillisecond = "SELECT DATEDIFF(millisecond, start_time, end_time) as execution_time_ms FROM performance_log";
        String dateDiffSecond = "SELECT DATEDIFF(second, login_time, logout_time) as session_duration_seconds FROM user_sessions";
        String dateDiffMinute = "SELECT DATEDIFF(minute, created_date, updated_date) as minutes_between_updates FROM products";
        String dateDiffWeek = "SELECT DATEDIFF(week, start_date, end_date) as weeks_duration FROM projects";
        String dateDiffQuarter = "SELECT DATEDIFF(quarter, fiscal_start, fiscal_end) as quarters_in_fiscal_year FROM fiscal_periods";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(dateDiffMillisecond);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateDiffSecond);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(dateDiffMinute);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(dateDiffWeek);
        rs4.close();
        
        ResultSet rs5 = stmt.executeQuery(dateDiffQuarter);
        rs5.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInWhereClauses() throws SQLException {
        // DATEDIFF function in WHERE clauses
        String whereWithDateDiff = "SELECT * FROM orders WHERE DATEDIFF(day, order_date, GETDATE()) <= 30";
        String whereWithDateDiffMonths = "SELECT * FROM users WHERE DATEDIFF(month, created_date, GETDATE()) >= 6";
        String whereWithDateDiffYears = "SELECT * FROM employees WHERE DATEDIFF(year, hire_date, GETDATE()) > 5";
        String whereWithDateDiffHours = "SELECT * FROM sessions WHERE DATEDIFF(hour, created_date, GETDATE()) < 24";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithDateDiffMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithDateDiffYears);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithDateDiffHours);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInOrderByClauses() throws SQLException {
        // DATEDIFF function in ORDER BY clauses
        String orderByWithDateDiff = "SELECT * FROM orders ORDER BY DATEDIFF(day, order_date, GETDATE()) ASC";
        String orderByWithDateDiffMonths = "SELECT * FROM users ORDER BY DATEDIFF(month, created_date, GETDATE()) DESC";
        String orderByWithDateDiffYears = "SELECT * FROM employees ORDER BY DATEDIFF(year, hire_date, GETDATE()) ASC";
        String orderByWithDateDiffHours = "SELECT * FROM sessions ORDER BY DATEDIFF(hour, created_date, GETDATE()) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithDateDiffMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithDateDiffYears);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithDateDiffHours);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInGroupByClauses() throws SQLException {
        // DATEDIFF function in GROUP BY clauses
        String groupByWithDateDiff = "SELECT DATEDIFF(day, order_date, GETDATE()) as days_ago, COUNT(*) as order_count FROM orders GROUP BY DATEDIFF(day, order_date, GETDATE())";
        String groupByWithDateDiffMonths = "SELECT DATEDIFF(month, created_date, GETDATE()) as months_ago, COUNT(*) as user_count FROM users GROUP BY DATEDIFF(month, created_date, GETDATE())";
        String groupByWithDateDiffYears = "SELECT DATEDIFF(year, hire_date, GETDATE()) as years_ago, COUNT(*) as employee_count FROM employees GROUP BY DATEDIFF(year, hire_date, GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithDateDiffMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithDateDiffYears);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInCaseStatements() throws SQLException {
        // DATEDIFF function in CASE statements
        String caseWithDateDiff = "SELECT name, CASE WHEN DATEDIFF(day, created_date, GETDATE()) <= 7 THEN 'New' WHEN DATEDIFF(day, created_date, GETDATE()) <= 30 THEN 'Recent' ELSE 'Old' END as user_category FROM users";
        String caseWithDateDiffComplex = "SELECT product_name, CASE WHEN DATEDIFF(day, order_date, GETDATE()) <= 1 THEN 'Very Recent' WHEN DATEDIFF(day, order_date, GETDATE()) <= 7 THEN 'Recent' WHEN DATEDIFF(day, order_date, GETDATE()) <= 30 THEN 'Old' ELSE 'Very Old' END as order_category FROM orders";
        String caseWithDateDiffMultiple = "SELECT name, CASE WHEN DATEDIFF(hour, last_login, GETDATE()) <= 1 THEN 'Active' WHEN DATEDIFF(hour, last_login, GETDATE()) <= 24 THEN 'Recent' WHEN DATEDIFF(day, last_login, GETDATE()) <= 7 THEN 'Inactive' ELSE 'Dormant' END as user_status FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithDateDiffComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithDateDiffMultiple);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInSubqueries() throws SQLException {
        // DATEDIFF function in subqueries
        String subqueryWithDateDiff = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE DATEDIFF(day, order_date, GETDATE()) <= 30)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND DATEDIFF(day, order_date, GETDATE()) <= 7) as recent_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND DATEDIFF(day, order_date, GETDATE()) <= 30)";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND DATEDIFF(day, order_date, GETDATE()) <= 30)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(subqueryWithNotExists);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInJoins() throws SQLException {
        // DATEDIFF function in JOINs
        String joinWithDateDiff = "SELECT u.name, o.order_date, DATEDIFF(day, o.order_date, GETDATE()) as days_since_order FROM users u JOIN orders o ON u.id = o.user_id WHERE DATEDIFF(day, o.order_date, GETDATE()) <= 30";
        String joinWithDateDiffCondition = "SELECT p.name, c.category_name, DATEDIFF(month, p.created_date, GETDATE()) as months_since_creation FROM products p JOIN categories c ON p.category_id = c.id WHERE DATEDIFF(month, p.created_date, GETDATE()) <= 6";
        String joinWithDateDiffCalculation = "SELECT u.name, COUNT(o.id) as order_count, DATEDIFF(day, u.created_date, GETDATE()) as days_since_registration FROM users u LEFT JOIN orders o ON u.id = o.user_id AND DATEDIFF(day, o.order_date, GETDATE()) <= 30 GROUP BY u.name, u.created_date";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithDateDiffCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithDateDiffCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInAggregates() throws SQLException {
        // DATEDIFF function in aggregate functions
        String aggregateWithDateDiff = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN DATEDIFF(day, created_date, GETDATE()) <= 30 THEN 1 END) as new_users FROM users";
        String sumWithDateDiff = "SELECT SUM(DATEDIFF(day, start_date, end_date)) as total_project_days FROM projects";
        String avgWithDateDiff = "SELECT AVG(DATEDIFF(day, order_date, delivery_date)) as avg_delivery_days FROM orders";
        String maxWithDateDiff = "SELECT MAX(DATEDIFF(day, last_login, GETDATE())) as max_days_since_login FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithDateDiff);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithDateDiff);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithDateDiff);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInUpdateStatements() throws SQLException {
        // DATEDIFF function in UPDATE statements
        String updateWithDateDiff = "UPDATE users SET status = 'inactive' WHERE DATEDIFF(day, last_login, GETDATE()) > 90";
        String updateWithDateDiffCondition = "UPDATE orders SET status = 'overdue' WHERE DATEDIFF(day, order_date, GETDATE()) > 30 AND status = 'pending'";
        String updateWithDateDiffCalculation = "UPDATE products SET discount = 0.1 WHERE DATEDIFF(month, created_date, GETDATE()) > 6";
        String updateWithDateDiffComplex = "UPDATE employees SET bonus_eligible = 1 WHERE DATEDIFF(year, hire_date, GETDATE()) >= 1";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithDateDiff);
        int result2 = stmt.executeUpdate(updateWithDateDiffCondition);
        int result3 = stmt.executeUpdate(updateWithDateDiffCalculation);
        int result4 = stmt.executeUpdate(updateWithDateDiffComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInInsertStatements() throws SQLException {
        // DATEDIFF function in INSERT statements
        String insertWithDateDiff = "INSERT INTO user_analytics (user_id, days_since_registration, analysis_date) SELECT id, DATEDIFF(day, created_date, GETDATE()), GETDATE() FROM users";
        String insertWithDateDiffCondition = "INSERT INTO inactive_users (user_id, days_inactive, last_activity) SELECT id, DATEDIFF(day, last_login, GETDATE()), last_login FROM users WHERE DATEDIFF(day, last_login, GETDATE()) > 30";
        String insertWithDateDiffCalculation = "INSERT INTO order_metrics (order_id, days_to_delivery, order_date) SELECT id, DATEDIFF(day, order_date, delivery_date), order_date FROM orders WHERE delivery_date IS NOT NULL";
        String insertWithDateDiffComplex = "INSERT INTO employee_tenure (employee_id, years_of_service, hire_date) SELECT id, DATEDIFF(year, hire_date, GETDATE()), hire_date FROM employees";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithDateDiff);
        int result2 = stmt.executeUpdate(insertWithDateDiffCondition);
        int result3 = stmt.executeUpdate(insertWithDateDiffCalculation);
        int result4 = stmt.executeUpdate(insertWithDateDiffComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionInPreparedStatement() throws SQLException {
        // DATEDIFF function in PreparedStatement
        String selectWithDateDiffParam = "SELECT * FROM orders WHERE DATEDIFF(day, order_date, GETDATE()) <= ?";
        String selectWithDateDiffParamCondition = "SELECT * FROM users WHERE DATEDIFF(month, created_date, GETDATE()) >= ?";
        String selectWithDateDiffParamCalculation = "SELECT DATEDIFF(day, ?, order_date) as days_difference FROM orders WHERE order_date > ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithDateDiffParam);
        pstmt1.setInt(1, 30);
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithDateDiffParamCondition);
        pstmt2.setInt(1, 6);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithDateDiffParamCalculation);
        pstmt3.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        pstmt3.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void dateDiffFunctionInMethods() throws SQLException {
        // DATEDIFF function in methods
        String userQuery = getUserQueryWithDateDiff();
        String orderQuery = getOrderQueryWithDateDiff();
        String productQuery = getProductQueryWithDateDiff();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(userQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderQuery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(productQuery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    private String getUserQueryWithDateDiff() {
        return "SELECT name, created_date, DATEDIFF(day, created_date, GETDATE()) as days_since_registration FROM users WHERE DATEDIFF(day, created_date, GETDATE()) <= 30";
    }
    
    private String getOrderQueryWithDateDiff() {
        return "SELECT order_id, order_date, DATEDIFF(day, order_date, GETDATE()) as days_since_order FROM orders WHERE DATEDIFF(day, order_date, GETDATE()) <= 7";
    }
    
    private String getProductQueryWithDateDiff() {
        return "SELECT name, created_date, DATEDIFF(month, created_date, GETDATE()) as months_since_creation FROM products WHERE DATEDIFF(month, created_date, GETDATE()) <= 6";
    }
    
    public void dateDiffFunctionWithDynamicSQL() throws SQLException {
        // DATEDIFF function with dynamic SQL construction
        String baseQuery = "SELECT * FROM orders WHERE DATEDIFF(day, order_date, GETDATE()) <= ";
        String days = "30";
        String endQuery = "";
        
        String dynamicQuery = baseQuery + days + endQuery;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionWithTransaction() throws SQLException {
        // DATEDIFF function with transaction
        String transactionWithDateDiff = "BEGIN TRANSACTION; UPDATE users SET status = 'inactive' WHERE DATEDIFF(day, last_login, GETDATE()) > 90; INSERT INTO user_activity_log (action, timestamp) VALUES ('INACTIVE_USERS_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithDateDiffRollback = "BEGIN TRANSACTION; UPDATE orders SET status = 'overdue' WHERE DATEDIFF(day, order_date, GETDATE()) > 30; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No orders updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Orders updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithDateDiff);
        
        boolean hasResult = stmt.execute(transactionWithDateDiffRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void dateDiffFunctionWithBusinessLogic() throws SQLException {
        // DATEDIFF function with business logic
        String businessLogicWithDateDiff = "SELECT name, CASE WHEN DATEDIFF(day, last_login, GETDATE()) <= 7 THEN 'Active' WHEN DATEDIFF(day, last_login, GETDATE()) <= 30 THEN 'Inactive' ELSE 'Dormant' END as user_status, DATEDIFF(day, created_date, GETDATE()) as days_since_registration FROM users";
        String businessLogicComplex = "SELECT product_name, order_date, DATEDIFF(day, order_date, GETDATE()) as days_since_order, CASE WHEN DATEDIFF(day, order_date, GETDATE()) <= 1 THEN 'Very Recent' WHEN DATEDIFF(day, order_date, GETDATE()) <= 7 THEN 'Recent' WHEN DATEDIFF(day, order_date, GETDATE()) <= 30 THEN 'Old' ELSE 'Very Old' END as order_category FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithDateDiff);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
}
