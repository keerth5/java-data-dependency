// @testdata/CollectionMappingUsageTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Sample code to test rule sql‑java‑158: CollectionMappingUsage
 */
public class CollectionMappingUsageTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    // Violation: inserting collection directly via toString()
    public void insertCollectionDirect(List<String> names) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "INSERT INTO users(names) VALUES('" + names.toString() + "')"; // Violation

        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
            System.out.println("Inserted collection directly (violation).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: inserting collection using batch insert
    public void insertCollectionBatch(List<String> names) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "INSERT INTO users(name) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String name : names) {
                pstmt.setString(1, name);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Inserted collection using batch (non-violation).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: storing collection as JSON string
    public void insertCollectionAsJson(List<String> names) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "INSERT INTO users(names) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String json = objectMapper.writeValueAsString(names);
            pstmt.setString(1, json);
            pstmt.executeUpdate();
            System.out.println("Inserted collection as JSON (non-violation).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Example usage
    public static void main(String[] args) {
        CollectionMappingUsageTest test = new CollectionMappingUsageTest();
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        test.insertCollectionDirect(names);   // Violation
        test.insertCollectionBatch(names);    // Non-violation
        test.insertCollectionAsJson(names);   // Non-violation
    }
}
