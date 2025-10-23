package com.example.database;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Test file for sql-java-067: JpaQueryUsage
 * Detects JPA Query interface usage
 * JPA Query affects JPQL query patterns
 */
public class JpaQueryExample {
    
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    
    public JpaQueryExample() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPU");
        this.entityManager = entityManagerFactory.createEntityManager();
    }
    
    // Basic Query operations
    public void basicQueryOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // Create JPQL Query
        Query query1 = em.createQuery("SELECT e FROM Employee e");
        List results1 = query1.getResultList();
        
        // Query with WHERE clause
        Query query2 = em.createQuery("SELECT e FROM Employee e WHERE e.department = 'IT'");
        List results2 = query2.getResultList();
        
        // Query with parameters
        Query query3 = em.createQuery("SELECT e FROM Employee e WHERE e.department = :dept");
        query3.setParameter("dept", "Sales");
        List results3 = query3.getResultList();
        
        // Get single result
        Query query4 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query4.setParameter("id", 1L);
        Object singleResult = query4.getSingleResult();
        
        em.close();
    }
    
    // Query parameter operations
    public void queryParameterOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // Named parameters
        Query query1 = em.createQuery("SELECT e FROM Employee e WHERE e.name = :name AND e.age > :age");
        query1.setParameter("name", "John");
        query1.setParameter("age", 25);
        List results1 = query1.getResultList();
        
        // Positional parameters
        Query query2 = em.createQuery("SELECT e FROM Employee e WHERE e.name = ?1 AND e.age > ?2");
        query2.setParameter(1, "John");
        query2.setParameter(2, 25);
        List results2 = query2.getResultList();
        
        // Date parameter
        Query query3 = em.createQuery("SELECT e FROM Employee e WHERE e.hireDate > :date");
        query3.setParameter("date", new Date());
        List results3 = query3.getResultList();
        
        // Calendar parameter
        Query query4 = em.createQuery("SELECT e FROM Employee e WHERE e.hireDate > :date");
        query4.setParameter("date", Calendar.getInstance());
        List results4 = query4.getResultList();
        
        // Temporal parameter
        Query query5 = em.createQuery("SELECT e FROM Employee e WHERE e.hireDate = :date");
        query5.setParameter("date", new Date(), TemporalType.DATE);
        List results5 = query5.getResultList();
        
        em.close();
    }
    
    // Query execution methods
    public void queryExecutionMethods() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // getResultList
        Query query1 = em.createQuery("SELECT e FROM Employee e");
        List resultList = query1.getResultList();
        
        // getSingleResult
        Query query2 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query2.setParameter("id", 1L);
        Object singleResult = query2.getSingleResult();
        
        // executeUpdate
        Query query3 = em.createQuery("UPDATE Employee e SET e.active = false WHERE e.age > 65");
        em.getTransaction().begin();
        int updateCount = query3.executeUpdate();
        em.getTransaction().commit();
        
        em.close();
    }
    
    // Query pagination
    public void queryPagination() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        Query query = em.createQuery("SELECT e FROM Employee e ORDER BY e.name");
        
        // Set first result (offset)
        query.setFirstResult(0);
        
        // Set max results (limit)
        query.setMaxResults(10);
        
        List results = query.getResultList();
        
        // Pagination with calculation
        int pageNumber = 2;
        int pageSize = 20;
        Query pagedQuery = em.createQuery("SELECT e FROM Employee e ORDER BY e.id");
        pagedQuery.setFirstResult(pageNumber * pageSize);
        pagedQuery.setMaxResults(pageSize);
        List pagedResults = pagedQuery.getResultList();
        
        em.close();
    }
    
    // Query hints
    public void queryHints() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        // Cache hints
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
        query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        
        // Timeout hint
        query.setHint("javax.persistence.query.timeout", 5000);
        
        // Lock timeout hint
        query.setHint("javax.persistence.lock.timeout", 10000);
        
        // Fetch size hint
        query.setHint("javax.persistence.fetchgraph", em.getEntityGraph("Employee.detail"));
        query.setHint("javax.persistence.loadgraph", em.getEntityGraph("Employee.basic"));
        
        // Hibernate-specific hints
        query.setHint("org.hibernate.cacheable", true);
        query.setHint("org.hibernate.cacheRegion", "employeeCache");
        query.setHint("org.hibernate.fetchSize", 50);
        query.setHint("org.hibernate.readOnly", true);
        query.setHint("org.hibernate.comment", "Find all employees");
        
        List results = query.getResultList();
        
        em.close();
    }
    
    // Query lock modes
    public void queryLockModes() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        
        // Optimistic lock
        Query query1 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query1.setParameter("id", 1L);
        query1.setLockMode(LockModeType.OPTIMISTIC);
        Object result1 = query1.getSingleResult();
        
        // Optimistic force increment
        Query query2 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query2.setParameter("id", 2L);
        query2.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        Object result2 = query2.getSingleResult();
        
        // Pessimistic read
        Query query3 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query3.setParameter("id", 3L);
        query3.setLockMode(LockModeType.PESSIMISTIC_READ);
        Object result3 = query3.getSingleResult();
        
        // Pessimistic write
        Query query4 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query4.setParameter("id", 4L);
        query4.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        Object result4 = query4.getSingleResult();
        
        // Pessimistic force increment
        Query query5 = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        query5.setParameter("id", 5L);
        query5.setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        Object result5 = query5.getSingleResult();
        
        // Get lock mode
        LockModeType lockMode = query1.getLockMode();
        
        em.getTransaction().commit();
        em.close();
    }
    
    // Query flush mode
    public void queryFlushMode() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        // Set flush mode AUTO
        query.setFlushMode(FlushModeType.AUTO);
        
        // Set flush mode COMMIT
        query.setFlushMode(FlushModeType.COMMIT);
        
        // Get flush mode
        FlushModeType flushMode = query.getFlushMode();
        
        List results = query.getResultList();
        
        em.close();
    }
    
    // Named queries
    public void namedQueryOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // Create named query
        Query query1 = em.createNamedQuery("Employee.findAll");
        List results1 = query1.getResultList();
        
        // Named query with parameters
        Query query2 = em.createNamedQuery("Employee.findByDepartment");
        query2.setParameter("dept", "IT");
        List results2 = query2.getResultList();
        
        // Named query with pagination
        Query query3 = em.createNamedQuery("Employee.findAll");
        query3.setFirstResult(0);
        query3.setMaxResults(10);
        List results3 = query3.getResultList();
        
        em.close();
    }
    
    // Native queries
    public void nativeQueryOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // Basic native query
        Query query1 = em.createNativeQuery("SELECT * FROM employees");
        List results1 = query1.getResultList();
        
        // Native query with result class
        Query query2 = em.createNativeQuery("SELECT * FROM employees WHERE department = ?1", Employee.class);
        query2.setParameter(1, "IT");
        List<Employee> results2 = query2.getResultList();
        
        // Native query with result set mapping
        Query query3 = em.createNativeQuery(
            "SELECT e.id, e.name, d.name as dept_name FROM employees e JOIN departments d ON e.dept_id = d.id",
            "EmployeeDepartmentMapping"
        );
        List results3 = query3.getResultList();
        
        // Native query with parameters
        Query query4 = em.createNativeQuery("SELECT * FROM employees WHERE age > :age");
        query4.setParameter("age", 30);
        List results4 = query4.getResultList();
        
        // Native update query
        Query query5 = em.createNativeQuery("UPDATE employees SET active = 0 WHERE age > 65");
        em.getTransaction().begin();
        int updateCount = query5.executeUpdate();
        em.getTransaction().commit();
        
        // Named native query
        Query query6 = em.createNamedQuery("Employee.nativeFindAll");
        List results6 = query6.getResultList();
        
        em.close();
    }
    
    // Query unwrap
    public void queryUnwrap() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        // Unwrap to Hibernate Query
        org.hibernate.query.Query hibernateQuery = query.unwrap(org.hibernate.query.Query.class);
        
        // Unwrap to generic type
        Object unwrapped = query.unwrap(Object.class);
        
        em.close();
    }
    
    // Query getters
    public void queryGetters() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        // Get max results
        int maxResults = query.getMaxResults();
        
        // Get first result
        int firstResult = query.getFirstResult();
        
        // Get hints
        Map<String, Object> hints = query.getHints();
        
        // Get parameters
        java.util.Set<Parameter<?>> parameters = query.getParameters();
        
        // Get parameter
        Parameter<?> param = query.getParameter("name");
        Parameter<String> typedParam = query.getParameter("name", String.class);
        Parameter<?> positionalParam = query.getParameter(1);
        Parameter<String> typedPositionalParam = query.getParameter(1, String.class);
        
        // Check if parameter is bound
        boolean isBound = query.isBound(param);
        
        // Get parameter value
        Object paramValue = query.getParameterValue("name");
        Object paramValue2 = query.getParameterValue(param);
        
        em.close();
    }
    
    // Method accepting Query as parameter
    public List executeQuery(Query query) {
        return query.getResultList();
    }
    
    // Method returning Query
    public Query createQuery(String jpql) {
        return entityManager.createQuery(jpql);
    }
    
    // Static method with Query
    public static List<Employee> findEmployees(EntityManager em, Query query) {
        return query.getResultList();
    }
    
    // Inner class demonstrating Query usage
    class EmployeeRepository {
        private EntityManager em;
        
        public EmployeeRepository(EntityManager em) {
            this.em = em;
        }
        
        public List<Employee> findAll() {
            Query query = em.createQuery("SELECT e FROM Employee e");
            return query.getResultList();
        }
        
        public List<Employee> findByDepartment(String department) {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.department = :dept");
            query.setParameter("dept", department);
            return query.getResultList();
        }
        
        public Employee findById(Long id) {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
            query.setParameter("id", id);
            return (Employee) query.getSingleResult();
        }
        
        public List<Employee> findWithPagination(int page, int size) {
            Query query = em.createQuery("SELECT e FROM Employee e ORDER BY e.name");
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        }
        
        public Long count() {
            Query query = em.createQuery("SELECT COUNT(e) FROM Employee e");
            return (Long) query.getSingleResult();
        }
        
        public int updateSalary(String department, double multiplier) {
            Query query = em.createQuery(
                "UPDATE Employee e SET e.salary = e.salary * :mult WHERE e.department = :dept"
            );
            query.setParameter("mult", multiplier);
            query.setParameter("dept", department);
            return query.executeUpdate();
        }
        
        public int deleteInactive() {
            Query query = em.createQuery("DELETE FROM Employee WHERE active = false");
            return query.executeUpdate();
        }
    }
    
    // Query builder pattern
    static class QueryBuilder {
        private Query query;
        
        public QueryBuilder(EntityManager em, String jpql) {
            this.query = em.createQuery(jpql);
        }
        
        public QueryBuilder setParameter(String name, Object value) {
            query.setParameter(name, value);
            return this;
        }
        
        public QueryBuilder setFirstResult(int offset) {
            query.setFirstResult(offset);
            return this;
        }
        
        public QueryBuilder setMaxResults(int limit) {
            query.setMaxResults(limit);
            return this;
        }
        
        public QueryBuilder setHint(String hintName, Object value) {
            query.setHint(hintName, value);
            return this;
        }
        
        public Query build() {
            return query;
        }
        
        public List getResultList() {
            return query.getResultList();
        }
        
        public Object getSingleResult() {
            return query.getSingleResult();
        }
    }
    
    // Employee entity class
    @javax.persistence.Entity
    @javax.persistence.Table(name = "employees")
    @javax.persistence.NamedQueries({
        @javax.persistence.NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
        @javax.persistence.NamedQuery(name = "Employee.findByDepartment", query = "SELECT e FROM Employee e WHERE e.department = :dept")
    })
    @javax.persistence.NamedNativeQueries({
        @javax.persistence.NamedNativeQuery(name = "Employee.nativeFindAll", query = "SELECT * FROM employees", resultClass = Employee.class)
    })
    static class Employee {
        @javax.persistence.Id
        @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        private String name;
        private Integer age;
        private String department;
        private Boolean active;
        private Double salary;
        
        @javax.persistence.Temporal(TemporalType.DATE)
        private Date hireDate;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public Double getSalary() { return salary; }
        public void setSalary(Double salary) { this.salary = salary; }
        public Date getHireDate() { return hireDate; }
        public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    }
}

