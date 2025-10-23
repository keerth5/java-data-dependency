// @testdata/AccessControlImplementationTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sample code to test rule sql‑java‑126: AccessControlImplementation
 */
public class AccessControlImplementationTest {

    // Violation: enforcing access control before performing database update
    public void updateRecordWithAccessControl(String userRole, int recordId, String newValue) {
        if (!"ADMIN".equals(userRole)) { // access control check
            System.out.println("Access denied: only ADMIN can update records.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbadmin";
        String password = "adminpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.createStatement().executeUpdate("UPDATE users SET name='" + newValue + "' WHERE id=" + recordId);
            System.out.println("Record updated by admin.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: controlling read access based on user role
    public void readDataWithAccessControl(String userRole) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if ("ADMIN".equals(userRole)) {
                System.out.println("Reading all sensitive records."); // access control logic
            } else {
                System.out.println("Reading only public records.");
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: no access control, all users allowed
    public void readDataWithoutAccessControl() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("All users can read all records (no access control).");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: credentials via environment variables, no access control logic
    public void writeDataWithoutAccessControl() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.createStatement().executeUpdate("INSERT INTO users(name) VALUES('NewUser')");
            System.out.println("Inserted new record without access control.");
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
