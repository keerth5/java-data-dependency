package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-029: TopClauseUsage
 * Detects "TOP" clause usage in SQL statements
 */
public class TopClauseUsageExample {
    
    public void basicTopClauseUsage() throws SQLException {
        // Basic TOP clause usage
        String selectTop10 = "SELECT TOP 10 * FROM users";
        String selectTop5 = "SELECT TOP 5 name, email FROM users";
        String selectTop100 = "SELECT TOP 100 * FROM orders";
        String selectTop1 = "SELECT TOP 1 * FROM products ORDER BY price DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectTop10);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectTop5);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectTop100);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectTop1);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithOrderBy() throws SQLException {
        // TOP clause with ORDER BY
        String topWithOrderBy = "SELECT TOP 10 * FROM users ORDER BY created_date DESC";
        String topWithOrderByMultiple = "SELECT TOP 5 * FROM orders ORDER BY total_amount DESC, order_date ASC";
        String topWithOrderByString = "SELECT TOP 20 * FROM products ORDER BY name ASC";
        String topWithOrderByNumeric = "SELECT TOP 15 * FROM employees ORDER BY salary DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithOrderBy);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithOrderByMultiple);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithOrderByString);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(topWithOrderByNumeric);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithWhereClause() throws SQLException {
        // TOP clause with WHERE clause
        String topWithWhere = "SELECT TOP 10 * FROM users WHERE status = 'active'";
        String topWithWhereDate = "SELECT TOP 5 * FROM orders WHERE order_date > '2024-01-01'";
        String topWithWhereNumeric = "SELECT TOP 20 * FROM products WHERE price > 100";
        String topWithWhereMultiple = "SELECT TOP 15 * FROM users WHERE age > 18 AND status = 'active'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithWhere);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithWhereDate);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithWhereNumeric);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(topWithWhereMultiple);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithJoins() throws SQLException {
        // TOP clause with JOINs
        String topWithJoin = "SELECT TOP 10 u.name, o.order_date FROM users u JOIN orders o ON u.id = o.user_id";
        String topWithLeftJoin = "SELECT TOP 5 p.name, c.category_name FROM products p LEFT JOIN categories c ON p.category_id = c.id";
        String topWithInnerJoin = "SELECT TOP 20 u.name, p.phone FROM users u INNER JOIN profiles p ON u.id = p.user_id";
        String topWithMultipleJoins = "SELECT TOP 15 u.name, o.order_date, p.product_name FROM users u JOIN orders o ON u.id = o.user_id JOIN order_items oi ON o.id = oi.order_id JOIN products p ON oi.product_id = p.id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithJoin);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithLeftJoin);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithInnerJoin);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(topWithMultipleJoins);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithSubqueries() throws SQLException {
        // TOP clause with subqueries
        String topWithSubquery = "SELECT TOP 10 * FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > 1000)";
        String topWithExists = "SELECT TOP 5 * FROM products WHERE EXISTS (SELECT 1 FROM reviews WHERE product_id = products.id)";
        String topWithNotExists = "SELECT TOP 20 * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id)";
        String topWithComplexSubquery = "SELECT TOP 15 * FROM users WHERE id IN (SELECT user_id FROM orders WHERE order_date > '2024-01-01' AND total_amount > 500)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithSubquery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithExists);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithNotExists);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(topWithComplexSubquery);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithAggregates() throws SQLException {
        // TOP clause with aggregate functions
        String topWithCount = "SELECT TOP 10 COUNT(*) as order_count, user_id FROM orders GROUP BY user_id ORDER BY order_count DESC";
        String topWithSum = "SELECT TOP 5 SUM(total_amount) as total_spent, user_id FROM orders GROUP BY user_id ORDER BY total_spent DESC";
        String topWithAvg = "SELECT TOP 20 AVG(price) as avg_price, category_id FROM products GROUP BY category_id ORDER BY avg_price DESC";
        String topWithMax = "SELECT TOP 15 MAX(order_date) as latest_order, user_id FROM orders GROUP BY user_id ORDER BY latest_order DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithCount);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithSum);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithAvg);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(topWithMax);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithCaseStatements() throws SQLException {
        // TOP clause with CASE statements
        String topWithCase = "SELECT TOP 10 name, CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END as age_group FROM users ORDER BY age";
        String topWithCaseComplex = "SELECT TOP 5 name, CASE WHEN total_orders > 100 THEN 'VIP' WHEN total_orders > 50 THEN 'Premium' WHEN total_orders > 10 THEN 'Regular' ELSE 'New' END as customer_type FROM users ORDER BY total_orders DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithCase);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithCaseComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithFunctions() throws SQLException {
        // TOP clause with functions
        String topWithStringFunctions = "SELECT TOP 10 UPPER(name) as name_upper, LOWER(email) as email_lower FROM users";
        String topWithDateFunctions = "SELECT TOP 5 name, YEAR(created_date) as creation_year, MONTH(created_date) as creation_month FROM users ORDER BY created_date DESC";
        String topWithMathFunctions = "SELECT TOP 20 name, ROUND(price, 2) as rounded_price, CEILING(price) as ceiling_price FROM products ORDER BY price DESC";
        String topWithNullFunctions = "SELECT TOP 15 name, ISNULL(phone, 'N/A') as phone, COALESCE(email, 'no-email@example.com') as email FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithStringFunctions);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithDateFunctions);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithMathFunctions);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(topWithNullFunctions);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithPreparedStatement() throws SQLException {
        // TOP clause with PreparedStatement
        String topWithParam = "SELECT TOP ? * FROM users WHERE status = ?";
        String topWithParamOrderBy = "SELECT TOP ? * FROM orders ORDER BY total_amount DESC";
        String topWithParamWhere = "SELECT TOP ? * FROM products WHERE price > ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(topWithParam);
        pstmt1.setInt(1, 10);
        pstmt1.setString(2, "active");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(topWithParamOrderBy);
        pstmt2.setInt(1, 5);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(topWithParamWhere);
        pstmt3.setInt(1, 20);
        pstmt3.setDouble(2, 100.0);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void topClauseInMethods() throws SQLException {
        // TOP clause in methods
        String userQuery = getUserQueryWithTop();
        String orderQuery = getOrderQueryWithTop();
        String productQuery = getProductQueryWithTop();
        
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
    
    private String getUserQueryWithTop() {
        return "SELECT TOP 10 name, email, created_date FROM users WHERE status = 'active' ORDER BY created_date DESC";
    }
    
    private String getOrderQueryWithTop() {
        return "SELECT TOP 5 order_id, user_id, total_amount, order_date FROM orders WHERE order_date > '2024-01-01' ORDER BY total_amount DESC";
    }
    
    private String getProductQueryWithTop() {
        return "SELECT TOP 20 name, price, category FROM products WHERE price > 100 ORDER BY price ASC";
    }
    
    public void topClauseWithDynamicSQL() throws SQLException {
        // TOP clause with dynamic SQL construction
        String baseQuery = "SELECT TOP 10 * FROM users WHERE";
        String statusCondition = " status = 'active'";
        String orderClause = " ORDER BY created_date DESC";
        
        String dynamicQuery = baseQuery + statusCondition + orderClause;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithTransaction() throws SQLException {
        // TOP clause with transaction
        String selectTopUsers = "SELECT TOP 5 * FROM users WHERE status = 'active'";
        String selectTopOrders = "SELECT TOP 10 * FROM orders WHERE order_date > '2024-01-01'";
        String insertLog = "INSERT INTO audit_log (action, timestamp) VALUES ('TOP_QUERY_EXECUTED', GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            ResultSet rs1 = stmt.executeQuery(selectTopUsers);
            rs1.close();
            
            ResultSet rs2 = stmt.executeQuery(selectTopOrders);
            rs2.close();
            
            int result = stmt.executeUpdate(insertLog);
            
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    
    public void topClauseWithComplexQueries() throws SQLException {
        // TOP clause with complex queries
        String complexQuery1 = "SELECT TOP 10 u.name, COUNT(o.id) as order_count, SUM(o.total_amount) as total_spent FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.id, u.name ORDER BY total_spent DESC";
        String complexQuery2 = "SELECT TOP 5 p.name, AVG(r.rating) as avg_rating, COUNT(r.id) as review_count FROM products p LEFT JOIN reviews r ON p.id = r.product_id GROUP BY p.id, p.name HAVING COUNT(r.id) > 5 ORDER BY avg_rating DESC";
        String complexQuery3 = "SELECT TOP 20 u.name, p.phone, o.order_date, o.total_amount FROM users u LEFT JOIN profiles p ON u.id = p.user_id LEFT JOIN orders o ON u.id = o.user_id WHERE u.status = 'active' ORDER BY o.order_date DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(complexQuery1);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(complexQuery2);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(complexQuery3);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void topClauseWithPagination() throws SQLException {
        // TOP clause with pagination (using variables)
        String topWithPagination = "SELECT TOP 10 * FROM users WHERE id > 100 ORDER BY id";
        String topWithPaginationOffset = "SELECT TOP 10 * FROM users WHERE id NOT IN (SELECT TOP 20 id FROM users ORDER BY id) ORDER BY id";
        String topWithPaginationComplex = "SELECT TOP 10 * FROM orders WHERE order_id > (SELECT MAX(order_id) - 100 FROM orders) ORDER BY order_id DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(topWithPagination);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(topWithPaginationOffset);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(topWithPaginationComplex);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
