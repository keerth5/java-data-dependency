package com.example.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.dao.DataAccessException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Test file for sql-java-055: JdbcTemplateUsage
 * Detects Spring JdbcTemplate usage
 * JdbcTemplate affects Spring Framework dependency
 */
public class JdbcTemplateExample {
    
    // Direct field declaration with JdbcTemplate
    private JdbcTemplate jdbcTemplate;
    
    private DataSource dataSource;
    
    // Constructor with JdbcTemplate initialization
    public JdbcTemplateExample(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    // Setter method for JdbcTemplate
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // Getter method returning JdbcTemplate
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    
    // Query operations using JdbcTemplate
    public void queryOperations() {
        // Simple query for list
        String sql = "SELECT * FROM employees";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        
        // Query with RowMapper
        List<Employee> employees = jdbcTemplate.query(sql, new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setAge(rs.getInt("age"));
                return emp;
            }
        });
        
        // Query with lambda RowMapper
        List<Employee> empList = jdbcTemplate.query(
            "SELECT * FROM employees WHERE age > ?",
            (rs, rowNum) -> {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                return e;
            },
            25
        );
        
        // Query for single object
        Employee emp = jdbcTemplate.queryForObject(
            "SELECT * FROM employees WHERE id = ?",
            new Object[]{1},
            new RowMapper<Employee>() {
                public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setName(rs.getString("name"));
                    return e;
                }
            }
        );
        
        // Query for single value
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM employees", Integer.class);
        
        String name = jdbcTemplate.queryForObject(
            "SELECT name FROM employees WHERE id = ?",
            new Object[]{1},
            String.class
        );
    }
    
    // Update operations using JdbcTemplate
    public void updateOperations() {
        // Simple update
        int rows = jdbcTemplate.update(
            "UPDATE employees SET age = ? WHERE id = ?",
            30, 1
        );
        
        // Insert operation
        jdbcTemplate.update(
            "INSERT INTO employees (id, name, age) VALUES (?, ?, ?)",
            100, "John Doe", 35
        );
        
        // Delete operation
        jdbcTemplate.update(
            "DELETE FROM employees WHERE id = ?",
            100
        );
        
        // Update with PreparedStatementCreator
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO employees (id, name, age) VALUES (?, ?, ?)"
                );
                ps.setInt(1, 101);
                ps.setString(2, "Jane Doe");
                ps.setInt(3, 28);
                return ps;
            }
        });
        
        // Update with PreparedStatementSetter
        jdbcTemplate.update(
            "INSERT INTO employees (id, name, age) VALUES (?, ?, ?)",
            new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setInt(1, 102);
                    ps.setString(2, "Bob Smith");
                    ps.setInt(3, 40);
                }
            }
        );
    }
    
    // Batch operations using JdbcTemplate
    public void batchOperations() {
        String sql = "INSERT INTO employees (id, name, age) VALUES (?, ?, ?)";
        
        List<Object[]> batchArgs = List.of(
            new Object[]{200, "Alice", 30},
            new Object[]{201, "Bob", 35},
            new Object[]{202, "Charlie", 40}
        );
        
        // Batch update with array of parameters
        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batchArgs);
        
        // Batch update with BatchPreparedStatementSetter
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, 300 + i);
                ps.setString(2, "Employee" + i);
                ps.setInt(3, 25 + i);
            }
            
            @Override
            public int getBatchSize() {
                return 5;
            }
        });
    }
    
    // Execute operations using JdbcTemplate
    public void executeOperations() {
        // Execute statement
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS temp_table (id INT, name VARCHAR(100))");
        
        // Execute with callback
        jdbcTemplate.execute(
            "INSERT INTO employees (id, name, age) VALUES (?, ?, ?)",
            new PreparedStatementCallback<Boolean>() {
                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setInt(1, 400);
                    ps.setString(2, "Test User");
                    ps.setInt(3, 50);
                    return ps.execute();
                }
            }
        );
    }
    
    // Query with callbacks using JdbcTemplate
    public void queryWithCallbacks() {
        String sql = "SELECT * FROM employees";
        
        // Query with ResultSetExtractor
        List<Employee> employees = jdbcTemplate.query(sql, new ResultSetExtractor<List<Employee>>() {
            @Override
            public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Employee> list = new java.util.ArrayList<>();
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setAge(rs.getInt("age"));
                    list.add(emp);
                }
                return list;
            }
        });
        
        // Query with RowCallbackHandler
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("Employee: " + id + " - " + name);
            }
        });
    }
    
    // Method accepting JdbcTemplate as parameter
    public void processWithJdbcTemplate(JdbcTemplate template) {
        template.queryForList("SELECT * FROM employees");
    }
    
    // Method returning JdbcTemplate
    public JdbcTemplate createJdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
    
    // Static method with JdbcTemplate
    public static void staticOperation(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("TRUNCATE TABLE temp_table");
    }
    
    // Inner class demonstrating JdbcTemplate usage
    class EmployeeDao {
        private JdbcTemplate jdbcTemplate;
        
        public EmployeeDao(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }
        
        public List<Employee> findAll() {
            return jdbcTemplate.query("SELECT * FROM employees", 
                (rs, rowNum) -> new Employee(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age")
                )
            );
        }
    }
    
    // Helper class for PreparedStatementCallback
    interface PreparedStatementCallback<T> {
        T doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException;
    }
    
    // Employee model class
    static class Employee {
        private int id;
        private String name;
        private int age;
        
        public Employee() {}
        
        public Employee(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
}

