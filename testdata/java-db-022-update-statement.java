package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Test file for sql-java-022: UpdateStatementUsage
 * Detects "UPDATE" statements in Java string literals
 */
public class UpdateStatementUsageExample {
    
    public void basicUpdateStatements() throws SQLException {
        // Basic UPDATE statements in string literals
        String updateUser = "UPDATE users SET name = 'John Updated', email = 'john.updated@example.com' WHERE id = 1";
        String updateOrder = "UPDATE orders SET status = 'shipped', shipping_date = '2024-01-15' WHERE order_id = 123";
        String updateProduct = "UPDATE products SET price = 899.99, stock_quantity = 50 WHERE product_id = 456";
        String updateCategory = "UPDATE categories SET description = 'Updated description' WHERE category_id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateUser);
        int result2 = stmt.executeUpdate(updateOrder);
        int result3 = stmt.executeUpdate(updateProduct);
        int result4 = stmt.executeUpdate(updateCategory);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithPreparedStatement() throws SQLException {
        // UPDATE statements with PreparedStatement
        String updateUser = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";
        String updateOrder = "UPDATE orders SET status = ?, updated_date = ? WHERE order_id = ?";
        String updateProduct = "UPDATE products SET name = ?, price = ?, description = ? WHERE product_id = ?";
        String updateLog = "UPDATE audit_log SET action = ?, timestamp = ? WHERE log_id = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(updateUser);
        pstmt1.setString(1, "Jane Updated");
        pstmt1.setString(2, "jane.updated@example.com");
        pstmt1.setInt(3, 26);
        pstmt1.setInt(4, 2);
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(updateOrder);
        pstmt2.setString(1, "delivered");
        pstmt2.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        pstmt2.setInt(3, 124);
        pstmt2.executeUpdate();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(updateProduct);
        pstmt3.setString(1, "Updated Tablet");
        pstmt3.setDouble(2, 279.99);
        pstmt3.setString(3, "Updated description");
        pstmt3.setInt(4, 457);
        pstmt3.executeUpdate();
        pstmt3.close();
        
        PreparedStatement pstmt4 = conn.prepareStatement(updateLog);
        pstmt4.setString(1, "UPDATE");
        pstmt4.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
        pstmt4.setInt(3, 789);
        pstmt4.executeUpdate();
        pstmt4.close();
        
        conn.close();
    }
    
    public void updateStatementsWithWhereClause() throws SQLException {
        // UPDATE statements with WHERE clauses
        String updateByStatus = "UPDATE users SET last_login = NOW() WHERE status = 'active'";
        String updateByDate = "UPDATE orders SET status = 'expired' WHERE order_date < '2023-01-01'";
        String updateByAmount = "UPDATE products SET discount = 0.1 WHERE price > 500.00";
        String updateByMultipleConditions = "UPDATE users SET status = 'inactive' WHERE age > 65 AND last_login < '2023-01-01'";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateByStatus);
        int result2 = stmt.executeUpdate(updateByDate);
        int result3 = stmt.executeUpdate(updateByAmount);
        int result4 = stmt.executeUpdate(updateByMultipleConditions);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithJoins() throws SQLException {
        // UPDATE statements with JOINs
        String updateWithJoin = "UPDATE users u SET u.status = 'premium' WHERE u.id IN (SELECT o.user_id FROM orders o WHERE o.total_amount > 1000)";
        String updateWithInnerJoin = "UPDATE products p INNER JOIN categories c ON p.category_id = c.id SET p.featured = 1 WHERE c.name = 'Electronics'";
        String updateWithSubquery = "UPDATE orders SET priority = 'high' WHERE user_id IN (SELECT id FROM users WHERE status = 'vip')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithJoin);
        int result2 = stmt.executeUpdate(updateWithInnerJoin);
        int result3 = stmt.executeUpdate(updateWithSubquery);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithSetClause() throws SQLException {
        // UPDATE statements with multiple SET clauses
        String updateMultipleFields = "UPDATE users SET name = 'Updated Name', email = 'updated@example.com', age = 30, status = 'active', last_modified = NOW() WHERE id = 1";
        String updateWithCalculations = "UPDATE products SET price = price * 1.1, stock_quantity = stock_quantity - 1 WHERE category_id = 1";
        String updateWithCase = "UPDATE orders SET status = CASE WHEN total_amount > 1000 THEN 'priority' ELSE 'normal' END WHERE status = 'pending'";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateMultipleFields);
        int result2 = stmt.executeUpdate(updateWithCalculations);
        int result3 = stmt.executeUpdate(updateWithCase);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithFunctions() throws SQLException {
        // UPDATE statements with database functions
        String updateWithStringFunctions = "UPDATE users SET name = UPPER(name), email = LOWER(email) WHERE status = 'active'";
        String updateWithDateFunctions = "UPDATE orders SET days_since_order = DATEDIFF(day, order_date, GETDATE()) WHERE status = 'pending'";
        String updateWithMathFunctions = "UPDATE products SET price = ROUND(price * 1.05, 2), discount = FLOOR(discount * 100) / 100 WHERE category_id = 2";
        String updateWithNullFunctions = "UPDATE users SET phone = COALESCE(phone, 'N/A'), address = ISNULL(address, 'Not provided') WHERE id > 100";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithStringFunctions);
        int result2 = stmt.executeUpdate(updateWithDateFunctions);
        int result3 = stmt.executeUpdate(updateWithMathFunctions);
        int result4 = stmt.executeUpdate(updateWithNullFunctions);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithSubqueries() throws SQLException {
        // UPDATE statements with subqueries
        String updateWithSubquery = "UPDATE users SET total_orders = (SELECT COUNT(*) FROM orders WHERE user_id = users.id) WHERE status = 'active'";
        String updateWithExists = "UPDATE products SET featured = 1 WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id)";
        String updateWithNotExists = "UPDATE users SET status = 'inactive' WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND order_date > '2023-01-01')";
        String updateWithInSubquery = "UPDATE orders SET priority = 'high' WHERE user_id IN (SELECT id FROM users WHERE status = 'vip')";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithSubquery);
        int result2 = stmt.executeUpdate(updateWithExists);
        int result3 = stmt.executeUpdate(updateWithNotExists);
        int result4 = stmt.executeUpdate(updateWithInSubquery);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithCase() throws SQLException {
        // UPDATE statements with CASE expressions
        String updateWithCase = "UPDATE users SET age_group = CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END";
        String updateWithCaseMultiple = "UPDATE orders SET status = CASE WHEN total_amount > 1000 THEN 'priority' WHEN total_amount > 500 THEN 'standard' ELSE 'basic' END, priority = CASE WHEN DATEDIFF(day, order_date, GETDATE()) > 7 THEN 'urgent' ELSE 'normal' END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithCase);
        int result2 = stmt.executeUpdate(updateWithCaseMultiple);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithAggregates() throws SQLException {
        // UPDATE statements with aggregate functions
        String updateWithCount = "UPDATE categories SET product_count = (SELECT COUNT(*) FROM products WHERE category_id = categories.id)";
        String updateWithSum = "UPDATE users SET total_spent = (SELECT SUM(total_amount) FROM orders WHERE user_id = users.id)";
        String updateWithAvg = "UPDATE products SET avg_rating = (SELECT AVG(rating) FROM reviews WHERE product_id = products.id)";
        String updateWithMax = "UPDATE users SET last_order_date = (SELECT MAX(order_date) FROM orders WHERE user_id = users.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithCount);
        int result2 = stmt.executeUpdate(updateWithSum);
        int result3 = stmt.executeUpdate(updateWithAvg);
        int result4 = stmt.executeUpdate(updateWithMax);
        
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsInMethods() throws SQLException {
        // UPDATE statements in methods
        String userUpdate = getUserUpdateQuery();
        String orderUpdate = getOrderUpdateQuery();
        String productUpdate = getProductUpdateQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(userUpdate);
        int result2 = stmt.executeUpdate(orderUpdate);
        int result3 = stmt.executeUpdate(productUpdate);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserUpdateQuery() {
        return "UPDATE users SET last_login = NOW(), login_count = login_count + 1 WHERE id = ?";
    }
    
    private String getOrderUpdateQuery() {
        return "UPDATE orders SET status = 'processing', updated_by = 'system', updated_date = CURRENT_TIMESTAMP WHERE order_id = ?";
    }
    
    private String getProductUpdateQuery() {
        return "UPDATE products SET stock_quantity = stock_quantity - ?, last_sold = NOW() WHERE product_id = ? AND stock_quantity >= ?";
    }
    
    public void updateStatementsWithDynamicSQL() throws SQLException {
        // UPDATE statements with dynamic SQL construction
        String baseUpdate = "UPDATE users SET";
        String nameUpdate = " name = 'Dynamic Name'";
        String emailUpdate = ", email = 'dynamic@example.com'";
        String statusUpdate = ", status = 'updated'";
        String whereClause = " WHERE id = 1";
        
        String dynamicUpdate = baseUpdate + nameUpdate + emailUpdate + statusUpdate + whereClause;
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        int result = stmt.executeUpdate(dynamicUpdate);
        stmt.close();
        conn.close();
    }
    
    public void updateStatementsWithBatch() throws SQLException {
        // UPDATE statements with batch operations
        String updateQuery = "UPDATE batch_products SET price = ?, stock_quantity = ? WHERE product_id = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(updateQuery);
        
        // Add multiple updates to batch
        pstmt.setDouble(1, 1099.99);
        pstmt.setInt(2, 25);
        pstmt.setInt(3, 1);
        pstmt.addBatch();
        
        pstmt.setDouble(1, 39.99);
        pstmt.setInt(2, 50);
        pstmt.setInt(3, 2);
        pstmt.addBatch();
        
        pstmt.setDouble(1, 89.99);
        pstmt.setInt(2, 15);
        pstmt.setInt(3, 3);
        pstmt.addBatch();
        
        // Execute batch
        int[] results = pstmt.executeBatch();
        
        pstmt.close();
        conn.close();
    }
    
    public void updateStatementsWithTransaction() throws SQLException {
        // UPDATE statements with transaction
        String updateUser = "UPDATE users SET status = 'inactive' WHERE id = 1";
        String updateOrders = "UPDATE orders SET status = 'cancelled' WHERE user_id = 1";
        String updateLog = "UPDATE transaction_log SET action = 'User Deactivated', timestamp = NOW() WHERE user_id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            int result1 = stmt.executeUpdate(updateUser);
            int result2 = stmt.executeUpdate(updateOrders);
            int result3 = stmt.executeUpdate(updateLog);
            
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    
    public void updateStatementsWithConditionalLogic() throws SQLException {
        // UPDATE statements with conditional logic
        String updateWithIf = "UPDATE products SET price = CASE WHEN category_id = 1 THEN price * 1.1 ELSE price * 1.05 END";
        String updateWithComplexCondition = "UPDATE users SET status = CASE WHEN last_login < DATE_SUB(NOW(), INTERVAL 1 YEAR) THEN 'inactive' WHEN total_orders = 0 THEN 'new' ELSE 'active' END";
        String updateWithNullCheck = "UPDATE orders SET notes = CASE WHEN notes IS NULL THEN 'No notes' ELSE CONCAT(notes, ' - Updated') END";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithIf);
        int result2 = stmt.executeUpdate(updateWithComplexCondition);
        int result3 = stmt.executeUpdate(updateWithNullCheck);
        
        stmt.close();
        conn.close();
    }
}
