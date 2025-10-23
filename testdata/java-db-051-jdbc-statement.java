package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-051: JdbcStatementUsage
 * Detects java.sql.Statement interface usage
 */
public class JdbcStatementUsageExample {
    
    public void basicStatementUsage() throws SQLException {
        // Basic Statement usage
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Simple SELECT query
        String selectQuery = "SELECT * FROM users WHERE status = 'active'";
        ResultSet rs = stmt.executeQuery(selectQuery);
        rs.close();
        
        // Simple INSERT query
        String insertQuery = "INSERT INTO users (name, email) VALUES ('John Doe', 'john@example.com')";
        int insertResult = stmt.executeUpdate(insertQuery);
        
        // Simple UPDATE query
        String updateQuery = "UPDATE users SET status = 'inactive' WHERE id = 1";
        int updateResult = stmt.executeUpdate(updateQuery);
        
        // Simple DELETE query
        String deleteQuery = "DELETE FROM users WHERE status = 'inactive'";
        int deleteResult = stmt.executeUpdate(deleteQuery);
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithDynamicSQL() throws SQLException {
        // Statement with dynamic SQL construction
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String tableName = "users";
        String condition = "status = 'active'";
        String dynamicQuery = "SELECT * FROM " + tableName + " WHERE " + condition;
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        
        String columnName = "name";
        String value = "John Doe";
        String dynamicInsert = "INSERT INTO " + tableName + " (" + columnName + ") VALUES ('" + value + "')";
        int result = stmt.executeUpdate(dynamicInsert);
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithUserInput() throws SQLException {
        // Statement with user input (potential SQL injection)
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String username = "admin"; // This could come from user input
        String password = "password123"; // This could come from user input
        
        // Vulnerable to SQL injection
        String loginQuery = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        ResultSet rs = stmt.executeQuery(loginQuery);
        rs.close();
        
        String searchTerm = "test"; // This could come from user input
        String searchQuery = "SELECT * FROM products WHERE name LIKE '%" + searchTerm + "%'";
        ResultSet searchRs = stmt.executeQuery(searchQuery);
        searchRs.close();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithBatchOperations() throws SQLException {
        // Statement with batch operations
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Add multiple statements to batch
        stmt.addBatch("INSERT INTO users (name, email) VALUES ('User1', 'user1@example.com')");
        stmt.addBatch("INSERT INTO users (name, email) VALUES ('User2', 'user2@example.com')");
        stmt.addBatch("INSERT INTO users (name, email) VALUES ('User3', 'user3@example.com')");
        
        // Execute batch
        int[] results = stmt.executeBatch();
        
        // Clear batch
        stmt.clearBatch();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithMultipleQueries() throws SQLException {
        // Statement with multiple queries
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Multiple SELECT queries
        String query1 = "SELECT COUNT(*) FROM users";
        String query2 = "SELECT COUNT(*) FROM products";
        String query3 = "SELECT COUNT(*) FROM orders";
        
        ResultSet rs1 = stmt.executeQuery(query1);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(query2);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(query3);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithComplexQueries() throws SQLException {
        // Statement with complex queries
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Complex JOIN query
        String joinQuery = "SELECT u.name, o.order_number, p.product_name FROM users u " +
                          "JOIN orders o ON u.id = o.user_id " +
                          "JOIN order_items oi ON o.id = oi.order_id " +
                          "JOIN products p ON oi.product_id = p.id";
        ResultSet rs = stmt.executeQuery(joinQuery);
        rs.close();
        
        // Complex aggregate query
        String aggregateQuery = "SELECT u.name, COUNT(o.id) as order_count, SUM(o.total_amount) as total_spent " +
                               "FROM users u LEFT JOIN orders o ON u.id = o.user_id " +
                               "GROUP BY u.id, u.name " +
                               "HAVING COUNT(o.id) > 5";
        ResultSet aggregateRs = stmt.executeQuery(aggregateQuery);
        aggregateRs.close();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithSubqueries() throws SQLException {
        // Statement with subqueries
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Subquery in WHERE clause
        String subqueryWhere = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > 1000)";
        ResultSet rs1 = stmt.executeQuery(subqueryWhere);
        rs1.close();
        
        // Subquery in SELECT clause
        String subquerySelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id) as order_count FROM users";
        ResultSet rs2 = stmt.executeQuery(subquerySelect);
        rs2.close();
        
        // Subquery with EXISTS
        String subqueryExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM order_items WHERE product_id = products.id)";
        ResultSet rs3 = stmt.executeQuery(subqueryExists);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithStoredProcedures() throws SQLException {
        // Statement with stored procedures
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Execute stored procedure
        String spQuery = "EXEC GetActiveUsers";
        ResultSet rs = stmt.executeQuery(spQuery);
        rs.close();
        
        // Execute stored procedure with parameters
        String spWithParams = "EXEC GetUserOrders @UserId = 1";
        ResultSet rs2 = stmt.executeQuery(spWithParams);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithDDLOperations() throws SQLException {
        // Statement with DDL operations
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // CREATE TABLE
        String createTable = "CREATE TABLE temp_users (id INT PRIMARY KEY, name VARCHAR(100))";
        stmt.executeUpdate(createTable);
        
        // ALTER TABLE
        String alterTable = "ALTER TABLE temp_users ADD email VARCHAR(255)";
        stmt.executeUpdate(alterTable);
        
        // DROP TABLE
        String dropTable = "DROP TABLE temp_users";
        stmt.executeUpdate(dropTable);
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithTransaction() throws SQLException {
        // Statement with transaction
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        try {
            // Begin transaction
            conn.setAutoCommit(false);
            
            // Multiple operations in transaction
            String insert1 = "INSERT INTO users (name, email) VALUES ('User1', 'user1@example.com')";
            stmt.executeUpdate(insert1);
            
            String insert2 = "INSERT INTO users (name, email) VALUES ('User2', 'user2@example.com')";
            stmt.executeUpdate(insert2);
            
            // Commit transaction
            conn.commit();
            
        } catch (SQLException e) {
            // Rollback transaction
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            stmt.close();
            conn.close();
        }
    }
    
    public void statementWithErrorHandling() throws SQLException {
        // Statement with error handling
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        try {
            String query = "SELECT * FROM non_existent_table";
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
        } catch (SQLException e) {
            // Handle SQL exception
            System.err.println("SQL Error: " + e.getMessage());
        }
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithResultSetNavigation() throws SQLException {
        // Statement with ResultSet navigation
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users ORDER BY name";
        ResultSet rs = stmt.executeQuery(query);
        
        // Navigate through ResultSet
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            System.out.println("User: " + name + ", Email: " + email);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void statementWithResultSetMetadata() throws SQLException {
        // Statement with ResultSet metadata
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        // Get ResultSet metadata
        java.sql.ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnType = metaData.getColumnTypeName(i);
            System.out.println("Column: " + columnName + ", Type: " + columnType);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void statementWithConnectionProperties() throws SQLException {
        // Statement with connection properties
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Set statement properties
        stmt.setQueryTimeout(30); // 30 seconds timeout
        stmt.setMaxRows(1000); // Maximum 1000 rows
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        rs.close();
        
        stmt.close();
        conn.close();
    }
    
    public void statementWithLargeResultSet() throws SQLException {
        // Statement with large ResultSet
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        // Set fetch size for large ResultSet
        stmt.setFetchSize(100);
        
        String query = "SELECT * FROM large_table";
        ResultSet rs = stmt.executeQuery(query);
        
        int count = 0;
        while (rs.next() && count < 10000) {
            // Process row
            count++;
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void statementWithMultipleResultSets() throws SQLException {
        // Statement with multiple ResultSets
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String multiQuery = "SELECT * FROM users; SELECT * FROM products; SELECT * FROM orders";
        boolean hasResult = stmt.execute(multiQuery);
        
        int resultSetCount = 0;
        do {
            if (hasResult) {
                ResultSet rs = stmt.getResultSet();
                resultSetCount++;
                rs.close();
            }
            hasResult = stmt.getMoreResults();
        } while (hasResult);
        
        stmt.close();
        conn.close();
    }
}
