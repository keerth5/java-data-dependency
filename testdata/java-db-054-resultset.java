package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Test file for sql-java-054: ResultSetUsage
 * Detects java.sql.ResultSet interface usage
 */
public class ResultSetUsageExample {
    
    public void basicResultSetUsage() throws SQLException {
        // Basic ResultSet usage
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users WHERE status = 'active'";
        ResultSet rs = stmt.executeQuery(query);
        
        // Navigate through ResultSet
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            int age = rs.getInt("age");
            System.out.println("User: " + name + ", Email: " + email + ", Age: " + age);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithDifferentDataTypes() throws SQLException {
        // ResultSet with different data types
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM products";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            // String data
            String name = rs.getString("name");
            String description = rs.getString("description");
            
            // Numeric data
            int id = rs.getInt("id");
            BigDecimal price = rs.getBigDecimal("price");
            double discount = rs.getDouble("discount");
            long quantity = rs.getLong("quantity");
            
            // Boolean data
            boolean isActive = rs.getBoolean("is_active");
            
            // Date data
            java.sql.Date createdDate = rs.getDate("created_date");
            java.sql.Timestamp lastUpdated = rs.getTimestamp("last_updated");
            
            System.out.println("Product: " + name + ", Price: " + price);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithColumnIndex() throws SQLException {
        // ResultSet with column index
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT name, email, age FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            // Access columns by index (1-based)
            String name = rs.getString(1);
            String email = rs.getString(2);
            int age = rs.getInt(3);
            
            System.out.println("User: " + name + ", Email: " + email + ", Age: " + age);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithNullValues() throws SQLException {
        // ResultSet with null values
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT name, email, phone FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            
            // Check for null values
            if (rs.wasNull()) {
                phone = "No phone number";
            }
            
            System.out.println("User: " + name + ", Email: " + email + ", Phone: " + phone);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithNavigation() throws SQLException {
        // ResultSet with navigation
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users ORDER BY name";
        ResultSet rs = stmt.executeQuery(query);
        
        // Navigate forward
        while (rs.next()) {
            String name = rs.getString("name");
            System.out.println("User: " + name);
        }
        
        // Navigate backward (if supported)
        while (rs.previous()) {
            String name = rs.getString("name");
            System.out.println("Previous User: " + name);
        }
        
        // Go to first row
        if (rs.first()) {
            String name = rs.getString("name");
            System.out.println("First User: " + name);
        }
        
        // Go to last row
        if (rs.last()) {
            String name = rs.getString("name");
            System.out.println("Last User: " + name);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithMetadata() throws SQLException {
        // ResultSet with metadata
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        // Get ResultSet metadata
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        System.out.println("Column Count: " + columnCount);
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnType = metaData.getColumnTypeName(i);
            int columnSize = metaData.getColumnDisplaySize(i);
            boolean isNullable = metaData.isNullable(i) == ResultSetMetaData.columnNullable;
            
            System.out.println("Column: " + columnName + ", Type: " + columnType + 
                             ", Size: " + columnSize + ", Nullable: " + isNullable);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithScrollableCursor() throws SQLException {
        // ResultSet with scrollable cursor
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Create scrollable ResultSet
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        String query = "SELECT * FROM users ORDER BY name";
        ResultSet rs = stmt.executeQuery(query);
        
        // Get total number of rows
        rs.last();
        int totalRows = rs.getRow();
        rs.beforeFirst();
        
        System.out.println("Total rows: " + totalRows);
        
        // Navigate to specific row
        if (rs.absolute(5)) {
            String name = rs.getString("name");
            System.out.println("Row 5: " + name);
        }
        
        // Navigate relative to current position
        if (rs.relative(3)) {
            String name = rs.getString("name");
            System.out.println("3 rows forward: " + name);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithUpdatableCursor() throws SQLException {
        // ResultSet with updatable cursor
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Create updatable ResultSet
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        String query = "SELECT * FROM users WHERE status = 'active'";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            // Update current row
            rs.updateString("email", rs.getString("email").toLowerCase());
            rs.updateRow();
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithInsertRow() throws SQLException {
        // ResultSet with insert row
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Create updatable ResultSet
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        // Move to insert row
        rs.moveToInsertRow();
        
        // Set values for new row
        rs.updateString("name", "New User");
        rs.updateString("email", "newuser@example.com");
        rs.updateInt("age", 25);
        rs.updateBoolean("is_active", true);
        
        // Insert the row
        rs.insertRow();
        
        // Move back to current row
        rs.moveToCurrentRow();
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithDeleteRow() throws SQLException {
        // ResultSet with delete row
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Create updatable ResultSet
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        String query = "SELECT * FROM users WHERE status = 'inactive'";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            // Delete current row
            rs.deleteRow();
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithBatchUpdates() throws SQLException {
        // ResultSet with batch updates
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        // Create updatable ResultSet
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        String query = "SELECT * FROM users WHERE status = 'active'";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            // Update current row
            rs.updateString("email", rs.getString("email").toLowerCase());
            rs.updateRow();
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithPreparedStatement() throws SQLException {
        // ResultSet with PreparedStatement
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        String query = "SELECT * FROM users WHERE id = ? AND status = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, 1);
        pstmt.setString(2, "active");
        
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            System.out.println("User: " + name + ", Email: " + email);
        }
        
        rs.close();
        pstmt.close();
        conn.close();
    }
    
    public void resultSetWithJoin() throws SQLException {
        // ResultSet with JOIN
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT u.name, u.email, o.order_number, o.total_amount " +
                      "FROM users u JOIN orders o ON u.id = o.user_id";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String userName = rs.getString("name");
            String userEmail = rs.getString("email");
            String orderNumber = rs.getString("order_number");
            BigDecimal totalAmount = rs.getBigDecimal("total_amount");
            
            System.out.println("User: " + userName + ", Order: " + orderNumber + ", Amount: " + totalAmount);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithAggregates() throws SQLException {
        // ResultSet with aggregates
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT COUNT(*) as user_count, AVG(age) as avg_age, MAX(age) as max_age, MIN(age) as min_age FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        if (rs.next()) {
            int userCount = rs.getInt("user_count");
            double avgAge = rs.getDouble("avg_age");
            int maxAge = rs.getInt("max_age");
            int minAge = rs.getInt("min_age");
            
            System.out.println("User Count: " + userCount + ", Avg Age: " + avgAge + 
                             ", Max Age: " + maxAge + ", Min Age: " + minAge);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithGroupBy() throws SQLException {
        // ResultSet with GROUP BY
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT status, COUNT(*) as count FROM users GROUP BY status";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String status = rs.getString("status");
            int count = rs.getInt("count");
            System.out.println("Status: " + status + ", Count: " + count);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithSubquery() throws SQLException {
        // ResultSet with subquery
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE total_amount > 1000)";
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            String name = rs.getString("name");
            String email = rs.getString("email");
            System.out.println("High-value customer: " + name + ", Email: " + email);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithErrorHandling() throws SQLException {
        // ResultSet with error handling
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                System.out.println("User: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Error processing ResultSet: " + e.getMessage());
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
    }
    
    public void resultSetWithConnectionProperties() throws SQLException {
        // ResultSet with connection properties
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        // Set fetch size for large ResultSet
        rs.setFetchSize(100);
        
        // Set fetch direction
        rs.setFetchDirection(ResultSet.FETCH_FORWARD);
        
        while (rs.next()) {
            String name = rs.getString("name");
            System.out.println("User: " + name);
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithLargeData() throws SQLException {
        // ResultSet with large data
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM large_table";
        ResultSet rs = stmt.executeQuery(query);
        
        // Set fetch size for large ResultSet
        rs.setFetchSize(1000);
        
        int count = 0;
        while (rs.next() && count < 10000) {
            // Process row
            count++;
        }
        
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void resultSetWithStreaming() throws SQLException {
        // ResultSet with streaming
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);
        
        // Process ResultSet as stream
        List<String> userNames = new ArrayList<>();
        while (rs.next()) {
            String name = rs.getString("name");
            userNames.add(name);
        }
        
        // Process the list
        userNames.forEach(System.out::println);
        
        rs.close();
        stmt.close();
        conn.close();
    }
}
