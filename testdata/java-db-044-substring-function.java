package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-044: SubstringFunctionUsage
 * Detects "SUBSTRING(" function usage
 */
public class SubstringFunctionUsageExample {
    
    public void basicSubstringFunctionUsage() throws SQLException {
        // Basic SUBSTRING function usage
        String substringFunction = "SELECT SUBSTRING(name, 1, 5) as name_start FROM users";
        String substringFunctionWithLength = "SELECT SUBSTRING(email, 1, 10) as email_start FROM users";
        String substringFunctionWithVariable = "SELECT SUBSTRING(description, 1, 20) as short_description FROM products";
        String substringFunctionWithColumn = "SELECT SUBSTRING(phone_number, 1, 3) as area_code FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(substringFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(substringFunctionWithLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(substringFunctionWithVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(substringFunctionWithColumn);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInWhereClauses() throws SQLException {
        // SUBSTRING function in WHERE clauses
        String whereWithSubstring = "SELECT * FROM users WHERE SUBSTRING(name, 1, 1) = 'A'";
        String whereWithSubstringLength = "SELECT * FROM products WHERE SUBSTRING(name, 1, 3) = 'Pro'";
        String whereWithSubstringPattern = "SELECT * FROM contacts WHERE SUBSTRING(phone_number, 1, 3) = '555'";
        String whereWithSubstringComparison = "SELECT * FROM orders WHERE SUBSTRING(order_number, 1, 2) = 'OR'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithSubstringLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithSubstringPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithSubstringComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInOrderByClauses() throws SQLException {
        // SUBSTRING function in ORDER BY clauses
        String orderByWithSubstring = "SELECT * FROM users ORDER BY SUBSTRING(name, 1, 1) ASC";
        String orderByWithSubstringLength = "SELECT * FROM products ORDER BY SUBSTRING(name, 1, 3) DESC";
        String orderByWithSubstringPattern = "SELECT * FROM contacts ORDER BY SUBSTRING(phone_number, 1, 3) ASC";
        String orderByWithSubstringComparison = "SELECT * FROM orders ORDER BY SUBSTRING(order_number, 1, 2) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithSubstringLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithSubstringPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithSubstringComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInGroupByClauses() throws SQLException {
        // SUBSTRING function in GROUP BY clauses
        String groupByWithSubstring = "SELECT SUBSTRING(name, 1, 1) as first_letter, COUNT(*) as user_count FROM users GROUP BY SUBSTRING(name, 1, 1)";
        String groupByWithSubstringLength = "SELECT SUBSTRING(name, 1, 3) as name_prefix, COUNT(*) as product_count FROM products GROUP BY SUBSTRING(name, 1, 3)";
        String groupByWithSubstringPattern = "SELECT SUBSTRING(phone_number, 1, 3) as area_code, COUNT(*) as contact_count FROM contacts GROUP BY SUBSTRING(phone_number, 1, 3)";
        String groupByWithSubstringComparison = "SELECT SUBSTRING(order_number, 1, 2) as order_prefix, COUNT(*) as order_count FROM orders GROUP BY SUBSTRING(order_number, 1, 2)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithSubstringLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithSubstringPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithSubstringComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInCaseStatements() throws SQLException {
        // SUBSTRING function in CASE statements
        String caseWithSubstring = "SELECT name, CASE WHEN SUBSTRING(name, 1, 1) = 'A' THEN 'A Group' WHEN SUBSTRING(name, 1, 1) = 'B' THEN 'B Group' ELSE 'Other Group' END as name_group FROM users";
        String caseWithSubstringLength = "SELECT product_name, CASE WHEN SUBSTRING(name, 1, 3) = 'Pro' THEN 'Professional' WHEN SUBSTRING(name, 1, 3) = 'Ent' THEN 'Enterprise' ELSE 'Standard' END as product_type FROM products";
        String caseWithSubstringPattern = "SELECT phone_number, CASE WHEN SUBSTRING(phone_number, 1, 3) = '555' THEN 'Local' WHEN SUBSTRING(phone_number, 1, 3) = '800' THEN 'Toll Free' ELSE 'Other' END as phone_type FROM contacts";
        String caseWithSubstringComplex = "SELECT order_number, CASE WHEN SUBSTRING(order_number, 1, 2) = 'OR' THEN 'Regular Order' WHEN SUBSTRING(order_number, 1, 2) = 'SP' THEN 'Special Order' WHEN SUBSTRING(order_number, 1, 2) = 'RT' THEN 'Return Order' ELSE 'Unknown' END as order_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithSubstringLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithSubstringPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithSubstringComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInSubqueries() throws SQLException {
        // SUBSTRING function in subqueries
        String subqueryWithSubstring = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE SUBSTRING(order_number, 1, 2) = 'OR')";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND SUBSTRING(order_number, 1, 2) = 'OR') as regular_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND SUBSTRING(order_number, 1, 2) = 'SP')";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND SUBSTRING(order_number, 1, 2) = 'RT')";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithSubstring);
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
    
    public void substringFunctionInJoins() throws SQLException {
        // SUBSTRING function in JOINs
        String joinWithSubstring = "SELECT u.name, o.order_number, SUBSTRING(o.order_number, 1, 2) as order_type FROM users u JOIN orders o ON u.id = o.user_id WHERE SUBSTRING(o.order_number, 1, 2) = 'OR'";
        String joinWithSubstringCondition = "SELECT p.name, c.category_name, SUBSTRING(p.name, 1, 3) as product_prefix FROM products p JOIN categories c ON p.category_id = c.id WHERE SUBSTRING(p.name, 1, 3) = 'Pro'";
        String joinWithSubstringCalculation = "SELECT u.name, COUNT(o.id) as order_count, SUBSTRING(u.name, 1, 1) as first_letter FROM users u LEFT JOIN orders o ON u.id = o.user_id AND SUBSTRING(o.order_number, 1, 2) = 'OR' GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithSubstringCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithSubstringCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInAggregates() throws SQLException {
        // SUBSTRING function in aggregate functions
        String aggregateWithSubstring = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN SUBSTRING(name, 1, 1) = 'A' THEN 1 END) as a_users FROM users";
        String sumWithSubstring = "SELECT SUM(CASE WHEN SUBSTRING(order_number, 1, 2) = 'OR' THEN total_amount ELSE 0 END) as regular_order_total FROM orders";
        String avgWithSubstring = "SELECT AVG(CASE WHEN SUBSTRING(product_name, 1, 3) = 'Pro' THEN price ELSE NULL END) as pro_product_avg_price FROM products";
        String maxWithSubstring = "SELECT MAX(CASE WHEN SUBSTRING(phone_number, 1, 3) = '555' THEN LEN(phone_number) ELSE 0 END) as max_local_phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithSubstring);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithSubstring);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithSubstring);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInUpdateStatements() throws SQLException {
        // SUBSTRING function in UPDATE statements
        String updateWithSubstring = "UPDATE users SET name_prefix = SUBSTRING(name, 1, 3) WHERE name IS NOT NULL";
        String updateWithSubstringCondition = "UPDATE products SET category_code = SUBSTRING(name, 1, 3) WHERE name IS NOT NULL";
        String updateWithSubstringCalculation = "UPDATE contacts SET area_code = SUBSTRING(phone_number, 1, 3) WHERE phone_number IS NOT NULL";
        String updateWithSubstringComplex = "UPDATE orders SET order_type = SUBSTRING(order_number, 1, 2) WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithSubstring);
        int result2 = stmt.executeUpdate(updateWithSubstringCondition);
        int result3 = stmt.executeUpdate(updateWithSubstringCalculation);
        int result4 = stmt.executeUpdate(updateWithSubstringComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInInsertStatements() throws SQLException {
        // SUBSTRING function in INSERT statements
        String insertWithSubstring = "INSERT INTO user_prefixes (user_id, name_prefix) SELECT id, SUBSTRING(name, 1, 3) FROM users WHERE name IS NOT NULL";
        String insertWithSubstringCondition = "INSERT INTO product_codes (product_id, product_code) SELECT id, SUBSTRING(name, 1, 3) FROM products WHERE name IS NOT NULL";
        String insertWithSubstringCalculation = "INSERT INTO contact_areas (contact_id, area_code) SELECT id, SUBSTRING(phone_number, 1, 3) FROM contacts WHERE phone_number IS NOT NULL";
        String insertWithSubstringComplex = "INSERT INTO order_types (order_id, order_type) SELECT id, SUBSTRING(order_number, 1, 2) FROM orders WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithSubstring);
        int result2 = stmt.executeUpdate(insertWithSubstringCondition);
        int result3 = stmt.executeUpdate(insertWithSubstringCalculation);
        int result4 = stmt.executeUpdate(insertWithSubstringComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionInPreparedStatement() throws SQLException {
        // SUBSTRING function in PreparedStatement
        String selectWithSubstringParam = "SELECT * FROM users WHERE SUBSTRING(name, 1, ?) = ?";
        String selectWithSubstringParamCondition = "SELECT * FROM products WHERE SUBSTRING(name, 1, ?) = ?";
        String selectWithSubstringParamCalculation = "SELECT SUBSTRING(name, 1, ?) as name_prefix FROM users WHERE name IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithSubstringParam);
        pstmt1.setInt(1, 3);
        pstmt1.setString(2, "Pro");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithSubstringParamCondition);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "A");
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithSubstringParamCalculation);
        pstmt3.setInt(1, 5);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void substringFunctionInMethods() throws SQLException {
        // SUBSTRING function in methods
        String userQuery = getUserQueryWithSubstring();
        String orderQuery = getOrderQueryWithSubstring();
        String productQuery = getProductQueryWithSubstring();
        
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
    
    private String getUserQueryWithSubstring() {
        return "SELECT name, SUBSTRING(name, 1, 3) as name_prefix FROM users WHERE SUBSTRING(name, 1, 1) = 'A'";
    }
    
    private String getOrderQueryWithSubstring() {
        return "SELECT order_number, SUBSTRING(order_number, 1, 2) as order_type FROM orders WHERE SUBSTRING(order_number, 1, 2) = 'OR'";
    }
    
    private String getProductQueryWithSubstring() {
        return "SELECT name, SUBSTRING(name, 1, 3) as product_code FROM products WHERE SUBSTRING(name, 1, 3) = 'Pro'";
    }
    
    public void substringFunctionWithDynamicSQL() throws SQLException {
        // SUBSTRING function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE SUBSTRING(name, 1, ";
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
    
    public void substringFunctionWithTransaction() throws SQLException {
        // SUBSTRING function with transaction
        String transactionWithSubstring = "BEGIN TRANSACTION; UPDATE users SET name_prefix = SUBSTRING(name, 1, 3) WHERE name IS NOT NULL; INSERT INTO user_activity_log (action, timestamp) VALUES ('NAME_PREFIX_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithSubstringRollback = "BEGIN TRANSACTION; UPDATE products SET category_code = SUBSTRING(name, 1, 3) WHERE name IS NOT NULL; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithSubstring);
        
        boolean hasResult = stmt.execute(transactionWithSubstringRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionWithBusinessLogic() throws SQLException {
        // SUBSTRING function with business logic
        String businessLogicWithSubstring = "SELECT name, SUBSTRING(name, 1, 1) as first_letter, CASE WHEN SUBSTRING(name, 1, 1) = 'A' THEN 'A Group' WHEN SUBSTRING(name, 1, 1) = 'B' THEN 'B Group' ELSE 'Other Group' END as name_group FROM users";
        String businessLogicComplex = "SELECT order_number, SUBSTRING(order_number, 1, 2) as order_type, CASE WHEN SUBSTRING(order_number, 1, 2) = 'OR' THEN 'Regular Order' WHEN SUBSTRING(order_number, 1, 2) = 'SP' THEN 'Special Order' WHEN SUBSTRING(order_number, 1, 2) = 'RT' THEN 'Return Order' ELSE 'Unknown' END as order_category FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void substringFunctionWithStringManipulation() throws SQLException {
        // SUBSTRING function with string manipulation
        String stringManipulationWithSubstring = "SELECT name, SUBSTRING(name, 1, 3) as prefix, LEN(name) as name_length, SUBSTRING(name, 1, 3) + '...' as truncated_name FROM users";
        String stringManipulationComplex = "SELECT product_name, SUBSTRING(name, 1, 5) as short_name, UPPER(SUBSTRING(name, 1, 3)) as upper_prefix, SUBSTRING(name, 1, 3) + '_' + SUBSTRING(name, LEN(name)-2, 3) as formatted_name FROM products";
        String stringManipulationAdvanced = "SELECT phone_number, SUBSTRING(phone_number, 1, 3) as area_code, SUBSTRING(phone_number, 4, 3) as exchange, SUBSTRING(phone_number, 7, 4) as last_four, SUBSTRING(phone_number, 1, 3) + '-' + SUBSTRING(phone_number, 4, 3) + '-' + SUBSTRING(phone_number, 7, 4) as formatted_phone FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringManipulationWithSubstring);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(stringManipulationComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(stringManipulationAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
