package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-038: TempTableUsage
 * Detects "#" temporary table usage in SQL statements
 */
public class TempTableUsageExample {
    
    public void basicTempTableUsage() throws SQLException {
        // Basic temporary table usage
        String createTempTable = "CREATE TABLE #temp_users (id INT, name NVARCHAR(50), email NVARCHAR(100))";
        String insertIntoTempTable = "INSERT INTO #temp_users (id, name, email) VALUES (1, 'John', 'john@example.com')";
        String selectFromTempTable = "SELECT * FROM #temp_users WHERE id = 1";
        String dropTempTable = "DROP TABLE #temp_users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(createTempTable);
        stmt.execute(insertIntoTempTable);
        
        ResultSet rs = stmt.executeQuery(selectFromTempTable);
        rs.close();
        
        stmt.execute(dropTempTable);
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithSelectInto() throws SQLException {
        // Temporary table with SELECT INTO
        String selectInto = "SELECT id, name, email INTO #temp_active_users FROM users WHERE status = 'active'";
        String selectIntoWithJoin = "SELECT u.id, u.name, o.order_date INTO #temp_user_orders FROM users u JOIN orders o ON u.id = o.user_id";
        String selectIntoWithAggregate = "SELECT category_id, COUNT(*) as product_count, AVG(price) as avg_price INTO #temp_category_stats FROM products GROUP BY category_id";
        String selectIntoWithSubquery = "SELECT * INTO #temp_high_value_orders FROM orders WHERE total_amount > (SELECT AVG(total_amount) FROM orders)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(selectInto);
        stmt.execute(selectIntoWithJoin);
        stmt.execute(selectIntoWithAggregate);
        stmt.execute(selectIntoWithSubquery);
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithJoins() throws SQLException {
        // Temporary table with JOINs
        String tempTableWithJoin = "CREATE TABLE #temp_products (id INT, name NVARCHAR(100), category_id INT); INSERT INTO #temp_products SELECT id, name, category_id FROM products WHERE price > 100; SELECT p.name, c.category_name FROM #temp_products p JOIN categories c ON p.category_id = c.id";
        String tempTableWithLeftJoin = "CREATE TABLE #temp_users (id INT, name NVARCHAR(50)); INSERT INTO #temp_users SELECT id, name FROM users WHERE status = 'active'; SELECT u.name, p.phone FROM #temp_users u LEFT JOIN profiles p ON u.id = p.user_id";
        String tempTableWithMultipleJoins = "CREATE TABLE #temp_orders (id INT, user_id INT, product_id INT); INSERT INTO #temp_orders SELECT id, user_id, product_id FROM orders WHERE order_date > '2024-01-01'; SELECT o.id, u.name, p.product_name FROM #temp_orders o JOIN users u ON o.user_id = u.id JOIN products p ON o.product_id = p.id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tempTableWithJoin);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tempTableWithLeftJoin);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tempTableWithMultipleJoins);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithSubqueries() throws SQLException {
        // Temporary table with subqueries
        String tempTableWithSubquery = "CREATE TABLE #temp_user_stats (user_id INT, order_count INT, total_spent DECIMAL(10,2)); INSERT INTO #temp_user_stats SELECT user_id, COUNT(*), SUM(total_amount) FROM orders GROUP BY user_id; SELECT * FROM #temp_user_stats WHERE order_count > 5";
        String tempTableWithExists = "CREATE TABLE #temp_products_with_orders (product_id INT, product_name NVARCHAR(100)); INSERT INTO #temp_products_with_orders SELECT p.id, p.name FROM products p WHERE EXISTS (SELECT 1 FROM orders o WHERE o.product_id = p.id); SELECT * FROM #temp_products_with_orders";
        String tempTableWithNotExists = "CREATE TABLE #temp_inactive_users (user_id INT, user_name NVARCHAR(50)); INSERT INTO #temp_inactive_users SELECT u.id, u.name FROM users u WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id); SELECT * FROM #temp_inactive_users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tempTableWithSubquery);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tempTableWithExists);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tempTableWithNotExists);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithAggregates() throws SQLException {
        // Temporary table with aggregate functions
        String tempTableWithAggregate = "CREATE TABLE #temp_category_summary (category_id INT, product_count INT, total_value DECIMAL(10,2)); INSERT INTO #temp_category_summary SELECT category_id, COUNT(*), SUM(price) FROM products GROUP BY category_id; SELECT * FROM #temp_category_summary ORDER BY total_value DESC";
        String tempTableWithMultipleAggregates = "CREATE TABLE #temp_user_analytics (user_id INT, order_count INT, avg_order_value DECIMAL(10,2), max_order_value DECIMAL(10,2)); INSERT INTO #temp_user_analytics SELECT user_id, COUNT(*), AVG(total_amount), MAX(total_amount) FROM orders GROUP BY user_id; SELECT * FROM #temp_user_analytics WHERE order_count > 3";
        String tempTableWithHaving = "CREATE TABLE #temp_high_value_categories (category_id INT, total_products INT, avg_price DECIMAL(10,2)); INSERT INTO #temp_high_value_categories SELECT category_id, COUNT(*), AVG(price) FROM products GROUP BY category_id HAVING AVG(price) > 100; SELECT * FROM #temp_high_value_categories";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tempTableWithAggregate);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tempTableWithMultipleAggregates);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tempTableWithHaving);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithFunctions() throws SQLException {
        // Temporary table with functions
        String tempTableWithStringFunctions = "CREATE TABLE #temp_user_names (id INT, name_upper NVARCHAR(50), name_lower NVARCHAR(50)); INSERT INTO #temp_user_names SELECT id, UPPER(name), LOWER(name) FROM users; SELECT * FROM #temp_user_names";
        String tempTableWithDateFunctions = "CREATE TABLE #temp_order_dates (order_id INT, order_year INT, order_month INT, order_day INT); INSERT INTO #temp_order_dates SELECT id, YEAR(order_date), MONTH(order_date), DAY(order_date) FROM orders; SELECT * FROM #temp_order_dates WHERE order_year = 2024";
        String tempTableWithMathFunctions = "CREATE TABLE #temp_product_prices (product_id INT, original_price DECIMAL(10,2), rounded_price DECIMAL(10,2), ceiling_price DECIMAL(10,2)); INSERT INTO #temp_product_prices SELECT id, price, ROUND(price, 2), CEILING(price) FROM products; SELECT * FROM #temp_product_prices";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tempTableWithStringFunctions);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tempTableWithDateFunctions);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(tempTableWithMathFunctions);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithCaseStatements() throws SQLException {
        // Temporary table with CASE statements
        String tempTableWithCase = "CREATE TABLE #temp_user_categories (user_id INT, age_group NVARCHAR(20), status_category NVARCHAR(20)); INSERT INTO #temp_user_categories SELECT id, CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END, CASE WHEN status = 'active' THEN 'Active' ELSE 'Inactive' END FROM users; SELECT * FROM #temp_user_categories";
        String tempTableWithComplexCase = "CREATE TABLE #temp_product_tiers (product_id INT, price_tier NVARCHAR(20), stock_status NVARCHAR(20)); INSERT INTO #temp_product_tiers SELECT id, CASE WHEN price < 50 THEN 'Budget' WHEN price < 200 THEN 'Mid-range' WHEN price < 500 THEN 'Premium' ELSE 'Luxury' END, CASE WHEN stock_quantity > 10 THEN 'In Stock' WHEN stock_quantity > 0 THEN 'Low Stock' ELSE 'Out of Stock' END FROM products; SELECT * FROM #temp_product_tiers";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(tempTableWithCase);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(tempTableWithComplexCase);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithPreparedStatement() throws SQLException {
        // Temporary table with PreparedStatement
        String createTempTableParam = "CREATE TABLE #temp_users (id INT, name NVARCHAR(50), email NVARCHAR(100))";
        String insertIntoTempTableParam = "INSERT INTO #temp_users (id, name, email) VALUES (?, ?, ?)";
        String selectFromTempTableParam = "SELECT * FROM #temp_users WHERE id = ?";
        String dropTempTableParam = "DROP TABLE #temp_users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        stmt.execute(createTempTableParam);
        stmt.close();
        
        PreparedStatement pstmt1 = conn.prepareStatement(insertIntoTempTableParam);
        pstmt1.setInt(1, 1);
        pstmt1.setString(2, "John");
        pstmt1.setString(3, "john@example.com");
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectFromTempTableParam);
        pstmt2.setInt(1, 1);
        ResultSet rs = pstmt2.executeQuery();
        rs.close();
        pstmt2.close();
        
        Statement stmt2 = conn.createStatement();
        stmt2.execute(dropTempTableParam);
        stmt2.close();
        
        conn.close();
    }
    
    public void tempTableInMethods() throws SQLException {
        // Temporary table in methods
        String userTempTable = getUserTempTableQuery();
        String orderTempTable = getOrderTempTableQuery();
        String productTempTable = getProductTempTableQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(userTempTable);
        stmt.execute(orderTempTable);
        stmt.execute(productTempTable);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserTempTableQuery() {
        return "CREATE TABLE #temp_active_users (id INT, name NVARCHAR(50), email NVARCHAR(100)); INSERT INTO #temp_active_users SELECT id, name, email FROM users WHERE status = 'active'; SELECT * FROM #temp_active_users; DROP TABLE #temp_active_users";
    }
    
    private String getOrderTempTableQuery() {
        return "CREATE TABLE #temp_recent_orders (id INT, user_id INT, total_amount DECIMAL(10,2)); INSERT INTO #temp_recent_orders SELECT id, user_id, total_amount FROM orders WHERE order_date > '2024-01-01'; SELECT * FROM #temp_recent_orders; DROP TABLE #temp_recent_orders";
    }
    
    private String getProductTempTableQuery() {
        return "CREATE TABLE #temp_expensive_products (id INT, name NVARCHAR(100), price DECIMAL(10,2)); INSERT INTO #temp_expensive_products SELECT id, name, price FROM products WHERE price > 100; SELECT * FROM #temp_expensive_products; DROP TABLE #temp_expensive_products";
    }
    
    public void tempTableWithDynamicSQL() throws SQLException {
        // Temporary table with dynamic SQL construction
        String baseCreate = "CREATE TABLE #temp_";
        String tableName = "users";
        String columns = " (id INT, name NVARCHAR(50))";
        String insert = "; INSERT INTO #temp_" + tableName + " SELECT id, name FROM users";
        String select = "; SELECT * FROM #temp_" + tableName;
        String drop = "; DROP TABLE #temp_" + tableName;
        
        String dynamicTempTable = baseCreate + tableName + columns + insert + select + drop;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        boolean hasResult = stmt.execute(dynamicTempTable);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithTransaction() throws SQLException {
        // Temporary table with transaction
        String transactionWithTempTable = "BEGIN TRANSACTION; CREATE TABLE #temp_orders (id INT, user_id INT, total_amount DECIMAL(10,2)); INSERT INTO #temp_orders SELECT id, user_id, total_amount FROM orders WHERE order_date > '2024-01-01'; SELECT COUNT(*) as order_count FROM #temp_orders; COMMIT TRANSACTION";
        String transactionWithTempTableRollback = "BEGIN TRANSACTION; CREATE TABLE #temp_products (id INT, name NVARCHAR(100)); INSERT INTO #temp_products SELECT id, name FROM products WHERE price > 1000; IF (SELECT COUNT(*) FROM #temp_products) = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No expensive products found' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Expensive products found' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(transactionWithTempTable);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(transactionWithTempTableRollback);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void tempTableWithPerformanceOptimization() throws SQLException {
        // Temporary table with performance optimization
        String performanceTempTable = "CREATE TABLE #temp_user_orders (user_id INT, order_count INT, total_spent DECIMAL(10,2)); INSERT INTO #temp_user_orders SELECT user_id, COUNT(*), SUM(total_amount) FROM orders GROUP BY user_id; CREATE INDEX IX_temp_user_orders_user_id ON #temp_user_orders(user_id); SELECT * FROM #temp_user_orders WHERE order_count > 5 ORDER BY total_spent DESC";
        String performanceTempTableComplex = "CREATE TABLE #temp_product_analysis (product_id INT, product_name NVARCHAR(100), order_count INT, total_revenue DECIMAL(10,2)); INSERT INTO #temp_product_analysis SELECT p.id, p.name, COUNT(o.id), SUM(o.total_amount) FROM products p LEFT JOIN orders o ON p.id = o.product_id GROUP BY p.id, p.name; SELECT * FROM #temp_product_analysis WHERE order_count > 0 ORDER BY total_revenue DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(performanceTempTable);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(performanceTempTableComplex);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
