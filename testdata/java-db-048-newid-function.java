package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-048: NewIdFunctionUsage
 * Detects "NEWID()" function usage
 */
public class NewIdFunctionUsageExample {
    
    public void basicNewIdFunctionUsage() throws SQLException {
        // Basic NEWID() function usage
        String newIdFunction = "SELECT NEWID() as new_guid FROM users";
        String newIdFunctionWithColumn = "SELECT id, name, NEWID() as session_id FROM users";
        String newIdFunctionWithVariable = "SELECT NEWID() as unique_id FROM products";
        String newIdFunctionWithString = "SELECT NEWID() as transaction_id FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(newIdFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(newIdFunctionWithColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(newIdFunctionWithVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(newIdFunctionWithString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInInsertStatements() throws SQLException {
        // NEWID() function in INSERT statements
        String insertWithNewId = "INSERT INTO user_sessions (user_id, session_id, created_date) VALUES (1, NEWID(), GETDATE())";
        String insertWithNewIdColumn = "INSERT INTO products (id, name, unique_code) VALUES (NEWID(), 'Laptop', 'LAP001')";
        String insertWithNewIdVariable = "INSERT INTO orders (id, user_id, order_number, transaction_id) VALUES (NEWID(), 1, 'OR001', NEWID())";
        String insertWithNewIdString = "INSERT INTO audit_log (id, action, timestamp, unique_id) VALUES (NEWID(), 'LOGIN', GETDATE(), NEWID())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertWithNewId);
        int result2 = stmt.executeUpdate(insertWithNewIdColumn);
        int result3 = stmt.executeUpdate(insertWithNewIdVariable);
        int result4 = stmt.executeUpdate(insertWithNewIdString);
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInUpdateStatements() throws SQLException {
        // NEWID() function in UPDATE statements
        String updateWithNewId = "UPDATE users SET session_id = NEWID() WHERE id = 1";
        String updateWithNewIdColumn = "UPDATE products SET unique_code = NEWID() WHERE category_id = 1";
        String updateWithNewIdVariable = "UPDATE orders SET transaction_id = NEWID() WHERE status = 'pending'";
        String updateWithNewIdString = "UPDATE audit_log SET unique_id = NEWID() WHERE action = 'LOGIN'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateWithNewId);
        int result2 = stmt.executeUpdate(updateWithNewIdColumn);
        int result3 = stmt.executeUpdate(updateWithNewIdVariable);
        int result4 = stmt.executeUpdate(updateWithNewIdString);
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInSelectStatements() throws SQLException {
        // NEWID() function in SELECT statements
        String selectWithNewId = "SELECT id, name, NEWID() as temp_id FROM users";
        String selectWithNewIdColumn = "SELECT product_id, product_name, NEWID() as unique_identifier FROM products";
        String selectWithNewIdVariable = "SELECT order_id, order_number, NEWID() as session_token FROM orders";
        String selectWithNewIdString = "SELECT user_id, action, NEWID() as audit_id FROM audit_log";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectWithNewIdColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectWithNewIdVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectWithNewIdString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInWhereClauses() throws SQLException {
        // NEWID() function in WHERE clauses
        String whereWithNewId = "SELECT * FROM users WHERE session_id = NEWID()";
        String whereWithNewIdColumn = "SELECT * FROM products WHERE unique_code = NEWID()";
        String whereWithNewIdVariable = "SELECT * FROM orders WHERE transaction_id = NEWID()";
        String whereWithNewIdString = "SELECT * FROM audit_log WHERE unique_id = NEWID()";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(whereWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(whereWithNewIdColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(whereWithNewIdVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(whereWithNewIdString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInOrderByClauses() throws SQLException {
        // NEWID() function in ORDER BY clauses
        String orderByWithNewId = "SELECT * FROM users ORDER BY NEWID()";
        String orderByWithNewIdColumn = "SELECT * FROM products ORDER BY NEWID()";
        String orderByWithNewIdVariable = "SELECT * FROM orders ORDER BY NEWID()";
        String orderByWithNewIdString = "SELECT * FROM audit_log ORDER BY NEWID()";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(orderByWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderByWithNewIdColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(orderByWithNewIdVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(orderByWithNewIdString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInGroupByClauses() throws SQLException {
        // NEWID() function in GROUP BY clauses
        String groupByWithNewId = "SELECT NEWID() as group_id, COUNT(*) as user_count FROM users GROUP BY NEWID()";
        String groupByWithNewIdColumn = "SELECT NEWID() as group_id, COUNT(*) as product_count FROM products GROUP BY NEWID()";
        String groupByWithNewIdVariable = "SELECT NEWID() as group_id, COUNT(*) as order_count FROM orders GROUP BY NEWID()";
        String groupByWithNewIdString = "SELECT NEWID() as group_id, COUNT(*) as audit_count FROM audit_log GROUP BY NEWID()";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(groupByWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(groupByWithNewIdColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(groupByWithNewIdVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithNewIdString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInCaseStatements() throws SQLException {
        // NEWID() function in CASE statements
        String caseWithNewId = "SELECT name, CASE WHEN status = 'active' THEN NEWID() ELSE NULL END as session_id FROM users";
        String caseWithNewIdColumn = "SELECT product_name, CASE WHEN category_id = 1 THEN NEWID() ELSE NULL END as unique_code FROM products";
        String caseWithNewIdVariable = "SELECT order_number, CASE WHEN status = 'pending' THEN NEWID() ELSE NULL END as transaction_id FROM orders";
        String caseWithNewIdString = "SELECT action, CASE WHEN action = 'LOGIN' THEN NEWID() ELSE NULL END as audit_id FROM audit_log";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(caseWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(caseWithNewIdColumn);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(caseWithNewIdVariable);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(caseWithNewIdString);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInSubqueries() throws SQLException {
        // NEWID() function in subqueries
        String subqueryWithNewId = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE transaction_id = NEWID())";
        String subqueryInSelect = "SELECT name, (SELECT NEWID() FROM orders WHERE user_id = users.id) as temp_id FROM users";
        String subqueryWithExists = "SELECT * FROM products WHERE EXISTS (SELECT 1 FROM orders WHERE product_id = products.id AND transaction_id = NEWID())";
        String subqueryWithNotExists = "SELECT * FROM users WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND transaction_id = NEWID())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithNewId);
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
    
    public void newIdFunctionInJoins() throws SQLException {
        // NEWID() function in JOINs
        String joinWithNewId = "SELECT u.name, o.order_number, NEWID() as session_id FROM users u JOIN orders o ON u.id = o.user_id";
        String joinWithNewIdCondition = "SELECT p.name, c.category_name, NEWID() as unique_code FROM products p JOIN categories c ON p.category_id = c.id";
        String joinWithNewIdCalculation = "SELECT u.name, COUNT(o.id) as order_count, NEWID() as temp_id FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.name";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(joinWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(joinWithNewIdCondition);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(joinWithNewIdCalculation);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInAggregates() throws SQLException {
        // NEWID() function in aggregate functions
        String aggregateWithNewId = "SELECT COUNT(*) as total_users, NEWID() as batch_id FROM users";
        String sumWithNewId = "SELECT SUM(total_amount) as total_revenue, NEWID() as report_id FROM orders";
        String avgWithNewId = "SELECT AVG(price) as avg_price, NEWID() as calculation_id FROM products";
        String maxWithNewId = "SELECT MAX(created_date) as latest_date, NEWID() as query_id FROM audit_log";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithNewId);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithNewId);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(maxWithNewId);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionInPreparedStatement() throws SQLException {
        // NEWID() function in PreparedStatement
        String selectWithNewIdParam = "SELECT * FROM users WHERE session_id = NEWID()";
        String selectWithNewIdParamCondition = "SELECT * FROM products WHERE unique_code = NEWID()";
        String selectWithNewIdParamCalculation = "SELECT NEWID() as unique_id FROM users";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithNewIdParam);
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithNewIdParamCondition);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithNewIdParamCalculation);
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void newIdFunctionInMethods() throws SQLException {
        // NEWID() function in methods
        String userQuery = getUserQueryWithNewId();
        String orderQuery = getOrderQueryWithNewId();
        String productQuery = getProductQueryWithNewId();
        
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
    
    private String getUserQueryWithNewId() {
        return "SELECT id, name, NEWID() as session_id FROM users WHERE status = 'active'";
    }
    
    private String getOrderQueryWithNewId() {
        return "SELECT order_id, order_number, NEWID() as transaction_id FROM orders WHERE status = 'pending'";
    }
    
    private String getProductQueryWithNewId() {
        return "SELECT product_id, product_name, NEWID() as unique_code FROM products WHERE category_id = 1";
    }
    
    public void newIdFunctionWithDynamicSQL() throws SQLException {
        // NEWID() function with dynamic SQL construction
        String baseQuery = "SELECT * FROM users WHERE session_id = ";
        String newIdFunction = "NEWID()";
        
        String dynamicQuery = baseQuery + newIdFunction;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionWithTransaction() throws SQLException {
        // NEWID() function with transaction
        String transactionWithNewId = "BEGIN TRANSACTION; INSERT INTO user_sessions (user_id, session_id, created_date) VALUES (1, NEWID(), GETDATE()); INSERT INTO user_activity_log (action, timestamp) VALUES ('SESSION_CREATED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithNewIdRollback = "BEGIN TRANSACTION; INSERT INTO products (id, name, unique_code) VALUES (NEWID(), 'Test Product', 'TEST001'); IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products inserted' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Product inserted' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithNewId);
        
        boolean hasResult = stmt.execute(transactionWithNewIdRollback);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionWithBusinessLogic() throws SQLException {
        // NEWID() function with business logic
        String businessLogicWithNewId = "SELECT name, CASE WHEN status = 'active' THEN NEWID() ELSE NULL END as session_id, CASE WHEN status = 'active' THEN 'Session Created' ELSE 'No Session' END as session_status FROM users";
        String businessLogicComplex = "SELECT order_number, CASE WHEN status = 'pending' THEN NEWID() ELSE NULL END as transaction_id, CASE WHEN status = 'pending' THEN 'Transaction Created' ELSE 'No Transaction' END as transaction_status FROM orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void newIdFunctionWithRandomSampling() throws SQLException {
        // NEWID() function with random sampling
        String randomSamplingWithNewId = "SELECT TOP 10 * FROM users ORDER BY NEWID()";
        String randomSamplingComplex = "SELECT TOP 5 * FROM products ORDER BY NEWID()";
        String randomSamplingAdvanced = "SELECT TOP 20 * FROM orders ORDER BY NEWID()";
        String randomSamplingWithCondition = "SELECT TOP 15 * FROM audit_log WHERE action = 'LOGIN' ORDER BY NEWID()";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(randomSamplingWithNewId);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(randomSamplingComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(randomSamplingAdvanced);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(randomSamplingWithCondition);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
}
