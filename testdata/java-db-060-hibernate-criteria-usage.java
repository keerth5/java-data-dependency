package com.example.database;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Subqueries;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import java.util.List;

/**
 * Test file for sql-java-060: HibernateCriteriaUsage
 * Detects Hibernate Criteria API usage
 * Criteria API affects dynamic query construction
 */
public class HibernateCriteriaExample {
    
    // Field declaration with Criteria
    private Criteria criteria;
    
    private SessionFactory sessionFactory;
    private Session session;
    
    // Constructor initializing session
    public HibernateCriteriaExample() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.session = sessionFactory.openSession();
    }
    
    // Setter for Criteria
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }
    
    // Getter returning Criteria
    public Criteria getCriteria() {
        return criteria;
    }
    
    // Basic Criteria usage
    public void basicCriteriaUsage() {
        Session session = sessionFactory.openSession();
        
        // Create Criteria for entity
        Criteria criteria = session.createCriteria(Employee.class);
        List<Employee> employees = criteria.list();
        
        // Create Criteria with alias
        Criteria criteriaDWithAlias = session.createCriteria(Employee.class, "emp");
        List results = criteriaDWithAlias.list();
        
        // Unique result
        Criteria uniqueCriteria = session.createCriteria(Employee.class);
        uniqueCriteria.add(Restrictions.eq("id", 1));
        Employee employee = (Employee) uniqueCriteria.uniqueResult();
        
        session.close();
    }
    
    // Criteria with restrictions
    public void criteriaWithRestrictions() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // Equality restriction
        criteria.add(Restrictions.eq("department", "IT"));
        criteria.add(Restrictions.ne("status", "INACTIVE"));
        
        // Comparison restrictions
        criteria.add(Restrictions.gt("age", 25));
        criteria.add(Restrictions.ge("age", 25));
        criteria.add(Restrictions.lt("age", 60));
        criteria.add(Restrictions.le("age", 60));
        criteria.add(Restrictions.between("age", 25, 60));
        
        // Like restrictions
        criteria.add(Restrictions.like("name", "John%"));
        criteria.add(Restrictions.ilike("name", "john%"));
        
        // Null checks
        criteria.add(Restrictions.isNull("manager"));
        criteria.add(Restrictions.isNotNull("email"));
        
        // In restriction
        criteria.add(Restrictions.in("department", new String[]{"IT", "Sales", "Marketing"}));
        
        // Size restrictions
        criteria.add(Restrictions.sizeEq("projects", 3));
        criteria.add(Restrictions.sizeNe("projects", 0));
        criteria.add(Restrictions.sizeGt("projects", 1));
        criteria.add(Restrictions.sizeLt("projects", 10));
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with logical operators
    public void criteriaWithLogicalOperators() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // AND condition
        criteria.add(Restrictions.and(
            Restrictions.eq("department", "IT"),
            Restrictions.gt("age", 25)
        ));
        
        // OR condition
        criteria.add(Restrictions.or(
            Restrictions.eq("department", "Sales"),
            Restrictions.eq("department", "Marketing")
        ));
        
        // NOT condition
        criteria.add(Restrictions.not(Restrictions.eq("status", "INACTIVE")));
        
        // Conjunction (AND junction)
        Junction conjunction = Restrictions.conjunction();
        conjunction.add(Restrictions.ge("age", 25));
        conjunction.add(Restrictions.le("age", 50));
        conjunction.add(Restrictions.eq("active", true));
        criteria.add(conjunction);
        
        // Disjunction (OR junction)
        Junction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("department", "IT"));
        disjunction.add(Restrictions.eq("department", "Engineering"));
        disjunction.add(Restrictions.eq("department", "Product"));
        criteria.add(disjunction);
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with ordering
    public void criteriaWithOrdering() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // Ascending order
        criteria.addOrder(Order.asc("name"));
        
        // Descending order
        criteria.addOrder(Order.desc("age"));
        
        // Multiple ordering
        criteria.addOrder(Order.asc("department"))
                .addOrder(Order.desc("salary"))
                .addOrder(Order.asc("name"));
        
        // Case-insensitive ordering
        criteria.addOrder(Order.asc("name").ignoreCase());
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with pagination
    public void criteriaWithPagination() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // Set first result (offset)
        criteria.setFirstResult(0);
        
        // Set max results (limit)
        criteria.setMaxResults(10);
        
        // Combined pagination
        int pageNumber = 2;
        int pageSize = 20;
        criteria.setFirstResult(pageNumber * pageSize);
        criteria.setMaxResults(pageSize);
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with projections
    public void criteriaWithProjections() {
        Session session = sessionFactory.openSession();
        
        // Single property projection
        Criteria criteria1 = session.createCriteria(Employee.class);
        criteria1.setProjection(Projections.property("name"));
        List<String> names = criteria1.list();
        
        // Aggregate projections
        Criteria criteria2 = session.createCriteria(Employee.class);
        criteria2.setProjection(Projections.rowCount());
        Long count = (Long) criteria2.uniqueResult();
        
        Criteria criteria3 = session.createCriteria(Employee.class);
        criteria3.setProjection(Projections.sum("salary"));
        Double totalSalary = (Double) criteria3.uniqueResult();
        
        Criteria criteria4 = session.createCriteria(Employee.class);
        criteria4.setProjection(Projections.avg("age"));
        Double avgAge = (Double) criteria4.uniqueResult();
        
        Criteria criteria5 = session.createCriteria(Employee.class);
        criteria5.setProjection(Projections.min("salary"));
        Double minSalary = (Double) criteria5.uniqueResult();
        
        Criteria criteria6 = session.createCriteria(Employee.class);
        criteria6.setProjection(Projections.max("salary"));
        Double maxSalary = (Double) criteria6.uniqueResult();
        
        // Multiple projections (projection list)
        Criteria criteria7 = session.createCriteria(Employee.class);
        criteria7.setProjection(Projections.projectionList()
            .add(Projections.property("department"))
            .add(Projections.count("id"))
            .add(Projections.avg("salary"))
        );
        criteria7.addOrder(Order.desc("department"));
        List results = criteria7.list();
        
        // Distinct projection
        Criteria criteria8 = session.createCriteria(Employee.class);
        criteria8.setProjection(Projections.distinct(Projections.property("department")));
        List<String> departments = criteria8.list();
        
        // Group by projection
        Criteria criteria9 = session.createCriteria(Employee.class);
        criteria9.setProjection(Projections.projectionList()
            .add(Projections.groupProperty("department"))
            .add(Projections.count("id").as("employeeCount"))
        );
        List groupResults = criteria9.list();
        
        session.close();
    }
    
    // Criteria with associations (joins)
    public void criteriaWithAssociations() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // Create alias (inner join)
        criteria.createAlias("department", "dept");
        criteria.add(Restrictions.eq("dept.name", "IT"));
        
        // Create criteria for association
        Criteria deptCriteria = criteria.createCriteria("department");
        deptCriteria.add(Restrictions.eq("location", "New York"));
        
        // Left outer join
        criteria.createAlias("manager", "mgr", JoinType.LEFT_OUTER_JOIN);
        
        // Fetch mode
        criteria.setFetchMode("projects", org.hibernate.FetchMode.JOIN);
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with DetachedCriteria (subqueries)
    public void criteriaWithDetachedCriteria() {
        Session session = sessionFactory.openSession();
        
        // Create DetachedCriteria for subquery
        DetachedCriteria avgSalaryCriteria = DetachedCriteria.forClass(Employee.class)
            .setProjection(Projections.avg("salary"));
        
        // Main criteria with subquery
        Criteria criteria = session.createCriteria(Employee.class);
        criteria.add(Subqueries.gt("salary", avgSalaryCriteria));
        
        // Subquery with propertyEq
        DetachedCriteria managerCriteria = DetachedCriteria.forClass(Employee.class, "mgr")
            .add(Restrictions.eq("department", "IT"))
            .setProjection(Projections.property("mgr.id"));
        
        criteria.add(Subqueries.propertyIn("managerId", managerCriteria));
        
        // Exists subquery
        DetachedCriteria existsCriteria = DetachedCriteria.forClass(Project.class)
            .add(Restrictions.eq("status", "ACTIVE"))
            .add(Restrictions.eqProperty("employeeId", "id"));
        
        criteria.add(Subqueries.exists(existsCriteria));
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with Example (Query By Example)
    public void criteriaWithExample() {
        Session session = sessionFactory.openSession();
        
        // Create example object
        Employee exampleEmployee = new Employee();
        exampleEmployee.setDepartment("IT");
        exampleEmployee.setActive(true);
        
        // Create Example
        Example example = Example.create(exampleEmployee);
        example.excludeZeroes();
        example.ignoreCase();
        example.enableLike(MatchMode.START);
        example.excludeProperty("id");
        
        // Use Example in Criteria
        Criteria criteria = session.createCriteria(Employee.class);
        criteria.add(example);
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Criteria with result transformers
    public void criteriaWithResultTransformers() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // Alias to bean transformer
        criteria.setProjection(Projections.projectionList()
            .add(Projections.property("id").as("id"))
            .add(Projections.property("name").as("name"))
            .add(Projections.property("age").as("age"))
        );
        criteria.setResultTransformer(Transformers.aliasToBean(EmployeeDTO.class));
        
        List<EmployeeDTO> dtos = criteria.list();
        
        // Distinct root entity transformer
        Criteria criteria2 = session.createCriteria(Employee.class);
        criteria2.setFetchMode("projects", org.hibernate.FetchMode.JOIN);
        criteria2.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Employee> employees = criteria2.list();
        
        // Alias to map transformer
        Criteria criteria3 = session.createCriteria(Employee.class);
        criteria3.setProjection(Projections.projectionList()
            .add(Projections.property("name").as("employeeName"))
            .add(Projections.property("department").as("dept"))
        );
        criteria3.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<java.util.Map> maps = criteria3.list();
        
        session.close();
    }
    
    // Criteria with advanced features
    public void advancedCriteriaFeatures() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Employee.class);
        
        // Set comment
        criteria.setComment("Find all active IT employees");
        
        // Set cache mode
        criteria.setCacheMode(org.hibernate.CacheMode.NORMAL);
        
        // Enable caching
        criteria.setCacheable(true);
        criteria.setCacheRegion("employeeCache");
        
        // Set fetch size
        criteria.setFetchSize(50);
        
        // Set timeout
        criteria.setTimeout(30);
        
        // Set lock mode
        criteria.setLockMode(org.hibernate.LockMode.PESSIMISTIC_WRITE);
        criteria.setLockMode("department", org.hibernate.LockMode.PESSIMISTIC_READ);
        
        // Set read only
        criteria.setReadOnly(true);
        
        // Set flush mode
        criteria.setFlushMode(org.hibernate.FlushMode.MANUAL);
        
        List<Employee> employees = criteria.list();
        session.close();
    }
    
    // Method accepting Criteria as parameter
    public void processWithCriteria(Criteria crit) {
        crit.add(Restrictions.eq("active", true));
        List results = crit.list();
    }
    
    // Method returning Criteria
    public Criteria createCriteria(Class<?> entityClass) {
        return session.createCriteria(entityClass);
    }
    
    // Method returning Criteria with restrictions
    public Criteria createActiveCriteria() {
        Criteria crit = session.createCriteria(Employee.class);
        crit.add(Restrictions.eq("active", true));
        return crit;
    }
    
    // Static method with Criteria
    public static List<Employee> findEmployees(Session session, Criteria criteria) {
        return criteria.list();
    }
    
    // Inner class demonstrating Criteria usage
    class EmployeeRepository {
        private Session session;
        
        public EmployeeRepository(Session session) {
            this.session = session;
        }
        
        public List<Employee> findByDepartment(String department) {
            Criteria criteria = session.createCriteria(Employee.class);
            criteria.add(Restrictions.eq("department", department));
            return criteria.list();
        }
        
        public List<Employee> findByAgeRange(int minAge, int maxAge) {
            Criteria criteria = session.createCriteria(Employee.class);
            criteria.add(Restrictions.between("age", minAge, maxAge));
            return criteria.list();
        }
        
        public Employee findById(int id) {
            Criteria criteria = session.createCriteria(Employee.class);
            criteria.add(Restrictions.eq("id", id));
            return (Employee) criteria.uniqueResult();
        }
        
        public List<Employee> findWithPagination(int page, int size) {
            Criteria criteria = session.createCriteria(Employee.class);
            criteria.setFirstResult(page * size);
            criteria.setMaxResults(size);
            criteria.addOrder(Order.asc("name"));
            return criteria.list();
        }
    }
    
    // Criteria builder pattern
    static class CriteriaBuilder {
        private Criteria criteria;
        
        public CriteriaBuilder(Session session, Class<?> entityClass) {
            this.criteria = session.createCriteria(entityClass);
        }
        
        public CriteriaBuilder addRestriction(Criterion criterion) {
            criteria.add(criterion);
            return this;
        }
        
        public CriteriaBuilder addOrder(Order order) {
            criteria.addOrder(order);
            return this;
        }
        
        public CriteriaBuilder setPagination(int offset, int limit) {
            criteria.setFirstResult(offset);
            criteria.setMaxResults(limit);
            return this;
        }
        
        public Criteria build() {
            return criteria;
        }
        
        public List list() {
            return criteria.list();
        }
    }
    
    // Employee entity class
    static class Employee {
        private int id;
        private String name;
        private int age;
        private String department;
        private boolean active;
        private Double salary;
        
        public Employee() {}
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public Double getSalary() { return salary; }
        public void setSalary(Double salary) { this.salary = salary; }
    }
    
    // DTO class for transformers
    static class EmployeeDTO {
        private int id;
        private String name;
        private int age;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
    
    // Project entity class
    static class Project {
        private int id;
        private String name;
        private String status;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

