// sql-java-198: UserDefinedFunctionUsage
// Detects user-defined function calls
// This file tests detection of custom/user-defined function patterns

package com.example.jdbc;

import java.sql.*;

public class UserDefinedFunctionExample {
    
    // VIOLATION: Calling user-defined function
    public void callUserDefinedFunction(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT dbo.CalculateAge(birth_date) FROM users");
        while (rs.next()) {
            System.out.println("Age: " + rs.getInt(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function with schema prefix
    public void callSchemaFunction(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT hr.GetEmployeeFullName(?) FROM employees WHERE id = ?"
        );
        ps.setInt(1, 1001);
        ps.setInt(2, 1001);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Full name: " + rs.getString(1));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User-defined function in WHERE clause
    public void functionInWhere(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT * FROM orders WHERE finance.CalculateTotal(order_id) > 1000"
        );
        while (rs.next()) {
            System.out.println("Order: " + rs.getInt(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function with multiple parameters
    public void multiParamFunction(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT util.FormatAddress(?, ?, ?, ?) FROM customers WHERE id = ?"
        );
        ps.setString(1, "street");
        ps.setString(2, "city");
        ps.setString(3, "state");
        ps.setString(4, "zip");
        ps.setInt(5, 1001);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Address: " + rs.getString(1));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User-defined function in SELECT with alias
    public void functionWithAlias(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT name, accounting.CalculateTax(amount) AS tax_amount FROM invoices"
        );
        while (rs.next()) {
            System.out.println(rs.getString(1) + " Tax: " + rs.getDouble(2));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function in ORDER BY
    public void functionInOrderBy(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM products ORDER BY util.GetProductPriority(category, price)"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Product: " + rs.getString(1));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User-defined function in GROUP BY
    public void functionInGroupBy(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT util.GetRegion(city), COUNT(*) FROM customers GROUP BY util.GetRegion(city)"
        );
        while (rs.next()) {
            System.out.println("Region: " + rs.getString(1) + " Count: " + rs.getInt(2));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function in HAVING clause
    public void functionInHaving(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT department, AVG(salary) FROM employees GROUP BY department " +
            "HAVING AVG(salary) > hr.GetDepartmentThreshold(department)"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getDouble(2));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: Nested user-defined functions
    public void nestedUserFunctions(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT util.FormatCurrency(accounting.CalculateTotal(order_id)) FROM orders"
        );
        while (rs.next()) {
            System.out.println("Formatted total: " + rs.getString(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function with date parameters
    public void dateFunction(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT util.GetBusinessDays(?, ?) FROM dual"
        );
        ps.setDate(1, new Date(System.currentTimeMillis()));
        ps.setDate(2, new Date(System.currentTimeMillis() + 86400000 * 30));
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Business days: " + rs.getInt(1));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User-defined function returning table
    public void tableValuedFunction(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT * FROM util.GetEmployeesByDepartment('IT')"
        );
        while (rs.next()) {
            System.out.println("Employee: " + rs.getString(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function in JOIN
    public void functionInJoin(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT u.name, util.GetUserRole(u.id) FROM users u " +
            "JOIN util.GetUserPermissions(u.id) p ON u.id = p.user_id"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + " Role: " + rs.getString(2));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User-defined function in subquery
    public void functionInSubquery(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT * FROM orders WHERE total > (SELECT util.GetAverageOrderValue())"
        );
        while (rs.next()) {
            System.out.println("High value order: " + rs.getInt(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: User-defined function in CASE statement
    public void functionInCase(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT CASE WHEN util.IsWeekend(created_date) = 1 THEN 'Weekend' ELSE 'Weekday' END " +
            "FROM orders"
        );
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Day type: " + rs.getString(1));
        }
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User-defined function in UPDATE
    public void functionInUpdate(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE products SET priority = util.CalculatePriority(category, price) WHERE id = ?"
        );
        ps.setInt(1, 1001);
        ps.executeUpdate();
        ps.close();
    }
    
    // VIOLATION: User-defined function in INSERT
    public void functionInInsert(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO orders (customer_id, total, order_date) VALUES (?, util.CalculateOrderTotal(?), ?)"
        );
        ps.setInt(1, 1001);
        ps.setInt(2, 1001);
        ps.setDate(3, new Date(System.currentTimeMillis()));
        ps.executeUpdate();
        ps.close();
    }
    
    // VIOLATION: User-defined function with custom return type
    public void customReturnTypeFunction(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT util.GetCustomerInfo(customer_id) FROM orders"
        );
        while (rs.next()) {
            Object customerInfo = rs.getObject(1);
            System.out.println("Customer info: " + customerInfo);
        }
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Built-in SQL functions (not user-defined)
    public void builtInFunctions(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT UPPER(name), LENGTH(email) FROM users");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " " + rs.getInt(2));
        }
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Simple column selection
    public void simpleSelect(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT id, name, email FROM users WHERE id = ?");
        ps.setInt(1, 1001);
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: Regular table reference
    public void tableReference(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users u JOIN orders o ON u.id = o.user_id");
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Simple arithmetic operations
    public void arithmeticOperations(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT price * quantity, price + tax FROM products");
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: Literal values
    public void literalValues(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 'Hello', 42, 3.14 FROM dual");
        rs.close();
        stmt.close();
    }
}

// Additional class with user-defined function patterns
class UserFunctionHelper {
    
    // VIOLATION: Dynamic user function call
    public void dynamicUserFunction(Connection conn, String schema, String functionName, Object... params) 
            throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT ").append(schema).append(".").append(functionName).append("(");
        for (int i = 0; i < params.length; i++) {
            sql.append(i > 0 ? ",?" : "?");
        }
        sql.append(")");
        
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // VIOLATION: User function in stored procedure
    public void userFunctionInProcedure(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call process_with_user_function(?, ?)}");
        cs.setString(1, "util.CalculateValue");
        cs.setString(2, "accounting.GetTaxRate");
        cs.execute();
        cs.close();
    }
}

