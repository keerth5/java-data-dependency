// sql-java-195: InputOutputParameterUsage
// Detects input-output (INOUT) parameter usage in stored procedures
// This file tests detection of bidirectional parameter patterns

package com.example.jdbc;

import java.sql.*;

public class InputOutputParameterExample {
    
    // VIOLATION: INOUT parameter - set and register same parameter
    public int incrementCounter(Connection conn, int initialValue) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call increment_value(?)}");
        cstmt.setInt(1, initialValue);
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.execute();
        int newValue = cstmt.getInt(1);
        cstmt.close();
        return newValue;
    }
    
    // VIOLATION: INOUT parameter with string manipulation
    public String processString(Connection connection, String input) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call uppercase_and_trim(?)}");
        stmt.setString(1, input);
        stmt.registerOutParameter(1, Types.VARCHAR);
        stmt.execute();
        String output = stmt.getString(1);
        stmt.close();
        return output;
    }
    
    // VIOLATION: INOUT parameter with double calculation
    public double applyTax(Connection conn, double amount) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call calculate_with_tax(?)}");
        cs.setDouble(1, amount);
        cs.registerOutParameter(1, Types.DOUBLE);
        cs.execute();
        double totalWithTax = cs.getDouble(1);
        cs.close();
        return totalWithTax;
    }
    
    // VIOLATION: Multiple INOUT parameters
    public void swapValues(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call swap_values(?, ?)}");
        cstmt.setInt(1, 100);
        cstmt.setInt(2, 200);
        cstmt.registerOutParameter(1, Types.INTEGER);
        cstmt.registerOutParameter(2, Types.INTEGER);
        cstmt.execute();
        
        int value1 = cstmt.getInt(1);
        int value2 = cstmt.getInt(2);
        System.out.println("After swap: " + value1 + ", " + value2);
        cstmt.close();
    }
    
    // VIOLATION: INOUT parameter with timestamp
    public Timestamp updateTimestamp(Connection conn, Timestamp currentTime) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call adjust_timestamp(?)}");
        stmt.setTimestamp(1, currentTime);
        stmt.registerOutParameter(1, Types.TIMESTAMP);
        stmt.execute();
        Timestamp adjustedTime = stmt.getTimestamp(1);
        stmt.close();
        return adjustedTime;
    }
    
    // VIOLATION: INOUT parameter in transaction
    public void updateBalanceInOut(Connection connection, int accountId, double amount) throws SQLException {
        connection.setAutoCommit(false);
        try {
            CallableStatement cs = connection.prepareCall("{call update_balance(?, ?)}");
            cs.setInt(1, accountId);
            cs.setDouble(2, amount);
            cs.registerOutParameter(2, Types.DOUBLE);
            cs.execute();
            double newBalance = cs.getDouble(2);
            System.out.println("New balance: " + newBalance);
            connection.commit();
            cs.close();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    // VIOLATION: INOUT parameter with BigDecimal
    public java.math.BigDecimal calculateDiscount(Connection conn, java.math.BigDecimal price) 
            throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call apply_discount(?)}");
        cstmt.setBigDecimal(1, price);
        cstmt.registerOutParameter(1, Types.DECIMAL);
        cstmt.execute();
        java.math.BigDecimal discountedPrice = cstmt.getBigDecimal(1);
        cstmt.close();
        return discountedPrice;
    }
    
    // VIOLATION: INOUT parameter with boolean
    public boolean toggleFlag(Connection connection, boolean currentFlag) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call toggle_status(?)}");
        stmt.setBoolean(1, currentFlag);
        stmt.registerOutParameter(1, Types.BOOLEAN);
        stmt.execute();
        boolean newFlag = stmt.getBoolean(1);
        stmt.close();
        return newFlag;
    }
    
    // VIOLATION: INOUT parameter in try-with-resources
    public long multiplyValue(Connection conn, long value, int multiplier) throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{call multiply_long(?, ?)}")) {
            cs.setLong(1, value);
            cs.setInt(2, multiplier);
            cs.registerOutParameter(1, Types.BIGINT);
            cs.execute();
            return cs.getLong(1);
        }
    }
    
    // VIOLATION: INOUT parameter with date
    public Date adjustDate(Connection connection, Date inputDate, int days) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call add_days(?, ?)}");
        cstmt.setDate(1, inputDate);
        cstmt.setInt(2, days);
        cstmt.registerOutParameter(1, Types.DATE);
        cstmt.execute();
        Date adjustedDate = cstmt.getDate(1);
        cstmt.close();
        return adjustedDate;
    }
    
    // VIOLATION: Mixed IN, OUT, and INOUT parameters
    public void complexParameterMix(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call complex_proc(?, ?, ?, ?)}");
        // IN parameter
        stmt.setInt(1, 100);
        // INOUT parameter
        stmt.setString(2, "initial");
        stmt.registerOutParameter(2, Types.VARCHAR);
        // OUT parameter
        stmt.registerOutParameter(3, Types.DOUBLE);
        // INOUT parameter
        stmt.setBoolean(4, true);
        stmt.registerOutParameter(4, Types.BOOLEAN);
        
        stmt.execute();
        
        String modifiedString = stmt.getString(2);
        double outputValue = stmt.getDouble(3);
        boolean modifiedFlag = stmt.getBoolean(4);
        
        stmt.close();
    }
    
    // VIOLATION: INOUT parameter with byte array
    public byte[] processData(Connection connection, byte[] data) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call encrypt_data(?)}");
        cs.setBytes(1, data);
        cs.registerOutParameter(1, Types.BINARY);
        cs.execute();
        byte[] encryptedData = cs.getBytes(1);
        cs.close();
        return encryptedData;
    }
    
    // VIOLATION: INOUT parameter in loop
    public void processMultipleInOut(Connection conn, int[] values) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call square_value(?)}");
        cstmt.registerOutParameter(1, Types.INTEGER);
        
        for (int i = 0; i < values.length; i++) {
            cstmt.setInt(1, values[i]);
            cstmt.execute();
            values[i] = cstmt.getInt(1);
        }
        cstmt.close();
    }
    
    // VIOLATION: INOUT with named parameter
    public String processWithNamedParam(Connection connection, String value) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call process_text(:textParam)}");
        stmt.setString("textParam", value);
        stmt.registerOutParameter("textParam", Types.VARCHAR);
        stmt.execute();
        String result = stmt.getString("textParam");
        stmt.close();
        return result;
    }
    
    // VIOLATION: INOUT parameter with null check
    public Integer processNullableValue(Connection conn, Integer input) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call handle_nullable(?)}");
        if (input != null) {
            cs.setInt(1, input);
        } else {
            cs.setNull(1, Types.INTEGER);
        }
        cs.registerOutParameter(1, Types.INTEGER);
        cs.execute();
        
        int result = cs.getInt(1);
        if (cs.wasNull()) {
            cs.close();
            return null;
        }
        cs.close();
        return result;
    }
    
    // VIOLATION: INOUT parameter with error handling
    public double safeInOutCall(Connection connection, double value) {
        CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall("{call safe_calculation(?)}");
            stmt.setDouble(1, value);
            stmt.registerOutParameter(1, Types.DOUBLE);
            stmt.execute();
            return stmt.getDouble(1);
        } catch (SQLException e) {
            System.err.println("INOUT parameter error: " + e.getMessage());
            return value;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // NON-VIOLATION: Only IN parameter (set but not registered)
    public void inputParameterOnly(Connection conn, int value) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call simple_insert(?)}");
        cstmt.setInt(1, value);
        cstmt.execute();
        cstmt.close();
    }
    
    // NON-VIOLATION: Only OUT parameter (registered but not set)
    public int outputParameterOnly(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_count(?)}");
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.execute();
        int count = stmt.getInt(1);
        stmt.close();
        return count;
    }
    
    // NON-VIOLATION: Separate IN and OUT parameters (not same parameter)
    public String separateInOut(Connection conn, int id) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call get_name(?, ?)}");
        cs.setInt(1, id);  // IN parameter
        cs.registerOutParameter(2, Types.VARCHAR);  // OUT parameter (different index)
        cs.execute();
        String name = cs.getString(2);
        cs.close();
        return name;
    }
    
    // NON-VIOLATION: PreparedStatement without INOUT
    public void preparedStatementUsage(Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?");
        pstmt.setString(1, "John");
        pstmt.setInt(2, 1001);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // NON-VIOLATION: Regular query without parameters
    public void simpleQuery(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        rs.close();
        stmt.close();
    }
}

// Additional class with INOUT patterns
class InOutHelper {
    
    // VIOLATION: Generic INOUT handler
    public Object handleInOutParameter(Connection conn, String procName, Object value, int sqlType) 
            throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call " + procName + "(?)}");
        stmt.setObject(1, value);
        stmt.registerOutParameter(1, sqlType);
        stmt.execute();
        Object result = stmt.getObject(1);
        stmt.close();
        return result;
    }
    
    // VIOLATION: INOUT with custom type
    public void processCustomType(Connection connection, String typeName) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call modify_custom_type(?)}");
        cs.setObject(1, createCustomObject());
        cs.registerOutParameter(1, Types.STRUCT, typeName);
        cs.execute();
        Object modifiedObject = cs.getObject(1);
        cs.close();
    }
    
    private Object createCustomObject() {
        return new Object();
    }
}

