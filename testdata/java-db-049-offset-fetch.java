package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-049: OffsetFetchUsage
 * Detects "OFFSET" and "FETCH" pagination syntax
 */
public class OffsetFetchUsageExample {
    
    public void basicOffsetFetchUsage() throws SQLException {
        // Basic OFFSET and FETCH usage
        String offsetFetch = "SELECT * FROM users ORDER BY id OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY";
        String offsetFetchWithOrderBy = "SELECT * FROM products ORDER BY name OFFSET 20 ROWS FETCH NEXT 5 ROWS ONLY";
        String offsetFetchWithWhere = "SELECT * FROM orders WHERE status = 'active' ORDER BY order_date OFFSET 0 ROWS FETCH NEXT 25 ROWS ONLY";
        String offsetFetchWithJoin = "SELECT u.name, o.order_number FROM users u JOIN orders o ON u.id = o.user_id ORDER BY o.order_date OFFSET 5 ROWS FETCH NEXT 15 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetch);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithOrderBy);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithWhere);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(offsetFetchWithJoin);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithDifferentRowCounts() throws SQLException {
        // OFFSET and FETCH with different row counts
        String offsetFetchSmall = "SELECT * FROM users ORDER BY id OFFSET 0 ROWS FETCH NEXT 5 ROWS ONLY";
        String offsetFetchMedium = "SELECT * FROM products ORDER BY name OFFSET 10 ROWS FETCH NEXT 20 ROWS ONLY";
        String offsetFetchLarge = "SELECT * FROM orders ORDER BY order_date OFFSET 50 ROWS FETCH NEXT 100 ROWS ONLY";
        String offsetFetchVariable = "SELECT * FROM contacts ORDER BY name OFFSET 25 ROWS FETCH NEXT 30 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchSmall);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchMedium);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchLarge);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(offsetFetchVariable);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithAggregates() throws SQLException {
        // OFFSET and FETCH with aggregate functions
        String offsetFetchWithCount = "SELECT COUNT(*) as user_count FROM users ORDER BY id OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        String offsetFetchWithSum = "SELECT SUM(total_amount) as total_revenue FROM orders ORDER BY order_date OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        String offsetFetchWithAvg = "SELECT AVG(price) as avg_price FROM products ORDER BY name OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        String offsetFetchWithMax = "SELECT MAX(created_date) as latest_date FROM audit_log ORDER BY created_date OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchWithCount);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithSum);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithAvg);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(offsetFetchWithMax);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithGroupBy() throws SQLException {
        // OFFSET and FETCH with GROUP BY
        String offsetFetchWithGroupBy = "SELECT category_id, COUNT(*) as product_count FROM products GROUP BY category_id ORDER BY product_count DESC OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
        String offsetFetchWithGroupByComplex = "SELECT user_id, COUNT(*) as order_count, SUM(total_amount) as total_spent FROM orders GROUP BY user_id ORDER BY total_spent DESC OFFSET 5 ROWS FETCH NEXT 15 ROWS ONLY";
        String offsetFetchWithGroupByAdvanced = "SELECT YEAR(order_date) as order_year, COUNT(*) as yearly_orders FROM orders GROUP BY YEAR(order_date) ORDER BY order_year DESC OFFSET 0 ROWS FETCH NEXT 5 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchWithGroupBy);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithGroupByComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithGroupByAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithSubqueries() throws SQLException {
        // OFFSET and FETCH with subqueries
        String offsetFetchWithSubquery = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > 1000) ORDER BY name OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
        String offsetFetchWithSubqueryComplex = "SELECT * FROM products WHERE category_id IN (SELECT id FROM categories WHERE name LIKE '%Electronics%') ORDER BY price DESC OFFSET 5 ROWS FETCH NEXT 20 ROWS ONLY";
        String offsetFetchWithSubqueryAdvanced = "SELECT * FROM orders WHERE user_id IN (SELECT id FROM users WHERE status = 'active') ORDER BY order_date DESC OFFSET 10 ROWS FETCH NEXT 25 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchWithSubquery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithSubqueryComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithSubqueryAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithJoins() throws SQLException {
        // OFFSET and FETCH with JOINs
        String offsetFetchWithJoin = "SELECT u.name, o.order_number, o.total_amount FROM users u JOIN orders o ON u.id = o.user_id ORDER BY o.order_date DESC OFFSET 0 ROWS FETCH NEXT 15 ROWS ONLY";
        String offsetFetchWithLeftJoin = "SELECT p.name, c.category_name, p.price FROM products p LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.price DESC OFFSET 5 ROWS FETCH NEXT 20 ROWS ONLY";
        String offsetFetchWithMultipleJoins = "SELECT u.name, o.order_number, p.product_name FROM users u JOIN orders o ON u.id = o.user_id JOIN products p ON o.product_id = p.id ORDER BY o.order_date DESC OFFSET 10 ROWS FETCH NEXT 25 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchWithJoin);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithLeftJoin);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithMultipleJoins);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithCaseStatements() throws SQLException {
        // OFFSET and FETCH with CASE statements
        String offsetFetchWithCase = "SELECT name, CASE WHEN status = 'active' THEN 'Active User' ELSE 'Inactive User' END as user_status FROM users ORDER BY name OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
        String offsetFetchWithCaseComplex = "SELECT product_name, CASE WHEN price > 1000 THEN 'Expensive' WHEN price > 100 THEN 'Moderate' ELSE 'Affordable' END as price_category FROM products ORDER BY price DESC OFFSET 5 ROWS FETCH NEXT 15 ROWS ONLY";
        String offsetFetchWithCaseAdvanced = "SELECT order_number, CASE WHEN total_amount > 5000 THEN 'High Value' WHEN total_amount > 1000 THEN 'Medium Value' ELSE 'Low Value' END as order_value FROM orders ORDER BY total_amount DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchWithCase);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithCaseComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithCaseAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithFunctions() throws SQLException {
        // OFFSET and FETCH with functions
        String offsetFetchWithFunctions = "SELECT name, UPPER(name) as upper_name, LEN(name) as name_length FROM users ORDER BY name_length DESC OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
        String offsetFetchWithDateFunctions = "SELECT order_number, YEAR(order_date) as order_year, MONTH(order_date) as order_month FROM orders ORDER BY order_date DESC OFFSET 5 ROWS FETCH NEXT 15 ROWS ONLY";
        String offsetFetchWithStringFunctions = "SELECT product_name, LEFT(name, 10) as short_name, RIGHT(name, 5) as suffix FROM products ORDER BY name OFFSET 10 ROWS FETCH NEXT 20 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(offsetFetchWithFunctions);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(offsetFetchWithDateFunctions);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(offsetFetchWithStringFunctions);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithPreparedStatement() throws SQLException {
        // OFFSET and FETCH with PreparedStatement
        String selectWithOffsetFetchParam = "SELECT * FROM users ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String selectWithOffsetFetchParamCondition = "SELECT * FROM products WHERE category_id = ? ORDER BY name OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String selectWithOffsetFetchParamCalculation = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithOffsetFetchParam);
        pstmt1.setInt(1, 10);
        pstmt1.setInt(2, 10);
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithOffsetFetchParamCondition);
        pstmt2.setInt(1, 1);
        pstmt2.setInt(2, 5);
        pstmt2.setInt(3, 10);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithOffsetFetchParamCalculation);
        pstmt3.setInt(1, 1);
        pstmt3.setInt(2, 0);
        pstmt3.setInt(3, 5);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void offsetFetchInMethods() throws SQLException {
        // OFFSET and FETCH in methods
        String userQuery = getUserQueryWithOffsetFetch();
        String orderQuery = getOrderQueryWithOffsetFetch();
        String productQuery = getProductQueryWithOffsetFetch();
        
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
    
    private String getUserQueryWithOffsetFetch() {
        return "SELECT * FROM users ORDER BY name OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
    }
    
    private String getOrderQueryWithOffsetFetch() {
        return "SELECT * FROM orders ORDER BY order_date DESC OFFSET 5 ROWS FETCH NEXT 15 ROWS ONLY";
    }
    
    private String getProductQueryWithOffsetFetch() {
        return "SELECT * FROM products ORDER BY price DESC OFFSET 10 ROWS FETCH NEXT 20 ROWS ONLY";
    }
    
    public void offsetFetchWithDynamicSQL() throws SQLException {
        // OFFSET and FETCH with dynamic SQL construction
        String baseQuery = "SELECT * FROM users ORDER BY id OFFSET ";
        String offset = "10";
        String fetch = " ROWS FETCH NEXT 10 ROWS ONLY";
        
        String dynamicQuery = baseQuery + offset + fetch;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithTransaction() throws SQLException {
        // OFFSET and FETCH with transaction
        String transactionWithOffsetFetch = "BEGIN TRANSACTION; SELECT * FROM users ORDER BY id OFFSET 0 ROWS FETCH NEXT 5 ROWS ONLY; INSERT INTO user_activity_log (action, timestamp) VALUES ('PAGINATION_QUERY', GETDATE()); COMMIT TRANSACTION";
        String transactionWithOffsetFetchRollback = "BEGIN TRANSACTION; SELECT * FROM products ORDER BY name OFFSET 5 ROWS FETCH NEXT 10 ROWS ONLY; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products found' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products retrieved' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(transactionWithOffsetFetch);
        rs1.close();
        
        boolean hasResult = stmt.execute(transactionWithOffsetFetchRollback);
        if (hasResult) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithBusinessLogic() throws SQLException {
        // OFFSET and FETCH with business logic
        String businessLogicWithOffsetFetch = "SELECT name, CASE WHEN status = 'active' THEN 'Active User' ELSE 'Inactive User' END as user_status FROM users ORDER BY name OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
        String businessLogicComplex = "SELECT order_number, CASE WHEN total_amount > 5000 THEN 'High Value' WHEN total_amount > 1000 THEN 'Medium Value' ELSE 'Low Value' END as order_value FROM orders ORDER BY total_amount DESC OFFSET 0 ROWS FETCH NEXT 20 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithOffsetFetch);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void offsetFetchWithPagination() throws SQLException {
        // OFFSET and FETCH with pagination scenarios
        String paginationPage1 = "SELECT * FROM users ORDER BY id OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY";
        String paginationPage2 = "SELECT * FROM users ORDER BY id OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY";
        String paginationPage3 = "SELECT * FROM users ORDER BY id OFFSET 20 ROWS FETCH NEXT 10 ROWS ONLY";
        String paginationPage4 = "SELECT * FROM users ORDER BY id OFFSET 30 ROWS FETCH NEXT 10 ROWS ONLY";
        String paginationPage5 = "SELECT * FROM users ORDER BY id OFFSET 40 ROWS FETCH NEXT 10 ROWS ONLY";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(paginationPage1);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(paginationPage2);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(paginationPage3);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(paginationPage4);
        rs4.close();
        
        ResultSet rs5 = stmt.executeQuery(paginationPage5);
        rs5.close();
        
        stmt.close();
        conn.close();
    }
}
