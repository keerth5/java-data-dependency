package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-026: IsNullFunctionUsage
 * Detects "ISNULL(" SQL Server function usage
 */
public class IsNullFunctionUsageExample {
    
    public void basicIsNullFunctionUsage() throws SQLException {
        // Basic ISNULL() function usage
        String selectWithIsNull = "SELECT name, ISNULL(phone, 'N/A') as phone FROM users";
        String selectWithIsNullDefault = "SELECT name, ISNULL(email, 'no-email@example.com') as email FROM users";
        String selectWithIsNullNumeric = "SELECT name, ISNULL(age, 0) as age FROM users";
        String selectWithIsNullDate = "SELECT name, ISNULL(last_login, '1900-01-01') as last_login FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithIsNullDefault);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithIsNullNumeric);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithIsNullDate);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInSelectStatements() throws SQLException {
        // ISNULL() function in SELECT statements
        String selectWithMultipleIsNull = "SELECT name, ISNULL(phone, 'N/A') as phone, ISNULL(address, 'No Address') as address FROM users";
        String selectWithIsNullCalculation = "SELECT name, ISNULL(salary, 0) + ISNULL(bonus, 0) as total_compensation FROM employees";
        String selectWithIsNullString = "SELECT name, ISNULL(description, 'No Description') as description FROM products";
        String selectWithIsNullNested = "SELECT name, ISNULL(ISNULL(phone, mobile), 'No Phone') as contact FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithMultipleIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithIsNullCalculation);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithIsNullString);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithIsNullNested);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInWhereClauses() throws SQLException {
        // ISNULL() function in WHERE clauses
        String whereWithIsNull = "SELECT * FROM users WHERE ISNULL(phone, '') = ''";
        String whereWithIsNullNot = "SELECT * FROM users WHERE ISNULL(phone, '') != ''";
        String whereWithIsNullCondition = "SELECT * FROM products WHERE ISNULL(price, 0) > 100";
        String whereWithIsNullDate = "SELECT * FROM orders WHERE ISNULL(shipped_date, '1900-01-01') = '1900-01-01'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithIsNullNot);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithIsNullCondition);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithIsNullDate);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInOrderByClauses() throws SQLException {
        // ISNULL() function in ORDER BY clauses
        String orderByWithIsNull = "SELECT * FROM users ORDER BY ISNULL(last_login, '1900-01-01') DESC";
        String orderByWithIsNullString = "SELECT * FROM products ORDER BY ISNULL(name, 'ZZZ') ASC";
        String orderByWithIsNullNumeric = "SELECT * FROM employees ORDER BY ISNULL(salary, 0) DESC";
        String orderByWithIsNullMultiple = "SELECT * FROM orders ORDER BY ISNULL(priority, 0) DESC, ISNULL(order_date, '1900-01-01') ASC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithIsNullString);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithIsNullNumeric);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithIsNullMultiple);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInGroupByClauses() throws SQLException {
        // ISNULL() function in GROUP BY clauses
        String groupByWithIsNull = "SELECT ISNULL(category, 'Uncategorized') as category, COUNT(*) as count FROM products GROUP BY ISNULL(category, 'Uncategorized')";
        String groupByWithIsNullStatus = "SELECT ISNULL(status, 'Unknown') as status, SUM(amount) as total FROM orders GROUP BY ISNULL(status, 'Unknown')";
        String groupByWithIsNullDepartment = "SELECT ISNULL(department, 'No Department') as department, AVG(salary) as avg_salary FROM employees GROUP BY ISNULL(department, 'No Department')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithIsNullStatus);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithIsNullDepartment);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInCaseStatements() throws SQLException {
        // ISNULL() function in CASE statements
        String caseWithIsNull = "SELECT name, CASE WHEN ISNULL(phone, '') = '' THEN 'No Phone' ELSE 'Has Phone' END as phone_status FROM users";
        String caseWithIsNullComplex = "SELECT name, CASE WHEN ISNULL(salary, 0) = 0 THEN 'Unpaid' WHEN ISNULL(salary, 0) < 50000 THEN 'Low' WHEN ISNULL(salary, 0) < 100000 THEN 'Medium' ELSE 'High' END as salary_level FROM employees";
        String caseWithIsNullDate = "SELECT name, CASE WHEN ISNULL(last_login, '1900-01-01') = '1900-01-01' THEN 'Never Logged In' ELSE 'Has Logged In' END as login_status FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithIsNullComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithIsNullDate);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInSubqueries() throws SQLException {
        // ISNULL() function in subqueries
        String subqueryWithIsNull = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE ISNULL(status, '') = 'completed')";
        String subqueryInSelect = "SELECT name, (SELECT ISNULL(COUNT(*), 0) FROM orders WHERE user_id = users.id) as order_count FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM reviews WHERE product_id = products.id AND ISNULL(rating, 0) > 3)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInJoins() throws SQLException {
        // ISNULL() function in JOINs
        String joinWithIsNull = "SELECT u.name, ISNULL(p.phone, 'N/A') as phone FROM users u LEFT JOIN profiles p ON u.id = p.user_id";
        String joinWithIsNullCalculation = "SELECT u.name, ISNULL(SUM(o.amount), 0) as total_spent FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.name";
        String joinWithIsNullCondition = "SELECT p.name, ISNULL(c.name, 'Uncategorized') as category FROM products p LEFT JOIN categories c ON p.category_id = c.id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithIsNullCalculation);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithIsNullCondition);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInAggregates() throws SQLException {
        // ISNULL() function in aggregate functions
        String aggregateWithIsNull = "SELECT COUNT(*) as total_users, COUNT(ISNULL(phone, '')) as users_with_phone FROM users";
        String sumWithIsNull = "SELECT SUM(ISNULL(salary, 0)) as total_salary FROM employees";
        String avgWithIsNull = "SELECT AVG(ISNULL(age, 0)) as average_age FROM users";
        String maxWithIsNull = "SELECT MAX(ISNULL(last_login, '1900-01-01')) as latest_login FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithIsNull);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithIsNull);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithIsNull);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithIsNull);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInUpdateStatements() throws SQLException {
        // ISNULL() function in UPDATE statements
        String updateWithIsNull = "UPDATE users SET phone = ISNULL(phone, 'N/A') WHERE phone IS NULL";
        String updateWithIsNullCalculation = "UPDATE employees SET total_compensation = ISNULL(salary, 0) + ISNULL(bonus, 0)";
        String updateWithIsNullCondition = "UPDATE products SET status = CASE WHEN ISNULL(price, 0) = 0 THEN 'Free' ELSE 'Paid' END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithIsNull);
        int result2 = stmt.executeUpdate(updateWithIsNullCalculation);
        int result3 = stmt.executeUpdate(updateWithIsNullCondition);
        
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionInInsertStatements() throws SQLException {
        // ISNULL() function in INSERT statements
        String insertWithIsNull = "INSERT INTO users (name, phone) VALUES ('John', ISNULL(@phone_param, 'N/A'))";
        String insertWithIsNullDefault = "INSERT INTO products (name, price) VALUES ('Product', ISNULL(@price_param, 0))";
        String insertWithIsNullDate = "INSERT INTO orders (user_id, order_date) VALUES (1, ISNULL(@order_date_param, GETDATE()))";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(insertWithIsNull);
        pstmt1.setString(1, "N/A");
        pstmt1.executeUpdate();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(insertWithIsNullDefault);
        pstmt2.setDouble(1, 0.0);
        pstmt2.executeUpdate();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(insertWithIsNullDate);
        pstmt3.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        pstmt3.executeUpdate();
        pstmt3.close();
        
        conn.close();
    }
    
    public void isNullFunctionInMethods() throws SQLException {
        // ISNULL() function in methods
        String userQuery = getUserQueryWithIsNull();
        String orderQuery = getOrderQueryWithIsNull();
        String productQuery = getProductQueryWithIsNull();
        
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
    
    private String getUserQueryWithIsNull() {
        return "SELECT name, ISNULL(phone, 'N/A') as phone, ISNULL(email, 'no-email@example.com') as email FROM users WHERE ISNULL(status, '') = 'active'";
    }
    
    private String getOrderQueryWithIsNull() {
        return "SELECT order_id, ISNULL(status, 'Unknown') as status, ISNULL(total_amount, 0) as total FROM orders WHERE ISNULL(order_date, '1900-01-01') > '2023-01-01'";
    }
    
    private String getProductQueryWithIsNull() {
        return "SELECT name, ISNULL(price, 0) as price, ISNULL(description, 'No Description') as description FROM products WHERE ISNULL(category, '') != ''";
    }
    
    public void isNullFunctionWithDynamicSQL() throws SQLException {
        // ISNULL() function with dynamic SQL construction
        String baseQuery = "SELECT name, ISNULL(phone, 'N/A') as phone FROM users WHERE";
        String statusCondition = " ISNULL(status, '') = 'active'";
        String phoneCondition = " AND ISNULL(phone, '') != ''";
        
        String dynamicQuery = baseQuery + statusCondition + phoneCondition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void isNullFunctionWithPreparedStatement() throws SQLException {
        // ISNULL() function with PreparedStatement
        String selectQuery = "SELECT name, ISNULL(phone, ?) as phone FROM users WHERE ISNULL(status, ?) = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        PreparedStatement pstmt = conn.prepareStatement(selectQuery);
        
        pstmt.setString(1, "N/A");
        pstmt.setString(2, "");
        pstmt.setString(3, "active");
        
        ResultSet rs = pstmt.executeQuery();
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void isNullFunctionWithTransaction() throws SQLException {
        // ISNULL() function with transaction
        String updateUsers = "UPDATE users SET phone = ISNULL(phone, 'N/A') WHERE phone IS NULL";
        String updateProducts = "UPDATE products SET description = ISNULL(description, 'No Description') WHERE description IS NULL";
        String insertLog = "INSERT INTO audit_log (action, timestamp) VALUES ('ISNULL_UPDATE', GETDATE())";
        
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
