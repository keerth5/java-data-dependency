package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-032: RowCountUsage
 * Detects "@@ROWCOUNT" system variable usage
 */
public class RowCountUsageExample {
    
    public void basicRowCountUsage() throws SQLException {
        // Basic @@ROWCOUNT usage
        String updateWithRowCount = "UPDATE users SET status = 'active'; SELECT @@ROWCOUNT as affected_rows";
        String insertWithRowCount = "INSERT INTO users (name, email) VALUES ('John', 'john@example.com'); SELECT @@ROWCOUNT as inserted_rows";
        String deleteWithRowCount = "DELETE FROM users WHERE status = 'inactive'; SELECT @@ROWCOUNT as deleted_rows";
        String selectWithRowCount = "SELECT * FROM users; SELECT @@ROWCOUNT as selected_rows";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(updateWithRowCount);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(insertWithRowCount);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(deleteWithRowCount);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        boolean hasResult4 = stmt.execute(selectWithRowCount);
        if (hasResult4) {
            ResultSet rs4 = stmt.getResultSet();
            rs4.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rowCountInConditionalLogic() throws SQLException {
        // @@ROWCOUNT in conditional logic
        String conditionalUpdate = "UPDATE users SET last_login = GETDATE() WHERE id = 1; IF @@ROWCOUNT > 0 SELECT 'User updated successfully' as message ELSE SELECT 'No user found' as message";
        String conditionalInsert = "INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); IF @@ROWCOUNT > 0 SELECT 'Order created' as message ELSE SELECT 'Order failed' as message";
        String conditionalDelete = "DELETE FROM products WHERE price < 10; IF @@ROWCOUNT > 0 SELECT 'Products deleted' as message ELSE SELECT 'No products to delete' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(conditionalUpdate);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(conditionalInsert);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        boolean hasResult3 = stmt.execute(conditionalDelete);
        if (hasResult3) {
            ResultSet rs3 = stmt.getResultSet();
            rs3.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rowCountInStoredProcedures() throws SQLException {
        // @@ROWCOUNT in stored procedure calls
        String execProcedureWithRowCount = "EXEC sp_update_user_status @user_id = 1, @status = 'active'; SELECT @@ROWCOUNT as procedure_affected_rows";
        String execProcedureComplex = "EXEC sp_bulk_insert_orders @order_data = 'data'; SELECT @@ROWCOUNT as bulk_inserted_rows";
        String executeProcedureWithRowCount = "EXECUTE sp_delete_old_orders @cutoff_date = '2023-01-01'; SELECT @@ROWCOUNT as deleted_old_orders";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        java.sql.CallableStatement cstmt1 = conn.prepareCall(execProcedureWithRowCount);
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
        
        java.sql.CallableStatement cstmt3 = conn.prepareCall(executeProcedureWithRowCount);
        boolean hasResult3 = cstmt3.execute();
        if (hasResult3) {
            ResultSet rs3 = cstmt3.getResultSet();
            rs3.close();
        }
        cstmt3.close();
        
        conn.close();
    }
    
    public void rowCountInBatchOperations() throws SQLException {
        // @@ROWCOUNT in batch operations
        String batchUpdate = "UPDATE users SET status = 'active' WHERE age > 18; SELECT @@ROWCOUNT as adults_updated; UPDATE users SET status = 'inactive' WHERE age <= 18; SELECT @@ROWCOUNT as minors_updated";
        String batchInsert = "INSERT INTO products (name, price) VALUES ('Laptop', 999.99); SELECT @@ROWCOUNT as laptop_inserted; INSERT INTO products (name, price) VALUES ('Mouse', 29.99); SELECT @@ROWCOUNT as mouse_inserted";
        String batchDelete = "DELETE FROM orders WHERE status = 'cancelled'; SELECT @@ROWCOUNT as cancelled_deleted; DELETE FROM orders WHERE order_date < '2023-01-01'; SELECT @@ROWCOUNT as old_deleted";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(batchUpdate);
        while (hasResult1) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult1 = stmt.getMoreResults();
        }
        
        boolean hasResult2 = stmt.execute(batchInsert);
        while (hasResult2) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult2 = stmt.getMoreResults();
        }
        
        boolean hasResult3 = stmt.execute(batchDelete);
        while (hasResult3) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult3 = stmt.getMoreResults();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rowCountInTransaction() throws SQLException {
        // @@ROWCOUNT in transaction
        String transactionWithRowCount = "BEGIN TRANSACTION; UPDATE users SET last_login = GETDATE() WHERE id = 1; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'Transaction rolled back' as message END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Transaction committed' as message END";
        String transactionComplex = "BEGIN TRANSACTION; INSERT INTO orders (user_id, product_name) VALUES (1, 'Laptop'); IF @@ROWCOUNT > 0 BEGIN UPDATE users SET total_orders = total_orders + 1 WHERE id = 1; IF @@ROWCOUNT > 0 COMMIT TRANSACTION ELSE ROLLBACK TRANSACTION END ELSE ROLLBACK TRANSACTION";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(transactionWithRowCount);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(transactionComplex);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rowCountInErrorHandling() throws SQLException {
        // @@ROWCOUNT in error handling
        String errorHandlingWithRowCount = "BEGIN TRY UPDATE users SET email = 'invalid-email' WHERE id = 1; IF @@ROWCOUNT = 0 SELECT 'No user found with ID 1' as error_message; END TRY BEGIN CATCH SELECT 'Error occurred: ' + ERROR_MESSAGE() as error_message; END CATCH";
        String errorHandlingComplex = "BEGIN TRY INSERT INTO orders (user_id, product_name) VALUES (999, 'Laptop'); IF @@ROWCOUNT = 0 SELECT 'Insert failed' as message; END TRY BEGIN CATCH IF @@ROWCOUNT = 0 SELECT 'No rows affected' as message; SELECT 'Error: ' + ERROR_MESSAGE() as error_message; END CATCH";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(errorHandlingWithRowCount);
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
    
    public void rowCountInValidation() throws SQLException {
        // @@ROWCOUNT in validation
        String validationWithRowCount = "UPDATE users SET status = 'active' WHERE id = 1; IF @@ROWCOUNT = 1 SELECT 'User activated successfully' as validation_message ELSE SELECT 'User not found or multiple users affected' as validation_message";
        String validationComplex = "INSERT INTO products (name, price, category_id) VALUES ('Tablet', 299.99, 1); IF @@ROWCOUNT = 1 BEGIN SELECT 'Product created successfully' as message; SELECT product_id FROM products WHERE name = 'Tablet' AND price = 299.99; END ELSE SELECT 'Product creation failed' as message";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(validationWithRowCount);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
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
    
    public void rowCountInLogging() throws SQLException {
        // @@ROWCOUNT in logging
        String loggingWithRowCount = "UPDATE users SET last_activity = GETDATE() WHERE status = 'active'; INSERT INTO audit_log (action, affected_rows, timestamp) VALUES ('UPDATE_USERS', @@ROWCOUNT, GETDATE())";
        String loggingComplex = "DELETE FROM expired_sessions WHERE expiry_date < GETDATE(); INSERT INTO system_log (operation, rows_affected, timestamp) VALUES ('CLEANUP_SESSIONS', @@ROWCOUNT, GETDATE()); SELECT @@ROWCOUNT as sessions_cleaned";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(loggingWithRowCount);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(loggingComplex);
        while (hasResult2) {
            ResultSet rs = stmt.getResultSet();
            rs.close();
            hasResult2 = stmt.getMoreResults();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void rowCountInPreparedStatement() throws SQLException {
        // @@ROWCOUNT in PreparedStatement
        String updateWithRowCountParam = "UPDATE users SET status = ? WHERE id = ?; SELECT @@ROWCOUNT as affected_rows";
        String insertWithRowCountParam = "INSERT INTO orders (user_id, product_name, quantity) VALUES (?, ?, ?); SELECT @@ROWCOUNT as inserted_rows";
        String deleteWithRowCountParam = "DELETE FROM products WHERE product_id = ?; SELECT @@ROWCOUNT as deleted_rows";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(updateWithRowCountParam);
        pstmt1.setString(1, "active");
        pstmt1.setInt(2, 1);
        boolean hasResult1 = pstmt1.execute();
        if (hasResult1) {
            ResultSet rs1 = pstmt1.getResultSet();
            rs1.close();
        }
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(insertWithRowCountParam);
        pstmt2.setInt(1, 1);
        pstmt2.setString(2, "Laptop");
        pstmt2.setInt(3, 1);
        boolean hasResult2 = pstmt2.execute();
        if (hasResult2) {
            ResultSet rs2 = pstmt2.getResultSet();
            rs2.close();
        }
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(deleteWithRowCountParam);
        pstmt3.setInt(1, 1);
        boolean hasResult3 = pstmt3.execute();
        if (hasResult3) {
            ResultSet rs3 = pstmt3.getResultSet();
            rs3.close();
        }
        pstmt3.close();
        
        conn.close();
    }
    
    public void rowCountInMethods() throws SQLException {
        // @@ROWCOUNT in methods
        String userQuery = getUserQueryWithRowCount();
        String orderQuery = getOrderQueryWithRowCount();
        String productQuery = getProductQueryWithRowCount();
        
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
    
    private String getUserQueryWithRowCount() {
        return "UPDATE users SET last_login = GETDATE() WHERE status = 'active'; SELECT @@ROWCOUNT as active_users_updated";
    }
    
    private String getOrderQueryWithRowCount() {
        return "INSERT INTO orders (user_id, product_name, quantity) VALUES (1, 'Laptop', 1); SELECT @@ROWCOUNT as orders_created";
    }
    
    private String getProductQueryWithRowCount() {
        return "DELETE FROM products WHERE discontinued = 1; SELECT @@ROWCOUNT as discontinued_products_deleted";
    }
    
    public void rowCountWithDynamicSQL() throws SQLException {
        // @@ROWCOUNT with dynamic SQL construction
        String baseUpdate = "UPDATE users SET status = 'active' WHERE";
        String condition = " id = 1";
        String rowCountSelect = "; SELECT @@ROWCOUNT as affected_rows";
        
        String dynamicQuery = baseUpdate + condition + rowCountSelect;
        
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
    
    public void rowCountWithPerformanceMonitoring() throws SQLException {
        // @@ROWCOUNT with performance monitoring
        String performanceUpdate = "UPDATE users SET last_activity = GETDATE() WHERE status = 'active'; INSERT INTO performance_log (operation, rows_affected, execution_time, timestamp) VALUES ('UPDATE_USER_ACTIVITY', @@ROWCOUNT, DATEDIFF(millisecond, @start_time, GETDATE()), GETDATE())";
        String performanceInsert = "INSERT INTO audit_log (user_id, action, timestamp) SELECT id, 'LOGIN', GETDATE() FROM users WHERE status = 'active'; INSERT INTO performance_log (operation, rows_affected, timestamp) VALUES ('AUDIT_LOGIN', @@ROWCOUNT, GETDATE())";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        boolean hasResult1 = stmt.execute(performanceUpdate);
        if (hasResult1) {
            ResultSet rs1 = stmt.getResultSet();
            rs1.close();
        }
        
        boolean hasResult2 = stmt.execute(performanceInsert);
        if (hasResult2) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
}
