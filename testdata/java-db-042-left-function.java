package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-042: LeftFunctionUsage
 * Detects "LEFT(" string function usage
 */
public class LeftFunctionUsageExample {
    
    public void basicLeftFunctionUsage() throws SQLException {
        // Basic LEFT function usage
        String leftFunction = "SELECT LEFT(name, 10) as name_prefix FROM users";
        String leftFunctionWithLength = "SELECT LEFT(email, 5) as email_prefix FROM users";
        String leftFunctionWithVariable = "SELECT LEFT(description, 20) as short_description FROM products";
        String leftFunctionWithColumn = "SELECT LEFT(phone_number, 3) as area_code FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(leftFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(leftFunctionWithLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(leftFunctionWithVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(leftFunctionWithColumn);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInWhereClauses() throws SQLException {
        // LEFT function in WHERE clauses
        String whereWithLeft = "SELECT * FROM users WHERE LEFT(name, 1) = 'A'";
        String whereWithLeftLength = "SELECT * FROM products WHERE LEFT(name, 3) = 'Pro'";
        String whereWithLeftPattern = "SELECT * FROM contacts WHERE LEFT(phone_number, 3) = '555'";
        String whereWithLeftComparison = "SELECT * FROM orders WHERE LEFT(order_number, 2) = 'OR'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithLeftLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithLeftPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithLeftComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInOrderByClauses() throws SQLException {
        // LEFT function in ORDER BY clauses
        String orderByWithLeft = "SELECT * FROM users ORDER BY LEFT(name, 1) ASC";
        String orderByWithLeftLength = "SELECT * FROM products ORDER BY LEFT(name, 3) DESC";
        String orderByWithLeftPattern = "SELECT * FROM contacts ORDER BY LEFT(phone_number, 3) ASC";
        String orderByWithLeftComparison = "SELECT * FROM orders ORDER BY LEFT(order_number, 2) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithLeftLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithLeftPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithLeftComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInGroupByClauses() throws SQLException {
        // LEFT function in GROUP BY clauses
        String groupByWithLeft = "SELECT LEFT(name, 1) as first_letter, COUNT(*) as user_count FROM users GROUP BY LEFT(name, 1)";
        String groupByWithLeftLength = "SELECT LEFT(name, 3) as name_prefix, COUNT(*) as product_count FROM products GROUP BY LEFT(name, 3)";
        String groupByWithLeftPattern = "SELECT LEFT(phone_number, 3) as area_code, COUNT(*) as contact_count FROM contacts GROUP BY LEFT(phone_number, 3)";
        String groupByWithLeftComparison = "SELECT LEFT(order_number, 2) as order_prefix, COUNT(*) as order_count FROM orders GROUP BY LEFT(order_number, 2)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithLeftLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithLeftPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithLeftComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInCaseStatements() throws SQLException {
        // LEFT function in CASE statements
        String caseWithLeft = "SELECT name, CASE WHEN LEFT(name, 1) = 'A' THEN 'A Group' WHEN LEFT(name, 1) = 'B' THEN 'B Group' ELSE 'Other Group' END as name_group FROM users";
        String caseWithLeftLength = "SELECT product_name, CASE WHEN LEFT(name, 3) = 'Pro' THEN 'Professional' WHEN LEFT(name, 3) = 'Ent' THEN 'Enterprise' ELSE 'Standard' END as product_type FROM products";
        String caseWithLeftPattern = "SELECT phone_number, CASE WHEN LEFT(phone_number, 3) = '555' THEN 'Local' WHEN LEFT(phone_number, 3) = '800' THEN 'Toll Free' ELSE 'Other' END as phone_type FROM contacts";
        String caseWithLeftComplex = "SELECT order_number, CASE WHEN LEFT(order_number, 2) = 'OR' THEN 'Regular Order' WHEN LEFT(order_number, 2) = 'SP' THEN 'Special Order' WHEN LEFT(order_number, 2) = 'RT' THEN 'Return Order' ELSE 'Unknown' END as order_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithLeftLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithLeftPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithLeftComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInSubqueries() throws SQLException {
        // LEFT function in subqueries
        String subqueryWithLeft = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE LEFT(order_number, 2) = 'OR')";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND LEFT(order_number, 2) = 'OR') as regular_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND LEFT(order_number, 2) = 'SP')";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND LEFT(order_number, 2) = 'RT')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithLeft);
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
    
    public void leftFunctionInJoins() throws SQLException {
        // LEFT function in JOINs
        String joinWithLeft = "SELECT u.name, o.order_number, LEFT(o.order_number, 2) as order_type FROM users u JOIN orders o ON u.id = o.user_id WHERE LEFT(o.order_number, 2) = 'OR'";
        String joinWithLeftCondition = "SELECT p.name, c.category_name, LEFT(p.name, 3) as product_prefix FROM products p JOIN categories c ON p.category_id = c.id WHERE LEFT(p.name, 3) = 'Pro'";
        String joinWithLeftCalculation = "SELECT u.name, COUNT(o.id) as order_count, LEFT(u.name, 1) as first_letter FROM users u LEFT JOIN orders o ON u.id = o.user_id AND LEFT(o.order_number, 2) = 'OR' GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithLeftCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithLeftCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInAggregates() throws SQLException {
        // LEFT function in aggregate functions
        String aggregateWithLeft = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN LEFT(name, 1) = 'A' THEN 1 END) as a_users FROM users";
        String sumWithLeft = "SELECT SUM(CASE WHEN LEFT(order_number, 2) = 'OR' THEN total_amount ELSE 0 END) as regular_order_total FROM orders";
        String avgWithLeft = "SELECT AVG(CASE WHEN LEFT(product_name, 3) = 'Pro' THEN price ELSE NULL END) as pro_product_avg_price FROM products";
        String maxWithLeft = "SELECT MAX(CASE WHEN LEFT(phone_number, 3) = '555' THEN LEN(phone_number) ELSE 0 END) as max_local_phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithLeft);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithLeft);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithLeft);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInUpdateStatements() throws SQLException {
        // LEFT function in UPDATE statements
        String updateWithLeft = "UPDATE users SET name_prefix = LEFT(name, 3) WHERE name IS NOT NULL";
        String updateWithLeftCondition = "UPDATE products SET category_code = LEFT(name, 3) WHERE name IS NOT NULL";
        String updateWithLeftCalculation = "UPDATE contacts SET area_code = LEFT(phone_number, 3) WHERE phone_number IS NOT NULL";
        String updateWithLeftComplex = "UPDATE orders SET order_type = LEFT(order_number, 2) WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithLeft);
        int result2 = stmt.executeUpdate(updateWithLeftCondition);
        int result3 = stmt.executeUpdate(updateWithLeftCalculation);
        int result4 = stmt.executeUpdate(updateWithLeftComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInInsertStatements() throws SQLException {
        // LEFT function in INSERT statements
        String insertWithLeft = "INSERT INTO user_prefixes (user_id, name_prefix) SELECT id, LEFT(name, 3) FROM users WHERE name IS NOT NULL";
        String insertWithLeftCondition = "INSERT INTO product_codes (product_id, product_code) SELECT id, LEFT(name, 3) FROM products WHERE name IS NOT NULL";
        String insertWithLeftCalculation = "INSERT INTO contact_areas (contact_id, area_code) SELECT id, LEFT(phone_number, 3) FROM contacts WHERE phone_number IS NOT NULL";
        String insertWithLeftComplex = "INSERT INTO order_types (order_id, order_type) SELECT id, LEFT(order_number, 2) FROM orders WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithLeft);
        int result2 = stmt.executeUpdate(insertWithLeftCondition);
        int result3 = stmt.executeUpdate(insertWithLeftCalculation);
        int result4 = stmt.executeUpdate(insertWithLeftComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionInPreparedStatement() throws SQLException {
        // LEFT function in PreparedStatement
        String selectWithLeftParam = "SELECT * FROM users WHERE LEFT(name, ?) = ?";
        String selectWithLeftParamCondition = "SELECT * FROM products WHERE LEFT(name, ?) = ?";
        String selectWithLeftParamCalculation = "SELECT LEFT(name, ?) as name_prefix FROM users WHERE name IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithLeftParam);
        pstmt1.setInt(1, 3);
        pstmt1.setString(2, "Pro");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithLeftParamCondition);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "A");
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithLeftParamCalculation);
        pstmt3.setInt(1, 5);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void leftFunctionInMethods() throws SQLException {
        // LEFT function in methods
        String userQuery = getUserQueryWithLeft();
        String orderQuery = getOrderQueryWithLeft();
        String productQuery = getProductQueryWithLeft();
        
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
    
    private String getUserQueryWithLeft() {
        return "SELECT name, LEFT(name, 3) as name_prefix FROM users WHERE LEFT(name, 1) = 'A'";
    }
    
    private String getOrderQueryWithLeft() {
        return "SELECT order_number, LEFT(order_number, 2) as order_type FROM orders WHERE LEFT(order_number, 2) = 'OR'";
    }
    
    private String getProductQueryWithLeft() {
        return "SELECT name, LEFT(name, 3) as product_code FROM products WHERE LEFT(name, 3) = 'Pro'";
    }
    
    public void leftFunctionWithDynamicSQL() throws SQLException {
        // LEFT function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE LEFT(name, ";
        String length = "3";
        String condition = ") = 'Pro'";
        
        String dynamicQuery = baseQuery + length + condition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionWithTransaction() throws SQLException {
        // LEFT function with transaction
        String transactionWithLeft = "BEGIN TRANSACTION; UPDATE users SET name_prefix = LEFT(name, 3) WHERE name IS NOT NULL; INSERT INTO user_activity_log (action, timestamp) VALUES ('NAME_PREFIX_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithLeftRollback = "BEGIN TRANSACTION; UPDATE products SET category_code = LEFT(name, 3) WHERE name IS NOT NULL; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithLeft);
        
        boolean hasResult = stmt.execute(transactionWithLeftRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionWithBusinessLogic() throws SQLException {
        // LEFT function with business logic
        String businessLogicWithLeft = "SELECT name, LEFT(name, 1) as first_letter, CASE WHEN LEFT(name, 1) = 'A' THEN 'A Group' WHEN LEFT(name, 1) = 'B' THEN 'B Group' ELSE 'Other Group' END as name_group FROM users";
        String businessLogicComplex = "SELECT order_number, LEFT(order_number, 2) as order_type, CASE WHEN LEFT(order_number, 2) = 'OR' THEN 'Regular Order' WHEN LEFT(order_number, 2) = 'SP' THEN 'Special Order' WHEN LEFT(order_number, 2) = 'RT' THEN 'Return Order' ELSE 'Unknown' END as order_category FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void leftFunctionWithStringManipulation() throws SQLException {
        // LEFT function with string manipulation
        String stringManipulationWithLeft = "SELECT name, LEFT(name, 3) as prefix, LEN(name) as name_length, LEFT(name, 3) + '...' as truncated_name FROM users";
        String stringManipulationComplex = "SELECT product_name, LEFT(name, 5) as short_name, UPPER(LEFT(name, 3)) as upper_prefix, LEFT(name, 3) + '_' + RIGHT(name, 3) as formatted_name FROM products";
        String stringManipulationAdvanced = "SELECT phone_number, LEFT(phone_number, 3) as area_code, SUBSTRING(phone_number, 4, 3) as exchange, LEFT(phone_number, 3) + '-' + SUBSTRING(phone_number, 4, 3) + '-' + RIGHT(phone_number, 4) as formatted_phone FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringManipulationWithLeft);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(stringManipulationComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(stringManipulationAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
