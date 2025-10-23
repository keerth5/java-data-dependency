// @testdata/RoleBasedSecurityUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑125: RoleBasedSecurityUsage
 */
public class RoleBasedSecurityUsageTest {

    // Violation: enforcing role-based access in database queries
    public void accessWithRoleCheck(String userRole) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if ("ADMIN".equals(userRole)) {
                System.out.println("Access granted to admin features.");
            } else if ("USER".equals(userRole)) {
                System.out.println("Access granted to user features.");
            } else {
                System.out.println("Access denied.");
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: restricting SQL operation based on roles
    public void deleteRecordBasedOnRole(String userRole, int recordId) {
        if (!"ADMIN".equals(userRole)) {
            System.out.println("Non-admins cannot delete records."); // role-based restriction
            return;
        }

        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbadmin";
        String password = "adminpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.createStatement().executeUpdate("DELETE FROM users WHERE id=" + recordId);
            System.out.println("Record deleted by admin.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: no role checks, open access
    public void accessWithoutRoleCheck() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Access granted to all users (no role-based security).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using environment variables for authentication, no role logic
    public void accessWithEnvCredentials() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Access granted using environment credentials (no role checks).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
