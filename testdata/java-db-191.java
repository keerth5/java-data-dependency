// sql-java-191: CallableStatementUsage
// Detects CallableStatement usage for stored procedures
// This file tests detection of CallableStatement patterns

package com.example.jdbc;

import java.sql.*;
import javax.sql.DataSource;

public class CallableStatementExample {
    
    // VIOLATION: Creating CallableStatement for stored procedure
    public void callStoredProcedure(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call update_employee_salary(?, ?)}");
        cstmt.setInt(1, 1001);
        cstmt.setDouble(2, 75000.0);
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: CallableStatement with output parameter
    public int callProcedureWithOutput(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_employee_count(?, ?)}");
        stmt.setString(1, "IT");
        stmt.registerOutParameter(2, Types.INTEGER);
        stmt.execute();
        int count = stmt.getInt(2);
        stmt.close();
        return count;
    }
    
    // VIOLATION: CallableStatement with function call
    public double callFunction(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{? = call calculate_bonus(?)}");
        cs.registerOutParameter(1, Types.DOUBLE);
        cs.setInt(2, 1001);
        cs.execute();
        double bonus = cs.getDouble(1);
        cs.close();
        return bonus;
    }
    
    // VIOLATION: Try-with-resources with CallableStatement
    public void executeProcedureWithTryWithResources(Connection conn, String department) throws SQLException {
        try (CallableStatement cstmt = conn.prepareCall("{call delete_old_records(?)}")) {
            cstmt.setString(1, department);
            cstmt.execute();
        }
    }
    
    // VIOLATION: CallableStatement with multiple parameters
    public void callComplexProcedure(Connection connection) throws SQLException {
        String sql = "{call process_order(?, ?, ?, ?)}";
        CallableStatement callable = connection.prepareCall(sql);
        callable.setInt(1, 12345);
        callable.setString(2, "PENDING");
        callable.setDouble(3, 999.99);
        callable.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        callable.execute();
        callable.close();
    }
    
    // VIOLATION: CallableStatement with ResultSet
    public void callProcedureReturningResultSet(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call get_all_employees(?)}");
        cstmt.setString(1, "Sales");
        ResultSet rs = cstmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        rs.close();
        cstmt.close();
    }
    
    // VIOLATION: CallableStatement with batch execution
    public void executeBatchProcedures(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call insert_log(?, ?)}");
        for (int i = 0; i < 10; i++) {
            cs.setInt(1, i);
            cs.setString(2, "Log message " + i);
            cs.addBatch();
        }
        cs.executeBatch();
        cs.close();
    }
    
    // VIOLATION: CallableStatement with named parameters (Oracle style)
    public void callProcedureWithNamedParams(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call update_customer(:custId, :status)}");
        cstmt.setInt("custId", 5001);
        cstmt.setString("status", "ACTIVE");
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: CallableStatement in a method that wraps the call
    public boolean executeStoredProcedure(String procedureName, Object... params) throws SQLException {
        Connection conn = getConnection();
        CallableStatement stmt = conn.prepareCall("{call " + procedureName + "}");
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        boolean result = stmt.execute();
        stmt.close();
        conn.close();
        return result;
    }
    
    // VIOLATION: CallableStatement with escape syntax
    public void callProcedureWithEscapeSyntax(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call sp_calculate_totals()}");
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: CallableStatement for database function
    public String callStringFunction(Connection conn, String input) throws SQLException {
        CallableStatement cs = conn.prepareCall("{? = call uppercase_text(?)}");
        cs.registerOutParameter(1, Types.VARCHAR);
        cs.setString(2, input);
        cs.execute();
        String result = cs.getString(1);
        cs.close();
        return result;
    }
    
    // VIOLATION: CallableStatement with INOUT parameter
    public void callProcedureWithInOutParam(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call increment_counter(?)}");
        stmt.setInt(1, 100);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.execute();
        int newValue = stmt.getInt(1);
        System.out.println("New counter value: " + newValue);
        stmt.close();
    }
    
    // VIOLATION: Multiple CallableStatement calls in a method
    public void executeMultipleProcedures(Connection conn) throws SQLException {
        CallableStatement cstmt1 = conn.prepareCall("{call proc1()}");
        cstmt1.execute();
        cstmt1.close();
        
        CallableStatement cstmt2 = conn.prepareCall("{call proc2(?)}");
        cstmt2.setString(1, "param");
        cstmt2.execute();
        cstmt2.close();
    }
    
    // VIOLATION: CallableStatement stored in field
    private CallableStatement procedureStmt;
    
    public void initializeProcedure(Connection conn) throws SQLException {
        procedureStmt = conn.prepareCall("{call recurring_task(?)}");
    }
    
    // NON-VIOLATION: PreparedStatement (not CallableStatement)
    public void executePreparedStatement(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users VALUES (?, ?)");
        pstmt.setInt(1, 1);
        pstmt.setString(2, "John");
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // NON-VIOLATION: Regular Statement
    public void executeStatement(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("UPDATE products SET price = 100 WHERE id = 1");
        stmt.close();
    }
    
    // NON-VIOLATION: Direct SQL query without stored procedure
    public void executeQuery(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM employees WHERE dept = ?");
        ps.setString(1, "IT");
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: Using JdbcTemplate (Spring)
    public void springJdbcTemplate(JdbcTemplate template) {
        template.update("UPDATE accounts SET balance = ? WHERE id = ?", 1000.0, 123);
    }
    
    // Helper method
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/db", "user", "pass");
    }
}

// Additional class with CallableStatement usage
class StoredProcedureExecutor {
    
    private DataSource dataSource;
    
    // VIOLATION: CallableStatement in another class
    public void executeProc(String procName) throws SQLException {
        Connection conn = dataSource.getConnection();
        CallableStatement cs = conn.prepareCall("{call " + procName + "()}");
        cs.execute();
        cs.close();
        conn.close();
    }
}

// Helper classes
class JdbcTemplate {
    void update(String sql, Object... args) { }
}

