// sql-java-194: OutputParameterRetrievalUsage
// Detects output parameter value retrieval from stored procedures
// This file tests detection of getXXX() methods for output parameters

package com.example.jdbc;

import java.sql.*;
import java.math.BigDecimal;

public class OutputParameterRetrievalExample {
    
    // VIOLATION: Retrieving integer output parameter
    public int getOutputInt(Connection conn, String department) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call get_employee_count(?, ?)}");
        cstmt.setString(1, department);
        cstmt.registerOutParameter(2, Types.INTEGER);
        cstmt.execute();
        int count = cstmt.getInt(2);
        cstmt.close();
        return count;
    }
    
    // VIOLATION: Retrieving string output parameter
    public String getOutputString(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_status(?, ?)}");
        stmt.setInt(1, 1001);
        stmt.registerOutParameter(2, Types.VARCHAR);
        stmt.execute();
        String status = stmt.getString(2);
        stmt.close();
        return status;
    }
    
    // VIOLATION: Retrieving double output parameter
    public double getOutputDouble(Connection conn, int employeeId) throws SQLException {
        CallableStatement cs = conn.prepareCall("{? = call calculate_bonus(?)}");
        cs.registerOutParameter(1, Types.DOUBLE);
        cs.setInt(2, employeeId);
        cs.execute();
        double bonus = cs.getDouble(1);
        cs.close();
        return bonus;
    }
    
    // VIOLATION: Retrieving BigDecimal output parameter
    public BigDecimal getOutputDecimal(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_account_balance(?, ?)}");
        stmt.setInt(1, 12345);
        stmt.registerOutParameter(2, Types.DECIMAL);
        stmt.execute();
        BigDecimal balance = stmt.getBigDecimal(2);
        stmt.close();
        return balance;
    }
    
    // VIOLATION: Retrieving date/timestamp output parameter
    public Timestamp getOutputTimestamp(Connection conn, int orderId) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call get_order_date(?, ?)}");
        cs.setInt(1, orderId);
        cs.registerOutParameter(2, Types.TIMESTAMP);
        cs.execute();
        Timestamp orderDate = cs.getTimestamp(2);
        cs.close();
        return orderDate;
    }
    
    // VIOLATION: Retrieving boolean output parameter
    public boolean getOutputBoolean(Connection connection, String username) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call check_user_active(?, ?)}");
        cstmt.setString(1, username);
        cstmt.registerOutParameter(2, Types.BOOLEAN);
        cstmt.execute();
        boolean isActive = cstmt.getBoolean(2);
        cstmt.close();
        return isActive;
    }
    
    // VIOLATION: Retrieving long output parameter
    public long getOutputLong(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call get_record_count(?)}");
        stmt.registerOutParameter(1, Types.BIGINT);
        stmt.execute();
        long count = stmt.getLong(1);
        stmt.close();
        return count;
    }
    
    // VIOLATION: Retrieving multiple output parameters
    public void getMultipleOutputs(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call get_statistics(?, ?, ?, ?)}");
        cs.setString(1, "2024");
        cs.registerOutParameter(2, Types.INTEGER);
        cs.registerOutParameter(3, Types.DOUBLE);
        cs.registerOutParameter(4, Types.VARCHAR);
        cs.execute();
        
        int count = cs.getInt(2);
        double average = cs.getDouble(3);
        String status = cs.getString(4);
        
        System.out.println("Count: " + count);
        System.out.println("Average: " + average);
        System.out.println("Status: " + status);
        cs.close();
    }
    
    // VIOLATION: Retrieving bytes output parameter
    public byte[] getOutputBytes(Connection conn, int documentId) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call get_document_data(?, ?)}");
        cstmt.setInt(1, documentId);
        cstmt.registerOutParameter(2, Types.BINARY);
        cstmt.execute();
        byte[] data = cstmt.getBytes(2);
        cstmt.close();
        return data;
    }
    
    // VIOLATION: Retrieving object output parameter
    public Object getOutputObject(Connection connection, int id) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call get_custom_data(?, ?)}");
        stmt.setInt(1, id);
        stmt.registerOutParameter(2, Types.JAVA_OBJECT);
        stmt.execute();
        Object result = stmt.getObject(2);
        stmt.close();
        return result;
    }
    
    // VIOLATION: Retrieving Date output parameter
    public Date getOutputDate(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call get_current_business_date(?)}");
        cs.registerOutParameter(1, Types.DATE);
        cs.execute();
        Date businessDate = cs.getDate(1);
        cs.close();
        return businessDate;
    }
    
    // VIOLATION: Retrieving Time output parameter
    public Time getOutputTime(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call get_processing_time(?, ?)}");
        cstmt.setInt(1, 1001);
        cstmt.registerOutParameter(2, Types.TIME);
        cstmt.execute();
        Time processingTime = cstmt.getTime(2);
        cstmt.close();
        return processingTime;
    }
    
    // VIOLATION: Retrieving Array output parameter
    public Array getOutputArray(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call get_sales_figures(?, ?)}");
        stmt.setInt(1, 2024);
        stmt.registerOutParameter(2, Types.ARRAY);
        stmt.execute();
        Array salesArray = stmt.getArray(2);
        stmt.close();
        return salesArray;
    }
    
    // VIOLATION: Retrieving Clob output parameter
    public Clob getOutputClob(Connection connection, int reportId) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call get_report_content(?, ?)}");
        cs.setInt(1, reportId);
        cs.registerOutParameter(2, Types.CLOB);
        cs.execute();
        Clob reportContent = cs.getClob(2);
        cs.close();
        return reportContent;
    }
    
    // VIOLATION: Retrieving Blob output parameter
    public Blob getOutputBlob(Connection conn, int imageId) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call get_image_data(?, ?)}");
        cstmt.setInt(1, imageId);
        cstmt.registerOutParameter(2, Types.BLOB);
        cstmt.execute();
        Blob imageData = cstmt.getBlob(2);
        cstmt.close();
        return imageData;
    }
    
    // VIOLATION: Retrieving output with named parameter
    public String getOutputWithNamedParam(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call proc_with_named_out(:inParam, :outParam)}");
        stmt.setString("inParam", "test");
        stmt.registerOutParameter("outParam", Types.VARCHAR);
        stmt.execute();
        String result = stmt.getString("outParam");
        stmt.close();
        return result;
    }
    
    // VIOLATION: Checking wasNull() after getting output
    public Integer getNullableOutput(Connection conn, int id) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call get_nullable_value(?, ?)}");
        cs.setInt(1, id);
        cs.registerOutParameter(2, Types.INTEGER);
        cs.execute();
        int value = cs.getInt(2);
        if (cs.wasNull()) {
            cs.close();
            return null;
        }
        cs.close();
        return value;
    }
    
    // VIOLATION: Retrieving output in try-with-resources
    public double getOutputInTryWithResources(Connection connection, int productId) throws SQLException {
        try (CallableStatement cstmt = connection.prepareCall("{call get_product_price(?, ?)}")) {
            cstmt.setInt(1, productId);
            cstmt.registerOutParameter(2, Types.DOUBLE);
            cstmt.execute();
            return cstmt.getDouble(2);
        }
    }
    
    // VIOLATION: Retrieving outputs in a loop
    public void retrieveMultipleOutputsInLoop(Connection conn, int[] ids) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call get_name(?, ?)}");
        cs.registerOutParameter(2, Types.VARCHAR);
        
        for (int id : ids) {
            cs.setInt(1, id);
            cs.execute();
            String name = cs.getString(2);
            System.out.println("ID " + id + ": " + name);
        }
        cs.close();
    }
    
    // NON-VIOLATION: Getting value from ResultSet (not output parameter)
    public String getFromResultSet(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT name FROM users WHERE id = ?");
        ps.setInt(1, 1001);
        ResultSet rs = ps.executeQuery();
        String name = null;
        if (rs.next()) {
            name = rs.getString(1);
        }
        rs.close();
        ps.close();
        return name;
    }
    
    // NON-VIOLATION: Setting parameter (not getting)
    public void setParameterOnly(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call update_user(?, ?)}");
        cstmt.setInt(1, 1001);
        cstmt.setString(2, "John");
        cstmt.execute();
        cstmt.close();
    }
    
    // NON-VIOLATION: PreparedStatement without output parameters
    public void preparedStatementUsage(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?");
        pstmt.setDouble(1, 1000.0);
        pstmt.setInt(2, 123);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    // NON-VIOLATION: Getting auto-generated keys (not output parameter)
    public long getGeneratedKey(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO users (name) VALUES (?)", 
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, "Alice");
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

// Additional class demonstrating output retrieval
class OutputRetriever {
    
    // VIOLATION: Generic output retrieval method
    public Object retrieveOutput(CallableStatement stmt, int paramIndex, int sqlType) throws SQLException {
        switch (sqlType) {
            case Types.INTEGER:
                return stmt.getInt(paramIndex);
            case Types.VARCHAR:
                return stmt.getString(paramIndex);
            case Types.DOUBLE:
                return stmt.getDouble(paramIndex);
            case Types.DATE:
                return stmt.getDate(paramIndex);
            default:
                return stmt.getObject(paramIndex);
        }
    }
}

