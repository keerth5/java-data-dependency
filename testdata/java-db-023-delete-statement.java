package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Test file for sql-java-023: DeleteStatementUsage
 * Detects "DELETE" statements in Java string literals
 */
public class DeleteStatementUsageExample {
    
    public void basicDeleteStatements() throws SQLException {
        // Basic DELETE statements in string literals
        String deleteUser = "DELETE FROM users WHERE id = 1";
        String deleteOrder = "DELETE FROM orders WHERE order_id = 123";
        String deleteProduct = "DELETE FROM products WHERE product_id = 456";
        String deleteCategory = "DELETE FROM categories WHERE category_id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteUser);
        int result2 = stmt.executeUpdate(deleteOrder);
        int result3 = stmt.executeUpdate(deleteProduct);
        int result4 = stmt.executeUpdate(deleteCategory);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithPreparedStatement() throws SQLException {
        // DELETE statements with PreparedStatement
        String deleteUser = "DELETE FROM users WHERE id = ?";
        String deleteOrder = "DELETE FROM orders WHERE order_id = ? AND status = ?";
        String deleteProduct = "DELETE FROM products WHERE product_id = ? AND category_id = ?";
        String deleteLog = "DELETE FROM audit_log WHERE log_id = ? AND action = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(deleteUser);
        pstmt1.setInt(1, 2);
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(deleteOrder);
        pstmt2.setInt(1, 124);
        pstmt2.setString(2, "cancelled");
        pstmt2.executeUpdate();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(deleteProduct);
        pstmt3.setInt(1, 457);
        pstmt3.setInt(2, 1);
        pstmt3.executeUpdate();
        pstmt3.close();
        
        PreparedStatement pstmt4 = conn.prepareStatement(deleteLog);
        pstmt4.setInt(1, 789);
        pstmt4.setString(2, "DELETE");
        pstmt4.executeUpdate();
        pstmt4.close();
        
        conn.close();
    }
    
    public void deleteStatementsWithWhereClause() throws SQLException {
        // DELETE statements with WHERE clauses
        String deleteByStatus = "DELETE FROM users WHERE status = 'inactive'";
        String deleteByDate = "DELETE FROM orders WHERE order_date < '2023-01-01'";
        String deleteByAmount = "DELETE FROM products WHERE price < 10.00";
        String deleteByMultipleConditions = "DELETE FROM users WHERE age > 65 AND last_login < '2023-01-01'";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteByStatus);
        int result2 = stmt.executeUpdate(deleteByDate);
        int result3 = stmt.executeUpdate(deleteByAmount);
        int result4 = stmt.executeUpdate(deleteByMultipleConditions);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithJoins() throws SQLException {
        // DELETE statements with JOINs
        String deleteWithJoin = "DELETE FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > 1000)";
        String deleteWithInnerJoin = "DELETE p FROM products p INNER JOIN categories c ON p.category_id = c.id WHERE c.name = 'Discontinued'";
        String deleteWithSubquery = "DELETE FROM orders WHERE user_id IN (SELECT id FROM users WHERE status = 'deleted')";
        String deleteWithExists = "DELETE FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithJoin);
        int result2 = stmt.executeUpdate(deleteWithInnerJoin);
        int result3 = stmt.executeUpdate(deleteWithSubquery);
        int result4 = stmt.executeUpdate(deleteWithExists);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithSubqueries() throws SQLException {
        // DELETE statements with subqueries
        String deleteWithSubquery = "DELETE FROM users WHERE id IN (SELECT user_id FROM orders WHERE order_date < '2023-01-01')";
        String deleteWithExists = "DELETE FROM products WHERE NOT EXISTS (SELECT 1 FROM orders WHERE product_id = products.id)";
        String deleteWithNotExists = "DELETE FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id)";
        String deleteWithComplexSubquery = "DELETE FROM orders WHERE user_id IN (SELECT id FROM users WHERE status = 'inactive' AND last_login < DATE_SUB(NOW(), INTERVAL 1 YEAR))";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithSubquery);
        int result2 = stmt.executeUpdate(deleteWithExists);
        int result3 = stmt.executeUpdate(deleteWithNotExists);
        int result4 = stmt.executeUpdate(deleteWithComplexSubquery);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithFunctions() throws SQLException {
        // DELETE statements with database functions
        String deleteWithDateFunction = "DELETE FROM orders WHERE DATEDIFF(day, order_date, GETDATE()) > 365";
        String deleteWithStringFunction = "DELETE FROM users WHERE LOWER(email) LIKE '%@olddomain.com'";
        String deleteWithMathFunction = "DELETE FROM products WHERE ROUND(price, 2) = 0.00";
        String deleteWithNullFunction = "DELETE FROM users WHERE ISNULL(phone, '') = '' AND ISNULL(address, '') = ''";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithDateFunction);
        int result2 = stmt.executeUpdate(deleteWithStringFunction);
        int result3 = stmt.executeUpdate(deleteWithMathFunction);
        int result4 = stmt.executeUpdate(deleteWithNullFunction);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithCase() throws SQLException {
        // DELETE statements with CASE expressions
        String deleteWithCase = "DELETE FROM users WHERE CASE WHEN age < 18 THEN 'minor' WHEN age > 65 THEN 'senior' ELSE 'adult' END = 'minor'";
        String deleteWithComplexCase = "DELETE FROM orders WHERE CASE WHEN total_amount > 1000 THEN 'high' WHEN total_amount > 500 THEN 'medium' ELSE 'low' END = 'low' AND status = 'pending'";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithCase);
        int result2 = stmt.executeUpdate(deleteWithComplexCase);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithAggregates() throws SQLException {
        // DELETE statements with aggregate functions
        String deleteWithCount = "DELETE FROM categories WHERE (SELECT COUNT(*) FROM products WHERE category_id = categories.id) = 0";
        String deleteWithSum = "DELETE FROM users WHERE (SELECT SUM(total_amount) FROM orders WHERE user_id = users.id) < 100";
        String deleteWithAvg = "DELETE FROM products WHERE (SELECT AVG(rating) FROM reviews WHERE product_id = products.id) < 2.0";
        String deleteWithMax = "DELETE FROM orders WHERE order_date < (SELECT MAX(order_date) FROM orders) - INTERVAL 1 YEAR";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithCount);
        int result2 = stmt.executeUpdate(deleteWithSum);
        int result3 = stmt.executeUpdate(deleteWithAvg);
        int result4 = stmt.executeUpdate(deleteWithMax);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithLimit() throws SQLException {
        // DELETE statements with LIMIT clauses
        String deleteWithLimit = "DELETE FROM users WHERE status = 'inactive' LIMIT 100";
        String deleteWithTop = "DELETE TOP 50 FROM orders WHERE status = 'cancelled'";
        String deleteWithLimitOrder = "DELETE FROM products WHERE stock_quantity = 0 ORDER BY created_date ASC LIMIT 25";
        String deleteWithTopOrder = "DELETE TOP 10 FROM audit_log ORDER BY timestamp ASC";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithLimit);
        int result2 = stmt.executeUpdate(deleteWithTop);
        int result3 = stmt.executeUpdate(deleteWithLimitOrder);
        int result4 = stmt.executeUpdate(deleteWithTopOrder);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsInMethods() throws SQLException {
        // DELETE statements in methods
        String userDelete = getUserDeleteQuery();
        String orderDelete = getOrderDeleteQuery();
        String productDelete = getProductDeleteQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(userDelete);
        int result2 = stmt.executeUpdate(orderDelete);
        int result3 = stmt.executeUpdate(productDelete);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserDeleteQuery() {
        return "DELETE FROM users WHERE id = ? AND status = 'inactive'";
    }
    
    private String getOrderDeleteQuery() {
        return "DELETE FROM orders WHERE order_id = ? AND status IN ('cancelled', 'expired')";
    }
    
    private String getProductDeleteQuery() {
        return "DELETE FROM products WHERE product_id = ? AND stock_quantity = 0 AND discontinued = 1";
    }
    
    public void deleteStatementsWithDynamicSQL() throws SQLException {
        // DELETE statements with dynamic SQL construction
        String baseDelete = "DELETE FROM users WHERE";
        String statusCondition = " status = 'inactive'";
        String dateCondition = " AND last_login < '2023-01-01'";
        String ageCondition = " AND age > 65";
        
        String dynamicDelete = baseDelete + statusCondition + dateCondition + ageCondition;
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        int result = stmt.executeUpdate(dynamicDelete);
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithBatch() throws SQLException {
        // DELETE statements with batch operations
        String deleteQuery = "DELETE FROM batch_orders WHERE order_id = ? AND status = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
        
        // Add multiple deletes to batch
        pstmt.setInt(1, 1);
        pstmt.setString(2, "cancelled");
        pstmt.addBatch();
        
        pstmt.setInt(1, 2);
        pstmt.setString(2, "expired");
        pstmt.addBatch();
        
        pstmt.setInt(1, 3);
        pstmt.setString(2, "failed");
        pstmt.addBatch();
        
        // Execute batch
        int[] results = pstmt.executeBatch();
        
        pstmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithTransaction() throws SQLException {
        // DELETE statements with transaction
        String deleteUser = "DELETE FROM users WHERE id = 1";
        String deleteOrders = "DELETE FROM orders WHERE user_id = 1";
        String deleteLog = "DELETE FROM transaction_log WHERE user_id = 1";
        String insertAudit = "INSERT INTO audit_log (action, table_name, record_id, timestamp) VALUES ('DELETE', 'users', 1, NOW())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            int result1 = stmt.executeUpdate(deleteUser);
            int result2 = stmt.executeUpdate(deleteOrders);
            int result3 = stmt.executeUpdate(deleteLog);
            int result4 = stmt.executeUpdate(insertAudit);
            
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    
    public void deleteStatementsWithConditionalLogic() throws SQLException {
        // DELETE statements with conditional logic
        String deleteWithIf = "DELETE FROM products WHERE CASE WHEN category_id = 1 THEN price < 100 ELSE price < 50 END";
        String deleteWithComplexCondition = "DELETE FROM users WHERE (status = 'inactive' AND last_login < DATE_SUB(NOW(), INTERVAL 1 YEAR)) OR (total_orders = 0 AND created_date < DATE_SUB(NOW(), INTERVAL 6 MONTH))";
        String deleteWithNullCheck = "DELETE FROM orders WHERE notes IS NULL AND status = 'pending' AND order_date < DATE_SUB(NOW(), INTERVAL 30 DAY)";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteWithIf);
        int result2 = stmt.executeUpdate(deleteWithComplexCondition);
        int result3 = stmt.executeUpdate(deleteWithNullCheck);
        
        stmt.close();
        conn.close();
    }
    
    public void deleteStatementsWithCascade() throws SQLException {
        // DELETE statements with cascade operations
        String deleteWithCascade = "DELETE FROM users WHERE id = 1"; // Assuming CASCADE is set up in database
        String deleteRelatedRecords = "DELETE FROM user_preferences WHERE user_id = 1";
        String deleteUserSessions = "DELETE FROM user_sessions WHERE user_id = 1";
        String deleteUserLogs = "DELETE FROM user_activity_log WHERE user_id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            // Delete related records first
            int result1 = stmt.executeUpdate(deleteRelatedRecords);
            int result2 = stmt.executeUpdate(deleteUserSessions);
            int result3 = stmt.executeUpdate(deleteUserLogs);
            int result4 = stmt.executeUpdate(deleteWithCascade);
            
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
