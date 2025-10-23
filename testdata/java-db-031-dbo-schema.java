package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-031: DboSchemaUsage
 * Detects "dbo." schema references in SQL statements
 */
public class DboSchemaUsageExample {
    
    public void basicDboSchemaUsage() throws SQLException {
        // Basic dbo schema references
        String selectFromDbo = "SELECT * FROM dbo.users";
        String selectFromDboWithWhere = "SELECT * FROM dbo.orders WHERE status = 'pending'";
        String selectFromDboWithJoin = "SELECT u.name, o.order_date FROM dbo.users u JOIN dbo.orders o ON u.id = o.user_id";
        String selectFromDboWithAlias = "SELECT p.name, c.category_name FROM dbo.products p JOIN dbo.categories c ON p.category_id = c.id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectFromDbo);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectFromDboWithWhere);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectFromDboWithJoin);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectFromDboWithAlias);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInInsertStatements() throws SQLException {
        // dbo schema in INSERT statements
        String insertIntoDbo = "INSERT INTO dbo.users (name, email, age) VALUES ('John', 'john@example.com', 30)";
        String insertIntoDboWithSelect = "INSERT INTO dbo.user_backup SELECT * FROM dbo.users WHERE status = 'active'";
        String insertIntoDboWithValues = "INSERT INTO dbo.orders (user_id, product_name, quantity) VALUES (1, 'Laptop', 1)";
        String insertIntoDboWithDefault = "INSERT INTO dbo.products (name, price) VALUES ('Tablet', 299.99)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(insertIntoDbo);
        int result2 = stmt.executeUpdate(insertIntoDboWithSelect);
        int result3 = stmt.executeUpdate(insertIntoDboWithValues);
        int result4 = stmt.executeUpdate(insertIntoDboWithDefault);
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInUpdateStatements() throws SQLException {
        // dbo schema in UPDATE statements
        String updateDbo = "UPDATE dbo.users SET name = 'John Updated', email = 'john.updated@example.com' WHERE id = 1";
        String updateDboWithJoin = "UPDATE dbo.products SET price = p.price * 1.1 FROM dbo.products p JOIN dbo.categories c ON p.category_id = c.id WHERE c.name = 'Electronics'";
        String updateDboWithSubquery = "UPDATE dbo.users SET status = 'inactive' WHERE id IN (SELECT user_id FROM dbo.orders WHERE order_date < '2023-01-01')";
        String updateDboWithCase = "UPDATE dbo.products SET status = CASE WHEN price > 1000 THEN 'Expensive' ELSE 'Affordable' END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(updateDbo);
        int result2 = stmt.executeUpdate(updateDboWithJoin);
        int result3 = stmt.executeUpdate(updateDboWithSubquery);
        int result4 = stmt.executeUpdate(updateDboWithCase);
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInDeleteStatements() throws SQLException {
        // dbo schema in DELETE statements
        String deleteFromDbo = "DELETE FROM dbo.users WHERE id = 1";
        String deleteFromDboWithJoin = "DELETE p FROM dbo.products p JOIN dbo.categories c ON p.category_id = c.id WHERE c.name = 'Discontinued'";
        String deleteFromDboWithSubquery = "DELETE FROM dbo.orders WHERE user_id IN (SELECT id FROM dbo.users WHERE status = 'deleted')";
        String deleteFromDboWithExists = "DELETE FROM dbo.products WHERE NOT EXISTS (SELECT 1 FROM dbo.orders WHERE product_id = dbo.products.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        int result1 = stmt.executeUpdate(deleteFromDbo);
        int result2 = stmt.executeUpdate(deleteFromDboWithJoin);
        int result3 = stmt.executeUpdate(deleteFromDboWithSubquery);
        int result4 = stmt.executeUpdate(deleteFromDboWithExists);
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInStoredProcedures() throws SQLException {
        // dbo schema in stored procedure calls
        String execDboProcedure = "EXEC dbo.sp_get_user_orders @user_id = 1";
        String execDboProcedureWithParams = "EXEC dbo.sp_create_user @name = 'John', @email = 'john@example.com'";
        String execDboProcedureComplex = "EXEC dbo.sp_process_orders @start_date = '2024-01-01', @end_date = '2024-12-31'";
        String executeDboProcedure = "EXECUTE dbo.sp_update_product_price @product_id = 1, @new_price = 99.99";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execDboProcedure);
        cstmt1.execute();
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(execDboProcedureWithParams);
        cstmt2.execute();
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(execDboProcedureComplex);
        cstmt3.execute();
        cstmt3.close();
        
        java.sql.CallableStatement cstmt4 = conn.prepareCall(executeDboProcedure);
        cstmt4.execute();
        cstmt4.close();
        
        conn.close();
    }
    
    public void dboSchemaInSubqueries() throws SQLException {
        // dbo schema in subqueries
        String subqueryWithDbo = "SELECT * FROM dbo.users WHERE id IN (SELECT user_id FROM dbo.orders WHERE total_amount > 1000)";
        String subqueryInSelect = "SELECT name, (SELECT COUNT(*) FROM dbo.orders WHERE user_id = dbo.users.id) as order_count FROM dbo.users";
        String subqueryWithExists = "SELECT * FROM dbo.products WHERE EXISTS (SELECT 1 FROM dbo.reviews WHERE product_id = dbo.products.id)";
        String subqueryWithNotExists = "SELECT * FROM dbo.users WHERE NOT EXISTS (SELECT 1 FROM dbo.orders WHERE user_id = dbo.users.id)";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(subqueryWithDbo);
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
    
    public void dboSchemaInAggregates() throws SQLException {
        // dbo schema in aggregate functions
        String aggregateWithDbo = "SELECT COUNT(*) as total_users, COUNT(phone) as users_with_phone FROM dbo.users";
        String sumWithDbo = "SELECT SUM(total_amount) as total_revenue FROM dbo.orders";
        String avgWithDbo = "SELECT AVG(price) as average_price FROM dbo.products";
        String groupByWithDbo = "SELECT status, COUNT(*) as count FROM dbo.users GROUP BY status";
        String havingWithDbo = "SELECT user_id, COUNT(*) as order_count FROM dbo.orders GROUP BY user_id HAVING COUNT(*) > 5";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(aggregateWithDbo);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(sumWithDbo);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(avgWithDbo);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(groupByWithDbo);
        rs4.close();
        
        ResultSet rs5 = stmt.executeQuery(havingWithDbo);
        rs5.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInFunctions() throws SQLException {
        // dbo schema in functions
        String functionWithDbo = "SELECT dbo.fn_get_user_age(user_id) as age FROM dbo.users";
        String scalarFunctionWithDbo = "SELECT name, dbo.fn_calculate_discount(price) as discount FROM dbo.products";
        String tableFunctionWithDbo = "SELECT * FROM dbo.fn_get_user_orders(1)";
        String aggregateFunctionWithDbo = "SELECT dbo.fn_get_total_sales('2024-01-01', '2024-12-31') as total_sales";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(functionWithDbo);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(scalarFunctionWithDbo);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(tableFunctionWithDbo);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(aggregateFunctionWithDbo);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInViews() throws SQLException {
        // dbo schema in views
        String selectFromDboView = "SELECT * FROM dbo.vw_active_users";
        String selectFromDboViewWithJoin = "SELECT * FROM dbo.vw_user_orders uo JOIN dbo.users u ON uo.user_id = u.id";
        String selectFromDboViewWithWhere = "SELECT * FROM dbo.vw_product_sales WHERE total_sales > 1000";
        String selectFromDboViewComplex = "SELECT * FROM dbo.vw_monthly_reports WHERE report_month = '2024-01'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(selectFromDboView);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(selectFromDboViewWithJoin);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(selectFromDboViewWithWhere);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(selectFromDboViewComplex);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaInPreparedStatement() throws SQLException {
        // dbo schema in PreparedStatement
        String selectWithDboParam = "SELECT * FROM dbo.users WHERE status = ?";
        String insertWithDboParam = "INSERT INTO dbo.orders (user_id, product_name, quantity) VALUES (?, ?, ?)";
        String updateWithDboParam = "UPDATE dbo.users SET last_login = GETDATE() WHERE id = ?";
        String deleteWithDboParam = "DELETE FROM dbo.products WHERE product_id = ?";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithDboParam);
        pstmt1.setString(1, "active");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(insertWithDboParam);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "Laptop");
        pstmt2.setInt(3, 1);
        pstmt2.executeUpdate();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(updateWithDboParam);
        pstmt3.setInt(1, 1);
        pstmt3.executeUpdate();
        pstmt3.close();
        
        PreparedStatement pstmt4 = conn.prepareStatement(deleteWithDboParam);
        pstmt4.setInt(1, 1);
        pstmt4.executeUpdate();
        pstmt4.close();
        
        conn.close();
    }
    
    public void dboSchemaInMethods() throws SQLException {
        // dbo schema in methods
        String userQuery = getUserQueryWithDbo();
        String orderQuery = getOrderQueryWithDbo();
        String productQuery = getProductQueryWithDbo();
        
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
    
    private String getUserQueryWithDbo() {
        return "SELECT name, email, created_date FROM dbo.users WHERE status = 'active' ORDER BY created_date DESC";
    }
    
    private String getOrderQueryWithDbo() {
        return "SELECT order_id, user_id, total_amount, order_date FROM dbo.orders WHERE order_date > '2024-01-01' ORDER BY total_amount DESC";
    }
    
    private String getProductQueryWithDbo() {
        return "SELECT name, price, category FROM dbo.products WHERE price > 100 ORDER BY price ASC";
    }
    
    public void dboSchemaWithDynamicSQL() throws SQLException {
        // dbo schema with dynamic SQL construction
        String baseQuery = "SELECT * FROM dbo.users WHERE";
        String statusCondition = " status = 'active'";
        String orderClause = " ORDER BY created_date DESC";
        
        String dynamicQuery = baseQuery + statusCondition + orderClause;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void dboSchemaWithTransaction() throws SQLException {
        // dbo schema with transaction
        String selectUsers = "SELECT * FROM dbo.users WHERE status = 'active'";
        String selectOrders = "SELECT * FROM dbo.orders WHERE order_date > '2024-01-01'";
        String insertLog = "INSERT INTO dbo.audit_log (action, timestamp) VALUES ('DBO_QUERY_EXECUTED', GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            
            ResultSet rs1 = stmt.executeQuery(selectUsers);
            rs1.close();
            
            ResultSet rs2 = stmt.executeQuery(selectOrders);
            rs2.close();
            
            int result = stmt.executeUpdate(insertLog);
            
            conn.commit();
            stmt.close();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
    
    public void dboSchemaWithMultiTenantIssues() throws SQLException {
        // dbo schema with multi-tenant deployment issues
        String tenantSpecificQuery = "SELECT * FROM dbo.users WHERE tenant_id = 'tenant1'";
        String crossTenantQuery = "SELECT * FROM dbo.orders o JOIN dbo.users u ON o.user_id = u.id WHERE u.tenant_id = 'tenant1'";
        String tenantIsolationQuery = "SELECT * FROM dbo.products WHERE tenant_id = 'tenant1' AND status = 'active'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(tenantSpecificQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(crossTenantQuery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(tenantIsolationQuery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
}
