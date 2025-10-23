package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-035: CommitTransactionUsage
 * Detects "COMMIT TRANSACTION" or "COMMIT TRAN" statements
 */
public class CommitTransactionUsageExample {
    
    public void basicCommitTransactionUsage() throws SQLException {
        // Basic COMMIT TRANSACTION usage
        String commitTransaction = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; COMMIT TRANSACTION";
        String commitTran = "BEGIN TRAN; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); COMMIT TRAN";
        String commitTransactionWithValidation = "BEGIN TRANSACTION; DELETE FROM products WHERE discontinued = 1; IF @@ROWCOUNT > 0 COMMIT TRANSACTION ELSE ROLLBACK TRANSACTION";
        String commitTranWithValidation = "BEGIN TRAN; UPDATE users SET email = 'new@example.com' WHERE id = 1; IF @@ROWCOUNT = 1 COMMIT TRAN ELSE ROLLBACK TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(commitTransaction);
        stmt.execute(commitTran);
        stmt.execute(commitTransactionWithValidation);
        stmt.execute(commitTranWithValidation);
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionInStoredProcedures() throws SQLException {
        // COMMIT TRANSACTION in stored procedure calls
        String execProcedureWithCommit = "EXEC sp_commit_user_update @user_id = 1, @status = 'active'";
        String execProcedureComplex = "EXEC sp_process_order_with_commit @order_id = 1, @status = 'PROCESSED'";
        String executeProcedureWithCommit = "EXECUTE sp_bulk_update_with_commit @table_name = 'users', @status = 'active'";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execProcedureWithCommit);
        cstmt1.execute();
        cstmt1.close();
        
        java.sql.CallableStatement cstmt2 = conn.prepareCall(execProcedureComplex);
        cstmt2.execute();
        cstmt2.close();
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(executeProcedureWithCommit);
        cstmt3.execute();
        cstmt3.close();
        
        conn.close();
    }
    
    public void commitTransactionWithNestedTransactions() throws SQLException {
        // COMMIT TRANSACTION with nested transactions
        String nestedCommitTransaction = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; BEGIN TRANSACTION; INSERT INTO audit_log (action, timestamp) VALUES ('USER_ACTIVATED', GETDATE()); COMMIT TRANSACTION; COMMIT TRANSACTION";
        String nestedCommitTran = "BEGIN TRAN; INSERT INTO orders (user_id, product_name) VALUES (1, 'Mouse'); BEGIN TRAN; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; COMMIT TRAN; COMMIT TRAN";
        String nestedCommitWithConditional = "BEGIN TRANSACTION; UPDATE products SET price = price * 1.1; BEGIN TRANSACTION; INSERT INTO price_history (product_id, old_price, new_price) SELECT id, price/1.1, price FROM products; IF @@ROWCOUNT > 0 COMMIT TRANSACTION ELSE ROLLBACK TRANSACTION; COMMIT TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(nestedCommitTransaction);
        stmt.execute(nestedCommitTran);
        stmt.execute(nestedCommitWithConditional);
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithSavepoints() throws SQLException {
        // COMMIT TRANSACTION with savepoints
        String commitWithSavepoint = "BEGIN TRANSACTION; UPDATE users SET status = 'active' WHERE id = 1; SAVE TRANSACTION savepoint1; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); ROLLBACK TRANSACTION savepoint1; COMMIT TRANSACTION";
        String commitWithMultipleSavepoints = "BEGIN TRANSACTION; INSERT INTO products (name, price) VALUES ('Tablet', 299.99); SAVE TRANSACTION sp1; UPDATE products SET price = price * 1.1; SAVE TRANSACTION sp2; DELETE FROM products WHERE price > 1000; ROLLBACK TRANSACTION sp2; COMMIT TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(commitWithSavepoint);
        stmt.execute(commitWithMultipleSavepoints);
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithErrorHandling() throws SQLException {
        // COMMIT TRANSACTION with error handling
        String commitWithTryCatch = "BEGIN TRANSACTION; BEGIN TRY UPDATE users SET email = 'new@example.com' WHERE id = 1; INSERT INTO audit_log (action, timestamp) VALUES ('EMAIL_UPDATED', GETDATE()); COMMIT TRANSACTION; SELECT 'Transaction committed successfully' as message; END TRY BEGIN CATCH ROLLBACK TRANSACTION; SELECT 'Error occurred: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String commitWithErrorHandling = "BEGIN TRANSACTION; BEGIN TRY INSERT INTO orders (user_id, product_name) VALUES (1, 'Keyboard'); UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; COMMIT TRANSACTION; SELECT 'Transaction completed successfully' as message; END TRY BEGIN CATCH ROLLBACK TRANSACTION; SELECT 'Transaction failed: ' + ERROR_MESSAGE() as error_message; END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(commitWithTryCatch);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(commitWithErrorHandling);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithConditionalLogic() throws SQLException {
        // COMMIT TRANSACTION with conditional logic
        String conditionalCommit = "BEGIN TRANSACTION; IF EXISTS (SELECT 1 FROM users WHERE id = 1) BEGIN UPDATE users SET last_login = GETDATE() WHERE id = 1; INSERT INTO login_log (user_id, login_time) VALUES (1, GETDATE()); COMMIT TRANSACTION; SELECT 'User login recorded' as message; END ELSE BEGIN ROLLBACK TRANSACTION; SELECT 'User not found' as message; END";
        String conditionalCommitTran = "BEGIN TRAN; IF (SELECT COUNT(*) FROM products WHERE category_id = 1) > 0 BEGIN UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO price_update_log (category_id, update_time) VALUES (1, GETDATE()); COMMIT TRAN; SELECT 'Prices updated' as message; END ELSE BEGIN ROLLBACK TRAN; SELECT 'No products found' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(conditionalCommit);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(conditionalCommitTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithValidation() throws SQLException {
        // COMMIT TRANSACTION with validation
        String validationCommit = "BEGIN TRANSACTION; DECLARE @user_id INT = 1; IF EXISTS (SELECT 1 FROM users WHERE id = @user_id AND status = 'active') BEGIN UPDATE users SET last_activity = GETDATE() WHERE id = @user_id; INSERT INTO activity_log (user_id, activity_type, timestamp) VALUES (@user_id, 'ACTIVITY_UPDATE', GETDATE()); COMMIT TRANSACTION; SELECT 'User activity updated' as validation_message; END ELSE BEGIN ROLLBACK TRANSACTION; SELECT 'User validation failed' as validation_message; END";
        String validationCommitTran = "BEGIN TRAN; DECLARE @order_id INT = 1; IF (SELECT COUNT(*) FROM orders WHERE id = @order_id AND status = 'pending') = 1 BEGIN UPDATE orders SET status = 'processing' WHERE id = @order_id; INSERT INTO order_status_log (order_id, old_status, new_status, timestamp) VALUES (@order_id, 'pending', 'processing', GETDATE()); COMMIT TRAN; SELECT 'Order status updated' as message; END ELSE BEGIN ROLLBACK TRAN; SELECT 'Order validation failed' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(validationCommit);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(validationCommitTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithLogging() throws SQLException {
        // COMMIT TRANSACTION with logging
        String loggingCommit = "BEGIN TRANSACTION; INSERT INTO users (name, email) VALUES ('John', 'john@example.com'); DECLARE @user_id INT = SCOPE_IDENTITY(); INSERT INTO audit_log (table_name, record_id, action, timestamp) VALUES ('users', @user_id, 'INSERT', GETDATE()); COMMIT TRANSACTION; SELECT @user_id as created_user_id";
        String loggingCommitTran = "BEGIN TRAN; UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO system_log (operation, affected_rows, timestamp) VALUES ('PRICE_UPDATE', @@ROWCOUNT, GETDATE()); COMMIT TRAN; SELECT 'Price update logged' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(loggingCommit);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(loggingCommitTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithPreparedStatement() throws SQLException {
        // COMMIT TRANSACTION with PreparedStatement
        String commitWithParam = "BEGIN TRANSACTION; UPDATE users SET status = ? WHERE id = ?; COMMIT TRANSACTION";
        String commitWithParamValidation = "BEGIN TRANSACTION; INSERT INTO orders (user_id, product_name, quantity) VALUES (?, ?, ?); IF @@ROWCOUNT = 1 COMMIT TRANSACTION ELSE ROLLBACK TRANSACTION";
        String commitWithParamConditional = "BEGIN TRANSACTION; IF ? = 1 BEGIN UPDATE users SET last_login = GETDATE() WHERE id = ?; COMMIT TRANSACTION; END ELSE BEGIN ROLLBACK TRANSACTION; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(commitWithParam);
        pstmt1.setString(1, "active");
        pstmt1.setInt(2, 1);
        pstmt1.execute();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(commitWithParamValidation);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "Laptop");
        pstmt2.setInt(3, 1);
        pstmt2.execute();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(commitWithParamConditional);
        pstmt3.setInt(1, 1);
        pstmt3.setInt(2, 1);
        pstmt3.execute();
        pstmt3.close();
        
        conn.close();
    }
    
    public void commitTransactionInMethods() throws SQLException {
        // COMMIT TRANSACTION in methods
        String userCommit = getUserCommitQuery();
        String orderCommit = getOrderCommitQuery();
        String productCommit = getProductCommitQuery();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(userCommit);
        stmt.execute(orderCommit);
        stmt.execute(productCommit);
        
        stmt.close();
        conn.close();
    }
    
    private String getUserCommitQuery() {
        return "BEGIN TRANSACTION; UPDATE users SET last_login = GETDATE() WHERE id = 1; INSERT INTO login_log (user_id, login_time) VALUES (1, GETDATE()); COMMIT TRANSACTION";
    }
    
    private String getOrderCommitQuery() {
        return "BEGIN TRAN; INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Mouse', 2); UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; COMMIT TRAN";
    }
    
    private String getProductCommitQuery() {
        return "BEGIN TRANSACTION; UPDATE products SET price = price * 1.1 WHERE category_id = 1; INSERT INTO price_history (category_id, update_time) VALUES (1, GETDATE()); COMMIT TRANSACTION";
    }
    
    public void commitTransactionWithDynamicSQL() throws SQLException {
        // COMMIT TRANSACTION with dynamic SQL construction
        String baseCommit = "BEGIN TRANSACTION; UPDATE users SET status = '";
        String status = "active";
        String commitClause = "' WHERE id = 1; COMMIT TRANSACTION";
        
        String dynamicCommit = baseCommit + status + commitClause;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        stmt.execute(dynamicCommit);
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithPerformanceMonitoring() throws SQLException {
        // COMMIT TRANSACTION with performance monitoring
        String performanceCommit = "BEGIN TRANSACTION; DECLARE @start_time DATETIME = GETDATE(); UPDATE users SET last_activity = GETDATE() WHERE status = 'active'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('UPDATE_USER_ACTIVITY', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE()); COMMIT TRANSACTION";
        String performanceCommitTran = "BEGIN TRAN; DECLARE @start_time DATETIME = GETDATE(); INSERT INTO orders (user_id, product_name) SELECT id, 'Bulk Order' FROM users WHERE status = 'active'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('BULK_ORDER_INSERT', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE()); COMMIT TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(performanceCommit);
        stmt.execute(performanceCommitTran);
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithDataConsistency() throws SQLException {
        // COMMIT TRANSACTION with data consistency scenarios
        String consistencyCommit = "BEGIN TRANSACTION; UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Laptop', 1); IF @@ROWCOUNT = 2 BEGIN COMMIT TRANSACTION; SELECT 'Data consistency maintained' as message; END ELSE BEGIN ROLLBACK TRANSACTION; SELECT 'Data consistency failed' as message; END";
        String consistencyCommitTran = "BEGIN TRAN; DELETE FROM expired_sessions WHERE expiry_date < GETDATE(); UPDATE users SET last_activity = GETDATE() WHERE id IN (SELECT user_id FROM expired_sessions WHERE expiry_date < GETDATE()); IF @@ROWCOUNT > 0 BEGIN COMMIT TRAN; SELECT 'Session cleanup completed' as message; END ELSE BEGIN ROLLBACK TRAN; SELECT 'Session cleanup failed' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(consistencyCommit);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(consistencyCommitTran);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void commitTransactionWithBatchOperations() throws SQLException {
        // COMMIT TRANSACTION with batch operations
        String batchCommit = "BEGIN TRANSACTION; INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com'); INSERT INTO users (name, email) VALUES ('Bob', 'bob@example.com'); INSERT INTO users (name, email) VALUES ('Charlie', 'charlie@example.com'); COMMIT TRANSACTION";
        String batchCommitTran = "BEGIN TRAN; UPDATE products SET price = price * 1.1 WHERE category_id = 1; UPDATE products SET price = price * 1.05 WHERE category_id = 2; UPDATE products SET price = price * 1.02 WHERE category_id = 3; COMMIT TRAN";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        stmt.execute(batchCommit);
        stmt.execute(batchCommitTran);
        
        stmt.close();
        conn.close();
    }
}
