package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-046: CharIndexFunctionUsage
 * Detects "CHARINDEX(" function usage
 */
public class CharIndexFunctionUsageExample {
    
    public void basicCharIndexFunctionUsage() throws SQLException {
        // Basic CHARINDEX function usage
        String charIndexFunction = "SELECT CHARINDEX('@', email) as at_position FROM users";
        String charIndexFunctionWithStart = "SELECT CHARINDEX('Pro', name, 1) as pro_position FROM products";
        String charIndexFunctionWithColumn = "SELECT CHARINDEX('555', phone_number) as area_code_position FROM contacts";
        String charIndexFunctionWithString = "SELECT CHARINDEX('OR', order_number) as order_type_position FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(charIndexFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(charIndexFunctionWithStart);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(charIndexFunctionWithColumn);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(charIndexFunctionWithString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInWhereClauses() throws SQLException {
        // CHARINDEX function in WHERE clauses
        String whereWithCharIndex = "SELECT * FROM users WHERE CHARINDEX('@', email) > 0";
        String whereWithCharIndexStart = "SELECT * FROM products WHERE CHARINDEX('Pro', name, 1) > 0";
        String whereWithCharIndexPattern = "SELECT * FROM contacts WHERE CHARINDEX('555', phone_number) = 1";
        String whereWithCharIndexComparison = "SELECT * FROM orders WHERE CHARINDEX('OR', order_number) > 0";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithCharIndexStart);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithCharIndexPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithCharIndexComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInOrderByClauses() throws SQLException {
        // CHARINDEX function in ORDER BY clauses
        String orderByWithCharIndex = "SELECT * FROM users ORDER BY CHARINDEX('@', email) ASC";
        String orderByWithCharIndexStart = "SELECT * FROM products ORDER BY CHARINDEX('Pro', name, 1) DESC";
        String orderByWithCharIndexPattern = "SELECT * FROM contacts ORDER BY CHARINDEX('555', phone_number) ASC";
        String orderByWithCharIndexComparison = "SELECT * FROM orders ORDER BY CHARINDEX('OR', order_number) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithCharIndexStart);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithCharIndexPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithCharIndexComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInGroupByClauses() throws SQLException {
        // CHARINDEX function in GROUP BY clauses
        String groupByWithCharIndex = "SELECT CHARINDEX('@', email) as at_position, COUNT(*) as user_count FROM users GROUP BY CHARINDEX('@', email)";
        String groupByWithCharIndexStart = "SELECT CHARINDEX('Pro', name, 1) as pro_position, COUNT(*) as product_count FROM products GROUP BY CHARINDEX('Pro', name, 1)";
        String groupByWithCharIndexPattern = "SELECT CHARINDEX('555', phone_number) as area_code_position, COUNT(*) as contact_count FROM contacts GROUP BY CHARINDEX('555', phone_number)";
        String groupByWithCharIndexComparison = "SELECT CHARINDEX('OR', order_number) as order_type_position, COUNT(*) as order_count FROM orders GROUP BY CHARINDEX('OR', order_number)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithCharIndexStart);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithCharIndexPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithCharIndexComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInCaseStatements() throws SQLException {
        // CHARINDEX function in CASE statements
        String caseWithCharIndex = "SELECT name, CASE WHEN CHARINDEX('@', email) > 0 THEN 'Valid Email' ELSE 'Invalid Email' END as email_status FROM users";
        String caseWithCharIndexStart = "SELECT product_name, CASE WHEN CHARINDEX('Pro', name, 1) > 0 THEN 'Professional' WHEN CHARINDEX('Ent', name, 1) > 0 THEN 'Enterprise' ELSE 'Standard' END as product_type FROM products";
        String caseWithCharIndexPattern = "SELECT phone_number, CASE WHEN CHARINDEX('555', phone_number) = 1 THEN 'Local' WHEN CHARINDEX('800', phone_number) = 1 THEN 'Toll Free' ELSE 'Other' END as phone_type FROM contacts";
        String caseWithCharIndexComplex = "SELECT order_number, CASE WHEN CHARINDEX('OR', order_number) > 0 THEN 'Regular Order' WHEN CHARINDEX('SP', order_number) > 0 THEN 'Special Order' WHEN CHARINDEX('RT', order_number) > 0 THEN 'Return Order' ELSE 'Unknown' END as order_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithCharIndexStart);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithCharIndexPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithCharIndexComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInSubqueries() throws SQLException {
        // CHARINDEX function in subqueries
        String subqueryWithCharIndex = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE CHARINDEX('OR', order_number) > 0)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND CHARINDEX('OR', order_number) > 0) as regular_orders FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND CHARINDEX('SP', order_number) > 0)";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND CHARINDEX('RT', order_number) > 0)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithCharIndex);
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
    
    public void charIndexFunctionInJoins() throws SQLException {
        // CHARINDEX function in JOINs
        String joinWithCharIndex = "SELECT u.name, o.order_number, CHARINDEX('OR', o.order_number) as order_type_position FROM users u JOIN orders o ON u.id = o.user_id WHERE CHARINDEX('OR', o.order_number) > 0";
        String joinWithCharIndexCondition = "SELECT p.name, c.category_name, CHARINDEX('Pro', p.name) as product_prefix_position FROM products p JOIN categories c ON p.category_id = c.id WHERE CHARINDEX('Pro', p.name) > 0";
        String joinWithCharIndexCalculation = "SELECT u.name, COUNT(o.id) as order_count, CHARINDEX('@', u.email) as email_at_position FROM users u LEFT JOIN orders o ON u.id = o.user_id AND CHARINDEX('OR', o.order_number) > 0 GROUP BY u.name, u.email";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithCharIndexCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithCharIndexCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInAggregates() throws SQLException {
        // CHARINDEX function in aggregate functions
        String aggregateWithCharIndex = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN CHARINDEX('@', email) > 0 THEN 1 END) as valid_email_users FROM users";
        String sumWithCharIndex = "SELECT SUM(CASE WHEN CHARINDEX('OR', order_number) > 0 THEN total_amount ELSE 0 END) as regular_order_total FROM orders";
        String avgWithCharIndex = "SELECT AVG(CASE WHEN CHARINDEX('Pro', name) > 0 THEN price ELSE NULL END) as pro_product_avg_price FROM products";
        String maxWithCharIndex = "SELECT MAX(CASE WHEN CHARINDEX('555', phone_number) > 0 THEN LEN(phone_number) ELSE 0 END) as max_local_phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithCharIndex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithCharIndex);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithCharIndex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInUpdateStatements() throws SQLException {
        // CHARINDEX function in UPDATE statements
        String updateWithCharIndex = "UPDATE users SET email_domain = SUBSTRING(email, CHARINDEX('@', email) + 1, LEN(email)) WHERE CHARINDEX('@', email) > 0";
        String updateWithCharIndexCondition = "UPDATE products SET product_type = CASE WHEN CHARINDEX('Pro', name) > 0 THEN 'Professional' ELSE 'Standard' END WHERE name IS NOT NULL";
        String updateWithCharIndexCalculation = "UPDATE contacts SET phone_type = CASE WHEN CHARINDEX('555', phone_number) = 1 THEN 'Local' ELSE 'Other' END WHERE phone_number IS NOT NULL";
        String updateWithCharIndexComplex = "UPDATE orders SET order_type = CASE WHEN CHARINDEX('OR', order_number) > 0 THEN 'Regular' WHEN CHARINDEX('SP', order_number) > 0 THEN 'Special' ELSE 'Unknown' END WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithCharIndex);
        int result2 = stmt.executeUpdate(updateWithCharIndexCondition);
        int result3 = stmt.executeUpdate(updateWithCharIndexCalculation);
        int result4 = stmt.executeUpdate(updateWithCharIndexComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInInsertStatements() throws SQLException {
        // CHARINDEX function in INSERT statements
        String insertWithCharIndex = "INSERT INTO user_email_domains (user_id, email_domain) SELECT id, SUBSTRING(email, CHARINDEX('@', email) + 1, LEN(email)) FROM users WHERE CHARINDEX('@', email) > 0";
        String insertWithCharIndexCondition = "INSERT INTO product_types (product_id, product_type) SELECT id, CASE WHEN CHARINDEX('Pro', name) > 0 THEN 'Professional' ELSE 'Standard' END FROM products WHERE name IS NOT NULL";
        String insertWithCharIndexCalculation = "INSERT INTO contact_phone_types (contact_id, phone_type) SELECT id, CASE WHEN CHARINDEX('555', phone_number) = 1 THEN 'Local' ELSE 'Other' END FROM contacts WHERE phone_number IS NOT NULL";
        String insertWithCharIndexComplex = "INSERT INTO order_types (order_id, order_type) SELECT id, CASE WHEN CHARINDEX('OR', order_number) > 0 THEN 'Regular' WHEN CHARINDEX('SP', order_number) > 0 THEN 'Special' ELSE 'Unknown' END FROM orders WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithCharIndex);
        int result2 = stmt.executeUpdate(insertWithCharIndexCondition);
        int result3 = stmt.executeUpdate(insertWithCharIndexCalculation);
        int result4 = stmt.executeUpdate(insertWithCharIndexComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionInPreparedStatement() throws SQLException {
        // CHARINDEX function in PreparedStatement
        String selectWithCharIndexParam = "SELECT * FROM users WHERE CHARINDEX(?, email) > 0";
        String selectWithCharIndexParamCondition = "SELECT * FROM products WHERE CHARINDEX(?, name) > 0";
        String selectWithCharIndexParamCalculation = "SELECT CHARINDEX(?, name) as search_position FROM users WHERE name IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithCharIndexParam);
        pstmt1.setString(1, "@");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithCharIndexParamCondition);
        pstmt2.setString(1, "Pro");
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithCharIndexParamCalculation);
        pstmt3.setString(1, "A");
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void charIndexFunctionInMethods() throws SQLException {
        // CHARINDEX function in methods
        String userQuery = getUserQueryWithCharIndex();
        String orderQuery = getOrderQueryWithCharIndex();
        String productQuery = getProductQueryWithCharIndex();
        
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
    
    private String getUserQueryWithCharIndex() {
        return "SELECT name, email, CHARINDEX('@', email) as at_position FROM users WHERE CHARINDEX('@', email) > 0";
    }
    
    private String getOrderQueryWithCharIndex() {
        return "SELECT order_number, CHARINDEX('OR', order_number) as order_type_position FROM orders WHERE CHARINDEX('OR', order_number) > 0";
    }
    
    private String getProductQueryWithCharIndex() {
        return "SELECT name, CHARINDEX('Pro', name) as pro_position FROM products WHERE CHARINDEX('Pro', name) > 0";
    }
    
    public void charIndexFunctionWithDynamicSQL() throws SQLException {
        // CHARINDEX function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE CHARINDEX('";
        String searchString = "@";
        String condition = "', email) > 0";
        
        String dynamicQuery = baseQuery + searchString + condition;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionWithTransaction() throws SQLException {
        // CHARINDEX function with transaction
        String transactionWithCharIndex = "BEGIN TRANSACTION; UPDATE users SET email_domain = SUBSTRING(email, CHARINDEX('@', email) + 1, LEN(email)) WHERE CHARINDEX('@', email) > 0; INSERT INTO user_activity_log (action, timestamp) VALUES ('EMAIL_DOMAIN_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithCharIndexRollback = "BEGIN TRANSACTION; UPDATE products SET product_type = CASE WHEN CHARINDEX('Pro', name) > 0 THEN 'Professional' ELSE 'Standard' END WHERE name IS NOT NULL; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithCharIndex);
        
        boolean hasResult = stmt.execute(transactionWithCharIndexRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionWithBusinessLogic() throws SQLException {
        // CHARINDEX function with business logic
        String businessLogicWithCharIndex = "SELECT name, email, CHARINDEX('@', email) as at_position, CASE WHEN CHARINDEX('@', email) > 0 THEN 'Valid Email' ELSE 'Invalid Email' END as email_status FROM users";
        String businessLogicComplex = "SELECT order_number, CHARINDEX('OR', order_number) as order_type_position, CASE WHEN CHARINDEX('OR', order_number) > 0 THEN 'Regular Order' WHEN CHARINDEX('SP', order_number) > 0 THEN 'Special Order' WHEN CHARINDEX('RT', order_number) > 0 THEN 'Return Order' ELSE 'Unknown' END as order_category FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void charIndexFunctionWithStringParsing() throws SQLException {
        // CHARINDEX function with string parsing
        String stringParsingWithCharIndex = "SELECT email, CHARINDEX('@', email) as at_position, SUBSTRING(email, 1, CHARINDEX('@', email) - 1) as username, SUBSTRING(email, CHARINDEX('@', email) + 1, LEN(email)) as domain FROM users WHERE CHARINDEX('@', email) > 0";
        String stringParsingComplex = "SELECT phone_number, CHARINDEX('555', phone_number) as area_code_position, CASE WHEN CHARINDEX('555', phone_number) = 1 THEN 'Local Area Code' ELSE 'Other Area Code' END as area_code_type FROM contacts WHERE CHARINDEX('555', phone_number) > 0";
        String stringParsingAdvanced = "SELECT order_number, CHARINDEX('OR', order_number) as order_type_position, SUBSTRING(order_number, 1, CHARINDEX('OR', order_number) - 1) as prefix, SUBSTRING(order_number, CHARINDEX('OR', order_number) + 2, LEN(order_number)) as suffix FROM orders WHERE CHARINDEX('OR', order_number) > 0";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringParsingWithCharIndex);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(stringParsingComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(stringParsingAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
