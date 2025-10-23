package com.example.database;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.cursor.Cursor;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Test file for sql-java-073: MyBatisSqlSessionUsage
 * Detects MyBatis SqlSession interface usage
 * MyBatis SqlSession affects SQL mapping patterns
 */
public class MyBatisSqlSessionExample {
    
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    
    public MyBatisSqlSessionExample() {
        // Initialize SqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }
    
    // Basic SqlSession operations
    public void basicSqlSessionOperations() {
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            // Select one
            Employee employee = session.selectOne("com.example.mapper.EmployeeMapper.findById", 1L);
            
            // Select list
            List<Employee> employees = session.selectList("com.example.mapper.EmployeeMapper.findAll");
            
            // Select map
            Map<String, Object> employeeMap = session.selectMap("com.example.mapper.EmployeeMapper.findByIdAsMap", 1L, "id");
            
            // Select with parameters
            List<Employee> deptEmployees = session.selectList(
                "com.example.mapper.EmployeeMapper.findByDepartment", 
                "IT"
            );
            
            // Insert
            Employee newEmployee = new Employee();
            newEmployee.setName("John Doe");
            newEmployee.setDepartment("IT");
            int insertResult = session.insert("com.example.mapper.EmployeeMapper.insert", newEmployee);
            
            // Update
            employee.setName("Jane Doe");
            int updateResult = session.update("com.example.mapper.EmployeeMapper.update", employee);
            
            // Delete
            int deleteResult = session.delete("com.example.mapper.EmployeeMapper.delete", 1L);
            
            // Commit
            session.commit();
            
        } finally {
            session.close();
        }
    }
    
    // SqlSession with different executor types
    public void sqlSessionWithExecutorTypes() {
        // Simple executor
        SqlSession simpleSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
        try {
            List<Employee> employees = simpleSession.selectList("com.example.mapper.EmployeeMapper.findAll");
        } finally {
            simpleSession.close();
        }
        
        // Reuse executor
        SqlSession reuseSession = sqlSessionFactory.openSession(ExecutorType.REUSE);
        try {
            List<Employee> employees = reuseSession.selectList("com.example.mapper.EmployeeMapper.findAll");
        } finally {
            reuseSession.close();
        }
        
        // Batch executor
        SqlSession batchSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            for (int i = 0; i < 100; i++) {
                Employee emp = new Employee();
                emp.setName("Employee " + i);
                batchSession.insert("com.example.mapper.EmployeeMapper.insert", emp);
            }
            batchSession.commit();
        } finally {
            batchSession.close();
        }
    }
    
    // SqlSession with auto-commit
    public void sqlSessionWithAutoCommit() {
        // Auto-commit enabled
        SqlSession autoCommitSession = sqlSessionFactory.openSession(true);
        try {
            Employee emp = new Employee();
            emp.setName("Auto Commit Employee");
            autoCommitSession.insert("com.example.mapper.EmployeeMapper.insert", emp);
            // No need to commit - auto-commit is enabled
        } finally {
            autoCommitSession.close();
        }
        
        // Auto-commit disabled (default)
        SqlSession manualCommitSession = sqlSessionFactory.openSession(false);
        try {
            Employee emp = new Employee();
            emp.setName("Manual Commit Employee");
            manualCommitSession.insert("com.example.mapper.EmployeeMapper.insert", emp);
            manualCommitSession.commit();
        } finally {
            manualCommitSession.close();
        }
    }
    
    // SqlSession with connection
    public void sqlSessionWithConnection() {
        // Using existing connection
        java.sql.Connection connection = null; // Assume connection is provided
        SqlSession session = sqlSessionFactory.openSession(connection);
        try {
            List<Employee> employees = session.selectList("com.example.mapper.EmployeeMapper.findAll");
        } finally {
            session.close();
        }
    }
    
    // SqlSession with RowBounds for pagination
    public void sqlSessionWithRowBounds() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // Pagination with RowBounds
            RowBounds rowBounds = new RowBounds(0, 10); // offset, limit
            List<Employee> employees = session.selectList(
                "com.example.mapper.EmployeeMapper.findAll", 
                null, 
                rowBounds
            );
            
            // Another pagination example
            RowBounds secondPage = new RowBounds(10, 10);
            List<Employee> secondPageEmployees = session.selectList(
                "com.example.mapper.EmployeeMapper.findAll", 
                null, 
                secondPage
            );
        } finally {
            session.close();
        }
    }
    
    // SqlSession with ResultHandler
    public void sqlSessionWithResultHandler() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            ResultHandler<Employee> resultHandler = new ResultHandler<Employee>() {
                @Override
                public void handleResult(ResultContext<? extends Employee> resultContext) {
                    Employee employee = resultContext.getResultObject();
                    System.out.println("Processing employee: " + employee.getName());
                }
            };
            
            session.select("com.example.mapper.EmployeeMapper.findAll", null, resultHandler);
        } finally {
            session.close();
        }
    }
    
    // SqlSession with Cursor for large result sets
    public void sqlSessionWithCursor() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Cursor<Employee> cursor = session.selectCursor("com.example.mapper.EmployeeMapper.findAll");
            for (Employee employee : cursor) {
                System.out.println("Employee: " + employee.getName());
            }
        } finally {
            session.close();
        }
    }
    
    // SqlSession transaction management
    public void sqlSessionTransactionManagement() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // Begin transaction (implicit)
            Employee emp1 = new Employee();
            emp1.setName("Employee 1");
            session.insert("com.example.mapper.EmployeeMapper.insert", emp1);
            
            Employee emp2 = new Employee();
            emp2.setName("Employee 2");
            session.insert("com.example.mapper.EmployeeMapper.insert", emp2);
            
            // Commit transaction
            session.commit();
            
        } catch (Exception e) {
            // Rollback on error
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    // SqlSession with multiple operations
    public void sqlSessionMultipleOperations() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // Batch operations
            for (int i = 0; i < 5; i++) {
                Employee emp = new Employee();
                emp.setName("Batch Employee " + i);
                emp.setDepartment("Batch Dept");
                session.insert("com.example.mapper.EmployeeMapper.insert", emp);
            }
            
            // Update operations
            List<Employee> employees = session.selectList("com.example.mapper.EmployeeMapper.findAll");
            for (Employee emp : employees) {
                emp.setDepartment("Updated Department");
                session.update("com.example.mapper.EmployeeMapper.update", emp);
            }
            
            // Delete operations
            session.delete("com.example.mapper.EmployeeMapper.deleteByDepartment", "Old Department");
            
            session.commit();
        } finally {
            session.close();
        }
    }
    
    // SqlSession with different parameter types
    public void sqlSessionWithParameters() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // Single parameter
            Employee emp = session.selectOne("com.example.mapper.EmployeeMapper.findById", 1L);
            
            // Map parameters
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("name", "John");
            params.put("department", "IT");
            List<Employee> employees = session.selectList("com.example.mapper.EmployeeMapper.findByParams", params);
            
            // Object parameter
            Employee searchEmp = new Employee();
            searchEmp.setName("John");
            searchEmp.setDepartment("IT");
            List<Employee> searchResults = session.selectList("com.example.mapper.EmployeeMapper.findByEmployee", searchEmp);
            
            // Multiple parameters
            List<Employee> rangeEmployees = session.selectList(
                "com.example.mapper.EmployeeMapper.findByAgeRange", 
                25, 60
            );
        } finally {
            session.close();
        }
    }
    
    // SqlSession with configuration
    public void sqlSessionWithConfiguration() {
        Configuration config = sqlSessionFactory.getConfiguration();
        
        // Open session with specific configuration
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // Access configuration
            boolean lazyLoadingEnabled = config.isLazyLoadingEnabled();
            boolean aggressiveLazyLoading = config.isAggressiveLazyLoading();
            boolean multipleResultSetsEnabled = config.isMultipleResultSetsEnabled();
            
            // Use session
            List<Employee> employees = session.selectList("com.example.mapper.EmployeeMapper.findAll");
        } finally {
            session.close();
        }
    }
    
    // SqlSession with cache operations
    public void sqlSessionWithCache() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // First query - will hit database
            List<Employee> employees1 = session.selectList("com.example.mapper.EmployeeMapper.findAll");
            
            // Second query - will hit cache
            List<Employee> employees2 = session.selectList("com.example.mapper.EmployeeMapper.findAll");
            
            // Clear cache
            session.clearCache();
            
            // Third query - will hit database again
            List<Employee> employees3 = session.selectList("com.example.mapper.EmployeeMapper.findAll");
        } finally {
            session.close();
        }
    }
    
    // Method accepting SqlSession as parameter
    public void processWithSqlSession(SqlSession session) {
        List<Employee> employees = session.selectList("com.example.mapper.EmployeeMapper.findAll");
        // Process employees
    }
    
    // Method returning SqlSession
    public SqlSession createSqlSession() {
        return sqlSessionFactory.openSession();
    }
    
    // Static method with SqlSession
    public static List<Employee> findAllEmployees(SqlSession session) {
        return session.selectList("com.example.mapper.EmployeeMapper.findAll");
    }
    
    // Inner class demonstrating SqlSession usage
    class EmployeeRepository {
        private SqlSessionFactory sessionFactory;
        
        public EmployeeRepository(SqlSessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }
        
        public Employee findById(Long id) {
            SqlSession session = sessionFactory.openSession();
            try {
                return session.selectOne("com.example.mapper.EmployeeMapper.findById", id);
            } finally {
                session.close();
            }
        }
        
        public List<Employee> findAll() {
            SqlSession session = sessionFactory.openSession();
            try {
                return session.selectList("com.example.mapper.EmployeeMapper.findAll");
            } finally {
                session.close();
            }
        }
        
        public void save(Employee employee) {
            SqlSession session = sessionFactory.openSession();
            try {
                session.insert("com.example.mapper.EmployeeMapper.insert", employee);
                session.commit();
            } finally {
                session.close();
            }
        }
        
        public void update(Employee employee) {
            SqlSession session = sessionFactory.openSession();
            try {
                session.update("com.example.mapper.EmployeeMapper.update", employee);
                session.commit();
            } finally {
                session.close();
            }
        }
        
        public void delete(Long id) {
            SqlSession session = sessionFactory.openSession();
            try {
                session.delete("com.example.mapper.EmployeeMapper.delete", id);
                session.commit();
            } finally {
                session.close();
            }
        }
        
        public List<Employee> findByDepartment(String department) {
            SqlSession session = sessionFactory.openSession();
            try {
                return session.selectList("com.example.mapper.EmployeeMapper.findByDepartment", department);
            } finally {
                session.close();
            }
        }
        
        public List<Employee> findWithPagination(int offset, int limit) {
            SqlSession session = sessionFactory.openSession();
            try {
                RowBounds rowBounds = new RowBounds(offset, limit);
                return session.selectList("com.example.mapper.EmployeeMapper.findAll", null, rowBounds);
            } finally {
                session.close();
            }
        }
    }
    
    // SqlSession wrapper class
    static class SqlSessionWrapper {
        private final SqlSession delegate;
        
        public SqlSessionWrapper(SqlSession delegate) {
            this.delegate = delegate;
        }
        
        public SqlSession getDelegate() {
            return delegate;
        }
        
        public <T> T selectOne(String statement) {
            return delegate.selectOne(statement);
        }
        
        public <T> T selectOne(String statement, Object parameter) {
            return delegate.selectOne(statement, parameter);
        }
        
        public <E> List<E> selectList(String statement) {
            return delegate.selectList(statement);
        }
        
        public <E> List<E> selectList(String statement, Object parameter) {
            return delegate.selectList(statement, parameter);
        }
        
        public void close() {
            delegate.close();
        }
    }
    
    // Employee entity class
    static class Employee {
        private Long id;
        private String name;
        private String department;
        private Integer age;
        
        public Employee() {}
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }
}
