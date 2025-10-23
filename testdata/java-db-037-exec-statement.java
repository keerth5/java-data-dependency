package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-037: ExecStatementUsage
 * Detects "EXEC" or "EXECUTE" statements for stored procedures
 */
public class ExecStatementUsageExample {
    
    public void basicExecStatementUsage() throws SQLException {
        // Basic EXEC statement usage
        String execProcedure = "EXEC sp_get_user_orders @user_id = 1";
        String executeProcedure = "EXECUTE sp_create_user @name = 'John', @email = 'john@example.com'";
        String execWithParams = "EXEC sp_update_product_price @product_id = 1, @new_price = 99.99";
        String executeWithParams = "EXECUTE sp_delete_old_orders @cutoff_date = '2023-01-01'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(execProcedure);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        stmt.execute(executeProcedure);
        stmt.execute(execWithParams);
        stmt.execute(executeWithParams);
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithOutputParameters() throws SQLException {
        // EXEC statement with output parameters
        String execWithOutput = "EXEC sp_get_user_count @total_users OUTPUT";
        String executeWithOutput = "EXECUTE sp_calculate_total_sales @start_date = '2024-01-01', @end_date = '2024-12-31', @total_sales OUTPUT";
        String execWithMultipleOutput = "EXEC sp_get_statistics @user_count OUTPUT, @order_count OUTPUT, @product_count OUTPUT";
        String executeWithMultipleOutput = "EXECUTE sp_get_monthly_report @month = 1, @year = 2024, @total_revenue OUTPUT, @total_orders OUTPUT";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execWithOutput);
        cstmt1.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt1.execute();
        int userCount = cstmt1.getInt(1);
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(executeWithOutput);
        cstmt2.setString(1, "2024-01-01");
        cstmt2.setString(2, "2024-12-31");
        cstmt2.registerOutParameter(3, java.sql.Types.DECIMAL);
        cstmt2.execute();
        double totalSales = cstmt2.getDouble(3);
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(execWithMultipleOutput);
        cstmt3.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt3.registerOutParameter(2, java.sql.Types.INTEGER);
        cstmt3.registerOutParameter(3, java.sql.Types.INTEGER);
        cstmt3.execute();
        int userCount2 = cstmt3.getInt(1);
        int orderCount = cstmt3.getInt(2);
        int productCount = cstmt3.getInt(3);
        cstmt3.close();
        
        java.sql.CallableStatement cstmt4 = conn.prepareCall(executeWithMultipleOutput);
        cstmt4.setInt(1, 1);
        cstmt4.setInt(2, 2024);
        cstmt4.registerOutParameter(3, java.sql.Types.DECIMAL);
        cstmt4.registerOutParameter(4, java.sql.Types.INTEGER);
        cstmt4.execute();
        double totalRevenue = cstmt4.getDouble(3);
        int totalOrders = cstmt4.getInt(4);
        cstmt4.close();
        
        conn.close();
    }
    
    public void execStatementWithReturnValues() throws SQLException {
        // EXEC statement with return values
        String execWithReturn = "EXEC @return_code = sp_validate_user @user_id = 1";
        String executeWithReturn = "EXECUTE @result = sp_process_order @order_id = 1";
        String execWithReturnCheck = "EXEC @status = sp_check_product_availability @product_id = 1, @quantity = 5";
        String executeWithReturnCheck = "EXECUTE @error_code = sp_validate_email @email = 'test@example.com'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execWithReturn);
        cstmt1.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt1.setInt(2, 1);
        cstmt1.execute();
        int returnCode = cstmt1.getInt(1);
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(executeWithReturn);
        cstmt2.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt2.setInt(2, 1);
        cstmt2.execute();
        int result = cstmt2.getInt(1);
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(execWithReturnCheck);
        cstmt3.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt3.setInt(2, 1);
        cstmt3.setInt(3, 5);
        cstmt3.execute();
        int status = cstmt3.getInt(1);
        cstmt3.close();
        
        java.sql.CallableStatement cstmt4 = conn.prepareCall(executeWithReturnCheck);
        cstmt4.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt4.setString(2, "test@example.com");
        cstmt4.execute();
        int errorCode = cstmt4.getInt(1);
        cstmt4.close();
        
        conn.close();
    }
    
    public void execStatementWithDynamicSQL() throws SQLException {
        // EXEC statement with dynamic SQL
        String execDynamicSQL = "EXEC sp_executesql N'SELECT * FROM users WHERE id = @user_id', N'@user_id INT', @user_id = 1";
        String executeDynamicSQL = "EXECUTE sp_executesql N'INSERT INTO orders (user_id, product_name) VALUES (@user_id, @product_name)', N'@user_id INT, @product_name NVARCHAR(50)', @user_id = 1, @product_name = 'Laptop'";
        String execDynamicWithParams = "EXEC sp_executesql N'UPDATE products SET price = @new_price WHERE id = @product_id', N'@product_id INT, @new_price DECIMAL(10,2)', @product_id = 1, @new_price = 99.99";
        String executeDynamicWithParams = "EXECUTE sp_executesql N'DELETE FROM users WHERE id = @user_id', N'@user_id INT', @user_id = 1";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(execDynamicSQL);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        stmt.execute(executeDynamicSQL);
        stmt.execute(execDynamicWithParams);
        stmt.execute(executeDynamicWithParams);
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithTransaction() throws SQLException {
        // EXEC statement with transaction
        String execInTransaction = "BEGIN TRANSACTION; EXEC sp_create_user_with_profile @name = 'John', @email = 'john@example.com'; COMMIT TRANSACTION";
        String executeInTransaction = "BEGIN TRAN; EXECUTE sp_process_order_batch @order_ids = '1,2,3'; COMMIT TRAN";
        String execWithTransactionRollback = "BEGIN TRANSACTION; EXEC sp_update_user_status @user_id = 1, @status = 'active'; IF @@ERROR != 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
        String executeWithTransactionRollback = "BEGIN TRAN; EXECUTE sp_bulk_delete_products @category_id = 1; IF @@ROWCOUNT = 0 ROLLBACK TRAN ELSE COMMIT TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(execInTransaction);
        stmt.execute(executeInTransaction);
        stmt.execute(execWithTransactionRollback);
        stmt.execute(executeWithTransactionRollback);
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithErrorHandling() throws SQLException {
        // EXEC statement with error handling
        String execWithTryCatch = "BEGIN TRY EXEC sp_create_user @name = 'John', @email = 'invalid-email'; END TRY BEGIN CATCH SELECT 'Error occurred: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String executeWithTryCatch = "BEGIN TRY EXECUTE sp_update_product @product_id = 999, @price = 99.99; END TRY BEGIN CATCH SELECT 'Error: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String execWithErrorCheck = "EXEC sp_validate_data @table_name = 'users'; IF @@ERROR != 0 SELECT 'Validation failed' as message ELSE SELECT 'Validation passed' as message";
        String executeWithErrorCheck = "EXECUTE sp_process_batch @batch_id = 1; IF @@ERROR != 0 SELECT 'Batch processing failed' as message ELSE SELECT 'Batch processing completed' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(execWithTryCatch);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(executeWithTryCatch);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(execWithErrorCheck);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        boolean hasResult4 = stmt.execute(executeWithErrorCheck);
        if (hasResult4) {
            ResultSet rs4 = stmt.getResultSet();
            rs4.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithConditionalLogic() throws SQLException {
        // EXEC statement with conditional logic
        String conditionalExec = "IF EXISTS (SELECT 1 FROM users WHERE id = 1) BEGIN EXEC sp_update_user_last_login @user_id = 1; SELECT 'User login updated' as message; END ELSE BEGIN SELECT 'User not found' as message; END";
        String conditionalExecute = "IF (SELECT COUNT(*) FROM products WHERE category_id = 1) > 0 BEGIN EXECUTE sp_update_category_prices @category_id = 1, @increase_percent = 10; SELECT 'Prices updated' as message; END ELSE BEGIN SELECT 'No products found' as message; END";
        String conditionalExecWithParams = "DECLARE @user_id INT = 1; IF @user_id > 0 BEGIN EXEC sp_get_user_details @user_id = @user_id; END ELSE BEGIN SELECT 'Invalid user ID' as message; END";
        String conditionalExecuteWithParams = "DECLARE @order_count INT = (SELECT COUNT(*) FROM orders WHERE user_id = 1); IF @order_count > 0 BEGIN EXECUTE sp_get_user_orders @user_id = 1; END ELSE BEGIN SELECT 'No orders found' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(conditionalExec);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(conditionalExecute);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(conditionalExecWithParams);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        boolean hasResult4 = stmt.execute(conditionalExecuteWithParams);
        if (hasResult4) {
            ResultSet rs4 = stmt.getResultSet();
            rs4.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithValidation() throws SQLException {
        // EXEC statement with validation
        String validationExec = "DECLARE @user_id INT = 1; IF EXISTS (SELECT 1 FROM users WHERE id = @user_id AND status = 'active') BEGIN EXEC sp_update_user_activity @user_id = @user_id; SELECT 'User activity updated' as validation_message; END ELSE BEGIN SELECT 'User validation failed' as validation_message; END";
        String validationExecute = "DECLARE @product_id INT = 1; IF (SELECT COUNT(*) FROM products WHERE id = @product_id AND discontinued = 0) = 1 BEGIN EXECUTE sp_update_product_stock @product_id = @product_id, @quantity = 10; SELECT 'Product stock updated' as message; END ELSE BEGIN SELECT 'Product validation failed' as message; END";
        String validationExecComplex = "DECLARE @order_id INT = 1; DECLARE @user_id INT = (SELECT user_id FROM orders WHERE id = @order_id); IF @user_id IS NOT NULL AND EXISTS (SELECT 1 FROM users WHERE id = @user_id) BEGIN EXEC sp_process_order @order_id = @order_id; SELECT 'Order processed' as message; END ELSE BEGIN SELECT 'Order validation failed' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(validationExec);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(validationExecute);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(validationExecComplex);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithLogging() throws SQLException {
        // EXEC statement with logging
        String loggingExec = "EXEC sp_create_user @name = 'John', @email = 'john@example.com'; INSERT INTO audit_log (action, timestamp) VALUES ('USER_CREATED', GETDATE()); SELECT 'User created and logged' as message";
        String loggingExecute = "EXECUTE sp_update_product @product_id = 1, @price = 99.99; INSERT INTO system_log (operation, timestamp) VALUES ('PRODUCT_UPDATED', GETDATE()); SELECT 'Product updated and logged' as message";
        String loggingExecComplex = "DECLARE @user_id INT; EXEC sp_create_user_with_return @name = 'Jane', @email = 'jane@example.com', @user_id = @user_id OUTPUT; INSERT INTO audit_log (table_name, record_id, action, timestamp) VALUES ('users', @user_id, 'INSERT', GETDATE()); SELECT @user_id as created_user_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(loggingExec);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(loggingExecute);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(loggingExecComplex);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementInMethods() throws SQLException {
        // EXEC statement in methods
        String userExec = getUserExecQuery();
        String orderExec = getOrderExecQuery();
        String productExec = getProductExecQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(userExec);
        stmt.execute(orderExec);
        stmt.execute(productExec);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserExecQuery() {
        return "EXEC sp_get_user_details @user_id = 1";
    }
    
    private String getOrderExecQuery() {
        return "EXECUTE sp_create_order @user_id = 1, @product_name = 'Laptop', @quantity = 1";
    }
    
    private String getProductExecQuery() {
        return "EXEC sp_update_product_price @product_id = 1, @new_price = 99.99";
    }
    
    public void execStatementWithDynamicSQL() throws SQLException {
        // EXEC statement with dynamic SQL construction
        String baseExec = "EXEC sp_get_";
        String procedureName = "user_orders";
        String params = " @user_id = 1";
        
        String dynamicExec = baseExec + procedureName + params;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        boolean hasResult = stmt.execute(dynamicExec);
        if (hasResult) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
        }
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithPerformanceMonitoring() throws SQLException {
        // EXEC statement with performance monitoring
        String performanceExec = "DECLARE @start_time DATETIME = GETDATE(); EXEC sp_bulk_update_users @status = 'active'; INSERT INTO performance_log (operation, execution_time, timestamp) VALUES ('BULK_USER_UPDATE', DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE())";
        String performanceExecute = "DECLARE @start_time DATETIME = GETDATE(); EXECUTE sp_process_orders_batch @batch_size = 100; INSERT INTO performance_log (operation, execution_time, timestamp) VALUES ('BATCH_ORDER_PROCESSING', DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(performanceExec);
        stmt.execute(performanceExecute);
        
        stmt.close();
        conn.close();
    }
    
    public void execStatementWithBusinessLogic() throws SQLException {
        // EXEC statement with business logic
        String businessLogicExec = "DECLARE @user_id INT = 1; DECLARE @order_count INT; EXEC sp_get_user_order_count @user_id = @user_id, @order_count = @order_count OUTPUT; IF @order_count > 10 BEGIN EXEC sp_apply_vip_discount @user_id = @user_id; SELECT 'VIP discount applied' as message; END ELSE BEGIN SELECT 'Standard user' as message; END";
        String businessLogicExecute = "DECLARE @product_id INT = 1; DECLARE @stock_level INT; EXECUTE sp_get_product_stock @product_id = @product_id, @stock_level = @stock_level OUTPUT; IF @stock_level < 10 BEGIN EXECUTE sp_reorder_product @product_id = @product_id; SELECT 'Product reordered' as message; END ELSE BEGIN SELECT 'Stock level adequate' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(businessLogicExec);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(businessLogicExecute);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
