package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-036: RollbackTransactionUsage
 * Detects "ROLLBACK TRANSACTION" or "ROLLBACK TRAN" statements
 */
public class RollbackTransactionUsageExample {
    
    public void basicRollbackTransactionUsage() throws SQLException {
        // Basic ROLLBACK TRANSACTION usage
        String rollbackTransaction = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; ROLLBACK TRANSACTION";
        String rollbackTran = "BEGIN TRAN; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); ROLLBACK TRAN";
        String rollbackTransactionWithValidation = "BEGIN TRANSACTION; DELETE FROM products WHERE discontinued = 1; IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
        String rollbackTranWithValidation = "BEGIN TRAN; UPDATE users SET email = 'test@example.com' WHERE id = 1; IF @@ROWCOUNT = 0 ROLLBACK TRAN ELSE COMMIT TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(rollbackTransaction);
        stmt.execute(rollbackTran);
        stmt.execute(rollbackTransactionWithValidation);
        stmt.execute(rollbackTranWithValidation);
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionInStoredProcedures() throws SQLException {
        // ROLLBACK TRANSACTION in stored procedure calls
        String execProcedureWithRollback = "EXEC sp_rollback_user_update @user_id = 1, @status = 'inactive'";
        String execProcedureComplex = "EXEC sp_process_order_with_rollback @order_id = 1, @status = 'FAILED'";
        String executeProcedureWithRollback = "EXECUTE sp_bulk_update_with_rollback @table_name = 'users', @status = 'error'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execProcedureWithRollback);
        cstmt1.execute();
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(execProcedureComplex);
        cstmt2.execute();
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(executeProcedureWithRollback);
        cstmt3.execute();
        cstmt3.close();
        
        conn.close();
    }
    
    public void rollbackTransactionWithNestedTransactions() throws SQLException {
        // ROLLBACK TRANSACTION with nested transactions
        String nestedRollbackTransaction = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; BEGIN TRANSACTION; INSERT INTO audit_log (action, timestamp) VALUES ('USER_ACTIVATED', GETDATE()); ROLLBACK TRANSACTION; ROLLBACK TRANSACTION";
        String nestedRollbackTran = "BEGIN TRAN; INSERT INTO orders (user_id, product_name) VALUES (1, 'Mouse'); BEGIN TRAN; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; ROLLBACK TRAN; ROLLBACK TRAN";
        String nestedRollbackWithConditional = "BEGIN TRANSACTION; UPDATE products SET price = price * 1.1; BEGIN TRANSACTION; INSERT INTO price_history (product_id, old_price, new_price) SELECT id, price/1.1, price FROM products; IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION; ROLLBACK TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(nestedRollbackTransaction);
        stmt.execute(nestedRollbackTran);
        stmt.execute(nestedRollbackWithConditional);
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithSavepoints() throws SQLException {
        // ROLLBACK TRANSACTION with savepoints
        String rollbackWithSavepoint = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; SAVE TRANSACTION savepoint1; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); ROLLBACK TRANSACTION savepoint1; COMMIT TRANSACTION";
        String rollbackWithMultipleSavepoints = "BEGIN TRANSACTION; INSERT INTO products (name, price) VALUES ('Tablet', 299.99); SAVE TRANSACTION sp1; UPDATE products SET price = price * 1.1; SAVE TRANSACTION sp2; DELETE FROM products WHERE price > 1000; ROLLBACK TRANSACTION sp2; ROLLBACK TRANSACTION sp1; COMMIT TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(rollbackWithSavepoint);
        stmt.execute(rollbackWithMultipleSavepoints);
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithErrorHandling() throws SQLException {
        // ROLLBACK TRANSACTION with error handling
        String rollbackWithTryCatch = "BEGIN TRANSACTION; BEGIN TRY UPDATE users SET email = 'invalid-email' WHERE id = 1; INSERT INTO audit_log (action, timestamp) VALUES ('EMAIL_UPDATED', GETDATE()); COMMIT TRANSACTION; END TRY BEGIN CATCH ROLLBACK TRANSACTION; SELECT 'Error occurred: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String rollbackWithErrorHandling = "BEGIN TRANSACTION; BEGIN TRY INSERT INTO orders (user_id, product_name) VALUES (999, 'Keyboard'); UPDATE users SET total_orders = total_orders + 1 WHERE id = 999; COMMIT TRANSACTION; END TRY BEGIN CATCH ROLLBACK TRANSACTION; SELECT 'Transaction failed: ' + ERROR_MESSAGE() as error_message; END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(rollbackWithTryCatch);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(rollbackWithErrorHandling);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithConditionalLogic() throws SQLException {
        // ROLLBACK TRANSACTION with conditional logic
        String conditionalRollback = "BEGIN TRANSACTION; IF NOT EXISTS (SELECT 1 FROM users WHERE id = 1) BEGIN ROLLBACK TRANSACTION; SELECT 'User not found, transaction rolled back' as message; END ELSE BEGIN UPDATE users SET last_login = GETDATE() WHERE id = 1; INSERT INTO login_log (user_id, login_time) VALUES (1, GETDATE()); COMMIT TRANSACTION; SELECT 'User login recorded' as message; END";
        String conditionalRollbackTran = "BEGIN TRAN; IF (SELECT COUNT(*) FROM products WHERE category_id = 1) = 0 BEGIN ROLLBACK TRAN; SELECT 'No products found, transaction rolled back' as message; END ELSE BEGIN UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO price_update_log (category_id, update_time) VALUES (1, GETDATE()); COMMIT TRAN; SELECT 'Prices updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(conditionalRollback);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(conditionalRollbackTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithValidation() throws SQLException {
        // ROLLBACK TRANSACTION with validation
        String validationRollback = "BEGIN TRANSACTION; DECLARE @user_id INT = 1; IF NOT EXISTS (SELECT 1 FROM users WHERE id = @user_id AND status = 'active') BEGIN ROLLBACK TRANSACTION; SELECT 'User validation failed, transaction rolled back' as validation_message; END ELSE BEGIN UPDATE users SET last_activity = GETDATE() WHERE id = @user_id; INSERT INTO activity_log (user_id, activity_type, timestamp) VALUES (@user_id, 'ACTIVITY_UPDATE', GETDATE()); COMMIT TRANSACTION; SELECT 'User activity updated' as validation_message; END";
        String validationRollbackTran = "BEGIN TRAN; DECLARE @order_id INT = 1; IF (SELECT COUNT(*) FROM orders WHERE id = @order_id AND status = 'pending') != 1 BEGIN ROLLBACK TRAN; SELECT 'Order validation failed, transaction rolled back' as message; END ELSE BEGIN UPDATE orders SET status = 'processing' WHERE id = @order_id; INSERT INTO order_status_log (order_id, old_status, new_status, timestamp) VALUES (@order_id, 'pending', 'processing', GETDATE()); COMMIT TRAN; SELECT 'Order status updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(validationRollback);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(validationRollbackTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithLogging() throws SQLException {
        // ROLLBACK TRANSACTION with logging
        String loggingRollback = "BEGIN TRANSACTION; BEGIN TRY INSERT INTO users (name, email) VALUES ('John', 'invalid-email'); DECLARE @user_id INT = SCOPE_IDENTITY(); INSERT INTO audit_log (table_name, record_id, action, timestamp) VALUES ('users', @user_id, 'INSERT', GETDATE()); COMMIT TRANSACTION; END TRY BEGIN CATCH ROLLBACK TRANSACTION; INSERT INTO error_log (error_message, timestamp) VALUES ('User creation failed: ' + ERROR_MESSAGE(), GETDATE()); END CATCH";
        String loggingRollbackTran = "BEGIN TRAN; BEGIN TRY UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO system_log (operation, affected_rows, timestamp) VALUES ('PRICE_UPDATE', @@ROWCOUNT, GETDATE()); COMMIT TRAN; END TRY BEGIN CATCH ROLLBACK TRAN; INSERT INTO error_log (error_message, timestamp) VALUES ('Price update failed: ' + ERROR_MESSAGE(), GETDATE()); END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(loggingRollback);
        stmt.execute(loggingRollbackTran);
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithPreparedStatement() throws SQLException {
        // ROLLBACK TRANSACTION with PreparedStatement
        String rollbackWithParam = "BEGIN TRANSACTION; UPDATE users SET status = ? WHERE id = ?; IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
        String rollbackWithParamValidation = "BEGIN TRANSACTION; INSERT INTO orders (user_id, product_name, quantity) VALUES (?, ?, ?); IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
        String rollbackWithParamConditional = "BEGIN TRANSACTION; IF ? = 0 BEGIN ROLLBACK TRANSACTION; END ELSE BEGIN UPDATE users SET last_login = GETDATE() WHERE id = ?; COMMIT TRANSACTION; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(rollbackWithParam);
        pstmt1.setString(1, "active");
        pstmt1.setInt(2, 1);
        pstmt1.execute();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(rollbackWithParamValidation);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "Laptop");
        pstmt2.setInt(3, 1);
        pstmt2.execute();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(rollbackWithParamConditional);
        pstmt3.setInt(1, 0);
        pstmt3.setInt(2, 1);
        pstmt3.execute();
        pstmt3.close();
        
        conn.close();
    }
    
    public void rollbackTransactionInMethods() throws SQLException {
        // ROLLBACK TRANSACTION in methods
        String userRollback = getUserRollbackQuery();
        String orderRollback = getOrderRollbackQuery();
        String productRollback = getProductRollbackQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(userRollback);
        stmt.execute(orderRollback);
        stmt.execute(productRollback);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserRollbackQuery() {
        return "BEGIN TRANSACTION; UPDATE users SET last_login = GETDATE() WHERE id = 999; IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
    }
    
    private String getOrderRollbackQuery() {
        return "BEGIN TRAN; INSERT INTO orders (user_id, product_name, quantity) VALUES (999, 'Mouse', 2); IF @@ROWCOUNT = 0 ROLLBACK TRAN ELSE COMMIT TRAN";
    }
    
    private String getProductRollbackQuery() {
        return "BEGIN TRANSACTION; UPDATE products SET price = price * 1.1 WHERE category_id = 999; IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
    }
    
    public void rollbackTransactionWithDynamicSQL() throws SQLException {
        // ROLLBACK TRANSACTION with dynamic SQL construction
        String baseRollback = "BEGIN TRANSACTION; UPDATE users SET status = '";
        String status = "active";
        String rollbackClause = "' WHERE id = 999; IF @@ROWCOUNT = 0 ROLLBACK TRANSACTION ELSE COMMIT TRANSACTION";
        
        String dynamicRollback = baseRollback + status + rollbackClause;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        stmt.execute(dynamicRollback);
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithPerformanceMonitoring() throws SQLException {
        // ROLLBACK TRANSACTION with performance monitoring
        String performanceRollback = "BEGIN TRANSACTION; DECLARE @start_time DATETIME = GETDATE(); BEGIN TRY UPDATE users SET last_activity = GETDATE() WHERE status = 'invalid'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('UPDATE_USER_ACTIVITY', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE()); COMMIT TRANSACTION; END TRY BEGIN CATCH ROLLBACK TRANSACTION; INSERT INTO error_log (error_message, timestamp) VALUES ('Performance update failed: ' + ERROR_MESSAGE(), GETDATE()); END CATCH";
        String performanceRollbackTran = "BEGIN TRAN; DECLARE @start_time DATETIME = GETDATE(); BEGIN TRY INSERT INTO orders (user_id, product_name) SELECT id, 'Bulk Order' FROM users WHERE status = 'invalid'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('BULK_ORDER_INSERT', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE()); COMMIT TRAN; END TRY BEGIN CATCH ROLLBACK TRAN; INSERT INTO error_log (error_message, timestamp) VALUES ('Bulk insert failed: ' + ERROR_MESSAGE(), GETDATE()); END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(performanceRollback);
        stmt.execute(performanceRollbackTran);
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithDataConsistency() throws SQLException {
        // ROLLBACK TRANSACTION with data consistency scenarios
        String consistencyRollback = "BEGIN TRANSACTION; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Laptop', 1); IF @@ROWCOUNT != 2 BEGIN ROLLBACK TRANSACTION; SELECT 'Data consistency failed, transaction rolled back' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Data consistency maintained' as message; END";
        String consistencyRollbackTran = "BEGIN TRAN; DELETE FROM expired_sessions WHERE expiry_date < GETDATE(); UPDATE users SET last_activity = GETDATE() WHERE id IN (SELECT user_id FROM expired_sessions WHERE expiry_date < GETDATE()); IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRAN; SELECT 'Session cleanup failed, transaction rolled back' as message; END ELSE BEGIN COMMIT TRAN; SELECT 'Session cleanup completed' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(consistencyRollback);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(consistencyRollbackTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rollbackTransactionWithBusinessLogic() throws SQLException {
        // ROLLBACK TRANSACTION with business logic
        String businessLogicRollback = "BEGIN TRANSACTION; DECLARE @user_id INT = 1; DECLARE @current_balance DECIMAL(10,2); SELECT @current_balance = balance FROM users WHERE id = @user_id; IF @current_balance < 100.00 BEGIN ROLLBACK TRANSACTION; SELECT 'Insufficient balance, transaction rolled back' as message; END ELSE BEGIN UPDATE users SET balance = balance - 100.00 WHERE id = @user_id; INSERT INTO transactions (user_id, amount, type, timestamp) VALUES (@user_id, -100.00, 'DEBIT', GETDATE()); COMMIT TRANSACTION; SELECT 'Transaction completed' as message; END";
        String businessLogicRollbackTran = "BEGIN TRAN; DECLARE @product_id INT = 1; DECLARE @stock_quantity INT; SELECT @stock_quantity = stock_quantity FROM products WHERE id = @product_id; IF @stock_quantity < 1 BEGIN ROLLBACK TRAN; SELECT 'Insufficient stock, transaction rolled back' as message; END ELSE BEGIN UPDATE products SET stock_quantity = stock_quantity - 1 WHERE id = @product_id; INSERT INTO inventory_log (product_id, quantity_change, timestamp) VALUES (@product_id, -1, GETDATE()); COMMIT TRAN; SELECT 'Stock updated' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(businessLogicRollback);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(businessLogicRollbackTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
