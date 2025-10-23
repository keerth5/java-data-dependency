package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-020: SelectStatementUsage
 * Detects "SELECT" statements in Java string literals
 */
public class SelectStatementUsageExample {
    
    public void basicSelectStatements() throws SQLException {
        // Basic SELECT statements in string literals
        String selectAllUsers = "SELECT * FROM users";
        String selectUserById = "SELECT id, name, email FROM users WHERE id = ?";
        String selectActiveUsers = "SELECT * FROM users WHERE status = 'active'";
        String selectUsersWithJoin = "SELECT u.name, p.title FROM users u JOIN profiles p ON u.id = p.user_id";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        
        Statement stmt = conn.createStatement();
        ResultSet rs1 = stmt.executeQuery(selectAllUsers);
        rs1.close();
        
        PreparedStatement pstmt = conn.prepareStatement(selectUserById);
        pstmt.setInt(1, 123);
        ResultSet rs2 = pstmt.executeQuery();
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectActiveUsers);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectUsersWithJoin);
        rs4.close();
        
        stmt.close();
        pstmt.close();
        conn.close();
    }
    
    public void selectStatementsWithWhereClause() throws SQLException {
        // SELECT statements with WHERE clauses
        String selectByDate = "SELECT * FROM orders WHERE order_date > '2024-01-01'";
        String selectByStatus = "SELECT id, customer_name FROM orders WHERE status = 'pending'";
        String selectByAmount = "SELECT * FROM orders WHERE total_amount > 1000.00";
        String selectByMultipleConditions = "SELECT * FROM users WHERE age > 18 AND status = 'active' AND city = 'New York'";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectByDate);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectByStatus);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectByAmount);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectByMultipleConditions);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithJoins() throws SQLException {
        // SELECT statements with JOIN operations
        String innerJoin = "SELECT u.name, o.order_date FROM users u INNER JOIN orders o ON u.id = o.user_id";
        String leftJoin = "SELECT u.name, p.phone FROM users u LEFT JOIN profiles p ON u.id = p.user_id";
        String rightJoin = "SELECT p.title, u.name FROM profiles p RIGHT JOIN users u ON p.user_id = u.id";
        String fullOuterJoin = "SELECT u.name, p.title FROM users u FULL OUTER JOIN profiles p ON u.id = p.user_id";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(innerJoin);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(leftJoin);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(rightJoin);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(fullOuterJoin);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithAggregates() throws SQLException {
        // SELECT statements with aggregate functions
        String countUsers = "SELECT COUNT(*) FROM users";
        String sumOrders = "SELECT SUM(total_amount) FROM orders WHERE status = 'completed'";
        String avgAge = "SELECT AVG(age) FROM users WHERE status = 'active'";
        String maxOrderDate = "SELECT MAX(order_date) FROM orders";
        String minOrderDate = "SELECT MIN(order_date) FROM orders";
        String groupBy = "SELECT status, COUNT(*) FROM orders GROUP BY status";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(countUsers);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumOrders);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgAge);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxOrderDate);
        rs4.close();
        
        ResultSet rs5 = stmt.executeQuery(minOrderDate);
        rs5.close();
        
        ResultSet rs6 = stmt.executeQuery(groupBy);
        rs6.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithOrderBy() throws SQLException {
        // SELECT statements with ORDER BY clauses
        String orderByName = "SELECT * FROM users ORDER BY name ASC";
        String orderByDate = "SELECT * FROM orders ORDER BY order_date DESC";
        String orderByMultiple = "SELECT * FROM users ORDER BY status ASC, name DESC";
        String orderByExpression = "SELECT * FROM orders ORDER BY total_amount * 1.1 DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByName);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByDate);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByMultiple);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByExpression);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithLimit() throws SQLException {
        // SELECT statements with LIMIT clauses
        String limitTop10 = "SELECT * FROM users LIMIT 10";
        String limitWithOffset = "SELECT * FROM orders LIMIT 20 OFFSET 40";
        String limitTop = "SELECT TOP 5 * FROM users ORDER BY created_date DESC";
        String limitWithWhere = "SELECT * FROM orders WHERE status = 'pending' LIMIT 50";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(limitTop10);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(limitWithOffset);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(limitTop);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(limitWithWhere);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithSubqueries() throws SQLException {
        // SELECT statements with subqueries
        String subqueryInWhere = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > 1000)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id) as order_count FROM users";
        String subqueryWithExists = "SELECT * FROM users WHERE EXISTS (SELECT 1 FROM orders WHERE user_id = users.id)";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryInWhere);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(subqueryInSelect);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(subqueryWithExists);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(subqueryWithNotExists);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithCase() throws SQLException {
        // SELECT statements with CASE expressions
        String caseSimple = "SELECT name, CASE WHEN age >= 18 THEN 'Adult' ELSE 'Minor' END as age_group FROM users";
        String caseMultiple = "SELECT status, CASE status WHEN 'active' THEN 'Active User' WHEN 'inactive' THEN 'Inactive User' ELSE 'Unknown' END as status_desc FROM users";
        String caseWithAggregate = "SELECT COUNT(CASE WHEN status = 'active' THEN 1 END) as active_count FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseSimple);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseMultiple);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithAggregate);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithFunctions() throws SQLException {
        // SELECT statements with database functions
        String stringFunctions = "SELECT UPPER(name), LOWER(email), LENGTH(description) FROM users";
        String dateFunctions = "SELECT name, YEAR(created_date), MONTH(created_date), DAY(created_date) FROM users";
        String mathFunctions = "SELECT name, ROUND(salary, 2), CEILING(bonus), FLOOR(deduction) FROM employees";
        String nullFunctions = "SELECT name, COALESCE(phone, 'N/A'), ISNULL(email, 'No Email') FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringFunctions);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(dateFunctions);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(mathFunctions);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(nullFunctions);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsWithPreparedStatement() throws SQLException {
        // SELECT statements with PreparedStatement
        String selectByParameter = "SELECT * FROM users WHERE id = ? AND status = ?";
        String selectByDateRange = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ?";
        String selectByLike = "SELECT * FROM users WHERE name LIKE ?";
        String selectByIn = "SELECT * FROM users WHERE id IN (?, ?, ?)";
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectByParameter);
        pstmt1.setInt(1, 123);
        pstmt1.setString(2, "active");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectByDateRange);
        pstmt2.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        pstmt2.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectByLike);
        pstmt3.setString(1, "%John%");
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        PreparedStatement pstmt4 = conn.prepareStatement(selectByIn);
        pstmt4.setInt(1, 1);
        pstmt4.setInt(2, 2);
        pstmt4.setInt(3, 3);
        ResultSet rs4 = pstmt4.executeQuery();
        rs4.close();
        pstmt4.close();
        
        conn.close();
    }
    
    public void selectStatementsWithComplexQueries() throws SQLException {
        // SELECT statements with complex queries
        String complexQuery = "SELECT u.name, COUNT(o.id) as order_count, SUM(o.total_amount) as total_spent " +
                            "FROM users u " +
                            "LEFT JOIN orders o ON u.id = o.user_id " +
                            "WHERE u.status = 'active' " +
                            "GROUP BY u.id, u.name " +
                            "HAVING COUNT(o.id) > 5 " +
                            "ORDER BY total_spent DESC " +
                            "LIMIT 10";
        
        String windowFunctionQuery = "SELECT name, salary, ROW_NUMBER() OVER (ORDER BY salary DESC) as rank FROM employees";
        
        String cteQuery = "WITH recent_orders AS (SELECT * FROM orders WHERE order_date > '2024-01-01') " +
                         "SELECT u.name, COUNT(r.id) FROM users u JOIN recent_orders r ON u.id = r.user_id GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(complexQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(windowFunctionQuery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteQuery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void selectStatementsInMethods() throws SQLException {
        // SELECT statements in methods
        String userQuery = getUserSelectQuery();
        String orderQuery = getOrderSelectQuery();
        String reportQuery = getReportSelectQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(userQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderQuery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(reportQuery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    private String getUserSelectQuery() {
        return "SELECT id, name, email, created_date FROM users WHERE status = 'active'";
    }
    
    private String getOrderSelectQuery() {
        return "SELECT o.id, u.name, o.order_date, o.total_amount FROM orders o JOIN users u ON o.user_id = u.id";
    }
    
    private String getReportSelectQuery() {
        return "SELECT DATE(order_date) as date, COUNT(*) as order_count, SUM(total_amount) as daily_total FROM orders GROUP BY DATE(order_date)";
    }
    
    public void selectStatementsWithDynamicSQL() throws SQLException {
        // SELECT statements with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE 1=1";
        String statusFilter = " AND status = 'active'";
        String ageFilter = " AND age > 18";
        String orderBy = " ORDER BY name ASC";
        
        String dynamicQuery = baseQuery + statusFilter + ageFilter + orderBy;
        
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
}
