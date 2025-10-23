package com.example.database;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test file for sql-java-056: NamedParameterJdbcTemplateUsage
 * Detects Spring NamedParameterJdbcTemplate usage
 * Named parameters affect Spring JDBC patterns
 */
public class NamedParameterJdbcTemplateExample {
    
    // Field declaration with NamedParameterJdbcTemplate
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    private DataSource dataSource;
    
    // Constructor initializing NamedParameterJdbcTemplate
    public NamedParameterJdbcTemplateExample(DataSource dataSource) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
    // Setter for NamedParameterJdbcTemplate
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }
    
    // Getter returning NamedParameterJdbcTemplate
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }
    
    // Query operations with Map parameters
    public void queryWithMapParameters() {
        String sql = "SELECT * FROM employees WHERE age > :age AND department = :dept";
        
        // Using HashMap for parameters
        Map<String, Object> params = new HashMap<>();
        params.put("age", 25);
        params.put("dept", "Engineering");
        
        // Query for list with Map
        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, params);
        
        // Query with RowMapper and Map
        List<Employee> employees = namedParameterJdbcTemplate.query(
            sql,
            params,
            new RowMapper<Employee>() {
                @Override
                public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setAge(rs.getInt("age"));
                    emp.setDepartment(rs.getString("department"));
                    return emp;
                }
            }
        );
        
        // Query for single object with Map
        String querySql = "SELECT * FROM employees WHERE id = :id";
        Map<String, Object> idParam = new HashMap<>();
        idParam.put("id", 1);
        
        Employee emp = namedParameterJdbcTemplate.queryForObject(
            querySql,
            idParam,
            (rs, rowNum) -> {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                return e;
            }
        );
    }
    
    // Query operations with MapSqlParameterSource
    public void queryWithMapSqlParameterSource() {
        String sql = "SELECT * FROM employees WHERE name = :name OR email = :email";
        
        // Using MapSqlParameterSource
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", "John Doe");
        params.addValue("email", "john@example.com");
        
        // Query with MapSqlParameterSource
        List<Employee> employees = namedParameterJdbcTemplate.query(
            sql,
            params,
            (rs, rowNum) -> new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("department")
            )
        );
        
        // Chained parameter addition
        MapSqlParameterSource chainedParams = new MapSqlParameterSource()
            .addValue("minAge", 20)
            .addValue("maxAge", 50)
            .addValue("active", true);
        
        String rangeSql = "SELECT * FROM employees WHERE age BETWEEN :minAge AND :maxAge AND active = :active";
        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(rangeSql, chainedParams);
        
        // Query for single value with MapSqlParameterSource
        String countSql = "SELECT COUNT(*) FROM employees WHERE department = :dept";
        MapSqlParameterSource countParams = new MapSqlParameterSource("dept", "IT");
        Integer count = namedParameterJdbcTemplate.queryForObject(countSql, countParams, Integer.class);
    }
    
    // Query operations with BeanPropertySqlParameterSource
    public void queryWithBeanPropertySqlParameterSource() {
        // Create employee search criteria bean
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setMinAge(25);
        criteria.setMaxAge(50);
        criteria.setDepartment("Sales");
        
        String sql = "SELECT * FROM employees WHERE age >= :minAge AND age <= :maxAge AND department = :department";
        
        // Using BeanPropertySqlParameterSource
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(criteria);
        
        List<Employee> employees = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Employee emp = new Employee();
            emp.setId(rs.getInt("id"));
            emp.setName(rs.getString("name"));
            emp.setAge(rs.getInt("age"));
            emp.setDepartment(rs.getString("department"));
            return emp;
        });
    }
    
    // Update operations with named parameters
    public void updateOperations() {
        // Simple update with Map
        String updateSql = "UPDATE employees SET age = :age, department = :dept WHERE id = :id";
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("age", 30);
        updateParams.put("dept", "Marketing");
        updateParams.put("id", 1);
        
        int rowsAffected = namedParameterJdbcTemplate.update(updateSql, updateParams);
        
        // Insert with MapSqlParameterSource
        String insertSql = "INSERT INTO employees (id, name, age, department) VALUES (:id, :name, :age, :dept)";
        MapSqlParameterSource insertParams = new MapSqlParameterSource()
            .addValue("id", 100)
            .addValue("name", "Jane Smith")
            .addValue("age", 35)
            .addValue("dept", "HR");
        
        namedParameterJdbcTemplate.update(insertSql, insertParams);
        
        // Delete with named parameters
        String deleteSql = "DELETE FROM employees WHERE id = :employeeId";
        Map<String, Object> deleteParams = new HashMap<>();
        deleteParams.put("employeeId", 100);
        
        namedParameterJdbcTemplate.update(deleteSql, deleteParams);
        
        // Update with BeanPropertySqlParameterSource
        Employee employee = new Employee(101, "Bob Johnson", 40, "Finance");
        String insertBeanSql = "INSERT INTO employees (id, name, age, department) VALUES (:id, :name, :age, :department)";
        SqlParameterSource beanParams = new BeanPropertySqlParameterSource(employee);
        
        namedParameterJdbcTemplate.update(insertBeanSql, beanParams);
    }
    
    // Batch operations with NamedParameterJdbcTemplate
    public void batchOperations() {
        String sql = "INSERT INTO employees (id, name, age, department) VALUES (:id, :name, :age, :dept)";
        
        // Batch update with array of Maps
        Map<String, Object>[] batchValues = new Map[3];
        batchValues[0] = Map.of("id", 200, "name", "Alice", "age", 28, "dept", "IT");
        batchValues[1] = Map.of("id", 201, "name", "Bob", "age", 32, "dept", "Sales");
        batchValues[2] = Map.of("id", 202, "name", "Charlie", "age", 45, "dept", "Marketing");
        
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
        
        // Batch update with SqlParameterSource array
        SqlParameterSource[] batchParams = new SqlParameterSource[3];
        batchParams[0] = new MapSqlParameterSource()
            .addValue("id", 300)
            .addValue("name", "David")
            .addValue("age", 29)
            .addValue("dept", "Engineering");
        batchParams[1] = new MapSqlParameterSource()
            .addValue("id", 301)
            .addValue("name", "Eve")
            .addValue("age", 33)
            .addValue("dept", "Product");
        batchParams[2] = new MapSqlParameterSource()
            .addValue("id", 302)
            .addValue("name", "Frank")
            .addValue("age", 38)
            .addValue("dept", "Operations");
        
        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }
    
    // Execute operations with NamedParameterJdbcTemplate
    public void executeOperations() {
        // Execute with named parameters
        String sql = "UPDATE employees SET salary = salary * :multiplier WHERE department = :dept";
        Map<String, Object> params = new HashMap<>();
        params.put("multiplier", 1.1);
        params.put("dept", "Engineering");
        
        namedParameterJdbcTemplate.update(sql, params);
    }
    
    // Complex queries with IN clause
    public void queriesWithInClause() {
        String sql = "SELECT * FROM employees WHERE id IN (:ids) AND department = :dept";
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", List.of(1, 2, 3, 4, 5));
        params.addValue("dept", "IT");
        
        List<Employee> employees = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Employee emp = new Employee();
            emp.setId(rs.getInt("id"));
            emp.setName(rs.getString("name"));
            return emp;
        });
    }
    
    // Method accepting NamedParameterJdbcTemplate as parameter
    public void processWithNamedTemplate(NamedParameterJdbcTemplate template) {
        Map<String, Object> params = Map.of("active", true);
        template.queryForList("SELECT * FROM employees WHERE active = :active", params);
    }
    
    // Method returning NamedParameterJdbcTemplate
    public NamedParameterJdbcTemplate createNamedTemplate(DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }
    
    // Inner class demonstrating NamedParameterJdbcTemplate usage
    class EmployeeRepository {
        private NamedParameterJdbcTemplate template;
        
        public EmployeeRepository(NamedParameterJdbcTemplate template) {
            this.template = template;
        }
        
        public Employee findById(int id) {
            String sql = "SELECT * FROM employees WHERE id = :id";
            Map<String, Object> params = Map.of("id", id);
            return template.queryForObject(sql, params, (rs, rowNum) -> 
                new Employee(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("department")
                )
            );
        }
        
        public List<Employee> findByDepartment(String department) {
            String sql = "SELECT * FROM employees WHERE department = :dept";
            MapSqlParameterSource params = new MapSqlParameterSource("dept", department);
            return template.query(sql, params, (rs, rowNum) -> 
                new Employee(rs.getInt("id"), rs.getString("name"), rs.getInt("age"), department)
            );
        }
    }
    
    // Employee model class
    static class Employee {
        private int id;
        private String name;
        private int age;
        private String department;
        
        public Employee() {}
        
        public Employee(int id, String name, int age, String department) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.department = department;
        }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
    
    // Search criteria bean for BeanPropertySqlParameterSource
    static class EmployeeSearchCriteria {
        private int minAge;
        private int maxAge;
        private String department;
        
        public int getMinAge() { return minAge; }
        public void setMinAge(int minAge) { this.minAge = minAge; }
        public int getMaxAge() { return maxAge; }
        public void setMaxAge(int maxAge) { this.maxAge = maxAge; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
}

