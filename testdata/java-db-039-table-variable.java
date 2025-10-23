package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-039: TableVariableUsage
 * Detects "@" table variable usage in SQL statements
 */
public class TableVariableUsageExample {
    
    public void basicTableVariableUsage() throws SQLException {
        // Basic table variable usage
        String declareTableVariable = "DECLARE @temp_users TABLE (id INT, name NVARCHAR(50), email NVARCHAR(100))";
        String insertIntoTableVariable = "INSERT INTO @temp_users (id, name, email) VALUES (1, 'John', 'john@example.com')";
        String selectFromTableVariable = "SELECT * FROM @temp_users WHERE id = 1";
        String updateTableVariable = "UPDATE @temp_users SET name = 'John Updated' WHERE id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(declareTableVariable);
        stmt.execute(insertIntoTableVariable);
        
        ResultSet rs = stmt.executeQuery(selectFromTableVariable);
        rs.close();
        
        stmt.execute(updateTableVariable);
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithSelectInto() throws SQLException {
        // Table variable with SELECT INTO
        String selectIntoTableVariable = "DECLARE @temp_active_users TABLE (id INT, name NVARCHAR(50), email NVARCHAR(100)); INSERT INTO @temp_active_users SELECT id, name, email FROM users WHERE status = 'active'; SELECT * FROM @temp_active_users";
        String selectIntoWithJoin = "DECLARE @temp_user_orders TABLE (user_id INT, user_name NVARCHAR(50), order_date DATETIME); INSERT INTO @temp_user_orders SELECT u.id, u.name, o.order_date FROM users u JOIN orders o ON u.id = o.user_id; SELECT * FROM @temp_user_orders";
        String selectIntoWithAggregate = "DECLARE @temp_category_stats TABLE (category_id INT, product_count INT, avg_price DECIMAL(10,2)); INSERT INTO @temp_category_stats SELECT category_id, COUNT(*), AVG(price) FROM products GROUP BY category_id; SELECT * FROM @temp_category_stats";
        String selectIntoWithSubquery = "DECLARE @temp_high_value_orders TABLE (order_id INT, total_amount DECIMAL(10,2)); INSERT INTO @temp_high_value_orders SELECT id, total_amount FROM orders WHERE total_amount > (SELECT AVG(total_amount) FROM orders); SELECT * FROM @temp_high_value_orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(selectIntoTableVariable);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(selectIntoWithJoin);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(selectIntoWithAggregate);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        boolean hasResult4 = stmt.execute(selectIntoWithSubquery);
        if (hasResult4) {
            ResultSet rs4 = stmt.getResultSet();
            rs4.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithJoins() throws SQLException {
        // Table variable with JOINs
        String tableVariableWithJoin = "DECLARE @temp_products TABLE (id INT, name NVARCHAR(100), category_id INT); INSERT INTO @temp_products SELECT id, name, category_id FROM products WHERE price > 100; SELECT p.name, c.category_name FROM @temp_products p JOIN categories c ON p.category_id = c.id";
        String tableVariableWithLeftJoin = "DECLARE @temp_users TABLE (id INT, name NVARCHAR(50)); INSERT INTO @temp_users SELECT id, name FROM users WHERE status = 'active'; SELECT u.name, p.phone FROM @temp_users u LEFT JOIN profiles p ON u.id = p.user_id";
        String tableVariableWithMultipleJoins = "DECLARE @temp_orders TABLE (id INT, user_id INT, product_id INT); INSERT INTO @temp_orders SELECT id, user_id, product_id FROM orders WHERE order_date > '2024-01-01'; SELECT o.id, u.name, p.product_name FROM @temp_orders o JOIN users u ON o.user_id = u.id JOIN products p ON o.product_id = p.id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tableVariableWithJoin);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tableVariableWithLeftJoin);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tableVariableWithMultipleJoins);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithSubqueries() throws SQLException {
        // Table variable with subqueries
        String tableVariableWithSubquery = "DECLARE @temp_user_stats TABLE (user_id INT, order_count INT, total_spent DECIMAL(10,2)); INSERT INTO @temp_user_stats SELECT user_id, COUNT(*), SUM(total_amount) FROM orders GROUP BY user_id; SELECT * FROM @temp_user_stats WHERE order_count > 5";
        String tableVariableWithExists = "DECLARE @temp_products_with_orders TABLE (product_id INT, product_name NVARCHAR(100)); INSERT INTO @temp_products_with_orders SELECT p.id, p.name FROM products p WHERE EXISTS (SELECT 1 FROM orders o WHERE o.product_id = p.id); SELECT * FROM @temp_products_with_orders";
        String tableVariableWithNotExists = "DECLARE @temp_inactive_users TABLE (user_id INT, user_name NVARCHAR(50)); INSERT INTO @temp_inactive_users SELECT u.id, u.name FROM users u WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id); SELECT * FROM @temp_inactive_users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tableVariableWithSubquery);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tableVariableWithExists);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tableVariableWithNotExists);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithAggregates() throws SQLException {
        // Table variable with aggregate functions
        String tableVariableWithAggregate = "DECLARE @temp_category_summary TABLE (category_id INT, product_count INT, total_value DECIMAL(10,2)); INSERT INTO @temp_category_summary SELECT category_id, COUNT(*), SUM(price) FROM products GROUP BY category_id; SELECT * FROM @temp_category_summary ORDER BY total_value DESC";
        String tableVariableWithMultipleAggregates = "DECLARE @temp_user_analytics TABLE (user_id INT, order_count INT, avg_order_value DECIMAL(10,2), max_order_value DECIMAL(10,2)); INSERT INTO @temp_user_analytics SELECT user_id, COUNT(*), AVG(total_amount), MAX(total_amount) FROM orders GROUP BY user_id; SELECT * FROM @temp_user_analytics WHERE order_count > 3";
        String tableVariableWithHaving = "DECLARE @temp_high_value_categories TABLE (category_id INT, total_products INT, avg_price DECIMAL(10,2)); INSERT INTO @temp_high_value_categories SELECT category_id, COUNT(*), AVG(price) FROM products GROUP BY category_id HAVING AVG(price) > 100; SELECT * FROM @temp_high_value_categories";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tableVariableWithAggregate);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tableVariableWithMultipleAggregates);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tableVariableWithHaving);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithFunctions() throws SQLException {
        // Table variable with functions
        String tableVariableWithStringFunctions = "DECLARE @temp_user_names TABLE (id INT, name_upper NVARCHAR(50), name_lower NVARCHAR(50)); INSERT INTO @temp_user_names SELECT id, UPPER(name), LOWER(name) FROM users; SELECT * FROM @temp_user_names";
        String tableVariableWithDateFunctions = "DECLARE @temp_order_dates TABLE (order_id INT, order_year INT, order_month INT, order_day INT); INSERT INTO @temp_order_dates SELECT id, YEAR(order_date), MONTH(order_date), DAY(order_date) FROM orders; SELECT * FROM @temp_order_dates WHERE order_year = 2024";
        String tableVariableWithMathFunctions = "DECLARE @temp_product_prices TABLE (product_id INT, original_price DECIMAL(10,2), rounded_price DECIMAL(10,2), ceiling_price DECIMAL(10,2)); INSERT INTO @temp_product_prices SELECT id, price, ROUND(price, 2), CEILING(price) FROM products; SELECT * FROM @temp_product_prices";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tableVariableWithStringFunctions);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tableVariableWithDateFunctions);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tableVariableWithMathFunctions);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithCaseStatements() throws SQLException {
        // Table variable with CASE statements
        String tableVariableWithCase = "DECLARE @temp_user_categories TABLE (user_id INT, age_group NVARCHAR(20), status_category NVARCHAR(20)); INSERT INTO @temp_user_categories SELECT id, CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END, CASE WHEN status = 'active' THEN 'Active' ELSE 'Inactive' END FROM users; SELECT * FROM @temp_user_categories";
        String tableVariableWithComplexCase = "DECLARE @temp_product_tiers TABLE (product_id INT, price_tier NVARCHAR(20), stock_status NVARCHAR(20)); INSERT INTO @temp_product_tiers SELECT id, CASE WHEN price < 50 THEN 'Budget' WHEN price < 200 THEN 'Mid-range' WHEN price < 500 THEN 'Premium' ELSE 'Luxury' END, CASE WHEN stock_quantity > 10 THEN 'In Stock' WHEN stock_quantity > 0 THEN 'Low Stock' ELSE 'Out of Stock' END FROM products; SELECT * FROM @temp_product_tiers";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tableVariableWithCase);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tableVariableWithComplexCase);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithPreparedStatement() throws SQLException {
        // Table variable with PreparedStatement
        String declareTableVariableParam = "DECLARE @temp_users TABLE (id INT, name NVARCHAR(50), email NVARCHAR(100))";
        String insertIntoTableVariableParam = "INSERT INTO @temp_users (id, name, email) VALUES (?, ?, ?)";
        String selectFromTableVariableParam = "SELECT * FROM @temp_users WHERE id = ?";
        String updateTableVariableParam = "UPDATE @temp_users SET name = ? WHERE id = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        stmt.execute(declareTableVariableParam);
        stmt.close();
        
        PreparedStatement pstmt1 = conn.prepareStatement(insertIntoTableVariableParam);
        pstmt1.setInt(1, 1);
        pstmt1.setString(2, "John");
        pstmt1.setString(3, "john@example.com");
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectFromTableVariableParam);
        pstmt2.setInt(1, 1);
        ResultSet rs = pstmt2.executeQuery();
        rs.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(updateTableVariableParam);
        pstmt3.setString(1, "John Updated");
        pstmt3.setInt(2, 1);
        pstmt3.executeUpdate();
        pstmt3.close();
        
        conn.close();
    }
    
    public void tableVariableInMethods() throws SQLException {
        // Table variable in methods
        String userTableVariable = getUserTableVariableQuery();
        String orderTableVariable = getOrderTableVariableQuery();
        String productTableVariable = getProductTableVariableQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(userTableVariable);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(orderTableVariable);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(productTableVariable);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    private String getUserTableVariableQuery() {
        return "DECLARE @temp_active_users TABLE (id INT, name NVARCHAR(50), email NVARCHAR(100)); INSERT INTO @temp_active_users SELECT id, name, email FROM users WHERE status = 'active'; SELECT * FROM @temp_active_users";
    }
    
    private String getOrderTableVariableQuery() {
        return "DECLARE @temp_recent_orders TABLE (id INT, user_id INT, total_amount DECIMAL(10,2)); INSERT INTO @temp_recent_orders SELECT id, user_id, total_amount FROM orders WHERE order_date > '2024-01-01'; SELECT * FROM @temp_recent_orders";
    }
    
    private String getProductTableVariableQuery() {
        return "DECLARE @temp_expensive_products TABLE (id INT, name NVARCHAR(100), price DECIMAL(10,2)); INSERT INTO @temp_expensive_products SELECT id, name, price FROM products WHERE price > 100; SELECT * FROM @temp_expensive_products";
    }
    
    public void tableVariableWithDynamicSQL() throws SQLException {
        // Table variable with dynamic SQL construction
        String baseDeclare = "DECLARE @temp_";
        String tableName = "users";
        String columns = " TABLE (id INT, name NVARCHAR(50))";
        String insert = "; INSERT INTO @temp_" + tableName + " SELECT id, name FROM users";
        String select = "; SELECT * FROM @temp_" + tableName;
        
        String dynamicTableVariable = baseDeclare + tableName + columns + insert + select;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        boolean hasResult = stmt.execute(dynamicTableVariable);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithTransaction() throws SQLException {
        // Table variable with transaction
        String transactionWithTableVariable = "BEGIN TRANSACTION; DECLARE @temp_orders TABLE (id INT, user_id INT, total_amount DECIMAL(10,2)); INSERT INTO @temp_orders SELECT id, user_id, total_amount FROM orders WHERE order_date > '2024-01-01'; SELECT COUNT(*) as order_count FROM @temp_orders; COMMIT TRANSACTION";
        String transactionWithTableVariableRollback = "BEGIN TRANSACTION; DECLARE @temp_products TABLE (id INT, name NVARCHAR(100)); INSERT INTO @temp_products SELECT id, name FROM products WHERE price > 1000; IF (SELECT COUNT(*) FROM @temp_products) = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No expensive products found' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Expensive products found' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(transactionWithTableVariable);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(transactionWithTableVariableRollback);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithPerformanceOptimization() throws SQLException {
        // Table variable with performance optimization
        String performanceTableVariable = "DECLARE @temp_user_orders TABLE (user_id INT, order_count INT, total_spent DECIMAL(10,2), PRIMARY KEY (user_id)); INSERT INTO @temp_user_orders SELECT user_id, COUNT(*), SUM(total_amount) FROM orders GROUP BY user_id; SELECT * FROM @temp_user_orders WHERE order_count > 5 ORDER BY total_spent DESC";
        String performanceTableVariableComplex = "DECLARE @temp_product_analysis TABLE (product_id INT, product_name NVARCHAR(100), order_count INT, total_revenue DECIMAL(10,2), PRIMARY KEY (product_id)); INSERT INTO @temp_product_analysis SELECT p.id, p.name, COUNT(o.id), SUM(o.total_amount) FROM products p LEFT JOIN orders o ON p.id = o.product_id GROUP BY p.id, p.name; SELECT * FROM @temp_product_analysis WHERE order_count > 0 ORDER BY total_revenue DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(performanceTableVariable);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(performanceTableVariableComplex);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tableVariableWithStoredProcedures() throws SQLException {
        // Table variable with stored procedures
        String tableVariableWithProcedure = "DECLARE @temp_results TABLE (id INT, name NVARCHAR(50)); INSERT INTO @temp_results EXEC sp_get_active_users; SELECT * FROM @temp_results";
        String tableVariableWithProcedureOutput = "DECLARE @temp_user_data TABLE (user_id INT, user_name NVARCHAR(50), order_count INT); EXEC sp_get_user_statistics @temp_user_data OUTPUT; SELECT * FROM @temp_user_data";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tableVariableWithProcedure);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tableVariableWithProcedureOutput);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
