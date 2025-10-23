package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-033: IdentityUsage
 * Detects "@@IDENTITY" or "SCOPE_IDENTITY()" usage
 */
public class IdentityUsageExample {
    
    public void basicIdentityUsage() throws SQLException {
        // Basic @@IDENTITY usage
        String insertWithIdentity = "INSERT INTO users (name, email) VALUES ('John', 'john@example.com'); SELECT @@IDENTITY as new_user_id";
        String insertWithScopeIdentity = "INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); SELECT SCOPE_IDENTITY() as new_order_id";
        String insertWithIdentityCheck = "INSERT INTO products (name, price) VALUES ('Tablet', 299.99); IF @@IDENTITY IS NOT NULL SELECT 'Product created with ID: ' + CAST(@@IDENTITY AS VARCHAR) as message";
        String insertWithScopeIdentityCheck = "INSERT INTO categories (name, description) VALUES ('Electronics', 'Electronic devices'); IF SCOPE_IDENTITY() IS NOT NULL SELECT 'Category created with ID: ' + CAST(SCOPE_IDENTITY() AS VARCHAR) as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(insertWithIdentity);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(insertWithScopeIdentity);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(insertWithIdentityCheck);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        boolean hasResult4 = stmt.execute(insertWithScopeIdentityCheck);
        if (hasResult4) {
            ResultSet rs4 = stmt.getResultSet();
            rs4.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInTransaction() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in transaction
        String transactionWithIdentity = "BEGIN TRANSACTION; INSERT INTO users (name, email) VALUES ('Jane', 'jane@example.com'); DECLARE @user_id INT = SCOPE_IDENTITY(); INSERT INTO orders (user_id, product_name) VALUES (@user_id, 'Mouse'); COMMIT TRANSACTION; SELECT @user_id as created_user_id, SCOPE_IDENTITY() as created_order_id";
        String transactionWithGlobalIdentity = "BEGIN TRANSACTION; INSERT INTO products (name, price) VALUES ('Keyboard', 79.99); DECLARE @product_id INT = @@IDENTITY; INSERT INTO order_items (order_id, product_id, quantity) VALUES (1, @product_id, 2); COMMIT TRANSACTION; SELECT @product_id as created_product_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(transactionWithIdentity);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(transactionWithGlobalIdentity);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInStoredProcedures() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in stored procedure calls
        String execProcedureWithIdentity = "EXEC sp_create_user @name = 'Bob', @email = 'bob@example.com'; SELECT SCOPE_IDENTITY() as new_user_id";
        String execProcedureComplex = "EXEC sp_create_order_with_items @user_id = 1, @product_name = 'Laptop'; SELECT @@IDENTITY as new_order_id, SCOPE_IDENTITY() as last_identity";
        String executeProcedureWithIdentity = "EXECUTE sp_bulk_insert_products @product_data = 'data'; SELECT @@IDENTITY as last_product_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execProcedureWithIdentity);
        boolean hasResult1 = cstmt1.execute();
        if (hasResult1) {
            ResultSet rs1 = cstmt1.getResultSet();
            rs1.close();
        }
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(execProcedureComplex);
        boolean hasResult2 = cstmt2.execute();
        if (hasResult2) {
            ResultSet rs2 = cstmt2.getResultSet();
            rs2.close();
        }
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(executeProcedureWithIdentity);
        boolean hasResult3 = cstmt3.execute();
        if (hasResult3) {
            ResultSet rs3 = cstmt3.getResultSet();
            rs3.close();
        }
        cstmt3.close();
        
        conn.close();
    }
    
    public void identityInBatchOperations() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in batch operations
        String batchInsertWithIdentity = "INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com'); DECLARE @user1_id INT = SCOPE_IDENTITY(); INSERT INTO users (name, email) VALUES ('Charlie', 'charlie@example.com'); DECLARE @user2_id INT = SCOPE_IDENTITY(); SELECT @user1_id as first_user_id, @user2_id as second_user_id";
        String batchInsertWithGlobalIdentity = "INSERT INTO products (name, price) VALUES ('Monitor', 199.99); DECLARE @product1_id INT = @@IDENTITY; INSERT INTO products (name, price) VALUES ('Speaker', 49.99); DECLARE @product2_id INT = @@IDENTITY; SELECT @product1_id as first_product_id, @product2_id as second_product_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(batchInsertWithIdentity);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(batchInsertWithGlobalIdentity);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInConditionalLogic() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in conditional logic
        String conditionalInsertWithIdentity = "INSERT INTO users (name, email) VALUES ('David', 'david@example.com'); IF SCOPE_IDENTITY() IS NOT NULL BEGIN SELECT 'User created successfully with ID: ' + CAST(SCOPE_IDENTITY() AS VARCHAR) as message; INSERT INTO user_profiles (user_id, bio) VALUES (SCOPE_IDENTITY(), 'New user profile'); END ELSE SELECT 'User creation failed' as message";
        String conditionalInsertWithGlobalIdentity = "INSERT INTO orders (user_id, product_name) VALUES (1, 'Headphones'); IF @@IDENTITY IS NOT NULL BEGIN SELECT 'Order created with ID: ' + CAST(@@IDENTITY AS VARCHAR) as message; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; END ELSE SELECT 'Order creation failed' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(conditionalInsertWithIdentity);
        while (hasResult1) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult1 = stmt.getMoreResults();
        }
        
        boolean hasResult2 = stmt.execute(conditionalInsertWithGlobalIdentity);
        while (hasResult2) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult2 = stmt.getMoreResults();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInErrorHandling() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in error handling
        String errorHandlingWithIdentity = "BEGIN TRY INSERT INTO users (name, email) VALUES ('Eve', 'eve@example.com'); DECLARE @user_id INT = SCOPE_IDENTITY(); IF @user_id IS NOT NULL SELECT 'User created with ID: ' + CAST(@user_id AS VARCHAR) as success_message; END TRY BEGIN CATCH SELECT 'Error occurred: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String errorHandlingComplex = "BEGIN TRY INSERT INTO products (name, price) VALUES ('Invalid Product', -10.00); DECLARE @product_id INT = @@IDENTITY; IF @product_id IS NOT NULL BEGIN INSERT INTO product_log (product_id, action) VALUES (@product_id, 'CREATED'); SELECT 'Product and log created' as message; END END TRY BEGIN CATCH SELECT 'Error: ' + ERROR_MESSAGE() as error_message; END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(errorHandlingWithIdentity);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(errorHandlingComplex);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInValidation() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in validation
        String validationWithIdentity = "INSERT INTO users (name, email) VALUES ('Frank', 'frank@example.com'); DECLARE @user_id INT = SCOPE_IDENTITY(); IF @user_id > 0 BEGIN SELECT 'User validation passed' as validation_message; INSERT INTO user_audit (user_id, action, timestamp) VALUES (@user_id, 'CREATED', GETDATE()); END ELSE SELECT 'User validation failed' as validation_message";
        String validationComplex = "INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Tablet', 1); DECLARE @order_id INT = @@IDENTITY; IF @order_id IS NOT NULL AND @order_id > 0 BEGIN SELECT 'Order validation passed' as message; UPDATE users SET last_order_date = GETDATE() WHERE id = 1; SELECT @order_id as validated_order_id; END ELSE SELECT 'Order validation failed' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(validationWithIdentity);
        while (hasResult1) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult1 = stmt.getMoreResults();
        }
        
        boolean hasResult2 = stmt.execute(validationComplex);
        while (hasResult2) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult2 = stmt.getMoreResults();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInLogging() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in logging
        String loggingWithIdentity = "INSERT INTO users (name, email) VALUES ('Grace', 'grace@example.com'); DECLARE @user_id INT = SCOPE_IDENTITY(); INSERT INTO audit_log (table_name, record_id, action, timestamp) VALUES ('users', @user_id, 'INSERT', GETDATE()); SELECT @user_id as logged_user_id";
        String loggingComplex = "INSERT INTO products (name, price, category_id) VALUES ('Gaming Console', 499.99, 1); DECLARE @product_id INT = @@IDENTITY; INSERT INTO product_audit (product_id, old_value, new_value, action, timestamp) VALUES (@product_id, NULL, 'Gaming Console', 'CREATED', GETDATE()); SELECT @product_id as logged_product_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(loggingWithIdentity);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(loggingComplex);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void identityInPreparedStatement() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in PreparedStatement
        String insertWithIdentityParam = "INSERT INTO users (name, email) VALUES (?, ?); SELECT SCOPE_IDENTITY() as new_user_id";
        String insertWithGlobalIdentityParam = "INSERT INTO orders (user_id, product_name, quantity) VALUES (?, ?, ?); SELECT @@IDENTITY as new_order_id";
        String insertWithIdentityCheckParam = "INSERT INTO products (name, price) VALUES (?, ?); IF @@IDENTITY IS NOT NULL SELECT 'Product created with ID: ' + CAST(@@IDENTITY AS VARCHAR) as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(insertWithIdentityParam);
        pstmt1.setString(1, "Henry");
        pstmt1.setString(2, "henry@example.com");
        boolean hasResult1 = pstmt1.execute();
        if (hasResult1) {
            ResultSet rs1 = pstmt1.getResultSet();
            rs1.close();
        }
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(insertWithGlobalIdentityParam);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "Laptop");
        pstmt2.setInt(3, 1);
        boolean hasResult2 = pstmt2.execute();
        if (hasResult2) {
            ResultSet rs2 = pstmt2.getResultSet();
            rs2.close();
        }
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(insertWithIdentityCheckParam);
        pstmt3.setString(1, "Smartphone");
        pstmt3.setDouble(2, 699.99);
        boolean hasResult3 = pstmt3.execute();
        if (hasResult3) {
            ResultSet rs3 = pstmt3.getResultSet();
            rs3.close();
        }
        pstmt3.close();
        
        conn.close();
    }
    
    public void identityInMethods() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() in methods
        String userQuery = getUserQueryWithIdentity();
        String orderQuery = getOrderQueryWithIdentity();
        String productQuery = getProductQueryWithIdentity();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(userQuery);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(orderQuery);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(productQuery);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    private String getUserQueryWithIdentity() {
        return "INSERT INTO users (name, email, status) VALUES ('Ivy', 'ivy@example.com', 'active'); SELECT SCOPE_IDENTITY() as new_user_id";
    }
    
    private String getOrderQueryWithIdentity() {
        return "INSERT INTO orders (user_id, product_name, quantity, order_date) VALUES (1, 'Mouse', 2, GETDATE()); SELECT @@IDENTITY as new_order_id";
    }
    
    private String getProductQueryWithIdentity() {
        return "INSERT INTO products (name, price, category_id, in_stock) VALUES ('Webcam', 99.99, 1, 1); IF @@IDENTITY IS NOT NULL SELECT 'Product created with ID: ' + CAST(@@IDENTITY AS VARCHAR) as message";
    }
    
    public void identityWithDynamicSQL() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() with dynamic SQL construction
        String baseInsert = "INSERT INTO users (name, email) VALUES (";
        String values = "'Jack', 'jack@example.com')";
        String identitySelect = "; SELECT SCOPE_IDENTITY() as new_user_id";
        
        String dynamicQuery = baseInsert + values + identitySelect;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        boolean hasResult = stmt.execute(dynamicQuery);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        stmt.close();
        conn.close();
    }
    
    public void identityWithDataMigration() throws SQLException {
        // @@IDENTITY and SCOPE_IDENTITY() with data migration scenarios
        String migrationWithIdentity = "INSERT INTO new_users (name, email, migrated_from) VALUES ('Kate', 'kate@example.com', 'legacy_system'); DECLARE @new_user_id INT = SCOPE_IDENTITY(); INSERT INTO migration_log (old_id, new_id, table_name, migration_date) VALUES (999, @new_user_id, 'users', GETDATE()); SELECT @new_user_id as migrated_user_id";
        String migrationComplex = "INSERT INTO new_products (name, price, legacy_id) VALUES ('Legacy Product', 199.99, 888); DECLARE @new_product_id INT = @@IDENTITY; UPDATE legacy_product_mapping SET new_product_id = @new_product_id WHERE legacy_product_id = 888; SELECT @new_product_id as migrated_product_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(migrationWithIdentity);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(migrationComplex);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
