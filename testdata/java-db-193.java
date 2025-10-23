// sql-java-193: StoredProcedureExecuteUsage
// Detects execute() method usage for stored procedures
// This file tests detection of stored procedure execution patterns

package com.example.jdbc;

import java.sql.*;

public class StoredProcedureExecuteExample {
    
    // VIOLATION: CallableStatement.execute() for stored procedure
    public void executeStoredProcedure(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call update_inventory(?, ?)}");
        cstmt.setInt(1, 1001);
        cstmt.setInt(2, 50);
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: execute() with output parameter retrieval
    public int executeProcedureWithOutput(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call calculate_total(?, ?)}");
        stmt.setInt(1, 100);
        stmt.registerOutParameter(2, Types.INTEGER);
        stmt.execute();
        int total = stmt.getInt(2);
        stmt.close();
        return total;
    }
    
    // VIOLATION: execute() in try-with-resources
    public void executeProcedureWithResources(Connection conn, String status) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{call update_order_status(?)}")) {
            cs.setString(1, status);
            cs.execute();
        }
    }
    
    // VIOLATION: execute() with boolean return check
    public void executeProcedureWithResultCheck(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call process_payments()}");
        boolean hasResults = cstmt.execute();
        if (hasResults) {
            ResultSet rs = cstmt.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs.close();
        }
        cstmt.close();
    }
    
    // VIOLATION: execute() for database function
    public double executeFunctionCall(Connection conn, int employeeId) throws SQLException {
        CallableStatement cs = conn.prepareCall("{? = call get_salary(?)}");
        cs.registerOutParameter(1, Types.DOUBLE);
        cs.setInt(2, employeeId);
        cs.execute();
        double salary = cs.getDouble(1);
        cs.close();
        return salary;
    }
    
    // VIOLATION: execute() with multiple result sets
    public void executeWithMultipleResults(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_employee_details(?)}");
        stmt.setInt(1, 1001);
        boolean hasResult = stmt.execute();
        
        while (hasResult || stmt.getUpdateCount() != -1) {
            if (hasResult) {
                ResultSet rs = stmt.getResultSet();
                processResultSet(rs);
                rs.close();
            }
            hasResult = stmt.getMoreResults();
        }
        stmt.close();
    }
    
    // VIOLATION: execute() in conditional block
    public void conditionalProcedureExecution(Connection conn, boolean shouldExecute) throws SQLException {
        if (shouldExecute) {
            CallableStatement cstmt = conn.prepareCall("{call cleanup_temp_data()}");
            cstmt.execute();
            cstmt.close();
        }
    }
    
    // VIOLATION: execute() in loop
    public void executeProcedureInLoop(Connection connection, String[] departments) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call generate_report(?)}");
        for (String dept : departments) {
            cs.setString(1, dept);
            cs.execute();
        }
        cs.close();
    }
    
    // VIOLATION: execute() with exception handling
    public void executeProcedureWithErrorHandling(Connection conn) {
        CallableStatement cstmt = null;
        try {
            cstmt = conn.prepareCall("{call risky_operation(?, ?)}");
            cstmt.setInt(1, 100);
            cstmt.setString(2, "TEST");
            cstmt.execute();
        } catch (SQLException e) {
            System.err.println("Procedure execution failed: " + e.getMessage());
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // VIOLATION: execute() for Oracle package procedure
    public void executeOraclePackageProcedure(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call hr_package.update_employee(?, ?)}");
        stmt.setInt(1, 1001);
        stmt.setString(2, "Manager");
        stmt.execute();
        stmt.close();
    }
    
    // VIOLATION: execute() for SQL Server stored procedure
    public void executeSqlServerProcedure(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call dbo.sp_UpdateCustomer ?, ?, ?}");
        cs.setInt(1, 5001);
        cs.setString(2, "John Doe");
        cs.setString(3, "john@example.com");
        cs.execute();
        cs.close();
    }
    
    // VIOLATION: execute() with transaction control
    public void executeProcedureInTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        try {
            CallableStatement cstmt = connection.prepareCall("{call transfer_funds(?, ?, ?)}");
            cstmt.setInt(1, 1001);
            cstmt.setInt(2, 2002);
            cstmt.setDouble(3, 500.00);
            cstmt.execute();
            connection.commit();
            cstmt.close();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    // VIOLATION: execute() with named parameters
    public void executeWithNamedParams(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call update_customer(:id, :name, :email)}");
        stmt.setInt("id", 100);
        stmt.setString("name", "Alice");
        stmt.setString("email", "alice@example.com");
        stmt.execute();
        stmt.close();
    }
    
    // VIOLATION: execute() returning status code
    public int executeProcedureWithStatusCode(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{? = call validate_transaction(?)}");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.setInt(2, 12345);
        cs.execute();
        int statusCode = cs.getInt(1);
        cs.close();
        return statusCode;
    }
    
    // NON-VIOLATION: PreparedStatement.execute() (not for stored procedure)
    public void executeUpdateStatement(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET status = ? WHERE id = ?");
        pstmt.setString(1, "ACTIVE");
        pstmt.setInt(2, 1001);
        pstmt.execute();
        pstmt.close();
    }
    
    // NON-VIOLATION: Statement.execute() with regular SQL
    public void executeRegularStatement(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE temp_table (id INT, name VARCHAR(100))");
        stmt.close();
    }
    
    // NON-VIOLATION: executeQuery() instead of execute()
    public void executeQuery(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE dept = ?");
        ps.setString(1, "IT");
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: executeUpdate() for DML
    public int executeUpdate(Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM logs WHERE created_date < ?");
        pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        int rowsDeleted = pstmt.executeUpdate();
        pstmt.close();
        return rowsDeleted;
    }
    
    // NON-VIOLATION: executeBatch() for batch operations
    public void executeBatch(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO audit_log VALUES (?, ?)");
        for (int i = 0; i < 10; i++) {
            ps.setInt(1, i);
            ps.setString(2, "Log " + i);
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }
    
    // NON-VIOLATION: Using Spring JdbcTemplate
    public void springJdbcTemplateCall(JdbcTemplate template) {
        template.update("INSERT INTO users VALUES (?, ?)", 1, "John");
    }
    
    // Helper method
    private void processResultSet(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}

// Additional class with execute() usage
class ProcedureExecutor {
    
    private Connection connection;
    
    // VIOLATION: execute() in separate method
    public void runProcedure(String procedureName, Object... params) throws SQLException {
        StringBuilder sql = new StringBuilder("{call ").append(procedureName).append("(");
        for (int i = 0; i < params.length; i++) {
            sql.append(i > 0 ? ",?" : "?");
        }
        sql.append(")}");
        
        CallableStatement stmt = connection.prepareCall(sql.toString());
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        stmt.execute();
        stmt.close();
    }
    
    // VIOLATION: Wrapper method with execute()
    public boolean executeProcedure(CallableStatement cstmt) throws SQLException {
        return cstmt.execute();
    }
}

// Helper class
class JdbcTemplate {
    void update(String sql, Object... args) { }
}

