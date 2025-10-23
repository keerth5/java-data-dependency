package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-040: DateAddFunctionUsage
 * Detects "DATEADD(" SQL Server date function usage
 */
public class DateAddFunctionUsageExample {
    
    public void basicDateAddFunctionUsage() throws SQLException {
        // Basic DATEADD function usage
        String dateAddDays = "SELECT DATEADD(day, 7, GETDATE()) as date_plus_7_days";
        String dateAddMonths = "SELECT DATEADD(month, 1, order_date) as next_month FROM orders";
        String dateAddYears = "SELECT DATEADD(year, 1, created_date) as next_year FROM users";
        String dateAddHours = "SELECT DATEADD(hour, 24, last_login) as next_day_login FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(dateAddDays);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateAddMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(dateAddYears);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(dateAddHours);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionWithDifferentDateParts() throws SQLException {
        // DATEADD function with different date parts
        String dateAddMillisecond = "SELECT DATEADD(millisecond, 500, GETDATE()) as date_plus_milliseconds";
        String dateAddSecond = "SELECT DATEADD(second, 30, order_date) as date_plus_seconds FROM orders";
        String dateAddMinute = "SELECT DATEADD(minute, 15, created_date) as date_plus_minutes FROM users";
        String dateAddWeek = "SELECT DATEADD(week, 2, last_activity) as date_plus_weeks FROM users";
        String dateAddQuarter = "SELECT DATEADD(quarter, 1, created_date) as date_plus_quarter FROM products";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(dateAddMillisecond);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateAddSecond);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(dateAddMinute);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(dateAddWeek);
        rs4.close();
        
        ResultSet rs5 = stmt.executeQuery(dateAddQuarter);
        rs5.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInWhereClauses() throws SQLException {
        // DATEADD function in WHERE clauses
        String whereWithDateAdd = "SELECT * FROM orders WHERE order_date > DATEADD(day, -30, GETDATE())";
        String whereWithDateAddMonths = "SELECT * FROM users WHERE created_date > DATEADD(month, -6, GETDATE())";
        String whereWithDateAddYears = "SELECT * FROM products WHERE created_date > DATEADD(year, -1, GETDATE())";
        String whereWithDateAddHours = "SELECT * FROM sessions WHERE created_date > DATEADD(hour, -24, GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithDateAddMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithDateAddYears);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithDateAddHours);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInOrderByClauses() throws SQLException {
        // DATEADD function in ORDER BY clauses
        String orderByWithDateAdd = "SELECT * FROM orders ORDER BY DATEADD(day, 7, order_date) DESC";
        String orderByWithDateAddMonths = "SELECT * FROM users ORDER BY DATEADD(month, 1, created_date) ASC";
        String orderByWithDateAddYears = "SELECT * FROM products ORDER BY DATEADD(year, 1, created_date) DESC";
        String orderByWithDateAddHours = "SELECT * FROM sessions ORDER BY DATEADD(hour, 24, created_date) ASC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithDateAddMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithDateAddYears);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithDateAddHours);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInGroupByClauses() throws SQLException {
        // DATEADD function in GROUP BY clauses
        String groupByWithDateAdd = "SELECT DATEADD(day, -DAY(order_date) + 1, order_date) as month_start, COUNT(*) as order_count FROM orders GROUP BY DATEADD(day, -DAY(order_date) + 1, order_date)";
        String groupByWithDateAddMonths = "SELECT DATEADD(month, -MONTH(created_date) + 1, created_date) as year_start, COUNT(*) as user_count FROM users GROUP BY DATEADD(month, -MONTH(created_date) + 1, created_date)";
        String groupByWithDateAddYears = "SELECT DATEADD(year, -YEAR(created_date) + 1, created_date) as decade_start, COUNT(*) as product_count FROM products GROUP BY DATEADD(year, -YEAR(created_date) + 1, created_date)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithDateAddMonths);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithDateAddYears);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInCaseStatements() throws SQLException {
        // DATEADD function in CASE statements
        String caseWithDateAdd = "SELECT name, CASE WHEN created_date > DATEADD(day, -30, GETDATE()) THEN 'New' WHEN created_date > DATEADD(day, -90, GETDATE()) THEN 'Recent' ELSE 'Old' END as user_category FROM users";
        String caseWithDateAddComplex = "SELECT product_name, CASE WHEN order_date > DATEADD(month, -1, GETDATE()) THEN 'Recent Order' WHEN order_date > DATEADD(month, -6, GETDATE()) THEN 'Recent' WHEN order_date > DATEADD(year, -1, GETDATE()) THEN 'Old' ELSE 'Very Old' END as order_category FROM orders";
        String caseWithDateAddMultiple = "SELECT name, CASE WHEN last_login > DATEADD(day, -7, GETDATE()) THEN 'Active' WHEN last_login > DATEADD(day, -30, GETDATE()) THEN 'Inactive' WHEN last_login > DATEADD(day, -90, GETDATE()) THEN 'Dormant' ELSE 'Abandoned' END as user_status FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithDateAddComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithDateAddMultiple);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInSubqueries() throws SQLException {
        // DATEADD function in subqueries
        String subqueryWithDateAdd = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE order_date > DATEADD(day, -30, GETDATE()))";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND order_date > DATEADD(day, -7, GETDATE())) as recent_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND order_date > DATEADD(month, -1, GETDATE()))";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND order_date > DATEADD(day, -30, GETDATE()))";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithDateAdd);
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
    
    public void dateAddFunctionInJoins() throws SQLException {
        // DATEADD function in JOINs
        String joinWithDateAdd = "SELECT u.name, o.order_date, DATEADD(day, 7, o.order_date) as expected_delivery FROM users u JOIN orders o ON u.id = o.user_id WHERE o.order_date > DATEADD(day, -30, GETDATE())";
        String joinWithDateAddCondition = "SELECT p.name, c.category_name, DATEADD(month, 1, p.created_date) as next_review_date FROM products p JOIN categories c ON p.category_id = c.id WHERE p.created_date > DATEADD(year, -1, GETDATE())";
        String joinWithDateAddCalculation = "SELECT u.name, COUNT(o.id) as order_count, DATEADD(day, -30, GETDATE()) as cutoff_date FROM users u LEFT JOIN orders o ON u.id = o.user_id AND o.order_date > DATEADD(day, -30, GETDATE()) GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithDateAddCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithDateAddCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInAggregates() throws SQLException {
        // DATEADD function in aggregate functions
        String aggregateWithDateAdd = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN created_date > DATEADD(day, -30, GETDATE()) THEN 1 END) as new_users FROM users";
        String sumWithDateAdd = "SELECT SUM(total_amount) as total_revenue FROM orders WHERE order_date > DATEADD(month, -1, GETDATE())";
        String avgWithDateAdd = "SELECT AVG(DATEDIFF(day, order_date, DATEADD(day, 7, order_date))) as avg_delivery_days FROM orders";
        String maxWithDateAdd = "SELECT MAX(DATEADD(day, 30, last_login)) as latest_activity_plus_30 FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithDateAdd);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithDateAdd);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithDateAdd);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInUpdateStatements() throws SQLException {
        // DATEADD function in UPDATE statements
        String updateWithDateAdd = "UPDATE users SET last_activity = DATEADD(day, 1, last_activity) WHERE status = 'active'";
        String updateWithDateAddCondition = "UPDATE orders SET expected_delivery = DATEADD(day, 7, order_date) WHERE status = 'pending'";
        String updateWithDateAddCalculation = "UPDATE products SET next_review_date = DATEADD(month, 6, created_date) WHERE category_id = 1";
        String updateWithDateAddComplex = "UPDATE users SET expiry_date = DATEADD(year, 1, created_date) WHERE status = 'premium'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithDateAdd);
        int result2 = stmt.executeUpdate(updateWithDateAddCondition);
        int result3 = stmt.executeUpdate(updateWithDateAddCalculation);
        int result4 = stmt.executeUpdate(updateWithDateAddComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInInsertStatements() throws SQLException {
        // DATEADD function in INSERT statements
        String insertWithDateAdd = "INSERT INTO orders (user_id, product_name, order_date, expected_delivery) VALUES (1, 'Laptop', GETDATE(), DATEADD(day, 7, GETDATE()))";
        String insertWithDateAddCondition = "INSERT INTO user_sessions (user_id, session_start, session_end) VALUES (1, GETDATE(), DATEADD(hour, 2, GETDATE()))";
        String insertWithDateAddCalculation = "INSERT INTO product_reviews (product_id, review_date, next_review_date) VALUES (1, GETDATE(), DATEADD(month, 3, GETDATE()))";
        String insertWithDateAddComplex = "INSERT INTO user_subscriptions (user_id, start_date, end_date) VALUES (1, GETDATE(), DATEADD(year, 1, GETDATE()))";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithDateAdd);
        int result2 = stmt.executeUpdate(insertWithDateAddCondition);
        int result3 = stmt.executeUpdate(insertWithDateAddCalculation);
        int result4 = stmt.executeUpdate(insertWithDateAddComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionInPreparedStatement() throws SQLException {
        // DATEADD function in PreparedStatement
        String selectWithDateAddParam = "SELECT * FROM orders WHERE order_date > DATEADD(day, ?, GETDATE())";
        String selectWithDateAddParamCondition = "SELECT * FROM users WHERE created_date > DATEADD(month, ?, GETDATE())";
        String selectWithDateAddParamCalculation = "SELECT DATEADD(day, ?, order_date) as future_date FROM orders WHERE order_date > ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithDateAddParam);
        pstmt1.setInt(1, -30);
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithDateAddParamCondition);
        pstmt2.setInt(1, -6);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithDateAddParamCalculation);
        pstmt3.setInt(1, 7);
        pstmt3.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void dateAddFunctionInMethods() throws SQLException {
        // DATEADD function in methods
        String userQuery = getUserQueryWithDateAdd();
        String orderQuery = getOrderQueryWithDateAdd();
        String productQuery = getProductQueryWithDateAdd();
        
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
    
    private String getUserQueryWithDateAdd() {
        return "SELECT name, created_date, DATEADD(day, 30, created_date) as trial_end_date FROM users WHERE created_date > DATEADD(day, -30, GETDATE())";
    }
    
    private String getOrderQueryWithDateAdd() {
        return "SELECT order_id, order_date, DATEADD(day, 7, order_date) as expected_delivery FROM orders WHERE order_date > DATEADD(day, -7, GETDATE())";
    }
    
    private String getProductQueryWithDateAdd() {
        return "SELECT name, created_date, DATEADD(month, 6, created_date) as next_review_date FROM products WHERE created_date > DATEADD(month, -6, GETDATE())";
    }
    
    public void dateAddFunctionWithDynamicSQL() throws SQLException {
        // DATEADD function with dynamic SQL construction
        String baseQuery = "SELECT * FROM orders WHERE order_date > DATEADD(day, ";
        String days = "-30";
        String endQuery = ", GETDATE())";
        
        String dynamicQuery = baseQuery + days + endQuery;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionWithTransaction() throws SQLException {
        // DATEADD function with transaction
        String transactionWithDateAdd = "BEGIN TRANSACTION; UPDATE users SET last_activity = DATEADD(day, 1, last_activity) WHERE status = 'active'; INSERT INTO activity_log (action, timestamp) VALUES ('ACTIVITY_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithDateAddRollback = "BEGIN TRANSACTION; UPDATE orders SET expected_delivery = DATEADD(day, 7, order_date) WHERE status = 'pending'; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No orders updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Orders updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithDateAdd);
        
        boolean hasResult = stmt.execute(transactionWithDateAddRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void dateAddFunctionWithBusinessLogic() throws SQLException {
        // DATEADD function with business logic
        String businessLogicWithDateAdd = "SELECT name, CASE WHEN last_login > DATEADD(day, -7, GETDATE()) THEN 'Active' WHEN last_login > DATEADD(day, -30, GETDATE()) THEN 'Inactive' ELSE 'Dormant' END as user_status, DATEADD(day, 30, last_login) as next_check_date FROM users";
        String businessLogicComplex = "SELECT product_name, order_date, DATEADD(day, 7, order_date) as expected_delivery, CASE WHEN DATEADD(day, 7, order_date) < GETDATE() THEN 'Overdue' WHEN DATEADD(day, 7, order_date) = CAST(GETDATE() AS DATE) THEN 'Due Today' ELSE 'On Time' END as delivery_status FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithDateAdd);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
}
