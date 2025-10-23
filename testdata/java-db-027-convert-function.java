package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-027: ConvertFunctionUsage
 * Detects "CONVERT(" SQL Server function usage
 */
public class ConvertFunctionUsageExample {
    
    public void basicConvertFunctionUsage() throws SQLException {
        // Basic CONVERT() function usage
        String selectWithConvert = "SELECT name, CONVERT(varchar, created_date, 120) as date_string FROM users";
        String selectWithConvertInt = "SELECT name, CONVERT(int, price) as price_int FROM products";
        String selectWithConvertDecimal = "SELECT name, CONVERT(decimal(10,2), amount) as amount_decimal FROM orders";
        String selectWithConvertDate = "SELECT name, CONVERT(date, order_date) as order_date_only FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithConvertInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithConvertDecimal);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithConvertDate);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionWithDateFormats() throws SQLException {
        // CONVERT() function with date formats
        String convertDateFormat101 = "SELECT name, CONVERT(varchar, created_date, 101) as us_date FROM users";
        String convertDateFormat120 = "SELECT name, CONVERT(varchar, created_date, 120) as iso_date FROM users";
        String convertDateFormat112 = "SELECT name, CONVERT(varchar, created_date, 112) as yyyymmdd FROM users";
        String convertDateFormat103 = "SELECT name, CONVERT(varchar, created_date, 103) as uk_date FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(convertDateFormat101);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(convertDateFormat120);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(convertDateFormat112);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(convertDateFormat103);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionWithNumericTypes() throws SQLException {
        // CONVERT() function with numeric types
        String convertToInt = "SELECT name, CONVERT(int, price) as price_int FROM products";
        String convertToDecimal = "SELECT name, CONVERT(decimal(10,2), amount) as amount_decimal FROM orders";
        String convertToFloat = "SELECT name, CONVERT(float, rating) as rating_float FROM products";
        String convertToMoney = "SELECT name, CONVERT(money, total_amount) as total_money FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(convertToInt);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(convertToDecimal);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(convertToFloat);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(convertToMoney);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionWithStringTypes() throws SQLException {
        // CONVERT() function with string types
        String convertToVarchar = "SELECT name, CONVERT(varchar(50), description) as description_varchar FROM products";
        String convertToNvarchar = "SELECT name, CONVERT(nvarchar(100), notes) as notes_nvarchar FROM orders";
        String convertToChar = "SELECT name, CONVERT(char(10), status) as status_char FROM users";
        String convertToText = "SELECT name, CONVERT(text, long_description) as long_desc_text FROM products";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(convertToVarchar);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(convertToNvarchar);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(convertToChar);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(convertToText);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInWhereClauses() throws SQLException {
        // CONVERT() function in WHERE clauses
        String whereWithConvert = "SELECT * FROM users WHERE CONVERT(varchar, created_date, 112) = '20240101'";
        String whereWithConvertInt = "SELECT * FROM products WHERE CONVERT(int, price) > 100";
        String whereWithConvertDate = "SELECT * FROM orders WHERE CONVERT(date, order_date) = '2024-01-01'";
        String whereWithConvertString = "SELECT * FROM users WHERE CONVERT(varchar, status) = 'active'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithConvertInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithConvertDate);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithConvertString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInOrderByClauses() throws SQLException {
        // CONVERT() function in ORDER BY clauses
        String orderByWithConvert = "SELECT * FROM users ORDER BY CONVERT(varchar, created_date, 112) DESC";
        String orderByWithConvertInt = "SELECT * FROM products ORDER BY CONVERT(int, price) ASC";
        String orderByWithConvertDate = "SELECT * FROM orders ORDER BY CONVERT(date, order_date) DESC";
        String orderByWithConvertString = "SELECT * FROM users ORDER BY CONVERT(varchar, name) ASC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithConvertInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithConvertDate);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithConvertString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInGroupByClauses() throws SQLException {
        // CONVERT() function in GROUP BY clauses
        String groupByWithConvert = "SELECT CONVERT(varchar, created_date, 112) as date_key, COUNT(*) as count FROM users GROUP BY CONVERT(varchar, created_date, 112)";
        String groupByWithConvertInt = "SELECT CONVERT(int, price/100) as price_range, COUNT(*) as count FROM products GROUP BY CONVERT(int, price/100)";
        String groupByWithConvertDate = "SELECT CONVERT(date, order_date) as order_date, SUM(total_amount) as total FROM orders GROUP BY CONVERT(date, order_date)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithConvertInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithConvertDate);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInCaseStatements() throws SQLException {
        // CONVERT() function in CASE statements
        String caseWithConvert = "SELECT name, CASE WHEN CONVERT(int, price) > 1000 THEN 'Expensive' WHEN CONVERT(int, price) > 100 THEN 'Moderate' ELSE 'Cheap' END as price_category FROM products";
        String caseWithConvertDate = "SELECT name, CASE WHEN CONVERT(date, created_date) = CONVERT(date, GETDATE()) THEN 'Today' WHEN CONVERT(date, created_date) = CONVERT(date, GETDATE() - 1) THEN 'Yesterday' ELSE 'Older' END as date_category FROM users";
        String caseWithConvertString = "SELECT name, CASE WHEN CONVERT(varchar, status) = 'active' THEN 'Active User' ELSE 'Inactive User' END as user_status FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithConvertDate);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithConvertString);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInSubqueries() throws SQLException {
        // CONVERT() function in subqueries
        String subqueryWithConvert = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE CONVERT(date, order_date) = '2024-01-01')";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND CONVERT(date, order_date) = CONVERT(date, GETDATE())) as today_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM reviews WHERE product_id = products.id AND CONVERT(int, rating) > 3)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInJoins() throws SQLException {
        // CONVERT() function in JOINs
        String joinWithConvert = "SELECT u.name, CONVERT(varchar, o.order_date, 120) as order_date_string FROM users u JOIN orders o ON u.id = o.user_id";
        String joinWithConvertCondition = "SELECT p.name, CONVERT(int, p.price) as price_int FROM products p JOIN categories c ON p.category_id = c.id WHERE CONVERT(varchar, c.name) = 'Electronics'";
        String joinWithConvertCalculation = "SELECT u.name, SUM(CONVERT(decimal(10,2), o.total_amount)) as total_spent FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithConvertCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithConvertCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInAggregates() throws SQLException {
        // CONVERT() function in aggregate functions
        String aggregateWithConvert = "SELECT COUNT(*) as total_users, COUNT(CONVERT(varchar, phone)) as users_with_phone FROM users";
        String sumWithConvert = "SELECT SUM(CONVERT(decimal(10,2), total_amount)) as total_revenue FROM orders";
        String avgWithConvert = "SELECT AVG(CONVERT(float, rating)) as average_rating FROM products";
        String maxWithConvert = "SELECT MAX(CONVERT(varchar, created_date, 120)) as latest_creation FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithConvert);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithConvert);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithConvert);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithConvert);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInUpdateStatements() throws SQLException {
        // CONVERT() function in UPDATE statements
        String updateWithConvert = "UPDATE users SET phone = CONVERT(varchar(20), phone_number) WHERE phone IS NULL";
        String updateWithConvertCalculation = "UPDATE products SET price_display = CONVERT(varchar, CONVERT(decimal(10,2), price))";
        String updateWithConvertCondition = "UPDATE orders SET status = CASE WHEN CONVERT(int, total_amount) > 1000 THEN 'High Value' ELSE 'Standard' END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithConvert);
        int result2 = stmt.executeUpdate(updateWithConvertCalculation);
        int result3 = stmt.executeUpdate(updateWithConvertCondition);
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInInsertStatements() throws SQLException {
        // CONVERT() function in INSERT statements
        String insertWithConvert = "INSERT INTO users (name, created_date_string) VALUES ('John', CONVERT(varchar, GETDATE(), 120))";
        String insertWithConvertInt = "INSERT INTO products (name, price_int) VALUES ('Product', CONVERT(int, 99.99))";
        String insertWithConvertDate = "INSERT INTO orders (user_id, order_date_only) VALUES (1, CONVERT(date, GETDATE()))";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithConvert);
        int result2 = stmt.executeUpdate(insertWithConvertInt);
        int result3 = stmt.executeUpdate(insertWithConvertDate);
        
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionInMethods() throws SQLException {
        // CONVERT() function in methods
        String userQuery = getUserQueryWithConvert();
        String orderQuery = getOrderQueryWithConvert();
        String productQuery = getProductQueryWithConvert();
        
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
    
    private String getUserQueryWithConvert() {
        return "SELECT name, CONVERT(varchar, created_date, 120) as created_date_string FROM users WHERE CONVERT(varchar, status) = 'active'";
    }
    
    private String getOrderQueryWithConvert() {
        return "SELECT order_id, CONVERT(date, order_date) as order_date_only, CONVERT(decimal(10,2), total_amount) as total_decimal FROM orders WHERE CONVERT(int, total_amount) > 100";
    }
    
    private String getProductQueryWithConvert() {
        return "SELECT name, CONVERT(int, price) as price_int, CONVERT(varchar, description) as description_varchar FROM products WHERE CONVERT(varchar, category) = 'Electronics'";
    }
    
    public void convertFunctionWithDynamicSQL() throws SQLException {
        // CONVERT() function with dynamic SQL construction
        String baseQuery = "SELECT name, CONVERT(varchar, created_date, 120) as date_string FROM users WHERE";
        String dateCondition = " CONVERT(date, created_date) = '2024-01-01'";
        String statusCondition = " AND CONVERT(varchar, status) = 'active'";
        
        String dynamicQuery = baseQuery + dateCondition + statusCondition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void convertFunctionWithPreparedStatement() throws SQLException {
        // CONVERT() function with PreparedStatement
        String selectQuery = "SELECT name, CONVERT(varchar, created_date, ?) as date_string FROM users WHERE CONVERT(varchar, status) = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(selectQuery);
        
        pstmt.setInt(1, 120);
        pstmt.setString(2, "active");
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void convertFunctionWithTransaction() throws SQLException {
        // CONVERT() function with transaction
        String updateUsers = "UPDATE users SET phone = CONVERT(varchar(20), phone_number) WHERE phone IS NULL";
        String updateProducts = "UPDATE products SET price_display = CONVERT(varchar, CONVERT(decimal(10,2), price))";
        String insertLog = "INSERT INTO audit_log (action, timestamp) VALUES ('CONVERT_UPDATE', GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            int result1 = stmt.executeUpdate(updateUsers);
            int result2 = stmt.executeUpdate(updateProducts);
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
