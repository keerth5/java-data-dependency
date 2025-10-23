// @testdata/JsonToSqlMappingTest.java
package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Sample code to test rule sql‑java‑157: JsonToSqlMapping
 */
public class JsonToSqlMappingTest {

    public static class User {
        public String name;
        public int age;
        public User(String name, int age) { this.name = name; this.age = age; }
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    // Violation: inserting JSON directly via toString()
    public void insertJsonDirect(User user) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "INSERT INTO users(data) VALUES('" + user.toString() + "')"; // Violation
        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
            System.out.println("Inserted JSON object directly (violation).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: proper serialization to JSON string
    public void insertJsonProper(User user) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "INSERT INTO users(data) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String jsonString = objectMapper.writeValueAsString(user); // Proper JSON mapping
            pstmt.setString(1, jsonString);
            pstmt.executeUpdate();
            System.out.println("Inserted JSON object via serialization (non-violation).");
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // Violation: reading JSON without deserialization
    public void readJsonDirect() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "SELECT data FROM users";
        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Violation: JSON stored as string, not mapped/deserialized
                String json = rs.getString("data");
                System.out.println("Read JSON directly (violation): " + json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: reading JSON with deserialization
    public void readJsonProper() {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String sql = "SELECT data FROM users";
        try (Connection conn = DriverManager.getConnection(url, "dbuser", "dbpassword");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String json = rs.getString("data");
                User user = objectMapper.readValue(json, User.class); // Proper deserialization
                System.out.println("Read JSON via mapping (non-violation): " + user.name + ", " + user.age);
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
