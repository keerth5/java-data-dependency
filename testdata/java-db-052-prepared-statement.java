package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.math.BigDecimal;

/**
 * Test file for sql-java-052: PreparedStatementUsage
 * Detects java.sql.PreparedStatement interface usage
 */
public class PreparedStatementUsageExample {
    
    public void basicPreparedStatementUsage() throws SQLException {
        // Basic PreparedStatement usage
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // SELECT with parameters
        String selectQuery = "SELECT * FROM users WHERE id = ? AND status = ?";
        PreparedStatement pstmt = conn.prepareStatement(selectQuery);
        pstmt.setInt(1, 1);
        pstmt.setString(2, "active");
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        
        // INSERT with parameters
        String insertQuery = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        PreparedStatement insertPstmt = conn.prepareStatement(insertQuery);
        insertPstmt.setString(1, "John Doe");
        insertPstmt.setString(2, "john@example.com");
        insertPstmt.setInt(3, 30);
        int insertResult = insertPstmt.executeUpdate();
        insertPstmt.close();
        
        // UPDATE with parameters
        String updateQuery = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        PreparedStatement updatePstmt = conn.prepareStatement(updateQuery);
        updatePstmt.setString(1, "Jane Doe");
        updatePstmt.setString(2, "jane@example.com");
        updatePstmt.setInt(3, 1);
        int updateResult = updatePstmt.executeUpdate();
        updatePstmt.close();
        
        // DELETE with parameters
        String deleteQuery = "DELETE FROM users WHERE id = ?";
        PreparedStatement deletePstmt = conn.prepareStatement(deleteQuery);
        deletePstmt.setInt(1, 1);
        int deleteResult = deletePstmt.executeUpdate();
        deletePstmt.close();
        
        conn.close();
    }
    
    public void preparedStatementWithDifferentDataTypes() throws SQLException {
        // PreparedStatement with different data types
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "INSERT INTO products (name, price, quantity, created_date, is_active) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        // String parameters
        pstmt.setString(1, "Laptop");
        
        // Numeric parameters
        pstmt.setBigDecimal(2, new BigDecimal("999.99"));
        pstmt.setInt(3, 10);
        
        // Date parameters
        pstmt.setDate(4, new Date(System.currentTimeMillis()));
        
        // Boolean parameters
        pstmt.setBoolean(5, true);
        
        int result = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithNullValues() throws SQLException {
        // PreparedStatement with null values
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "INSERT INTO users (name, email, phone, address) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setString(1, "John Doe");
        pstmt.setString(2, "john@example.com");
        pstmt.setNull(3, java.sql.Types.VARCHAR); // phone is null
        pstmt.setString(4, "123 Main St");
        
        int result = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithBatchOperations() throws SQLException {
        // PreparedStatement with batch operations
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        // Add multiple sets of parameters to batch
        pstmt.setString(1, "User1");
        pstmt.setString(2, "user1@example.com");
        pstmt.addBatch();
        
        pstmt.setString(1, "User2");
        pstmt.setString(2, "user2@example.com");
        pstmt.addBatch();
        
        pstmt.setString(1, "User3");
        pstmt.setString(2, "user3@example.com");
        pstmt.addBatch();
        
        // Execute batch
        int[] results = pstmt.executeBatch();
        
        // Clear batch
        pstmt.clearBatch();
        
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithLikeOperator() throws SQLException {
        // PreparedStatement with LIKE operator
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE name LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        // Search for names starting with 'J'
        pstmt.setString(1, "J%");
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        
        // Search for names containing 'doe'
        pstmt.setString(1, "%doe%");
        ResultSet rs2 = pstmt.executeQuery();
        rs2.close();
        
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithInClause() throws SQLException {
        // PreparedStatement with IN clause
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE id IN (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setInt(1, 1);
        pstmt.setInt(2, 2);
        pstmt.setInt(3, 3);
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithBetweenClause() throws SQLException {
        // PreparedStatement with BETWEEN clause
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setDate(1, new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000)); // 30 days ago
        pstmt.setDate(2, new Date(System.currentTimeMillis())); // today
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithJoins() throws SQLException {
        // PreparedStatement with JOINs
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT u.name, o.order_number, o.total_amount FROM users u " +
                      "JOIN orders o ON u.id = o.user_id WHERE u.id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setInt(1, 1);
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithSubqueries() throws SQLException {
        // PreparedStatement with subqueries
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setBigDecimal(1, new BigDecimal("1000.00"));
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithAggregates() throws SQLException {
        // PreparedStatement with aggregate functions
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT COUNT(*) as user_count FROM users WHERE created_date > ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setDate(1, new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000)); // 1 year ago
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithOrderBy() throws SQLException {
        // PreparedStatement with ORDER BY
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE status = ? ORDER BY name";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setString(1, "active");
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithGroupBy() throws SQLException {
        // PreparedStatement with GROUP BY
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT status, COUNT(*) as count FROM users WHERE created_date > ? GROUP BY status";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setDate(1, new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000)); // 30 days ago
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithHaving() throws SQLException {
        // PreparedStatement with HAVING clause
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT user_id, COUNT(*) as order_count FROM orders WHERE order_date > ? GROUP BY user_id HAVING COUNT(*) > ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setDate(1, new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000)); // 30 days ago
        pstmt.setInt(2, 5); // more than 5 orders
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithCaseStatement() throws SQLException {
        // PreparedStatement with CASE statement
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT name, CASE WHEN age < ? THEN 'Young' WHEN age < ? THEN 'Middle' ELSE 'Senior' END as age_group FROM users";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setInt(1, 25);
        pstmt.setInt(2, 50);
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithStoredProcedures() throws SQLException {
        // PreparedStatement with stored procedures
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "{call GetUserOrders(?)}";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setInt(1, 1);
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithOutputParameters() throws SQLException {
        // PreparedStatement with output parameters
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "{call CreateUser(?, ?, ?)}";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        pstmt.setString(1, "John Doe");
        pstmt.setString(2, "john@example.com");
        pstmt.registerOutParameter(3, java.sql.Types.INTEGER); // output parameter for new user ID
        
        pstmt.execute();
        int newUserId = pstmt.getInt(3);
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithTransaction() throws SQLException {
        // PreparedStatement with transaction
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            
            String insertUser = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement userPstmt = conn.prepareStatement(insertUser);
            userPstmt.setString(1, "John Doe");
            userPstmt.setString(2, "john@example.com");
            userPstmt.executeUpdate();
            userPstmt.close();
            
            String insertOrder = "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)";
            PreparedStatement orderPstmt = conn.prepareStatement(insertOrder);
            orderPstmt.setInt(1, 1);
            orderPstmt.setBigDecimal(2, new BigDecimal("100.00"));
            orderPstmt.executeUpdate();
            orderPstmt.close();
            
            conn.commit();
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
    
    public void preparedStatementWithErrorHandling() throws SQLException {
        // PreparedStatement with error handling
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        try {
            pstmt.setInt(1, 1);
            ResultSet rs = pstmt.executeQuery();
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } finally {
            pstmt.close();
            conn.close();
        }
    }
    
    public void preparedStatementWithResultSetNavigation() throws SQLException {
        // PreparedStatement with ResultSet navigation
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE status = ? ORDER BY name";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, "active");
        
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            System.out.println("User: " + name + ", Email: " + email);
        }
        
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithConnectionProperties() throws SQLException {
        // PreparedStatement with connection properties
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        // Set query timeout
        pstmt.setQueryTimeout(30);
        
        pstmt.setInt(1, 1);
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void preparedStatementWithLargeResultSet() throws SQLException {
        // PreparedStatement with large ResultSet
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM large_table WHERE category = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, "electronics");
        
        // Set fetch size for large ResultSet
        pstmt.setFetchSize(100);
        
        ResultSet rs = pstmt.executeQuery();
        
        int count = 0;
        while (rs.next() && count < 10000) {
            // Process row
            count++;
        }
        
        rs.close();
        pstmt.close();
        conn.close();
    }
}
