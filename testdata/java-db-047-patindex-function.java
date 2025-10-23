package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-047: PatIndexFunctionUsage
 * Detects "PATINDEX(" function usage
 */
public class PatIndexFunctionUsageExample {
    
    public void basicPatIndexFunctionUsage() throws SQLException {
        // Basic PATINDEX function usage
        String patIndexFunction = "SELECT PATINDEX('%@%', email) as at_position FROM users";
        String patIndexFunctionWithPattern = "SELECT PATINDEX('%Pro%', name) as pro_position FROM products";
        String patIndexFunctionWithColumn = "SELECT PATINDEX('%555%', phone_number) as area_code_position FROM contacts";
        String patIndexFunctionWithString = "SELECT PATINDEX('%OR%', order_number) as order_type_position FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(patIndexFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(patIndexFunctionWithPattern);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(patIndexFunctionWithColumn);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(patIndexFunctionWithString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInWhereClauses() throws SQLException {
        // PATINDEX function in WHERE clauses
        String whereWithPatIndex = "SELECT * FROM users WHERE PATINDEX('%@%', email) > 0";
        String whereWithPatIndexPattern = "SELECT * FROM products WHERE PATINDEX('%Pro%', name) > 0";
        String whereWithPatIndexWildcard = "SELECT * FROM contacts WHERE PATINDEX('%555%', phone_number) > 0";
        String whereWithPatIndexComparison = "SELECT * FROM orders WHERE PATINDEX('%OR%', order_number) > 0";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithPatIndexPattern);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithPatIndexWildcard);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithPatIndexComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInOrderByClauses() throws SQLException {
        // PATINDEX function in ORDER BY clauses
        String orderByWithPatIndex = "SELECT * FROM users ORDER BY PATINDEX('%@%', email) ASC";
        String orderByWithPatIndexPattern = "SELECT * FROM products ORDER BY PATINDEX('%Pro%', name) DESC";
        String orderByWithPatIndexWildcard = "SELECT * FROM contacts ORDER BY PATINDEX('%555%', phone_number) ASC";
        String orderByWithPatIndexComparison = "SELECT * FROM orders ORDER BY PATINDEX('%OR%', order_number) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithPatIndexPattern);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithPatIndexWildcard);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithPatIndexComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInGroupByClauses() throws SQLException {
        // PATINDEX function in GROUP BY clauses
        String groupByWithPatIndex = "SELECT PATINDEX('%@%', email) as at_position, COUNT(*) as user_count FROM users GROUP BY PATINDEX('%@%', email)";
        String groupByWithPatIndexPattern = "SELECT PATINDEX('%Pro%', name) as pro_position, COUNT(*) as product_count FROM products GROUP BY PATINDEX('%Pro%', name)";
        String groupByWithPatIndexWildcard = "SELECT PATINDEX('%555%', phone_number) as area_code_position, COUNT(*) as contact_count FROM contacts GROUP BY PATINDEX('%555%', phone_number)";
        String groupByWithPatIndexComparison = "SELECT PATINDEX('%OR%', order_number) as order_type_position, COUNT(*) as order_count FROM orders GROUP BY PATINDEX('%OR%', order_number)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithPatIndexPattern);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithPatIndexWildcard);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithPatIndexComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInCaseStatements() throws SQLException {
        // PATINDEX function in CASE statements
        String caseWithPatIndex = "SELECT name, CASE WHEN PATINDEX('%@%', email) > 0 THEN 'Valid Email' ELSE 'Invalid Email' END as email_status FROM users";
        String caseWithPatIndexPattern = "SELECT product_name, CASE WHEN PATINDEX('%Pro%', name) > 0 THEN 'Professional' WHEN PATINDEX('%Ent%', name) > 0 THEN 'Enterprise' ELSE 'Standard' END as product_type FROM products";
        String caseWithPatIndexWildcard = "SELECT phone_number, CASE WHEN PATINDEX('%555%', phone_number) > 0 THEN 'Local' WHEN PATINDEX('%800%', phone_number) > 0 THEN 'Toll Free' ELSE 'Other' END as phone_type FROM contacts";
        String caseWithPatIndexComplex = "SELECT order_number, CASE WHEN PATINDEX('%OR%', order_number) > 0 THEN 'Regular Order' WHEN PATINDEX('%SP%', order_number) > 0 THEN 'Special Order' WHEN PATINDEX('%RT%', order_number) > 0 THEN 'Return Order' ELSE 'Unknown' END as order_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithPatIndexPattern);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithPatIndexWildcard);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithPatIndexComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInSubqueries() throws SQLException {
        // PATINDEX function in subqueries
        String subqueryWithPatIndex = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE PATINDEX('%OR%', order_number) > 0)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND PATINDEX('%OR%', order_number) > 0) as regular_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND PATINDEX('%SP%', order_number) > 0)";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND PATINDEX('%RT%', order_number) > 0)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithPatIndex);
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
    
    public void patIndexFunctionInJoins() throws SQLException {
        // PATINDEX function in JOINs
        String joinWithPatIndex = "SELECT u.name, o.order_number, PATINDEX('%OR%', o.order_number) as order_type_position FROM users u JOIN orders o ON u.id = o.user_id WHERE PATINDEX('%OR%', o.order_number) > 0";
        String joinWithPatIndexCondition = "SELECT p.name, c.category_name, PATINDEX('%Pro%', p.name) as product_prefix_position FROM products p JOIN categories c ON p.category_id = c.id WHERE PATINDEX('%Pro%', p.name) > 0";
        String joinWithPatIndexCalculation = "SELECT u.name, COUNT(o.id) as order_count, PATINDEX('%@%', u.email) as email_at_position FROM users u LEFT JOIN orders o ON u.id = o.user_id AND PATINDEX('%OR%', o.order_number) > 0 GROUP BY u.name, u.email";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithPatIndexCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithPatIndexCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInAggregates() throws SQLException {
        // PATINDEX function in aggregate functions
        String aggregateWithPatIndex = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN PATINDEX('%@%', email) > 0 THEN 1 END) as valid_email_users FROM users";
        String sumWithPatIndex = "SELECT SUM(CASE WHEN PATINDEX('%OR%', order_number) > 0 THEN total_amount ELSE 0 END) as regular_order_total FROM orders";
        String avgWithPatIndex = "SELECT AVG(CASE WHEN PATINDEX('%Pro%', name) > 0 THEN price ELSE NULL END) as pro_product_avg_price FROM products";
        String maxWithPatIndex = "SELECT MAX(CASE WHEN PATINDEX('%555%', phone_number) > 0 THEN LEN(phone_number) ELSE 0 END) as max_local_phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithPatIndex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithPatIndex);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithPatIndex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInUpdateStatements() throws SQLException {
        // PATINDEX function in UPDATE statements
        String updateWithPatIndex = "UPDATE users SET email_domain = SUBSTRING(email, PATINDEX('%@%', email) + 1, LEN(email)) WHERE PATINDEX('%@%', email) > 0";
        String updateWithPatIndexCondition = "UPDATE products SET product_type = CASE WHEN PATINDEX('%Pro%', name) > 0 THEN 'Professional' ELSE 'Standard' END WHERE name IS NOT NULL";
        String updateWithPatIndexCalculation = "UPDATE contacts SET phone_type = CASE WHEN PATINDEX('%555%', phone_number) > 0 THEN 'Local' ELSE 'Other' END WHERE phone_number IS NOT NULL";
        String updateWithPatIndexComplex = "UPDATE orders SET order_type = CASE WHEN PATINDEX('%OR%', order_number) > 0 THEN 'Regular' WHEN PATINDEX('%SP%', order_number) > 0 THEN 'Special' ELSE 'Unknown' END WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithPatIndex);
        int result2 = stmt.executeUpdate(updateWithPatIndexCondition);
        int result3 = stmt.executeUpdate(updateWithPatIndexCalculation);
        int result4 = stmt.executeUpdate(updateWithPatIndexComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInInsertStatements() throws SQLException {
        // PATINDEX function in INSERT statements
        String insertWithPatIndex = "INSERT INTO user_email_domains (user_id, email_domain) SELECT id, SUBSTRING(email, PATINDEX('%@%', email) + 1, LEN(email)) FROM users WHERE PATINDEX('%@%', email) > 0";
        String insertWithPatIndexCondition = "INSERT INTO product_types (product_id, product_type) SELECT id, CASE WHEN PATINDEX('%Pro%', name) > 0 THEN 'Professional' ELSE 'Standard' END FROM products WHERE name IS NOT NULL";
        String insertWithPatIndexCalculation = "INSERT INTO contact_phone_types (contact_id, phone_type) SELECT id, CASE WHEN PATINDEX('%555%', phone_number) > 0 THEN 'Local' ELSE 'Other' END FROM contacts WHERE phone_number IS NOT NULL";
        String insertWithPatIndexComplex = "INSERT INTO order_types (order_id, order_type) SELECT id, CASE WHEN PATINDEX('%OR%', order_number) > 0 THEN 'Regular' WHEN PATINDEX('%SP%', order_number) > 0 THEN 'Special' ELSE 'Unknown' END FROM orders WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithPatIndex);
        int result2 = stmt.executeUpdate(insertWithPatIndexCondition);
        int result3 = stmt.executeUpdate(insertWithPatIndexCalculation);
        int result4 = stmt.executeUpdate(insertWithPatIndexComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionInPreparedStatement() throws SQLException {
        // PATINDEX function in PreparedStatement
        String selectWithPatIndexParam = "SELECT * FROM users WHERE PATINDEX(?, email) > 0";
        String selectWithPatIndexParamCondition = "SELECT * FROM products WHERE PATINDEX(?, name) > 0";
        String selectWithPatIndexParamCalculation = "SELECT PATINDEX(?, name) as search_position FROM users WHERE name IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithPatIndexParam);
        pstmt1.setString(1, "%@%");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithPatIndexParamCondition);
        pstmt2.setString(1, "%Pro%");
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithPatIndexParamCalculation);
        pstmt3.setString(1, "%A%");
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void patIndexFunctionInMethods() throws SQLException {
        // PATINDEX function in methods
        String userQuery = getUserQueryWithPatIndex();
        String orderQuery = getOrderQueryWithPatIndex();
        String productQuery = getProductQueryWithPatIndex();
        
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
    
    private String getUserQueryWithPatIndex() {
        return "SELECT name, email, PATINDEX('%@%', email) as at_position FROM users WHERE PATINDEX('%@%', email) > 0";
    }
    
    private String getOrderQueryWithPatIndex() {
        return "SELECT order_number, PATINDEX('%OR%', order_number) as order_type_position FROM orders WHERE PATINDEX('%OR%', order_number) > 0";
    }
    
    private String getProductQueryWithPatIndex() {
        return "SELECT name, PATINDEX('%Pro%', name) as pro_position FROM products WHERE PATINDEX('%Pro%', name) > 0";
    }
    
    public void patIndexFunctionWithDynamicSQL() throws SQLException {
        // PATINDEX function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE PATINDEX('";
        String pattern = "%@%";
        String condition = "', email) > 0";
        
        String dynamicQuery = baseQuery + pattern + condition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionWithTransaction() throws SQLException {
        // PATINDEX function with transaction
        String transactionWithPatIndex = "BEGIN TRANSACTION; UPDATE users SET email_domain = SUBSTRING(email, PATINDEX('%@%', email) + 1, LEN(email)) WHERE PATINDEX('%@%', email) > 0; INSERT INTO user_activity_log (action, timestamp) VALUES ('EMAIL_DOMAIN_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithPatIndexRollback = "BEGIN TRANSACTION; UPDATE products SET product_type = CASE WHEN PATINDEX('%Pro%', name) > 0 THEN 'Professional' ELSE 'Standard' END WHERE name IS NOT NULL; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithPatIndex);
        
        boolean hasResult = stmt.execute(transactionWithPatIndexRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionWithBusinessLogic() throws SQLException {
        // PATINDEX function with business logic
        String businessLogicWithPatIndex = "SELECT name, email, PATINDEX('%@%', email) as at_position, CASE WHEN PATINDEX('%@%', email) > 0 THEN 'Valid Email' ELSE 'Invalid Email' END as email_status FROM users";
        String businessLogicComplex = "SELECT order_number, PATINDEX('%OR%', order_number) as order_type_position, CASE WHEN PATINDEX('%OR%', order_number) > 0 THEN 'Regular Order' WHEN PATINDEX('%SP%', order_number) > 0 THEN 'Special Order' WHEN PATINDEX('%RT%', order_number) > 0 THEN 'Return Order' ELSE 'Unknown' END as order_category FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void patIndexFunctionWithPatternMatching() throws SQLException {
        // PATINDEX function with pattern matching
        String patternMatchingWithPatIndex = "SELECT email, PATINDEX('%@%.%', email) as email_pattern_position, CASE WHEN PATINDEX('%@%.%', email) > 0 THEN 'Valid Email Format' ELSE 'Invalid Email Format' END as email_validation FROM users";
        String patternMatchingComplex = "SELECT phone_number, PATINDEX('%[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]%', phone_number) as phone_pattern_position, CASE WHEN PATINDEX('%[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]%', phone_number) > 0 THEN 'Valid Phone Format' ELSE 'Invalid Phone Format' END as phone_validation FROM contacts";
        String patternMatchingAdvanced = "SELECT order_number, PATINDEX('%[A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9]%', order_number) as order_pattern_position, CASE WHEN PATINDEX('%[A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9]%', order_number) > 0 THEN 'Valid Order Format' ELSE 'Invalid Order Format' END as order_validation FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(patternMatchingWithPatIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(patternMatchingComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(patternMatchingAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
