package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-043: RightFunctionUsage
 * Detects "RIGHT(" string function usage
 */
public class RightFunctionUsageExample {
    
    public void basicRightFunctionUsage() throws SQLException {
        // Basic RIGHT function usage
        String rightFunction = "SELECT RIGHT(name, 5) as name_suffix FROM users";
        String rightFunctionWithLength = "SELECT RIGHT(email, 10) as email_suffix FROM users";
        String rightFunctionWithVariable = "SELECT RIGHT(description, 15) as short_description FROM products";
        String rightFunctionWithColumn = "SELECT RIGHT(phone_number, 4) as last_four_digits FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(rightFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(rightFunctionWithLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(rightFunctionWithVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(rightFunctionWithColumn);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInWhereClauses() throws SQLException {
        // RIGHT function in WHERE clauses
        String whereWithRight = "SELECT * FROM users WHERE RIGHT(name, 1) = 'n'";
        String whereWithRightLength = "SELECT * FROM products WHERE RIGHT(name, 3) = 'Pro'";
        String whereWithRightPattern = "SELECT * FROM contacts WHERE RIGHT(phone_number, 4) = '1234'";
        String whereWithRightComparison = "SELECT * FROM orders WHERE RIGHT(order_number, 2) = '01'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithRightLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithRightPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithRightComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInOrderByClauses() throws SQLException {
        // RIGHT function in ORDER BY clauses
        String orderByWithRight = "SELECT * FROM users ORDER BY RIGHT(name, 1) ASC";
        String orderByWithRightLength = "SELECT * FROM products ORDER BY RIGHT(name, 3) DESC";
        String orderByWithRightPattern = "SELECT * FROM contacts ORDER BY RIGHT(phone_number, 4) ASC";
        String orderByWithRightComparison = "SELECT * FROM orders ORDER BY RIGHT(order_number, 2) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithRightLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithRightPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithRightComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInGroupByClauses() throws SQLException {
        // RIGHT function in GROUP BY clauses
        String groupByWithRight = "SELECT RIGHT(name, 1) as last_letter, COUNT(*) as user_count FROM users GROUP BY RIGHT(name, 1)";
        String groupByWithRightLength = "SELECT RIGHT(name, 3) as name_suffix, COUNT(*) as product_count FROM products GROUP BY RIGHT(name, 3)";
        String groupByWithRightPattern = "SELECT RIGHT(phone_number, 4) as last_four, COUNT(*) as contact_count FROM contacts GROUP BY RIGHT(phone_number, 4)";
        String groupByWithRightComparison = "SELECT RIGHT(order_number, 2) as order_suffix, COUNT(*) as order_count FROM orders GROUP BY RIGHT(order_number, 2)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithRightLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithRightPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithRightComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInCaseStatements() throws SQLException {
        // RIGHT function in CASE statements
        String caseWithRight = "SELECT name, CASE WHEN RIGHT(name, 1) = 'n' THEN 'Ends with N' WHEN RIGHT(name, 1) = 's' THEN 'Ends with S' ELSE 'Other Ending' END as name_ending FROM users";
        String caseWithRightLength = "SELECT product_name, CASE WHEN RIGHT(name, 3) = 'Pro' THEN 'Professional' WHEN RIGHT(name, 3) = 'Ent' THEN 'Enterprise' ELSE 'Standard' END as product_type FROM products";
        String caseWithRightPattern = "SELECT phone_number, CASE WHEN RIGHT(phone_number, 4) = '0000' THEN 'Round Number' WHEN RIGHT(phone_number, 4) = '1234' THEN 'Sequential' ELSE 'Regular' END as phone_type FROM contacts";
        String caseWithRightComplex = "SELECT order_number, CASE WHEN RIGHT(order_number, 2) = '01' THEN 'First Order' WHEN RIGHT(order_number, 2) = '99' THEN 'Last Order' ELSE 'Regular Order' END as order_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithRightLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithRightPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithRightComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInSubqueries() throws SQLException {
        // RIGHT function in subqueries
        String subqueryWithRight = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE RIGHT(order_number, 2) = '01')";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND RIGHT(order_number, 2) = '01') as first_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND RIGHT(order_number, 2) = '99')";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND RIGHT(order_number, 2) = 'RT')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithRight);
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
    
    public void rightFunctionInJoins() throws SQLException {
        // RIGHT function in JOINs
        String joinWithRight = "SELECT u.name, o.order_number, RIGHT(o.order_number, 2) as order_suffix FROM users u JOIN orders o ON u.id = o.user_id WHERE RIGHT(o.order_number, 2) = '01'";
        String joinWithRightCondition = "SELECT p.name, c.category_name, RIGHT(p.name, 3) as product_suffix FROM products p JOIN categories c ON p.category_id = c.id WHERE RIGHT(p.name, 3) = 'Pro'";
        String joinWithRightCalculation = "SELECT u.name, COUNT(o.id) as order_count, RIGHT(u.name, 1) as last_letter FROM users u LEFT JOIN orders o ON u.id = o.user_id AND RIGHT(o.order_number, 2) = '01' GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithRightCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithRightCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInAggregates() throws SQLException {
        // RIGHT function in aggregate functions
        String aggregateWithRight = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN RIGHT(name, 1) = 'n' THEN 1 END) as n_users FROM users";
        String sumWithRight = "SELECT SUM(CASE WHEN RIGHT(order_number, 2) = '01' THEN total_amount ELSE 0 END) as first_order_total FROM orders";
        String avgWithRight = "SELECT AVG(CASE WHEN RIGHT(product_name, 3) = 'Pro' THEN price ELSE NULL END) as pro_product_avg_price FROM products";
        String maxWithRight = "SELECT MAX(CASE WHEN RIGHT(phone_number, 4) = '0000' THEN LEN(phone_number) ELSE 0 END) as max_round_phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithRight);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithRight);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithRight);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInUpdateStatements() throws SQLException {
        // RIGHT function in UPDATE statements
        String updateWithRight = "UPDATE users SET name_suffix = RIGHT(name, 3) WHERE name IS NOT NULL";
        String updateWithRightCondition = "UPDATE products SET category_suffix = RIGHT(name, 3) WHERE name IS NOT NULL";
        String updateWithRightCalculation = "UPDATE contacts SET last_four = RIGHT(phone_number, 4) WHERE phone_number IS NOT NULL";
        String updateWithRightComplex = "UPDATE orders SET order_suffix = RIGHT(order_number, 2) WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithRight);
        int result2 = stmt.executeUpdate(updateWithRightCondition);
        int result3 = stmt.executeUpdate(updateWithRightCalculation);
        int result4 = stmt.executeUpdate(updateWithRightComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInInsertStatements() throws SQLException {
        // RIGHT function in INSERT statements
        String insertWithRight = "INSERT INTO user_suffixes (user_id, name_suffix) SELECT id, RIGHT(name, 3) FROM users WHERE name IS NOT NULL";
        String insertWithRightCondition = "INSERT INTO product_suffixes (product_id, product_suffix) SELECT id, RIGHT(name, 3) FROM products WHERE name IS NOT NULL";
        String insertWithRightCalculation = "INSERT INTO contact_suffixes (contact_id, last_four) SELECT id, RIGHT(phone_number, 4) FROM contacts WHERE phone_number IS NOT NULL";
        String insertWithRightComplex = "INSERT INTO order_suffixes (order_id, order_suffix) SELECT id, RIGHT(order_number, 2) FROM orders WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithRight);
        int result2 = stmt.executeUpdate(insertWithRightCondition);
        int result3 = stmt.executeUpdate(insertWithRightCalculation);
        int result4 = stmt.executeUpdate(insertWithRightComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionInPreparedStatement() throws SQLException {
        // RIGHT function in PreparedStatement
        String selectWithRightParam = "SELECT * FROM users WHERE RIGHT(name, ?) = ?";
        String selectWithRightParamCondition = "SELECT * FROM products WHERE RIGHT(name, ?) = ?";
        String selectWithRightParamCalculation = "SELECT RIGHT(name, ?) as name_suffix FROM users WHERE name IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithRightParam);
        pstmt1.setInt(1, 3);
        pstmt1.setString(2, "Pro");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithRightParamCondition);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "n");
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithRightParamCalculation);
        pstmt3.setInt(1, 5);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void rightFunctionInMethods() throws SQLException {
        // RIGHT function in methods
        String userQuery = getUserQueryWithRight();
        String orderQuery = getOrderQueryWithRight();
        String productQuery = getProductQueryWithRight();
        
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
    
    private String getUserQueryWithRight() {
        return "SELECT name, RIGHT(name, 3) as name_suffix FROM users WHERE RIGHT(name, 1) = 'n'";
    }
    
    private String getOrderQueryWithRight() {
        return "SELECT order_number, RIGHT(order_number, 2) as order_suffix FROM orders WHERE RIGHT(order_number, 2) = '01'";
    }
    
    private String getProductQueryWithRight() {
        return "SELECT name, RIGHT(name, 3) as product_suffix FROM products WHERE RIGHT(name, 3) = 'Pro'";
    }
    
    public void rightFunctionWithDynamicSQL() throws SQLException {
        // RIGHT function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE RIGHT(name, ";
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
    
    public void rightFunctionWithTransaction() throws SQLException {
        // RIGHT function with transaction
        String transactionWithRight = "BEGIN TRANSACTION; UPDATE users SET name_suffix = RIGHT(name, 3) WHERE name IS NOT NULL; INSERT INTO user_activity_log (action, timestamp) VALUES ('NAME_SUFFIX_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithRightRollback = "BEGIN TRANSACTION; UPDATE products SET category_suffix = RIGHT(name, 3) WHERE name IS NOT NULL; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithRight);
        
        boolean hasResult = stmt.execute(transactionWithRightRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionWithBusinessLogic() throws SQLException {
        // RIGHT function with business logic
        String businessLogicWithRight = "SELECT name, RIGHT(name, 1) as last_letter, CASE WHEN RIGHT(name, 1) = 'n' THEN 'Ends with N' WHEN RIGHT(name, 1) = 's' THEN 'Ends with S' ELSE 'Other Ending' END as name_ending FROM users";
        String businessLogicComplex = "SELECT order_number, RIGHT(order_number, 2) as order_suffix, CASE WHEN RIGHT(order_number, 2) = '01' THEN 'First Order' WHEN RIGHT(order_number, 2) = '99' THEN 'Last Order' ELSE 'Regular Order' END as order_category FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void rightFunctionWithStringManipulation() throws SQLException {
        // RIGHT function with string manipulation
        String stringManipulationWithRight = "SELECT name, RIGHT(name, 3) as suffix, LEN(name) as name_length, '...' + RIGHT(name, 3) as truncated_name FROM users";
        String stringManipulationComplex = "SELECT product_name, RIGHT(name, 5) as short_name, UPPER(RIGHT(name, 3)) as upper_suffix, LEFT(name, 3) + '_' + RIGHT(name, 3) as formatted_name FROM products";
        String stringManipulationAdvanced = "SELECT phone_number, LEFT(phone_number, 3) as area_code, SUBSTRING(phone_number, 4, 3) as exchange, RIGHT(phone_number, 4) as last_four, LEFT(phone_number, 3) + '-' + SUBSTRING(phone_number, 4, 3) + '-' + RIGHT(phone_number, 4) as formatted_phone FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringManipulationWithRight);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(stringManipulationComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(stringManipulationAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
