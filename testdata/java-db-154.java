// @testdata/ResultSetTypeMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sample code to test rule sql‑java‑154: ResultSetTypeMapping
 */
public class ResultSetTypeMappingTest {

    // Violation: using getObject() without explicit type mapping
    public void fetchWithGetObject() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "SELECT id, name, age FROM users";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object id = rs.getObject("id");       // Violation: implicit type
                Object name = rs.getObject("name");   // Violation: implicit type
                Object age = rs.getObject("age");     // Violation: implicit type

                System.out.println("Fetched user (violation): id=" + id + ", name=" + name + ", age=" + age);
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Non-violation: using typed getters for ResultSet
    public void fetchWithTypedGetters() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "SELECT id, name, age FROM users";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");        // Non-violation: typed getter
                String name = rs.getString("name"); // Non-violation: typed getter
                int age = rs.getInt("age");      // Non-violation: typed getter

                System.out.println("Fetched user (non-violation): id=" + id + ", name=" + name + ", age=" + age);
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }

    // Violation: using getObject() with column index
    public void fetchWithGetObjectIndex() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "dbuser";
        String password = "dbpassword";

        String sql = "SELECT id, name, age FROM users";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object id = rs.getObject(1);   // Violation: column index, implicit type
                Object name = rs.getObject(2); // Violation
                Object age = rs.getObject(3);  // Violation

                System.out.println("Fetched user (violation with index): id=" + id + ", name=" + name + ", age=" + age);
            }
        } catch (SQLException e) {
            System.err.println("SQL exception: " + e.getMessage());
        }
    }
}
