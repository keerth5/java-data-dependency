package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test file for sql-java-050: CommonTableExpressionUsage
 * Detects "WITH" CTE patterns in SQL statements
 */
public class CommonTableExpressionUsageExample {
    
    public void basicCommonTableExpressionUsage() throws SQLException {
        // Basic WITH CTE usage
        String cteQuery = "WITH UserCTE AS (SELECT id, name FROM users WHERE status = 'active') SELECT * FROM UserCTE";
        String cteQueryWithMultiple = "WITH ProductCTE AS (SELECT id, name FROM products WHERE price > 100), OrderCTE AS (SELECT id, user_id FROM orders WHERE status = 'completed') SELECT p.name, o.id FROM ProductCTE p JOIN OrderCTE o ON p.id = o.product_id";
        String cteQueryWithAggregate = "WITH SalesCTE AS (SELECT user_id, SUM(total_amount) as total_sales FROM orders GROUP BY user_id) SELECT u.name, s.total_sales FROM users u JOIN SalesCTE s ON u.id = s.user_id";
        String cteQueryWithCalculation = "WITH PriceCTE AS (SELECT id, name, price, price * 1.1 as new_price FROM products) SELECT * FROM PriceCTE WHERE new_price > 1000";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteQueryWithMultiple);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteQueryWithAggregate);
        rs3.close();
        
        ResultSet rs4 = stmt.executeQuery(cteQueryWithCalculation);
        rs4.close();
        
        stmt.close();
        conn.close();
    }
    
    public void recursiveCommonTableExpressionUsage() throws SQLException {
        // Recursive WITH CTE usage
        String recursiveCTE = "WITH RecursiveCTE AS (SELECT id, name, parent_id, 0 as level FROM categories WHERE parent_id IS NULL UNION ALL SELECT c.id, c.name, c.parent_id, r.level + 1 FROM categories c INNER JOIN RecursiveCTE r ON c.parent_id = r.id) SELECT * FROM RecursiveCTE";
        String recursiveCTEWithPath = "WITH CategoryPath AS (SELECT id, name, CAST(name AS VARCHAR(MAX)) as path FROM categories WHERE parent_id IS NULL UNION ALL SELECT c.id, c.name, CAST(cp.path + ' > ' + c.name AS VARCHAR(MAX)) FROM categories c INNER JOIN CategoryPath cp ON c.parent_id = cp.id) SELECT * FROM CategoryPath";
        String recursiveCTEWithDepth = "WITH EmployeeHierarchy AS (SELECT id, name, manager_id, 0 as depth FROM employees WHERE manager_id IS NULL UNION ALL SELECT e.id, e.name, e.manager_id, eh.depth + 1 FROM employees e INNER JOIN EmployeeHierarchy eh ON e.manager_id = eh.id) SELECT * FROM EmployeeHierarchy WHERE depth <= 3";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(recursiveCTE);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(recursiveCTEWithPath);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(recursiveCTEWithDepth);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithJoins() throws SQLException {
        // CTE with JOINs
        String cteWithJoin = "WITH UserOrders AS (SELECT u.id, u.name, COUNT(o.id) as order_count FROM users u LEFT JOIN orders o ON u.id = o.user_id GROUP BY u.id, u.name) SELECT * FROM UserOrders WHERE order_count > 5";
        String cteWithMultipleJoins = "WITH ProductSales AS (SELECT p.id, p.name, c.category_name, SUM(oi.quantity) as total_sold FROM products p JOIN categories c ON p.category_id = c.id JOIN order_items oi ON p.id = oi.product_id GROUP BY p.id, p.name, c.category_name) SELECT * FROM ProductSales WHERE total_sold > 100";
        String cteWithComplexJoin = "WITH CustomerSummary AS (SELECT u.id, u.name, COUNT(DISTINCT o.id) as order_count, SUM(o.total_amount) as total_spent FROM users u JOIN orders o ON u.id = o.user_id JOIN order_items oi ON o.id = oi.order_id GROUP BY u.id, u.name) SELECT * FROM CustomerSummary ORDER BY total_spent DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithJoin);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithMultipleJoins);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithComplexJoin);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithSubqueries() throws SQLException {
        // CTE with subqueries
        String cteWithSubquery = "WITH TopCustomers AS (SELECT user_id, SUM(total_amount) as total_spent FROM orders GROUP BY user_id HAVING SUM(total_amount) > 10000) SELECT u.name, tc.total_spent FROM users u JOIN TopCustomers tc ON u.id = tc.user_id";
        String cteWithExistsSubquery = "WITH ActiveUsers AS (SELECT id, name FROM users WHERE EXISTS (SELECT 1 FROM orders WHERE user_id = users.id AND order_date > GETDATE() - 30)) SELECT * FROM ActiveUsers";
        String cteWithInSubquery = "WITH RecentOrders AS (SELECT * FROM orders WHERE order_date > GETDATE() - 7) SELECT u.name, ro.order_number FROM users u JOIN RecentOrders ro ON u.id = ro.user_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithSubquery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithExistsSubquery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithInSubquery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithWindowFunctions() throws SQLException {
        // CTE with window functions
        String cteWithWindowFunction = "WITH RankedProducts AS (SELECT id, name, price, ROW_NUMBER() OVER (ORDER BY price DESC) as price_rank FROM products) SELECT * FROM RankedProducts WHERE price_rank <= 10";
        String cteWithPartitionBy = "WITH UserOrderRanking AS (SELECT user_id, order_id, total_amount, ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY total_amount DESC) as order_rank FROM orders) SELECT * FROM UserOrderRanking WHERE order_rank = 1";
        String cteWithRunningTotal = "WITH SalesRunningTotal AS (SELECT order_date, total_amount, SUM(total_amount) OVER (ORDER BY order_date) as running_total FROM orders) SELECT * FROM SalesRunningTotal";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithWindowFunction);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithPartitionBy);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithRunningTotal);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithCaseStatements() throws SQLException {
        // CTE with CASE statements
        String cteWithCase = "WITH UserCategories AS (SELECT id, name, CASE WHEN age < 25 THEN 'Young' WHEN age < 50 THEN 'Middle' ELSE 'Senior' END as age_group FROM users) SELECT age_group, COUNT(*) as user_count FROM UserCategories GROUP BY age_group";
        String cteWithCaseComplex = "WITH ProductCategories AS (SELECT id, name, price, CASE WHEN price > 1000 THEN 'Premium' WHEN price > 100 THEN 'Standard' ELSE 'Budget' END as price_category FROM products) SELECT price_category, AVG(price) as avg_price FROM ProductCategories GROUP BY price_category";
        String cteWithCaseAdvanced = "WITH OrderAnalysis AS (SELECT id, total_amount, CASE WHEN total_amount > 5000 THEN 'High Value' WHEN total_amount > 1000 THEN 'Medium Value' ELSE 'Low Value' END as value_category, CASE WHEN order_date > GETDATE() - 30 THEN 'Recent' ELSE 'Old' END as time_category FROM orders) SELECT value_category, time_category, COUNT(*) as order_count FROM OrderAnalysis GROUP BY value_category, time_category";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithCase);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithCaseComplex);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithCaseAdvanced);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithAggregates() throws SQLException {
        // CTE with aggregate functions
        String cteWithAggregates = "WITH SalesSummary AS (SELECT user_id, COUNT(*) as order_count, SUM(total_amount) as total_spent, AVG(total_amount) as avg_order_value FROM orders GROUP BY user_id) SELECT u.name, ss.order_count, ss.total_spent, ss.avg_order_value FROM users u JOIN SalesSummary ss ON u.id = ss.user_id";
        String cteWithMultipleAggregates = "WITH ProductStats AS (SELECT category_id, COUNT(*) as product_count, MIN(price) as min_price, MAX(price) as max_price, AVG(price) as avg_price FROM products GROUP BY category_id) SELECT c.name, ps.product_count, ps.min_price, ps.max_price, ps.avg_price FROM categories c JOIN ProductStats ps ON c.id = ps.category_id";
        String cteWithComplexAggregates = "WITH MonthlySales AS (SELECT YEAR(order_date) as order_year, MONTH(order_date) as order_month, COUNT(*) as order_count, SUM(total_amount) as total_revenue FROM orders GROUP BY YEAR(order_date), MONTH(order_date)) SELECT order_year, order_month, order_count, total_revenue FROM MonthlySales ORDER BY order_year, order_month";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithAggregates);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithMultipleAggregates);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithComplexAggregates);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithDateFunctions() throws SQLException {
        // CTE with date functions
        String cteWithDateFunctions = "WITH RecentActivity AS (SELECT user_id, MAX(order_date) as last_order_date, DATEDIFF(day, MAX(order_date), GETDATE()) as days_since_last_order FROM orders GROUP BY user_id) SELECT u.name, ra.last_order_date, ra.days_since_last_order FROM users u JOIN RecentActivity ra ON u.id = ra.user_id";
        String cteWithDateCalculations = "WITH OrderTrends AS (SELECT YEAR(order_date) as order_year, MONTH(order_date) as order_month, COUNT(*) as monthly_orders FROM orders GROUP BY YEAR(order_date), MONTH(order_date)) SELECT order_year, order_month, monthly_orders FROM OrderTrends ORDER BY order_year, order_month";
        String cteWithDateFilters = "WITH QuarterlySales AS (SELECT YEAR(order_date) as order_year, CASE WHEN MONTH(order_date) IN (1,2,3) THEN 'Q1' WHEN MONTH(order_date) IN (4,5,6) THEN 'Q2' WHEN MONTH(order_date) IN (7,8,9) THEN 'Q3' ELSE 'Q4' END as quarter, SUM(total_amount) as quarterly_revenue FROM orders GROUP BY YEAR(order_date), CASE WHEN MONTH(order_date) IN (1,2,3) THEN 'Q1' WHEN MONTH(order_date) IN (4,5,6) THEN 'Q2' WHEN MONTH(order_date) IN (7,8,9) THEN 'Q3' ELSE 'Q4' END) SELECT * FROM QuarterlySales ORDER BY order_year, quarter";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithDateFunctions);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithDateCalculations);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithDateFilters);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithStringFunctions() throws SQLException {
        // CTE with string functions
        String cteWithStringFunctions = "WITH UserInitials AS (SELECT id, name, UPPER(LEFT(name, 1)) + UPPER(SUBSTRING(name, CHARINDEX(' ', name) + 1, 1)) as initials FROM users WHERE CHARINDEX(' ', name) > 0) SELECT * FROM UserInitials";
        String cteWithStringManipulation = "WITH ProductCodes AS (SELECT id, name, UPPER(LEFT(name, 3)) + RIGHT('000' + CAST(id AS VARCHAR), 3) as product_code FROM products) SELECT * FROM ProductCodes";
        String cteWithStringAnalysis = "WITH EmailAnalysis AS (SELECT id, email, SUBSTRING(email, 1, CHARINDEX('@', email) - 1) as username, SUBSTRING(email, CHARINDEX('@', email) + 1, LEN(email)) as domain FROM users WHERE CHARINDEX('@', email) > 0) SELECT domain, COUNT(*) as user_count FROM EmailAnalysis GROUP BY domain";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(cteWithStringFunctions);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(cteWithStringManipulation);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(cteWithStringAnalysis);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithPreparedStatement() throws SQLException {
        // CTE with PreparedStatement
        String selectWithCTEParam = "WITH UserCTE AS (SELECT id, name FROM users WHERE status = ?) SELECT * FROM UserCTE";
        String selectWithCTEParamCondition = "WITH ProductCTE AS (SELECT id, name, price FROM products WHERE category_id = ?) SELECT * FROM ProductCTE WHERE price > ?";
        String selectWithCTEParamCalculation = "WITH OrderCTE AS (SELECT user_id, COUNT(*) as order_count FROM orders WHERE order_date > ? GROUP BY user_id) SELECT u.name, oc.order_count FROM users u JOIN OrderCTE oc ON u.id = oc.user_id";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        
        PreparedStatement pstmt1 = conn.prepareStatement(selectWithCTEParam);
        pstmt1.setString(1, "active");
        ResultSet rs1 = pstmt1.executeQuery();
        rs1.close();
        pstmt1.close();
        
        PreparedStatement pstmt2 = conn.prepareStatement(selectWithCTEParamCondition);
        pstmt2.setInt(1, 1);
        pstmt2.setDouble(2, 100.0);
        ResultSet rs2 = pstmt2.executeQuery();
        rs2.close();
        pstmt2.close();
        
        PreparedStatement pstmt3 = conn.prepareStatement(selectWithCTEParamCalculation);
        pstmt3.setDate(1, new java.sql.Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000));
        ResultSet rs3 = pstmt3.executeQuery();
        rs3.close();
        pstmt3.close();
        
        conn.close();
    }
    
    public void cteInMethods() throws SQLException {
        // CTE in methods
        String userQuery = getUserQueryWithCTE();
        String orderQuery = getOrderQueryWithCTE();
        String productQuery = getProductQueryWithCTE();
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(userQuery);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(orderQuery);
        rs2.close();
        
        ResultSet rs3 = stmt.executeQuery(productQuery);
        rs3.close();
        
        stmt.close();
        conn.close();
    }
    
    private String getUserQueryWithCTE() {
        return "WITH ActiveUsers AS (SELECT id, name FROM users WHERE status = 'active') SELECT * FROM ActiveUsers";
    }
    
    private String getOrderQueryWithCTE() {
        return "WITH RecentOrders AS (SELECT * FROM orders WHERE order_date > GETDATE() - 30) SELECT * FROM RecentOrders";
    }
    
    private String getProductQueryWithCTE() {
        return "WITH ExpensiveProducts AS (SELECT * FROM products WHERE price > 1000) SELECT * FROM ExpensiveProducts";
    }
    
    public void cteWithDynamicSQL() throws SQLException {
        // CTE with dynamic SQL construction
        String baseQuery = "WITH UserCTE AS (SELECT id, name FROM users WHERE status = '";
        String status = "active";
        String endQuery = "') SELECT * FROM UserCTE";
        
        String dynamicQuery = baseQuery + status + endQuery;
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(dynamicQuery);
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void cteWithTransaction() throws SQLException {
        // CTE with transaction
        String transactionWithCTE = "BEGIN TRANSACTION; WITH UserSummary AS (SELECT id, name, COUNT(*) as order_count FROM users u JOIN orders o ON u.id = o.user_id GROUP BY u.id, u.name) SELECT * FROM UserSummary; INSERT INTO user_activity_log (action, timestamp) VALUES ('CTE_QUERY_EXECUTED', GETDATE()); COMMIT TRANSACTION";
        String transactionWithCTERollback = "BEGIN TRANSACTION; WITH ProductSummary AS (SELECT category_id, COUNT(*) as product_count FROM products GROUP BY category_id) SELECT * FROM ProductSummary; IF @@ROWCOUNT = 0 BEGIN ROLLBACK TRANSACTION; SELECT 'No products found' as message; END ELSE BEGIN COMMIT TRANSACTION; SELECT 'Products summarized' as message; END";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(transactionWithCTE);
        rs1.close();
        
        boolean hasResult = stmt.execute(transactionWithCTERollback);
        if (hasResult) {
            ResultSet rs2 = stmt.getResultSet();
            rs2.close();
        }
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithBusinessLogic() throws SQLException {
        // CTE with business logic
        String businessLogicWithCTE = "WITH CustomerTiers AS (SELECT user_id, SUM(total_amount) as total_spent, CASE WHEN SUM(total_amount) > 10000 THEN 'Gold' WHEN SUM(total_amount) > 5000 THEN 'Silver' ELSE 'Bronze' END as customer_tier FROM orders GROUP BY user_id) SELECT u.name, ct.total_spent, ct.customer_tier FROM users u JOIN CustomerTiers ct ON u.id = ct.user_id";
        String businessLogicComplex = "WITH ProductPerformance AS (SELECT p.id, p.name, COUNT(oi.order_id) as times_ordered, SUM(oi.quantity) as total_quantity_sold FROM products p LEFT JOIN order_items oi ON p.id = oi.product_id GROUP BY p.id, p.name) SELECT * FROM ProductPerformance WHERE times_ordered > 5 ORDER BY total_quantity_sold DESC";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(businessLogicWithCTE);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(businessLogicComplex);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
    
    public void cteWithComplexHierarchy() throws SQLException {
        // CTE with complex hierarchy
        String complexHierarchyCTE = "WITH CategoryHierarchy AS (SELECT id, name, parent_id, 0 as level, CAST(name AS VARCHAR(MAX)) as full_path FROM categories WHERE parent_id IS NULL UNION ALL SELECT c.id, c.name, c.parent_id, ch.level + 1, CAST(ch.full_path + ' > ' + c.name AS VARCHAR(MAX)) FROM categories c INNER JOIN CategoryHierarchy ch ON c.parent_id = ch.id) SELECT * FROM CategoryHierarchy ORDER BY level, name";
        String complexHierarchyWithFilter = "WITH EmployeeHierarchy AS (SELECT id, name, manager_id, 0 as depth, CAST(name AS VARCHAR(MAX)) as hierarchy_path FROM employees WHERE manager_id IS NULL UNION ALL SELECT e.id, e.name, e.manager_id, eh.depth + 1, CAST(eh.hierarchy_path + ' -> ' + e.name AS VARCHAR(MAX)) FROM employees e INNER JOIN EmployeeHierarchy eh ON e.manager_id = eh.id WHERE eh.depth < 5) SELECT * FROM EmployeeHierarchy WHERE depth <= 3";
        
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=TestDB", "user", "password");
        Statement stmt = conn.createStatement();
        
        ResultSet rs1 = stmt.executeQuery(complexHierarchyCTE);
        rs1.close();
        
        ResultSet rs2 = stmt.executeQuery(complexHierarchyWithFilter);
        rs2.close();
        
        stmt.close();
        conn.close();
    }
}
