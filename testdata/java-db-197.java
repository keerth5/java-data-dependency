// sql-java-197: FunctionCallUsage
// Detects SQL function call patterns
// This file tests detection of SQL function usage in queries

package com.example.jdbc;

import java.sql.*;

public class FunctionCallExample {
    
    // VIOLATION: Using SQL functions in SELECT
    public void selectWithFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT UPPER(name), LOWER(email), LENGTH(description) FROM users"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Date functions
    public void dateFunctions(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT GETDATE(), DATEADD(day, 30, created_date), DATEDIFF(day, created_date, GETDATE()) FROM orders"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Current: " + rs.getTimestamp(1));
            System.out.println("Future: " + rs.getDate(2));
            System.out.println("Days: " + rs.getInt(3));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: String functions
    public void stringFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT SUBSTRING(name, 1, 3), CHARINDEX('@', email), REPLACE(description, 'old', 'new') FROM users"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getString(3));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Mathematical functions
    public void mathFunctions(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT ROUND(price, 2), CEILING(amount), FLOOR(quantity), ABS(balance) FROM products"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Rounded: " + rs.getDouble(1));
            System.out.println("Ceiling: " + rs.getDouble(2));
            System.out.println("Floor: " + rs.getDouble(3));
            System.out.println("Abs: " + rs.getDouble(4));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: Aggregate functions
    public void aggregateFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT COUNT(*), SUM(amount), AVG(price), MAX(created_date), MIN(updated_date) FROM orders"
        );
        if (rs.next()) {
            System.out.println("Count: " + rs.getInt(1));
            System.out.println("Sum: " + rs.getDouble(2));
            System.out.println("Average: " + rs.getDouble(3));
            System.out.println("Max Date: " + rs.getTimestamp(4));
            System.out.println("Min Date: " + rs.getTimestamp(5));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Conditional functions
    public void conditionalFunctions(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT CASE WHEN age > 18 THEN 'Adult' ELSE 'Minor' END, " +
            "COALESCE(phone, 'N/A'), ISNULL(email, 'No email') FROM users"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: Conversion functions
    public void conversionFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT CAST(price AS VARCHAR), CONVERT(INT, quantity), STR(amount, 10, 2) FROM products"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getString(3));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Window functions
    public void windowFunctions(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT name, salary, ROW_NUMBER() OVER (ORDER BY salary DESC), " +
            "RANK() OVER (ORDER BY salary DESC), DENSE_RANK() OVER (ORDER BY salary DESC) FROM employees"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getDouble(2) + " " + 
                             rs.getInt(3) + " " + rs.getInt(4) + " " + rs.getInt(5));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: String manipulation functions
    public void stringManipulationFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT LTRIM(RTRIM(name)), REVERSE(description), " +
            "STUFF(phone, 1, 3, '***'), PATINDEX('%pattern%', text) FROM users"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + 
                             rs.getString(3) + " " + rs.getInt(4));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Date/time functions
    public void dateTimeFunctions(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT YEAR(created_date), MONTH(created_date), DAY(created_date), " +
            "DATEPART(hour, created_date), GETUTCDATE() FROM orders"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Year: " + rs.getInt(1) + " Month: " + rs.getInt(2) + 
                             " Day: " + rs.getInt(3) + " Hour: " + rs.getInt(4));
            System.out.println("UTC: " + rs.getTimestamp(5));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: Nested function calls
    public void nestedFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT UPPER(SUBSTRING(name, 1, 3)), " +
            "ROUND(AVG(price), 2), " +
            "CONCAT('User: ', UPPER(name)) FROM products p JOIN users u ON p.user_id = u.id"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getDouble(2) + " " + rs.getString(3));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Function in WHERE clause
    public void functionsInWhere(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM users WHERE UPPER(name) = ? AND LENGTH(email) > ?"
        );
        ps.setString(1, "JOHN");
        ps.setInt(2, 10);
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // VIOLATION: Function in ORDER BY
    public void functionsInOrderBy(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT name, email FROM users ORDER BY LENGTH(name), UPPER(email)"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getString(2));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Function in GROUP BY
    public void functionsInGroupBy(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT YEAR(created_date), COUNT(*) FROM orders GROUP BY YEAR(created_date)"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Year: " + rs.getInt(1) + " Count: " + rs.getInt(2));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: Function in HAVING clause
    public void functionsInHaving(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT department, COUNT(*) FROM employees GROUP BY department " +
            "HAVING COUNT(*) > 5 AND AVG(salary) > 50000"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getInt(2));
        }
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Simple column selection without functions
    public void simpleSelect(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, name, email FROM users");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3));
        }
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Simple WHERE clause without functions
    public void simpleWhere(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ? AND status = ?");
        ps.setInt(1, 1001);
        ps.setString(2, "ACTIVE");
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: Simple INSERT without functions
    public void simpleInsert(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (name, email) VALUES (?, ?)");
        ps.setString(1, "John");
        ps.setString(2, "john@example.com");
        ps.executeUpdate();
        ps.close();
    }
    
    // NON-VIOLATION: Simple UPDATE without functions
    public void simpleUpdate(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE users SET status = ? WHERE id = ?");
        ps.setString(1, "INACTIVE");
        ps.setInt(2, 1001);
        ps.executeUpdate();
        ps.close();
    }
    
    // NON-VIOLATION: Simple DELETE without functions
    public void simpleDelete(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id = ?");
        ps.setInt(1, 1001);
        ps.executeUpdate();
        ps.close();
    }
}

// Additional class with function usage
class FunctionHelper {
    
    // VIOLATION: Dynamic function call
    public void dynamicFunctionCall(Connection conn, String functionName, String column) throws SQLException {
        String sql = "SELECT " + functionName + "(" + column + ") FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Function in stored procedure call
    public void functionInProcedure(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call process_with_function(?, ?)}");
        cs.setString(1, "UPPER(name)");
        cs.setString(2, "LENGTH(email)");
        cs.execute();
        cs.close();
    }
}

