// sql-java-196: StoredProcedureReturnValueUsage
// Detects return value handling from stored procedures
// This file tests detection of stored procedure return value patterns

package com.example.jdbc;

import java.sql.*;

public class StoredProcedureReturnValueExample {
    
    // VIOLATION: Handling return value from stored procedure
    public int executeProcedureWithReturn(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{? = call calculate_total(?)}");
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.setInt(2, 100);
        cstmt.execute();
        int returnValue = cstmt.getInt(1);
        cstmt.close();
        return returnValue;
    }
    
    // VIOLATION: Checking return value for success/failure
    public boolean executeProcedureWithBooleanReturn(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{? = call validate_transaction(?)}");
        stmt.registerOutParameter(1, Types.BOOLEAN);
        stmt.setInt(2, 12345);
        stmt.execute();
        boolean isValid = stmt.getBoolean(1);
        stmt.close();
        return isValid;
    }
    
    // VIOLATION: Return value with error code
    public int executeProcedureWithErrorCode(Connection conn, String operation) throws SQLException {
        CallableStatement cs = conn.prepareCall("{? = call perform_operation(?)}");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.setString(2, operation);
        cs.execute();
        int errorCode = cs.getInt(1);
        if (errorCode != 0) {
            throw new SQLException("Operation failed with error code: " + errorCode);
        }
        cs.close();
        return errorCode;
    }
    
    // VIOLATION: Return value for status check
    public String getProcedureStatus(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{? = call get_system_status()}");
        cstmt.registerOutParameter(1, Types.VARCHAR);
        cstmt.execute();
        String status = cstmt.getString(1);
        cstmt.close();
        return status;
    }
    
    // VIOLATION: Return value with multiple outputs
    public void executeProcedureWithMultipleReturns(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{? = call process_data(?, ?)}");
        stmt.registerOutParameter(1, Types.INTEGER);  // Return value
        stmt.setString(2, "input");
        stmt.registerOutParameter(3, Types.VARCHAR);  // Output parameter
        stmt.execute();
        
        int returnCode = stmt.getInt(1);
        String result = stmt.getString(3);
        
        System.out.println("Return code: " + returnCode);
        System.out.println("Result: " + result);
        stmt.close();
    }
    
    // VIOLATION: Return value in try-with-resources
    public double calculateWithReturn(Connection connection, double amount) throws SQLException {
        try (CallableStatement cs = connection.prepareCall("{? = call calculate_tax(?)}")) {
            cs.registerOutParameter(1, Types.DOUBLE);
            cs.setDouble(2, amount);
            cs.execute();
            return cs.getDouble(1);
        }
    }
    
    // VIOLATION: Return value with null check
    public Integer getNullableReturnValue(Connection conn, int id) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{? = call get_optional_value(?)}");
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.setInt(2, id);
        cstmt.execute();
        
        int value = cstmt.getInt(1);
        if (cstmt.wasNull()) {
            cstmt.close();
            return null;
        }
        cstmt.close();
        return value;
    }
    
    // VIOLATION: Return value for record count
    public long getRecordCount(Connection connection, String tableName) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{? = call count_records(?)}");
        stmt.registerOutParameter(1, Types.BIGINT);
        stmt.setString(2, tableName);
        stmt.execute();
        long count = stmt.getLong(1);
        stmt.close();
        return count;
    }
    
    // VIOLATION: Return value with exception handling
    public int executeWithReturnHandling(Connection conn, String operation) {
        CallableStatement cstmt = null;
        try {
            cstmt = conn.prepareCall("{? = call risky_operation(?)}");
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, operation);
            cstmt.execute();
            return cstmt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Procedure execution failed: " + e.getMessage());
            return -1;
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
    
    // VIOLATION: Return value for validation
    public boolean validateData(Connection connection, String data) throws SQLException {
        CallableStatement cs = connection.prepareCall("{? = call validate_input(?)}");
        cs.registerOutParameter(1, Types.BOOLEAN);
        cs.setString(2, data);
        cs.execute();
        boolean isValid = cs.getBoolean(1);
        cs.close();
        return isValid;
    }
    
    // VIOLATION: Return value with custom type
    public Object getCustomReturnValue(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{? = call get_custom_object()}");
        stmt.registerOutParameter(1, Types.JAVA_OBJECT);
        stmt.execute();
        Object result = stmt.getObject(1);
        stmt.close();
        return result;
    }
    
    // VIOLATION: Return value in loop
    public void executeMultipleWithReturns(Connection connection, int[] ids) throws SQLException {
        CallableStatement cs = connection.prepareCall("{? = call process_id(?)}");
        cs.registerOutParameter(1, Types.INTEGER);
        
        for (int id : ids) {
            cs.setInt(2, id);
            cs.execute();
            int result = cs.getInt(1);
            System.out.println("ID " + id + " result: " + result);
        }
        cs.close();
    }
    
    // VIOLATION: Return value with named parameter
    public String getReturnWithNamedParam(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{:returnValue = call get_name()}");
        cstmt.registerOutParameter("returnValue", Types.VARCHAR);
        cstmt.execute();
        String name = cstmt.getString("returnValue");
        cstmt.close();
        return name;
    }
    
    // VIOLATION: Return value for ID generation
    public long generateId(Connection connection, String tableName) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{? = call generate_next_id(?)}");
        stmt.registerOutParameter(1, Types.BIGINT);
        stmt.setString(2, tableName);
        stmt.execute();
        long newId = stmt.getLong(1);
        stmt.close();
        return newId;
    }
    
    // NON-VIOLATION: Stored procedure without return value
    public void executeProcedureWithoutReturn(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call simple_procedure(?)}");
        cstmt.setString(1, "input");
        cstmt.execute();
        cstmt.close();
    }
    
    // NON-VIOLATION: Only output parameters (no return value)
    public String getOutputParameter(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_name(?, ?)}");
        stmt.setInt(1, 1001);
        stmt.registerOutParameter(2, Types.VARCHAR);
        stmt.execute();
        String name = stmt.getString(2);
        stmt.close();
        return name;
    }
    
    // NON-VIOLATION: PreparedStatement (not stored procedure)
    public int executeUpdate(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET status = ? WHERE id = ?");
        pstmt.setString(1, "ACTIVE");
        pstmt.setInt(2, 1001);
        int rowsUpdated = pstmt.executeUpdate();
        pstmt.close();
        return rowsUpdated;
    }
    
    // NON-VIOLATION: Regular query execution
    public void executeQuery(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Auto-generated keys (not return value)
    public long insertWithGeneratedKey(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO users (name) VALUES (?)", 
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, "John");
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        long id = 0;
        if (rs.next()) {
            id = rs.getLong(1);
        }
        rs.close();
        ps.close();
        return id;
    }
}

// Additional class with return value handling
class ReturnValueProcessor {
    
    // VIOLATION: Generic return value handler
    public Object processReturnValue(Connection conn, String procedureName, Object input, int returnType) 
            throws SQLException {
        CallableStatement stmt = conn.prepareCall("{? = call " + procedureName + "(?)}");
        stmt.registerOutParameter(1, returnType);
        stmt.setObject(2, input);
        stmt.execute();
        Object result = stmt.getObject(1);
        stmt.close();
        return result;
    }
    
    // VIOLATION: Return value with error mapping
    public int executeWithErrorMapping(Connection connection, String operation) throws SQLException {
        CallableStatement cs = connection.prepareCall("{? = call " + operation + "()}");
        cs.registerOutParameter(1, Types.INTEGER);
        cs.execute();
        int returnCode = cs.getInt(1);
        
        switch (returnCode) {
            case 0: return 0;  // Success
            case 1: throw new SQLException("Invalid input");
            case 2: throw new SQLException("Database error");
            default: throw new SQLException("Unknown error: " + returnCode);
        }
    }
}

