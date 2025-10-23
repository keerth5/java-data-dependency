package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-045: LenFunctionUsage
 * Detects "LEN(" function usage
 */
public class LenFunctionUsageExample {
    
    public void basicLenFunctionUsage() throws SQLException {
        // Basic LEN function usage
        String lenFunction = "SELECT LEN(name) as name_length FROM users";
        String lenFunctionWithColumn = "SELECT LEN(email) as email_length FROM users";
        String lenFunctionWithVariable = "SELECT LEN(description) as description_length FROM products";
        String lenFunctionWithString = "SELECT LEN(phone_number) as phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(lenFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(lenFunctionWithColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(lenFunctionWithVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(lenFunctionWithString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInWhereClauses() throws SQLException {
        // LEN function in WHERE clauses
        String whereWithLen = "SELECT * FROM users WHERE LEN(name) > 10";
        String whereWithLenLength = "SELECT * FROM products WHERE LEN(name) < 20";
        String whereWithLenPattern = "SELECT * FROM contacts WHERE LEN(phone_number) = 10";
        String whereWithLenComparison = "SELECT * FROM orders WHERE LEN(order_number) >= 8";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithLenLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithLenPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithLenComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInOrderByClauses() throws SQLException {
        // LEN function in ORDER BY clauses
        String orderByWithLen = "SELECT * FROM users ORDER BY LEN(name) ASC";
        String orderByWithLenLength = "SELECT * FROM products ORDER BY LEN(name) DESC";
        String orderByWithLenPattern = "SELECT * FROM contacts ORDER BY LEN(phone_number) ASC";
        String orderByWithLenComparison = "SELECT * FROM orders ORDER BY LEN(order_number) DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithLenLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithLenPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithLenComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInGroupByClauses() throws SQLException {
        // LEN function in GROUP BY clauses
        String groupByWithLen = "SELECT LEN(name) as name_length, COUNT(*) as user_count FROM users GROUP BY LEN(name)";
        String groupByWithLenLength = "SELECT LEN(name) as name_length, COUNT(*) as product_count FROM products GROUP BY LEN(name)";
        String groupByWithLenPattern = "SELECT LEN(phone_number) as phone_length, COUNT(*) as contact_count FROM contacts GROUP BY LEN(phone_number)";
        String groupByWithLenComparison = "SELECT LEN(order_number) as order_length, COUNT(*) as order_count FROM orders GROUP BY LEN(order_number)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithLenLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithLenPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithLenComparison);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInCaseStatements() throws SQLException {
        // LEN function in CASE statements
        String caseWithLen = "SELECT name, CASE WHEN LEN(name) > 20 THEN 'Long Name' WHEN LEN(name) > 10 THEN 'Medium Name' ELSE 'Short Name' END as name_category FROM users";
        String caseWithLenLength = "SELECT product_name, CASE WHEN LEN(name) > 30 THEN 'Very Long' WHEN LEN(name) > 20 THEN 'Long' WHEN LEN(name) > 10 THEN 'Medium' ELSE 'Short' END as product_name_length FROM products";
        String caseWithLenPattern = "SELECT phone_number, CASE WHEN LEN(phone_number) = 10 THEN 'Valid US Phone' WHEN LEN(phone_number) = 11 THEN 'Valid with Country Code' ELSE 'Invalid Length' END as phone_validity FROM contacts";
        String caseWithLenComplex = "SELECT order_number, CASE WHEN LEN(order_number) >= 10 THEN 'Long Order Number' WHEN LEN(order_number) >= 8 THEN 'Medium Order Number' ELSE 'Short Order Number' END as order_number_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithLenLength);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithLenPattern);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithLenComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInSubqueries() throws SQLException {
        // LEN function in subqueries
        String subqueryWithLen = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE LEN(order_number) > 10)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM orders WHERE user_id = users.id AND LEN(order_number) > 8) as long_order_count FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND LEN(order_number) > 10)";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND LEN(order_number) < 5)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithLen);
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
    
    public void lenFunctionInJoins() throws SQLException {
        // LEN function in JOINs
        String joinWithLen = "SELECT u.name, o.order_number, LEN(o.order_number) as order_number_length FROM users u JOIN orders o ON u.id = o.user_id WHERE LEN(o.order_number) > 10";
        String joinWithLenCondition = "SELECT p.name, c.category_name, LEN(p.name) as product_name_length FROM products p JOIN categories c ON p.category_id = c.id WHERE LEN(p.name) > 20";
        String joinWithLenCalculation = "SELECT u.name, COUNT(o.id) as order_count, LEN(u.name) as name_length FROM users u LEFT JOIN orders o ON u.id = o.user_id AND LEN(o.order_number) > 8 GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithLenCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithLenCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInAggregates() throws SQLException {
        // LEN function in aggregate functions
        String aggregateWithLen = "SELECT COUNT(*) as total_users, COUNT(CASE WHEN LEN(name) > 20 THEN 1 END) as long_name_users FROM users";
        String sumWithLen = "SELECT SUM(CASE WHEN LEN(order_number) > 10 THEN total_amount ELSE 0 END) as long_order_total FROM orders";
        String avgWithLen = "SELECT AVG(LEN(name)) as avg_name_length FROM users";
        String maxWithLen = "SELECT MAX(LEN(phone_number)) as max_phone_length FROM contacts";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithLen);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithLen);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithLen);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInUpdateStatements() throws SQLException {
        // LEN function in UPDATE statements
        String updateWithLen = "UPDATE users SET name_length = LEN(name) WHERE name IS NOT NULL";
        String updateWithLenCondition = "UPDATE products SET name_length = LEN(name) WHERE name IS NOT NULL";
        String updateWithLenCalculation = "UPDATE contacts SET phone_length = LEN(phone_number) WHERE phone_number IS NOT NULL";
        String updateWithLenComplex = "UPDATE orders SET order_number_length = LEN(order_number) WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithLen);
        int result2 = stmt.executeUpdate(updateWithLenCondition);
        int result3 = stmt.executeUpdate(updateWithLenCalculation);
        int result4 = stmt.executeUpdate(updateWithLenComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInInsertStatements() throws SQLException {
        // LEN function in INSERT statements
        String insertWithLen = "INSERT INTO user_name_lengths (user_id, name_length) SELECT id, LEN(name) FROM users WHERE name IS NOT NULL";
        String insertWithLenCondition = "INSERT INTO product_name_lengths (product_id, name_length) SELECT id, LEN(name) FROM products WHERE name IS NOT NULL";
        String insertWithLenCalculation = "INSERT INTO contact_phone_lengths (contact_id, phone_length) SELECT id, LEN(phone_number) FROM contacts WHERE phone_number IS NOT NULL";
        String insertWithLenComplex = "INSERT INTO order_number_lengths (order_id, order_number_length) SELECT id, LEN(order_number) FROM orders WHERE order_number IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithLen);
        int result2 = stmt.executeUpdate(insertWithLenCondition);
        int result3 = stmt.executeUpdate(insertWithLenCalculation);
        int result4 = stmt.executeUpdate(insertWithLenComplex);
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionInPreparedStatement() throws SQLException {
        // LEN function in PreparedStatement
        String selectWithLenParam = "SELECT * FROM users WHERE LEN(name) > ?";
        String selectWithLenParamCondition = "SELECT * FROM products WHERE LEN(name) < ?";
        String selectWithLenParamCalculation = "SELECT LEN(name) as name_length FROM users WHERE name IS NOT NULL";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithLenParam);
        pstmt1.setInt(1, 10);
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithLenParamCondition);
        pstmt2.setInt(1, 20);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithLenParamCalculation);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void lenFunctionInMethods() throws SQLException {
        // LEN function in methods
        String userQuery = getUserQueryWithLen();
        String orderQuery = getOrderQueryWithLen();
        String productQuery = getProductQueryWithLen();
        
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
    
    private String getUserQueryWithLen() {
        return "SELECT name, LEN(name) as name_length FROM users WHERE LEN(name) > 10";
    }
    
    private String getOrderQueryWithLen() {
        return "SELECT order_number, LEN(order_number) as order_number_length FROM orders WHERE LEN(order_number) > 8";
    }
    
    private String getProductQueryWithLen() {
        return "SELECT name, LEN(name) as product_name_length FROM products WHERE LEN(name) > 20";
    }
    
    public void lenFunctionWithDynamicSQL() throws SQLException {
        // LEN function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE LEN(name) > ";
        String length = "10";
        String endQuery = "";
        
        String dynamicQuery = baseQuery + length + endQuery;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionWithTransaction() throws SQLException {
        // LEN function with transaction
        String transactionWithLen = "BEGIN TRANSACTION; UPDATE users SET name_length = LEN(name) WHERE name IS NOT NULL; INSERT INTO user_activity_log (action, timestamp) VALUES ('NAME_LENGTH_UPDATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithLenRollback = "BEGIN TRANSACTION; UPDATE products SET name_length = LEN(name) WHERE name IS NOT NULL; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products updated' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithLen);
        
        boolean hasResult = stmt.execute(transactionWithLenRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionWithBusinessLogic() throws SQLException {
        // LEN function with business logic
        String businessLogicWithLen = "SELECT name, LEN(name) as name_length, CASE WHEN LEN(name) > 20 THEN 'Long Name' WHEN LEN(name) > 10 THEN 'Medium Name' ELSE 'Short Name' END as name_category FROM users";
        String businessLogicComplex = "SELECT order_number, LEN(order_number) as order_length, CASE WHEN LEN(order_number) >= 10 THEN 'Long Order Number' WHEN LEN(order_number) >= 8 THEN 'Medium Order Number' ELSE 'Short Order Number' END as order_number_type FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void lenFunctionWithStringValidation() throws SQLException {
        // LEN function with string validation
        String stringValidationWithLen = "SELECT name, LEN(name) as name_length, CASE WHEN LEN(name) = 0 THEN 'Empty Name' WHEN LEN(name) < 2 THEN 'Too Short' WHEN LEN(name) > 50 THEN 'Too Long' ELSE 'Valid Length' END as name_validation FROM users";
        String stringValidationComplex = "SELECT phone_number, LEN(phone_number) as phone_length, CASE WHEN LEN(phone_number) = 10 THEN 'Valid US Phone' WHEN LEN(phone_number) = 11 THEN 'Valid with Country Code' WHEN LEN(phone_number) < 10 THEN 'Too Short' ELSE 'Invalid Length' END as phone_validation FROM contacts";
        String stringValidationAdvanced = "SELECT email, LEN(email) as email_length, CASE WHEN LEN(email) < 5 THEN 'Too Short' WHEN LEN(email) > 100 THEN 'Too Long' WHEN LEN(email) BETWEEN 5 AND 100 THEN 'Valid Length' ELSE 'Invalid' END as email_validation FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(stringValidationWithLen);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(stringValidationComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(stringValidationAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
