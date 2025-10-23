package com.example.database;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.Date;
import java.util.List;

/**
 * Test file for sql-java-069: JpaCriteriaApiUsage
 * Detects JPA Criteria API usage
 * Criteria API affects programmatic query construction
 */
public class JpaCriteriaApiExample {
    
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    
    public JpaCriteriaApiExample() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPU");
        this.entityManager = entityManagerFactory.createEntityManager();
    }
    
    // Basic Criteria API operations
    public void basicCriteriaOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // Get CriteriaBuilder
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        
        // Create CriteriaQuery
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        
        // Define root
        Root<Employee> root = criteriaQuery.from(Employee.class);
        
        // Select
        criteriaQuery.select(root);
        
        // Execute query
        TypedQuery<Employee> query = em.createQuery(criteriaQuery);
        List<Employee> employees = query.getResultList();
        
        em.close();
    }
    
    // Criteria API with WHERE clause
    public void criteriaWithWhere() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Equal predicate
        cq.select(root).where(cb.equal(root.get("department"), "IT"));
        
        // Not equal predicate
        cq.select(root).where(cb.notEqual(root.get("status"), "INACTIVE"));
        
        // Greater than predicate
        cq.select(root).where(cb.gt(root.get("age"), 25));
        
        // Greater than or equal predicate
        cq.select(root).where(cb.ge(root.get("age"), 25));
        
        // Less than predicate
        cq.select(root).where(cb.lt(root.get("age"), 60));
        
        // Less than or equal predicate
        cq.select(root).where(cb.le(root.get("age"), 60));
        
        // Between predicate
        cq.select(root).where(cb.between(root.get("age"), 25, 60));
        
        // Like predicate
        cq.select(root).where(cb.like(root.get("name"), "John%"));
        
        // Not like predicate
        cq.select(root).where(cb.notLike(root.get("name"), "Test%"));
        
        // Is null predicate
        cq.select(root).where(cb.isNull(root.get("manager")));
        
        // Is not null predicate
        cq.select(root).where(cb.isNotNull(root.get("email")));
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with logical operators
    public void criteriaWithLogicalOperators() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // AND condition
        Predicate deptPredicate = cb.equal(root.get("department"), "IT");
        Predicate agePredicate = cb.gt(root.get("age"), 25);
        cq.select(root).where(cb.and(deptPredicate, agePredicate));
        
        // OR condition
        Predicate salesPredicate = cb.equal(root.get("department"), "Sales");
        Predicate marketingPredicate = cb.equal(root.get("department"), "Marketing");
        cq.select(root).where(cb.or(salesPredicate, marketingPredicate));
        
        // NOT condition
        cq.select(root).where(cb.not(cb.equal(root.get("status"), "INACTIVE")));
        
        // Complex combination
        Predicate complexPredicate = cb.and(
            cb.or(
                cb.equal(root.get("department"), "IT"),
                cb.equal(root.get("department"), "Engineering")
            ),
            cb.gt(root.get("age"), 30),
            cb.isTrue(root.get("active"))
        );
        cq.select(root).where(complexPredicate);
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with ordering
    public void criteriaWithOrdering() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Ascending order
        cq.select(root).orderBy(cb.asc(root.get("name")));
        
        // Descending order
        cq.select(root).orderBy(cb.desc(root.get("age")));
        
        // Multiple ordering
        cq.select(root).orderBy(
            cb.asc(root.get("department")),
            cb.desc(root.get("salary")),
            cb.asc(root.get("name"))
        );
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with pagination
    public void criteriaWithPagination() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        cq.select(root);
        
        TypedQuery<Employee> query = em.createQuery(cq);
        query.setFirstResult(0);
        query.setMaxResults(10);
        
        List<Employee> employees = query.getResultList();
        em.close();
    }
    
    // Criteria API with aggregate functions
    public void criteriaWithAggregateFunctions() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        // Count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        countQuery.select(cb.count(countRoot));
        Long count = em.createQuery(countQuery).getSingleResult();
        
        // Sum
        CriteriaQuery<Double> sumQuery = cb.createQuery(Double.class);
        Root<Employee> sumRoot = sumQuery.from(Employee.class);
        sumQuery.select(cb.sum(sumRoot.get("salary")));
        Double totalSalary = em.createQuery(sumQuery).getSingleResult();
        
        // Average
        CriteriaQuery<Double> avgQuery = cb.createQuery(Double.class);
        Root<Employee> avgRoot = avgQuery.from(Employee.class);
        avgQuery.select(cb.avg(avgRoot.get("age")));
        Double avgAge = em.createQuery(avgQuery).getSingleResult();
        
        // Min
        CriteriaQuery<Double> minQuery = cb.createQuery(Double.class);
        Root<Employee> minRoot = minQuery.from(Employee.class);
        minQuery.select(cb.min(minRoot.get("salary")));
        Double minSalary = em.createQuery(minQuery).getSingleResult();
        
        // Max
        CriteriaQuery<Double> maxQuery = cb.createQuery(Double.class);
        Root<Employee> maxRoot = maxQuery.from(Employee.class);
        maxQuery.select(cb.max(maxRoot.get("salary")));
        Double maxSalary = em.createQuery(maxQuery).getSingleResult();
        
        // Count distinct
        CriteriaQuery<Long> distinctQuery = cb.createQuery(Long.class);
        Root<Employee> distinctRoot = distinctQuery.from(Employee.class);
        distinctQuery.select(cb.countDistinct(distinctRoot.get("department")));
        Long distinctCount = em.createQuery(distinctQuery).getSingleResult();
        
        em.close();
    }
    
    // Criteria API with GROUP BY
    public void criteriaWithGroupBy() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Group by department
        cq.multiselect(root.get("department"), cb.count(root))
          .groupBy(root.get("department"));
        
        List<Object[]> results = em.createQuery(cq).getResultList();
        
        // Group by with HAVING
        CriteriaQuery<Object[]> havingQuery = cb.createQuery(Object[].class);
        Root<Employee> havingRoot = havingQuery.from(Employee.class);
        havingQuery.multiselect(havingRoot.get("department"), cb.avg(havingRoot.get("salary")))
                   .groupBy(havingRoot.get("department"))
                   .having(cb.gt(cb.avg(havingRoot.get("salary")), 50000));
        
        List<Object[]> havingResults = em.createQuery(havingQuery).getResultList();
        
        em.close();
    }
    
    // Criteria API with JOIN
    public void criteriaWithJoin() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Inner join
        Join<Employee, Department> department = root.join("department");
        cq.select(root).where(cb.equal(department.get("name"), "IT"));
        
        // Left join
        Join<Employee, Project> projects = root.join("projects", JoinType.LEFT);
        cq.select(root).where(cb.equal(projects.get("status"), "ACTIVE"));
        
        // Right join (if supported)
        Join<Employee, Employee> manager = root.join("manager", JoinType.RIGHT);
        
        // Fetch join
        root.fetch("department", JoinType.LEFT);
        root.fetch("projects", JoinType.LEFT);
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with subqueries
    public void criteriaWithSubqueries() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Subquery for average salary
        Subquery<Double> subquery = cq.subquery(Double.class);
        Root<Employee> subRoot = subquery.from(Employee.class);
        subquery.select(cb.avg(subRoot.get("salary")));
        
        cq.select(root).where(cb.gt(root.get("salary"), subquery));
        
        // Subquery with IN
        Subquery<String> deptSubquery = cq.subquery(String.class);
        Root<Department> deptRoot = deptSubquery.from(Department.class);
        deptSubquery.select(deptRoot.get("name"))
                    .where(cb.equal(deptRoot.get("location"), "New York"));
        
        cq.select(root).where(root.get("department").in(deptSubquery));
        
        // Subquery with EXISTS
        Subquery<Project> existsSubquery = cq.subquery(Project.class);
        Root<Project> projectRoot = existsSubquery.from(Project.class);
        existsSubquery.select(projectRoot)
                      .where(cb.and(
                          cb.equal(projectRoot.get("employee"), root),
                          cb.equal(projectRoot.get("status"), "ACTIVE")
                      ));
        
        cq.select(root).where(cb.exists(existsSubquery));
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with IN clause
    public void criteriaWithInClause() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // IN clause with values
        cq.select(root).where(root.get("department").in("IT", "Sales", "Marketing"));
        
        // IN clause with list
        List<String> departments = List.of("IT", "Engineering", "Product");
        cq.select(root).where(root.get("department").in(departments));
        
        // IN clause with IDs
        cq.select(root).where(root.get("id").in(1L, 2L, 3L, 4L, 5L));
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with expressions
    public void criteriaWithExpressions() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // String functions
        cq.select(root).where(cb.like(cb.lower(root.get("name")), "john%"));
        cq.select(root).where(cb.like(cb.upper(root.get("name")), "JOHN%"));
        cq.select(root).where(cb.equal(cb.length(root.get("name")), 10));
        cq.select(root).where(cb.like(cb.trim(root.get("name")), "John%"));
        cq.select(root).where(cb.like(cb.substring(root.get("name"), 1, 3), "Joh"));
        
        // Arithmetic functions
        Expression<Integer> ageExpression = cb.sum(root.get("age"), 10);
        cq.select(root).where(cb.gt(ageExpression, 35));
        
        Expression<Integer> diffExpression = cb.diff(root.get("age"), 5);
        cq.select(root).where(cb.lt(diffExpression, 30));
        
        Expression<Double> prodExpression = cb.prod(root.get("salary"), 1.1);
        cq.select(root).where(cb.gt(prodExpression, 60000));
        
        Expression<Double> quotExpression = cb.quot(root.get("salary"), 12);
        
        // Date functions
        Expression<Integer> yearExpression = cb.function("YEAR", Integer.class, root.get("hireDate"));
        cq.select(root).where(cb.equal(yearExpression, 2020));
        
        // Coalesce
        Expression<String> coalesceExpression = cb.coalesce(root.get("email"), "no-email@example.com");
        
        // Nullif
        Expression<String> nullifExpression = cb.nullif(root.get("status"), "INACTIVE");
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API for CriteriaUpdate
    public void criteriaUpdate() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        // Create CriteriaUpdate
        CriteriaUpdate<Employee> criteriaUpdate = cb.createCriteriaUpdate(Employee.class);
        Root<Employee> root = criteriaUpdate.from(Employee.class);
        
        // Set values
        criteriaUpdate.set("active", false);
        criteriaUpdate.set("status", "INACTIVE");
        
        // Where clause
        criteriaUpdate.where(cb.gt(root.get("age"), 65));
        
        // Execute update
        em.getTransaction().begin();
        int updated = em.createQuery(criteriaUpdate).executeUpdate();
        em.getTransaction().commit();
        
        // Multiple set operations
        CriteriaUpdate<Employee> multiUpdate = cb.createCriteriaUpdate(Employee.class);
        Root<Employee> multiRoot = multiUpdate.from(Employee.class);
        multiUpdate.set("salary", cb.prod(multiRoot.get("salary"), 1.1))
                   .set("lastUpdated", cb.currentTimestamp())
                   .where(cb.equal(multiRoot.get("department"), "IT"));
        
        em.getTransaction().begin();
        em.createQuery(multiUpdate).executeUpdate();
        em.getTransaction().commit();
        
        em.close();
    }
    
    // Criteria API for CriteriaDelete
    public void criteriaDelete() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        // Create CriteriaDelete
        CriteriaDelete<Employee> criteriaDelete = cb.createCriteriaDelete(Employee.class);
        Root<Employee> root = criteriaDelete.from(Employee.class);
        
        // Where clause
        criteriaDelete.where(cb.equal(root.get("active"), false));
        
        // Execute delete
        em.getTransaction().begin();
        int deleted = em.createQuery(criteriaDelete).executeUpdate();
        em.getTransaction().commit();
        
        em.close();
    }
    
    // Criteria API with CASE expression
    public void criteriaWithCase() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Simple CASE
        Expression<String> priorityCase = cb.selectCase()
            .when(cb.gt(root.get("salary"), 100000), "HIGH")
            .when(cb.gt(root.get("salary"), 50000), "MEDIUM")
            .otherwise("LOW");
        
        cq.multiselect(root.get("name"), priorityCase);
        
        // CASE with predicates
        Expression<String> statusCase = cb.selectCase()
            .when(cb.and(cb.isTrue(root.get("active")), cb.isNotNull(root.get("email"))), "ACTIVE")
            .when(cb.isFalse(root.get("active")), "INACTIVE")
            .otherwise("PENDING");
        
        List<Object[]> results = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Criteria API with Tuple result
    public void criteriaWithTuple() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Employee> root = cq.from(Employee.class);
        
        cq.multiselect(
            root.get("id").alias("id"),
            root.get("name").alias("name"),
            root.get("age").alias("age"),
            root.get("department").alias("department")
        );
        
        List<Tuple> results = em.createQuery(cq).getResultList();
        
        for (Tuple tuple : results) {
            Long id = tuple.get("id", Long.class);
            String name = tuple.get("name", String.class);
            Integer age = tuple.get("age", Integer.class);
            String dept = tuple.get("department", String.class);
        }
        
        em.close();
    }
    
    // Criteria API with Metamodel
    public void criteriaWithMetamodel() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Metamodel metamodel = em.getMetamodel();
        
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        
        // Using metamodel for type-safe queries
        EntityType<Employee> employeeType = metamodel.entity(Employee.class);
        
        cq.select(root);
        
        List<Employee> employees = em.createQuery(cq).getResultList();
        em.close();
    }
    
    // Method accepting CriteriaBuilder as parameter
    public List<Employee> findWithCriteriaBuilder(CriteriaBuilder cb, String department) {
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        cq.select(root).where(cb.equal(root.get("department"), department));
        return entityManager.createQuery(cq).getResultList();
    }
    
    // Method returning CriteriaQuery
    public CriteriaQuery<Employee> createCriteriaQuery() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        return cb.createQuery(Employee.class);
    }
    
    // Inner class demonstrating Criteria API usage
    class EmployeeRepository {
        private EntityManager em;
        
        public EmployeeRepository(EntityManager em) {
            this.em = em;
        }
        
        public List<Employee> findByDepartment(String department) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> root = cq.from(Employee.class);
            cq.select(root).where(cb.equal(root.get("department"), department));
            return em.createQuery(cq).getResultList();
        }
        
        public List<Employee> findByAgeRange(int minAge, int maxAge) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> root = cq.from(Employee.class);
            cq.select(root).where(cb.between(root.get("age"), minAge, maxAge));
            return em.createQuery(cq).getResultList();
        }
        
        public Long count() {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Employee> root = cq.from(Employee.class);
            cq.select(cb.count(root));
            return em.createQuery(cq).getSingleResult();
        }
    }
    
    // Employee entity class
    @javax.persistence.Entity
    @javax.persistence.Table(name = "employees")
    static class Employee {
        @javax.persistence.Id
        @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        private String name;
        private Integer age;
        private String department;
        private String status;
        private Boolean active;
        private Double salary;
        
        @javax.persistence.Temporal(TemporalType.DATE)
        private Date hireDate;
        
        @javax.persistence.Temporal(TemporalType.TIMESTAMP)
        private Date lastUpdated;
        
        @javax.persistence.ManyToOne
        private Employee manager;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public Double getSalary() { return salary; }
        public void setSalary(Double salary) { this.salary = salary; }
    }
    
    // Department entity class
    @javax.persistence.Entity
    static class Department {
        @javax.persistence.Id
        @javax.persistence.GeneratedValue
        private Long id;
        
        private String name;
        private String location;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
    
    // Project entity class
    @javax.persistence.Entity
    static class Project {
        @javax.persistence.Id
        @javax.persistence.GeneratedValue
        private Long id;
        
        private String name;
        private String status;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}

