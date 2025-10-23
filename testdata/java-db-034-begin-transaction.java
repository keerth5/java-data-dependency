package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-034: BeginTransactionUsage
 * Detects "BEGIN TRANSACTION" or "BEGIN TRAN" statements
 */
public class BeginTransactionUsageExample {
    
    public void basicBeginTransactionUsage() throws SQLException {
        // Basic BEGIN TRANSACTION usage
        String beginTransaction = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; COMMIT TRANSACTION";
        String beginTran = "BEGIN TRAN; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); COMMIT TRAN";
        String beginTransactionWithRollback = "BEGIN TRANSACTION; DELETE FROM products WHERE discontinued = 1; ROLLBACK TRANSACTION";
        String beginTranWithRollback = "BEGIN TRAN; UPDATE users SET email = 'test@example.com' WHERE id = 1; ROLLBACK TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(beginTransaction);
        stmt.execute(beginTran);
        stmt.execute(beginTransactionWithRollback);
        stmt.execute(beginTranWithRollback);
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionInStoredProcedures() throws SQLException {
        // BEGIN TRANSACTION in stored procedure calls
        String execProcedureWithTransaction = "EXEC sp_begin_user_transaction @user_id = 1, @action = 'UPDATE'";
        String execProcedureComplex = "EXEC sp_process_order_with_transaction @order_id = 1, @status = 'PROCESSING'";
        String executeProcedureWithTransaction = "EXECUTE sp_bulk_update_with_transaction @table_name = 'users', @status = 'active'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execProcedureWithTransaction);
        cstmt1.execute();
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(execProcedureComplex);
        cstmt2.execute();
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(executeProcedureWithTransaction);
        cstmt3.execute();
        cstmt3.close();
        
        conn.close();
    }
    
    public void beginTransactionWithNestedTransactions() throws SQLException {
        // BEGIN TRANSACTION with nested transactions
        String nestedTransaction = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; BEGIN TRANSACTION; INSERT INTO audit_log (action, timestamp) VALUES ('USER_ACTIVATED', GETDATE()); COMMIT TRANSACTION; COMMIT TRANSACTION";
        String nestedTran = "BEGIN TRAN; INSERT INTO orders (user_id, product_name) VALUES (1, 'Mouse'); BEGIN TRAN; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; COMMIT TRAN; COMMIT TRAN";
        String nestedTransactionWithRollback = "BEGIN TRANSACTION; UPDATE products SET price = price * 1.1; BEGIN TRANSACTION; INSERT INTO price_history (product_id, old_price, new_price) SELECT id, price/1.1, price FROM products; ROLLBACK TRANSACTION; COMMIT TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(nestedTransaction);
        stmt.execute(nestedTran);
        stmt.execute(nestedTransactionWithRollback);
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithSavepoints() throws SQLException {
        // BEGIN TRANSACTION with savepoints
        String transactionWithSavepoint = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; SAVE TRANSACTION savepoint1; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); ROLLBACK TRANSACTION savepoint1; COMMIT TRANSACTION";
        String transactionWithMultipleSavepoints = "BEGIN TRANSACTION; INSERT INTO products (name, price) VALUES ('Tablet', 299.99); SAVE TRANSACTION sp1; UPDATE products SET price = price * 1.1; SAVE TRANSACTION sp2; DELETE FROM products WHERE price > 1000; ROLLBACK TRANSACTION sp2; COMMIT TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(transactionWithSavepoint);
        stmt.execute(transactionWithMultipleSavepoints);
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithErrorHandling() throws SQLException {
        // BEGIN TRANSACTION with error handling
        String transactionWithTryCatch = "BEGIN TRANSACTION; BEGIN TRY UPDATE users SET email = 'new@example.com' WHERE id = 1; INSERT INTO audit_log (action, timestamp) VALUES ('EMAIL_UPDATED', GETDATE()); COMMIT TRANSACTION; END TRY BEGIN CATCH ROLLBACK TRANSACTION; SELECT 'Error occurred: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String transactionWithErrorHandling = "BEGIN TRANSACTION; BEGIN TRY INSERT INTO orders (user_id, product_name) VALUES (1, 'Keyboard'); UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; COMMIT TRANSACTION; SELECT 'Transaction completed successfully' as message; END TRY BEGIN CATCH ROLLBACK TRANSACTION; SELECT 'Transaction failed: ' + ERROR_MESSAGE() as error_message; END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(transactionWithTryCatch);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(transactionWithErrorHandling);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithConditionalLogic() throws SQLException {
        // BEGIN TRANSACTION with conditional logic
        String conditionalTransaction = "BEGIN TRANSACTION; IF EXISTS (SELECT 1 FROM users WHERE id = 1) BEGIN UPDATE users SET last_login = GETDATE() WHERE id = 1; INSERT INTO login_log (user_id, login_time) VALUES (1, GETDATE()); COMMIT TRANSACTION; SELECT 'User login recorded' as message; END ELSE BEGIN ROLLBACK TRANSACTION; SELECT 'User not found' as message; END";
        String conditionalTran = "BEGIN TRAN; IF (SELECT COUNT(*) FROM products WHERE category_id = 1) > 0 BEGIN UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO price_update_log (category_id, update_time) VALUES (1, GETDATE()); COMMIT TRAN; SELECT 'Prices updated' as message; END ELSE BEGIN ROLLBACK TRAN; SELECT 'No products found' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(conditionalTransaction);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(conditionalTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithValidation() throws SQLException {
        // BEGIN TRANSACTION with validation
        String validationTransaction = "BEGIN TRANSACTION; DECLARE @user_id INT = 1; IF EXISTS (SELECT 1 FROM users WHERE id = @user_id AND status = 'active') BEGIN UPDATE users SET last_activity = GETDATE() WHERE id = @user_id; INSERT INTO activity_log (user_id, activity_type, timestamp) VALUES (@user_id, 'ACTIVITY_UPDATE', GETDATE()); COMMIT TRANSACTION; SELECT 'User activity updated' as validation_message; END ELSE BEGIN ROLLBACK TRANSACTION; SELECT 'User validation failed' as validation_message; END";
        String validationTran = "BEGIN TRAN; DECLARE @order_id INT = 1; IF (SELECT COUNT(*) FROM orders WHERE id = @order_id AND status = 'pending') = 1 BEGIN UPDATE orders SET status = 'processing' WHERE id = @order_id; INSERT INTO order_status_log (order_id, old_status, new_status, timestamp) VALUES (@order_id, 'pending', 'processing', GETDATE()); COMMIT TRAN; SELECT 'Order status updated' as message; END ELSE BEGIN ROLLBACK TRAN; SELECT 'Order validation failed' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(validationTransaction);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(validationTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithLogging() throws SQLException {
        // BEGIN TRANSACTION with logging
        String loggingTransaction = "BEGIN TRANSACTION; INSERT INTO users (name, email) VALUES ('John', 'john@example.com'); DECLARE @user_id INT = SCOPE_IDENTITY(); INSERT INTO audit_log (table_name, record_id, action, timestamp) VALUES ('users', @user_id, 'INSERT', GETDATE()); COMMIT TRANSACTION; SELECT @user_id as created_user_id";
        String loggingTran = "BEGIN TRAN; UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO system_log (operation, affected_rows, timestamp) VALUES ('PRICE_UPDATE', @@ROWCOUNT, GETDATE()); COMMIT TRAN; SELECT 'Price update logged' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(loggingTransaction);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(loggingTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithPreparedStatement() throws SQLException {
        // BEGIN TRANSACTION with PreparedStatement
        String transactionWithParam = "BEGIN TRANSACTION; UPDATE users SET status = ? WHERE id = ?; COMMIT TRANSACTION";
        String transactionWithParamRollback = "BEGIN TRANSACTION; INSERT INTO orders (user_id, product_name, quantity) VALUES (?, ?, ?); ROLLBACK TRANSACTION";
        String transactionWithParamConditional = "BEGIN TRANSACTION; IF ? = 1 BEGIN UPDATE users SET last_login = GETDATE() WHERE id = ?; COMMIT TRANSACTION; END ELSE BEGIN ROLLBACK TRANSACTION; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(transactionWithParam);
        pstmt1.setString(1, "active");
        pstmt1.setInt(2, 1);
        pstmt1.execute();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(transactionWithParamRollback);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "Laptop");
        pstmt2.setInt(3, 1);
        pstmt2.execute();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(transactionWithParamConditional);
        pstmt3.setInt(1, 1);
        pstmt3.setInt(2, 1);
        pstmt3.execute();
        pstmt3.close();
        
        conn.close();
    }
    
    public void beginTransactionInMethods() throws SQLException {
        // BEGIN TRANSACTION in methods
        String userTransaction = getUserTransactionQuery();
        String orderTransaction = getOrderTransactionQuery();
        String productTransaction = getProductTransactionQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(userTransaction);
        stmt.execute(orderTransaction);
        stmt.execute(productTransaction);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserTransactionQuery() {
        return "BEGIN TRANSACTION; UPDATE users SET last_login = GETDATE() WHERE id = 1; INSERT INTO login_log (user_id, login_time) VALUES (1, GETDATE()); COMMIT TRANSACTION";
    }
    
    private String getOrderTransactionQuery() {
        return "BEGIN TRAN; INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Mouse', 2); UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; COMMIT TRAN";
    }
    
    private String getProductTransactionQuery() {
        return "BEGIN TRANSACTION; UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO price_history (category_id, update_time) VALUES (1, GETDATE()); COMMIT TRANSACTION";
    }
    
    public void beginTransactionWithDynamicSQL() throws SQLException {
        // BEGIN TRANSACTION with dynamic SQL construction
        String baseTransaction = "BEGIN TRANSACTION; UPDATE users SET status = '";
        String status = "active";
        String whereClause = "' WHERE id = 1; COMMIT TRANSACTION";
        
        String dynamicTransaction = baseTransaction + status + whereClause;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        stmt.execute(dynamicTransaction);
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithPerformanceMonitoring() throws SQLException {
        // BEGIN TRANSACTION with performance monitoring
        String performanceTransaction = "BEGIN TRANSACTION; DECLARE @start_time DATETIME = GETDATE(); UPDATE users SET last_activity = GETDATE() WHERE status = 'active'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('UPDATE_USER_ACTIVITY', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE()); COMMIT TRANSACTION";
        String performanceTran = "BEGIN TRAN; DECLARE @start_time DATETIME = GETDATE(); INSERT INTO orders (user_id, product_name) SELECT id, 'Bulk Order' FROM users WHERE status = 'active'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('BULK_ORDER_INSERT', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE()); COMMIT TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(performanceTransaction);
        stmt.execute(performanceTran);
        
        stmt.close();
        conn.close();
    }
    
    public void beginTransactionWithDataConsistency() throws SQLException {
        // BEGIN TRANSACTION with data consistency scenarios
        String consistencyTransaction = "BEGIN TRANSACTION; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Laptop', 1); IF @@ROWCOUNT = 2 BEGIN COMMIT TRANSACTION; SELECT 'Data consistency maintained' as message; END ELSE BEGIN ROLLBACK TRANSACTION; SELECT 'Data consistency failed' as message; END";
        String consistencyTran = "BEGIN TRAN; DELETE FROM expired_sessions WHERE expiry_date < GETDATE(); UPDATE users SET last_activity = GETDATE() WHERE id IN (SELECT user_id FROM expired_sessions WHERE expiry_date < GETDATE()); IF @@ROWCOUNT > 0 BEGIN COMMIT TRAN; SELECT 'Session cleanup completed' as message; END ELSE BEGIN ROLLBACK TRAN; SELECT 'Session cleanup failed' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(consistencyTransaction);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(consistencyTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
