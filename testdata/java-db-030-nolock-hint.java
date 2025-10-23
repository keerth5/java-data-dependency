package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-030: NolockHintUsage
 * Detects "WITH (NOLOCK)" or "NOLOCK" hints in SQL
 */
public class NolockHintUsageExample {
    
    public void basicNolockHintUsage() throws SQLException {
        // Basic NOLOCK hint usage
        String selectWithNolock = "SELECT * FROM users WITH (NOLOCK)";
        String selectWithNolockWhere = "SELECT * FROM orders WITH (NOLOCK) WHERE status = 'pending'";
        String selectWithNolockOrderBy = "SELECT * FROM products WITH (NOLOCK) ORDER BY price DESC";
        String selectWithNolockTop = "SELECT TOP 10 * FROM users WITH (NOLOCK) ORDER BY created_date DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithNolock);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithNolockWhere);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithNolockOrderBy);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithNolockTop);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void nolockHintWithJoins() throws SQLException {
        // NOLOCK hint with JOINs
        String joinWithNolock = "SELECT u.name, o.order_date FROM users u WITH (NOLOCK) JOIN orders o WITH (NOLOCK) ON u.id = o.user_id";
        String leftJoinWithNolock = "SELECT p.name, c.category_name FROM products p WITH (NOLOCK) LEFT JOIN categories c WITH (NOLOCK) ON p.category_id = c.id";
        String innerJoinWithNolock = "SELECT u.name, p.phone FROM users u WITH (NOLOCK) INNER JOIN profiles p WITH (NOLOCK) ON u.id = p.user_id";
        String multipleJoinsWithNolock = "SELECT u.name, o.order_date, p.product_name FROM users u WITH (NOLOCK) JOIN orders o WITH (NOLOCK) ON u.id = o.user_id JOIN order_items oi WITH (NOLOCK) ON o.id = oi.order_id JOIN products p WITH (NOLOCK) ON oi.product_id = p.id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithNolock);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(leftJoinWithNolock);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(innerJoinWithNolock);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(multipleJoinsWithNolock);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void nolockHintWithSubqueries() throws SQLException {
        // NOLOCK hint with subqueries
        String subqueryWithNolock = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WITH (NOLOCK) WHERE total_amount > 1000)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WITH (NOLOCK) WHERE user_id = users.id) as order_count FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM reviews WITH (NOLOCK) WHERE product_id = products.id)";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WITH (NOLOCK) WHERE user_id = users.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithNolock);
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
    
    public void nolockHintWithAggregates() throws SQLException {
        // NOLOCK hint with aggregate functions
        String aggregateWithNolock = "SELECT COUNT(*) as total_users, COUNT(phone) as users_with_phone FROM users WITH (NOLOCK)";
        String sumWithNolock = "SELECT SUM(total_amount) as total_revenue FROM orders WITH (NOLOCK)";
        String avgWithNolock = "SELECT AVG(price) as average_price FROM products WITH (NOLOCK)";
        String maxWithNolock = "SELECT MAX(created_date) as latest_creation FROM users WITH (NOLOCK)";
        String groupByWithNolock = "SELECT status, COUNT(*) as count FROM users WITH (NOLOCK) GROUP BY status";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithNolock);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithNolock);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithNolock);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithNolock);
        rs4.close();
        
        ResultSet rs5 = stmt.executeQuery(groupByWithNolock);
        rs5.close();
        
        stmt.close();
        conn.close();
    }
    
    public void nolockHintWithFunctions() throws SQLException {
        // NOLOCK hint with functions
        String stringFunctionsWithNolock = "SELECT UPPER(name) as name_upper, LOWER(email) as email_lower FROM users WITH (NOLOCK)";
        String dateFunctionsWithNolock = "SELECT name, YEAR(created_date) as creation_year, MONTH(created_date) as creation_month FROM users WITH (NOLOCK)";
        String mathFunctionsWithNolock = "SELECT name, ROUND(price, 2) as rounded_price, CEILING(price) as ceiling_price FROM products WITH (NOLOCK)";
        String nullFunctionsWithNolock = "SELECT name, ISNULL(phone, 'N/A') as phone, COALESCE(email, 'no-email@example.com') as email FROM users WITH (NOLOCK)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringFunctionsWithNolock);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateFunctionsWithNolock);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(mathFunctionsWithNolock);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(nullFunctionsWithNolock);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void nolockHintWithCaseStatements() throws SQLException {
        // NOLOCK hint with CASE statements
        String caseWithNolock = "SELECT name, CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END as age_group FROM users WITH (NOLOCK)";
        String caseComplexWithNolock = "SELECT name, CASE WHEN total_orders > 100 THEN 'VIP' WHEN total_orders > 50 THEN 'Premium' WHEN total_orders > 10 THEN 'Regular' ELSE 'New' END as customer_type FROM users WITH (NOLOCK)";
        String caseWithNolockDate = "SELECT name, CASE WHEN last_login > GETDATE() - 7 THEN 'Active' WHEN last_login > GETDATE() - 30 THEN 'Inactive' ELSE 'Dormant' END as status FROM users WITH (NOLOCK)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithNolock);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseComplexWithNolock);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithNolockDate);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void nolockHintWithPreparedStatement() throws SQLException {
        // NOLOCK hint with PreparedStatement
        String selectWithNolockParam = "SELECT * FROM users WITH (NOLOCK) WHERE status = ?";
        String selectWithNolockParamOrderBy = "SELECT * FROM orders WITH (NOLOCK) ORDER BY total_amount DESC";
        String selectWithNolockParamWhere = "SELECT * FROM products WITH (NOLOCK) WHERE price > ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithNolockParam);
        pstmt1.setString(1, "active");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithNolockParamOrderBy);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithNolockParamWhere);
        pstmt3.setDouble(1, 100.0);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void nolockHintInMethods() throws SQLException {
        // NOLOCK hint in methods
        String userQuery = getUserQueryWithNolock();
        String orderQuery = getOrderQueryWithNolock();
        String productQuery = getProductQueryWithNolock();
        
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
    
    private String getUserQueryWithNolock() {
        return "SELECT name, email, created_date FROM users WITH (NOLOCK) WHERE status = 'active' ORDER BY created_date DESC";
    }
    
    private String getOrderQueryWithNolock() {
        return "SELECT order_id, user_id, total_amount, order_date FROM orders WITH (NOLOCK) WHERE order_date > '2024-01-01' ORDER BY total_amount DESC";
    }
    
    private String getProductQueryWithNolock() {
        return "SELECT name, price, category FROM products WITH (NOLOCK) WHERE price > 100 ORDER BY price ASC";
    }
    
    public void nolockHintWithDynamicSQL() throws SQLException {
        // NOLOCK hint with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WITH (NOLOCK) WHERE";
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
    
    public void nolockHintWithTransaction() throws SQLException {
        // NOLOCK hint with transaction
        String selectUsersWithNolock = "SELECT * FROM users WITH (NOLOCK) WHERE status = 'active'";
        String selectOrdersWithNolock = "SELECT * FROM orders WITH (NOLOCK) WHERE order_date > '2024-01-01'";
        String insertLog = "INSERT INTO audit_log (action, timestamp) VALUES ('NOLOCK_QUERY_EXECUTED', GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            ResultSet rs1 = stmt.executeQuery(selectUsersWithNolock);
            rs1.close();
            
            ResultSet rs2 = stmt.executeQuery(selectOrdersWithNolock);
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
    
    public void nolockHintWithComplexQueries() throws SQLException {
        // NOLOCK hint with complex queries
        String complexQuery1 = "SELECT u.name, COUNT(o.id) as order_count, SUM(o.total_amount) as total_spent FROM users u WITH (NOLOCK) LEFT JOIN orders o WITH (NOLOCK) ON u.id = o.user_id GROUP BY u.id, u.name ORDER BY total_spent DESC";
        String complexQuery2 = "SELECT p.name, AVG(r.rating) as avg_rating, COUNT(r.id) as review_count FROM products p WITH (NOLOCK) LEFT JOIN reviews r WITH (NOLOCK) ON p.id = r.product_id GROUP BY p.id, p.name HAVING COUNT(r.id) > 5 ORDER BY avg_rating DESC";
        String complexQuery3 = "SELECT u.name, p.phone, o.order_date, o.total_amount FROM users u WITH (NOLOCK) LEFT JOIN profiles p WITH (NOLOCK) ON u.id = p.user_id LEFT JOIN orders o WITH (NOLOCK) ON u.id = o.user_id WHERE u.status = 'active' ORDER BY o.order_date DESC";
        
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
    
    public void nolockHintWithPerformanceOptimization() throws SQLException {
        // NOLOCK hint for performance optimization scenarios
        String reportingQuery = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN status = 'active' THEN 1 END) as active_users FROM users WITH (NOLOCK)";
        String analyticsQuery = "SELECT DATE(order_date) as order_date, COUNT(*) as order_count, SUM(total_amount) as daily_revenue FROM orders WITH (NOLOCK) GROUP BY DATE(order_date) ORDER BY order_date DESC";
        String dashboardQuery = "SELECT TOP 10 u.name, COUNT(o.id) as order_count, SUM(o.total_amount) as total_spent FROM users u WITH (NOLOCK) LEFT JOIN orders o WITH (NOLOCK) ON u.id = o.user_id GROUP BY u.id, u.name ORDER BY total_spent DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(reportingQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(analyticsQuery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(dashboardQuery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void nolockHintWithReadOnlyOperations() throws SQLException {
        // NOLOCK hint for read-only operations
        String readOnlyUsers = "SELECT name, email, created_date FROM users WITH (NOLOCK) WHERE status = 'active'";
        String readOnlyProducts = "SELECT name, price, description FROM products WITH (NOLOCK) WHERE category = 'Electronics'";
        String readOnlyOrders = "SELECT order_id, user_id, total_amount FROM orders WITH (NOLOCK) WHERE order_date > '2024-01-01'";
        String readOnlyStats = "SELECT COUNT(*) as total_count FROM users WITH (NOLOCK)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(readOnlyUsers);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(readOnlyProducts);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(readOnlyOrders);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(readOnlyStats);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
}
