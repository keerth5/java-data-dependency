// sql-java-192: StoredProcedureParameterRegistration
// Detects parameter registration for stored procedures
// This file tests detection of registerOutParameter and parameter registration patterns

package com.example.jdbc;

import java.sql.*;

public class ParameterRegistrationExample {
    
    // VIOLATION: registerOutParameter for output parameter
    public int getEmployeeCount(Connection conn, String department) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call get_employee_count(?, ?)}");
        cstmt.setString(1, department);
        cstmt.registerOutParameter(2, Types.INTEGER);
        cstmt.execute();
        int count = cstmt.getInt(2);
        cstmt.close();
        return count;
    }
    
    // VIOLATION: Multiple registerOutParameter calls
    public void getProcedureResults(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_summary(?, ?, ?, ?)}");
        stmt.setInt(1, 2024);
        stmt.registerOutParameter(2, Types.DECIMAL);
        stmt.registerOutParameter(3, Types.INTEGER);
        stmt.registerOutParameter(4, Types.VARCHAR);
        stmt.execute();
        double total = stmt.getDouble(2);
        int count = stmt.getInt(3);
        String status = stmt.getString(4);
        stmt.close();
    }
    
    // VIOLATION: registerOutParameter with scale for decimal
    public double calculateCommission(Connection conn, int employeeId) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call calc_commission(?, ?)}");
        cs.setInt(1, employeeId);
        cs.registerOutParameter(2, Types.DECIMAL, 2);
        cs.execute();
        double commission = cs.getDouble(2);
        cs.close();
        return commission;
    }
    
    // VIOLATION: registerOutParameter for function return value
    public String callFunction(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{? = call get_status(?)}");
        cstmt.registerOutParameter(1, Types.VARCHAR);
        cstmt.setInt(2, 100);
        cstmt.execute();
        String status = cstmt.getString(1);
        cstmt.close();
        return status;
    }
    
    // VIOLATION: registerOutParameter with type name (for complex types)
    public void callProcedureWithCustomType(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call get_customer_info(?, ?)}");
        stmt.setInt(1, 5001);
        stmt.registerOutParameter(2, Types.STRUCT, "CUSTOMER_TYPE");
        stmt.execute();
        Object customerData = stmt.getObject(2);
        stmt.close();
    }
    
    // VIOLATION: registerOutParameter for date/time types
    public Date getLastLoginDate(Connection connection, String username) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call get_last_login(?, ?)}");
        cs.setString(1, username);
        cs.registerOutParameter(2, Types.TIMESTAMP);
        cs.execute();
        Timestamp lastLogin = cs.getTimestamp(2);
        cs.close();
        return new Date(lastLogin.getTime());
    }
    
    // VIOLATION: registerOutParameter with CLOB/BLOB
    public void getProcedureWithLargeObject(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call get_document(?, ?)}");
        cstmt.setInt(1, 1001);
        cstmt.registerOutParameter(2, Types.CLOB);
        cstmt.execute();
        Clob documentContent = cstmt.getClob(2);
        cstmt.close();
    }
    
    // VIOLATION: registerOutParameter for cursor (Oracle REF CURSOR)
    public void getRefCursor(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_employee_cursor(?)}");
        stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        stmt.execute();
        ResultSet rs = (ResultSet) stmt.getObject(1);
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: registerOutParameter with array type
    public void getProcedureWithArray(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call get_sales_data(?, ?)}");
        cs.setInt(1, 2024);
        cs.registerOutParameter(2, Types.ARRAY, "NUMBER_ARRAY");
        cs.execute();
        Array salesArray = cs.getArray(2);
        cs.close();
    }
    
    // VIOLATION: Mixed input and output parameter registration
    public void complexParameterRegistration(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call process_transaction(?, ?, ?, ?)}");
        // Input parameters
        cstmt.setInt(1, 12345);
        cstmt.setDouble(2, 500.00);
        // Output parameters
        cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.registerOutParameter(4, Types.INTEGER);
        cstmt.execute();
        String message = cstmt.getString(3);
        int errorCode = cstmt.getInt(4);
        cstmt.close();
    }
    
    // VIOLATION: registerOutParameter for INOUT parameter
    public void inOutParameterRegistration(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call update_and_return_count(?)}");
        stmt.setInt(1, 100);
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.execute();
        int resultCount = stmt.getInt(1);
        stmt.close();
    }
    
    // VIOLATION: registerOutParameter in try-with-resources
    public String executeProcedureWithOutput(Connection connection, int id) throws SQLException {
        try (CallableStatement cs = connection.prepareCall("{call get_name(?, ?)}")) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.execute();
            return cs.getString(2);
        }
    }
    
    // VIOLATION: registerOutParameter with named parameter (if supported)
    public void namedParameterRegistration(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call proc_with_out(:inParam, :outParam)}");
        cstmt.setString("inParam", "value");
        cstmt.registerOutParameter("outParam", Types.INTEGER);
        cstmt.execute();
        int result = cstmt.getInt("outParam");
        cstmt.close();
    }
    
    // VIOLATION: Multiple output parameters with different types
    public void multipleOutputTypes(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_statistics(?, ?, ?, ?, ?)}");
        stmt.setString(1, "2024-01-01");
        stmt.registerOutParameter(2, Types.INTEGER);     // count
        stmt.registerOutParameter(3, Types.DOUBLE);      // average
        stmt.registerOutParameter(4, Types.DECIMAL);     // total
        stmt.registerOutParameter(5, Types.BOOLEAN);     // success flag
        stmt.execute();
        stmt.close();
    }
    
    // NON-VIOLATION: PreparedStatement without output parameters
    public void preparedStatementWithoutOutput(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users VALUES (?, ?)");
        pstmt.setInt(1, 1);
        pstmt.setString(2, "John");
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // NON-VIOLATION: CallableStatement without registerOutParameter
    public void callableWithoutOutputRegistration(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call simple_proc(?)}");
        cstmt.setString(1, "input");
        cstmt.execute();
        cstmt.close();
    }
    
    // NON-VIOLATION: Regular query without stored procedure
    public void executeQuery(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
        ps.setInt(1, 1001);
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: Statement without parameters
    public void simpleStatement(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Using Spring JdbcTemplate
    public void springJdbcCall(SimpleJdbcCall jdbcCall) {
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("empId", 1001);
        Map<String, Object> result = jdbcCall.execute(params);
    }
}

// Additional class demonstrating parameter registration
class ProcedureHelper {
    
    // VIOLATION: registerOutParameter in helper method
    public Object executeAndGetOutput(Connection conn, String procedureName, int paramIndex, int sqlType) 
            throws SQLException {
        CallableStatement cs = conn.prepareCall("{call " + procedureName + "(?)}");
        cs.registerOutParameter(paramIndex, sqlType);
        cs.execute();
        Object result = cs.getObject(paramIndex);
        cs.close();
        return result;
    }
}

// Helper classes
class SimpleJdbcCall {
    Map<String, Object> execute(SqlParameterSource params) { return null; }
}
interface SqlParameterSource { }
class MapSqlParameterSource implements SqlParameterSource {
    MapSqlParameterSource addValue(String name, Object value) { return this; }
}
class oracle {
    class jdbc {
        class OracleTypes {
            static final int CURSOR = -10;
        }
    }
}

