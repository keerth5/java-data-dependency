package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Test file for sql-java-021: InsertStatementUsage
 * Detects "INSERT" statements in Java string literals
 */
public class InsertStatementUsageExample {
    
    public void basicInsertStatements() throws SQLException {
        // Basic INSERT statements in string literals
        String insertUser = "INSERT INTO users (name, email, age) VALUES ('John Doe', 'john@example.com', 30)";
        String insertOrder = "INSERT INTO orders (user_id, product_name, quantity, price) VALUES (1, 'Laptop', 1, 999.99)";
        String insertProduct = "INSERT INTO products (name, description, price, category) VALUES ('Smartphone', 'Latest model', 699.99, 'Electronics')";
        String insertCategory = "INSERT INTO categories (name, description) VALUES ('Electronics', 'Electronic devices and gadgets')";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertUser);
        int result2 = stmt.executeUpdate(insertOrder);
        int result3 = stmt.executeUpdate(insertProduct);
        int result4 = stmt.executeUpdate(insertCategory);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithPreparedStatement() throws SQLException {
        // INSERT statements with PreparedStatement
        String insertUser = "INSERT INTO users (name, email, age, status) VALUES (?, ?, ?, ?)";
        String insertOrder = "INSERT INTO orders (user_id, product_id, quantity, order_date) VALUES (?, ?, ?, ?)";
        String insertProduct = "INSERT INTO products (name, description, price, category_id) VALUES (?, ?, ?, ?)";
        String insertLog = "INSERT INTO audit_log (action, table_name, record_id, timestamp) VALUES (?, ?, ?, ?)";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(insertUser);
        pstmt1.setString(1, "Jane Smith");
        pstmt1.setString(2, "jane@example.com");
        pstmt1.setInt(3, 25);
        pstmt1.setString(4, "active");
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(insertOrder);
        pstmt2.setInt(1, 1);
        pstmt2.setInt(2, 100);
        pstmt2.setInt(3, 2);
        pstmt2.setDate(4, new java.sql.Date(System.currentTimeMillis()));
        pstmt2.executeUpdate();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(insertProduct);
        pstmt3.setString(1, "Tablet");
        pstmt3.setString(2, "10-inch tablet");
        pstmt3.setDouble(3, 299.99);
        pstmt3.setInt(4, 1);
        pstmt3.executeUpdate();
        pstmt3.close();
        
        PreparedStatement pstmt4 = conn.prepareStatement(insertLog);
        pstmt4.setString(1, "INSERT");
        pstmt4.setString(2, "users");
        pstmt4.setInt(3, 123);
        pstmt4.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
        pstmt4.executeUpdate();
        pstmt4.close();
        
        conn.close();
    }
    
    public void insertStatementsWithSelect() throws SQLException {
        // INSERT statements with SELECT (INSERT INTO ... SELECT)
        String insertFromSelect = "INSERT INTO user_backup (id, name, email, created_date) SELECT id, name, email, created_date FROM users WHERE status = 'active'";
        String insertAggregated = "INSERT INTO daily_stats (date, total_orders, total_revenue) SELECT DATE(order_date), COUNT(*), SUM(total_amount) FROM orders GROUP BY DATE(order_date)";
        String insertWithJoin = "INSERT INTO user_orders_summary (user_id, user_name, order_count, total_spent) SELECT u.id, u.name, COUNT(o.id), SUM(o.total_amount) FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.id, u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertFromSelect);
        int result2 = stmt.executeUpdate(insertAggregated);
        int result3 = stmt.executeUpdate(insertWithJoin);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithValues() throws SQLException {
        // INSERT statements with multiple VALUES
        String insertMultipleUsers = "INSERT INTO users (name, email, age) VALUES ('Alice', 'alice@example.com', 28), ('Bob', 'bob@example.com', 32), ('Charlie', 'charlie@example.com', 24)";
        String insertMultipleOrders = "INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Book', 2), (2, 'Pen', 5), (3, 'Notebook', 1)";
        String insertMultipleProducts = "INSERT INTO products (name, price, category) VALUES ('Laptop', 999.99, 'Electronics'), ('Mouse', 29.99, 'Electronics'), ('Keyboard', 79.99, 'Electronics')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertMultipleUsers);
        int result2 = stmt.executeUpdate(insertMultipleOrders);
        int result3 = stmt.executeUpdate(insertMultipleProducts);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithDefaultValues() throws SQLException {
        // INSERT statements with DEFAULT VALUES
        String insertWithDefaults = "INSERT INTO users (name, email) VALUES ('David', 'david@example.com')";
        String insertWithDefaultKeyword = "INSERT INTO products (name, price) VALUES ('Monitor', DEFAULT)";
        String insertWithNull = "INSERT INTO orders (user_id, product_name, notes) VALUES (1, 'Headphones', NULL)";
        String insertWithCurrentTimestamp = "INSERT INTO audit_log (action, table_name, timestamp) VALUES ('INSERT', 'users', CURRENT_TIMESTAMP)";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithDefaults);
        int result2 = stmt.executeUpdate(insertWithDefaultKeyword);
        int result3 = stmt.executeUpdate(insertWithNull);
        int result4 = stmt.executeUpdate(insertWithCurrentTimestamp);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithSubqueries() throws SQLException {
        // INSERT statements with subqueries
        String insertWithSubquery = "INSERT INTO user_stats (user_id, total_orders) SELECT id, (SELECT COUNT(*) FROM orders WHERE user_id = users.id) FROM users";
        String insertWithExists = "INSERT INTO active_users_backup (id, name, email) SELECT id, name, email FROM users WHERE EXISTS (SELECT 1 FROM orders WHERE user_id = users.id)";
        String insertWithNotExists = "INSERT INTO inactive_users (id, name, email) SELECT id, name, email FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithSubquery);
        int result2 = stmt.executeUpdate(insertWithExists);
        int result3 = stmt.executeUpdate(insertWithNotExists);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithCase() throws SQLException {
        // INSERT statements with CASE expressions
        String insertWithCase = "INSERT INTO user_categories (user_id, category) SELECT id, CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END FROM users";
        String insertWithCaseAggregate = "INSERT INTO order_summary (date, order_count, high_value_count) SELECT DATE(order_date), COUNT(*), SUM(CASE WHEN total_amount > 1000 THEN 1 ELSE 0 END) FROM orders GROUP BY DATE(order_date)";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithCase);
        int result2 = stmt.executeUpdate(insertWithCaseAggregate);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithFunctions() throws SQLException {
        // INSERT statements with database functions
        String insertWithFunctions = "INSERT INTO products (name, price, created_date, hash) VALUES ('Gaming Console', 499.99, GETDATE(), HASHBYTES('MD5', 'Gaming Console'))";
        String insertWithStringFunctions = "INSERT INTO users (name, email, name_upper, email_domain) VALUES ('Eve', 'eve@example.com', UPPER('Eve'), SUBSTRING('eve@example.com', CHARINDEX('@', 'eve@example.com') + 1, LEN('eve@example.com')))";
        String insertWithDateFunctions = "INSERT INTO events (name, event_date, year, month, day) VALUES ('Conference', '2024-06-15', YEAR('2024-06-15'), MONTH('2024-06-15'), DAY('2024-06-15'))";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithFunctions);
        int result2 = stmt.executeUpdate(insertWithStringFunctions);
        int result3 = stmt.executeUpdate(insertWithDateFunctions);
        
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsInMethods() throws SQLException {
        // INSERT statements in methods
        String userInsert = getUserInsertQuery();
        String orderInsert = getOrderInsertQuery();
        String productInsert = getProductInsertQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(userInsert);
        int result2 = stmt.executeUpdate(orderInsert);
        int result3 = stmt.executeUpdate(productInsert);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserInsertQuery() {
        return "INSERT INTO users (name, email, age, status, created_date) VALUES ('Frank', 'frank@example.com', 35, 'active', NOW())";
    }
    
    private String getOrderInsertQuery() {
        return "INSERT INTO orders (user_id, product_name, quantity, price, order_date) VALUES (1, 'Smartphone', 1, 699.99, CURRENT_DATE)";
    }
    
    private String getProductInsertQuery() {
        return "INSERT INTO products (name, description, price, category, in_stock) VALUES ('Wireless Earbuds', 'Noise cancelling earbuds', 199.99, 'Electronics', 1)";
    }
    
    public void insertStatementsWithDynamicSQL() throws SQLException {
        // INSERT statements with dynamic SQL construction
        String baseInsert = "INSERT INTO users (name, email";
        String values = " VALUES ('Grace', 'grace@example.com'";
        
        String ageColumn = ", age";
        String ageValue = ", 29";
        
        String statusColumn = ", status";
        String statusValue = ", 'active'";
        
        String dynamicInsert = baseInsert + ageColumn + statusColumn + ") " + values + ageValue + statusValue + ")";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        int result = stmt.executeUpdate(dynamicInsert);
        stmt.close();
        conn.close();
    }
    
    public void insertStatementsWithBatch() throws SQLException {
        // INSERT statements with batch operations
        String insertQuery = "INSERT INTO batch_orders (user_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(insertQuery);
        
        // Add multiple inserts to batch
        pstmt.setInt(1, 1);
        pstmt.setString(2, "Laptop");
        pstmt.setInt(3, 1);
        pstmt.setDouble(4, 999.99);
        pstmt.addBatch();
        
        pstmt.setInt(1, 2);
        pstmt.setString(2, "Mouse");
        pstmt.setInt(3, 2);
        pstmt.setDouble(4, 29.99);
        pstmt.addBatch();
        
        pstmt.setInt(1, 3);
        pstmt.setString(2, "Keyboard");
        pstmt.setInt(3, 1);
        pstmt.setDouble(4, 79.99);
        pstmt.addBatch();
        
        // Execute batch
        int[] results = pstmt.executeBatch();
        
        pstmt.close();
        conn.close();
    }
    
    public void insertStatementsWithTransaction() throws SQLException {
        // INSERT statements with transaction
        String insertUser = "INSERT INTO users (name, email, age) VALUES ('Henry', 'henry@example.com', 31)";
        String insertOrder = "INSERT INTO orders (user_id, product_name, quantity) VALUES (LAST_INSERT_ID(), 'Tablet', 1)";
        String insertLog = "INSERT INTO transaction_log (action, timestamp) VALUES ('User and Order Created', NOW())";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            int result1 = stmt.executeUpdate(insertUser);
            int result2 = stmt.executeUpdate(insertOrder);
            int result3 = stmt.executeUpdate(insertLog);
            
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
