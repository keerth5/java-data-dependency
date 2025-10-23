package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-028: CastFunctionUsage
 * Detects "CAST(" function usage in SQL statements
 */
public class CastFunctionUsageExample {
    
    public void basicCastFunctionUsage() throws SQLException {
        // Basic CAST() function usage
        String selectWithCast = "SELECT name, CAST(created_date AS varchar) as date_string FROM users";
        String selectWithCastInt = "SELECT name, CAST(price AS int) as price_int FROM products";
        String selectWithCastDecimal = "SELECT name, CAST(amount AS decimal(10,2)) as amount_decimal FROM orders";
        String selectWithCastDate = "SELECT name, CAST(order_date AS date) as order_date_only FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithCastInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithCastDecimal);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithCastDate);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionWithNumericTypes() throws SQLException {
        // CAST() function with numeric types
        String castToInt = "SELECT name, CAST(price AS int) as price_int FROM products";
        String castToDecimal = "SELECT name, CAST(amount AS decimal(10,2)) as amount_decimal FROM orders";
        String castToFloat = "SELECT name, CAST(rating AS float) as rating_float FROM products";
        String castToNumeric = "SELECT name, CAST(total_amount AS numeric(10,2)) as total_numeric FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(castToInt);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(castToDecimal);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(castToFloat);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(castToNumeric);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionWithStringTypes() throws SQLException {
        // CAST() function with string types
        String castToVarchar = "SELECT name, CAST(description AS varchar(50)) as description_varchar FROM products";
        String castToNvarchar = "SELECT name, CAST(notes AS nvarchar(100)) as notes_nvarchar FROM orders";
        String castToChar = "SELECT name, CAST(status AS char(10)) as status_char FROM users";
        String castToText = "SELECT name, CAST(long_description AS text) as long_desc_text FROM products";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(castToVarchar);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(castToNvarchar);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(castToChar);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(castToText);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionWithDateTypes() throws SQLException {
        // CAST() function with date types
        String castToDate = "SELECT name, CAST(order_date AS date) as order_date_only FROM orders";
        String castToTime = "SELECT name, CAST(created_date AS time) as creation_time FROM users";
        String castToDateTime = "SELECT name, CAST(timestamp AS datetime) as full_datetime FROM audit_log";
        String castToDateTime2 = "SELECT name, CAST(created_date AS datetime2) as precise_datetime FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(castToDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(castToTime);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(castToDateTime);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(castToDateTime2);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInWhereClauses() throws SQLException {
        // CAST() function in WHERE clauses
        String whereWithCast = "SELECT * FROM users WHERE CAST(created_date AS date) = '2024-01-01'";
        String whereWithCastInt = "SELECT * FROM products WHERE CAST(price AS int) > 100";
        String whereWithCastString = "SELECT * FROM users WHERE CAST(status AS varchar) = 'active'";
        String whereWithCastDecimal = "SELECT * FROM orders WHERE CAST(total_amount AS decimal(10,2)) > 1000.00";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithCastInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithCastString);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithCastDecimal);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInOrderByClauses() throws SQLException {
        // CAST() function in ORDER BY clauses
        String orderByWithCast = "SELECT * FROM users ORDER BY CAST(created_date AS date) DESC";
        String orderByWithCastInt = "SELECT * FROM products ORDER BY CAST(price AS int) ASC";
        String orderByWithCastString = "SELECT * FROM users ORDER BY CAST(name AS varchar) ASC";
        String orderByWithCastDecimal = "SELECT * FROM orders ORDER BY CAST(total_amount AS decimal(10,2)) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithCastInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithCastString);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithCastDecimal);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInGroupByClauses() throws SQLException {
        // CAST() function in GROUP BY clauses
        String groupByWithCast = "SELECT CAST(created_date AS date) as date_key, COUNT(*) as count FROM users GROUP BY CAST(created_date AS date)";
        String groupByWithCastInt = "SELECT CAST(price AS int) as price_range, COUNT(*) as count FROM products GROUP BY CAST(price AS int)";
        String groupByWithCastString = "SELECT CAST(status AS varchar) as status_group, COUNT(*) as count FROM users GROUP BY CAST(status AS varchar)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithCastInt);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithCastString);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInCaseStatements() throws SQLException {
        // CAST() function in CASE statements
        String caseWithCast = "SELECT name, CASE WHEN CAST(price AS int) > 1000 THEN 'Expensive' WHEN CAST(price AS int) > 100 THEN 'Moderate' ELSE 'Cheap' END as price_category FROM products";
        String caseWithCastDate = "SELECT name, CASE WHEN CAST(created_date AS date) = CAST(GETDATE() AS date) THEN 'Today' WHEN CAST(created_date AS date) = CAST(GETDATE() - 1 AS date) THEN 'Yesterday' ELSE 'Older' END as date_category FROM users";
        String caseWithCastString = "SELECT name, CASE WHEN CAST(status AS varchar) = 'active' THEN 'Active User' ELSE 'Inactive User' END as user_status FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithCastDate);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithCastString);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInSubqueries() throws SQLException {
        // CAST() function in subqueries
        String subqueryWithCast = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE CAST(order_date AS date) = '2024-01-01')";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND CAST(order_date AS date) = CAST(GETDATE() AS date)) as today_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM reviews WHERE product_id = products.id AND CAST(rating AS int) > 3)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInJoins() throws SQLException {
        // CAST() function in JOINs
        String joinWithCast = "SELECT u.name, CAST(o.order_date AS varchar) as order_date_string FROM users u JOIN orders o ON u.id = o.user_id";
        String joinWithCastCondition = "SELECT p.name, CAST(p.price AS int) as price_int FROM products p JOIN categories c ON p.category_id = c.id WHERE CAST(c.name AS varchar) = 'Electronics'";
        String joinWithCastCalculation = "SELECT u.name, SUM(CAST(o.total_amount AS decimal(10,2))) as total_spent FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithCastCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithCastCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInAggregates() throws SQLException {
        // CAST() function in aggregate functions
        String aggregateWithCast = "SELECT COUNT(*) as total_users, COUNT(CAST(phone AS varchar)) as users_with_phone FROM users";
        String sumWithCast = "SELECT SUM(CAST(total_amount AS decimal(10,2))) as total_revenue FROM orders";
        String avgWithCast = "SELECT AVG(CAST(rating AS float)) as average_rating FROM products";
        String maxWithCast = "SELECT MAX(CAST(created_date AS varchar)) as latest_creation FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithCast);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithCast);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithCast);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithCast);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInUpdateStatements() throws SQLException {
        // CAST() function in UPDATE statements
        String updateWithCast = "UPDATE users SET phone = CAST(phone_number AS varchar(20)) WHERE phone IS NULL";
        String updateWithCastCalculation = "UPDATE products SET price_display = CAST(CAST(price AS decimal(10,2)) AS varchar)";
        String updateWithCastCondition = "UPDATE orders SET status = CASE WHEN CAST(total_amount AS int) > 1000 THEN 'High Value' ELSE 'Standard' END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithCast);
        int result2 = stmt.executeUpdate(updateWithCastCalculation);
        int result3 = stmt.executeUpdate(updateWithCastCondition);
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInInsertStatements() throws SQLException {
        // CAST() function in INSERT statements
        String insertWithCast = "INSERT INTO users (name, created_date_string) VALUES ('John', CAST(GETDATE() AS varchar))";
        String insertWithCastInt = "INSERT INTO products (name, price_int) VALUES ('Product', CAST(99.99 AS int))";
        String insertWithCastDate = "INSERT INTO orders (user_id, order_date_only) VALUES (1, CAST(GETDATE() AS date))";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithCast);
        int result2 = stmt.executeUpdate(insertWithCastInt);
        int result3 = stmt.executeUpdate(insertWithCastDate);
        
        stmt.close();
        conn.close();
    }
    
    public void castFunctionInMethods() throws SQLException {
        // CAST() function in methods
        String userQuery = getUserQueryWithCast();
        String orderQuery = getOrderQueryWithCast();
        String productQuery = getProductQueryWithCast();
        
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
    
    private String getUserQueryWithCast() {
        return "SELECT name, CAST(created_date AS varchar) as created_date_string FROM users WHERE CAST(status AS varchar) = 'active'";
    }
    
    private String getOrderQueryWithCast() {
        return "SELECT order_id, CAST(order_date AS date) as order_date_only, CAST(total_amount AS decimal(10,2)) as total_decimal FROM orders WHERE CAST(total_amount AS int) > 100";
    }
    
    private String getProductQueryWithCast() {
        return "SELECT name, CAST(price AS int) as price_int, CAST(description AS varchar) as description_varchar FROM products WHERE CAST(category AS varchar) = 'Electronics'";
    }
    
    public void castFunctionWithDynamicSQL() throws SQLException {
        // CAST() function with dynamic SQL construction
        String baseQuery = "SELECT name, CAST(created_date AS varchar) as date_string FROM users WHERE";
        String dateCondition = " CAST(created_date AS date) = '2024-01-01'";
        String statusCondition = " AND CAST(status AS varchar) = 'active'";
        
        String dynamicQuery = baseQuery + dateCondition + statusCondition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void castFunctionWithPreparedStatement() throws SQLException {
        // CAST() function with PreparedStatement
        String selectQuery = "SELECT name, CAST(created_date AS varchar) as date_string FROM users WHERE CAST(status AS varchar) = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(selectQuery);
        
        pstmt.setString(1, "active");
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void castFunctionWithTransaction() throws SQLException {
        // CAST() function with transaction
        String updateUsers = "UPDATE users SET phone = CAST(phone_number AS varchar(20)) WHERE phone IS NULL";
        String updateProducts = "UPDATE products SET price_display = CAST(CAST(price AS decimal(10,2)) AS varchar)";
        String insertLog = "INSERT INTO audit_log (action, timestamp) VALUES ('CAST_UPDATE', GETDATE())";
        
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
    
    public void castFunctionWithComplexExpressions() throws SQLException {
        // CAST() function with complex expressions
        String complexCast1 = "SELECT name, CAST(CAST(price AS decimal(10,2)) * 1.1 AS int) as price_with_tax FROM products";
        String complexCast2 = "SELECT name, CAST(CAST(created_date AS varchar) + ' 00:00:00' AS datetime) as full_datetime FROM users";
        String complexCast3 = "SELECT name, CAST(CAST(age AS varchar) + ' years old' AS varchar(50)) as age_description FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(complexCast1);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(complexCast2);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(complexCast3);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
