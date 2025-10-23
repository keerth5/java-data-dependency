package com.example.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Test file for sql-java-061: HibernateHqlUsage
 * Detects Hibernate HQL query usage
 * HQL usage creates Hibernate query language dependency
 */
public class HibernateHqlExample {
    
    private SessionFactory sessionFactory;
    private Session session;
    
    public HibernateHqlExample() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.session = sessionFactory.openSession();
    }
    
    // Basic HQL query operations
    public void basicHqlQueries() {
        Session session = sessionFactory.openSession();
        
        // Simple FROM query
        String hql1 = "FROM Employee";
        Query query1 = session.createQuery(hql1);
        List<Employee> employees = query1.list();
        
        // FROM with alias
        String hql2 = "FROM Employee e";
        Query query2 = session.createQuery(hql2);
        List results = query2.list();
        
        // SELECT query
        String hql3 = "SELECT e FROM Employee e";
        Query<Employee> query3 = session.createQuery(hql3, Employee.class);
        List<Employee> empList = query3.getResultList();
        
        // SELECT specific fields
        String hql4 = "SELECT e.id, e.name, e.age FROM Employee e";
        Query query4 = session.createQuery(hql4);
        List<Object[]> fieldList = query4.list();
        
        // SELECT with entity alias
        String hql5 = "SELECT emp FROM Employee emp";
        Query<Employee> query5 = session.createQuery(hql5, Employee.class);
        List<Employee> employees5 = query5.list();
        
        session.close();
    }
    
    // HQL queries with WHERE clause
    public void hqlQueriesWithWhere() {
        Session session = sessionFactory.openSession();
        
        // WHERE with equality
        String hql1 = "FROM Employee WHERE department = 'IT'";
        Query<Employee> query1 = session.createQuery(hql1, Employee.class);
        List<Employee> itEmployees = query1.list();
        
        // WHERE with comparison operators
        String hql2 = "FROM Employee e WHERE e.age > 25";
        Query query2 = session.createQuery(hql2);
        List employees2 = query2.list();
        
        String hql3 = "FROM Employee e WHERE e.age >= 25 AND e.age <= 60";
        Query query3 = session.createQuery(hql3);
        List employees3 = query3.getResultList();
        
        // WHERE with BETWEEN
        String hql4 = "FROM Employee e WHERE e.age BETWEEN 25 AND 60";
        Query query4 = session.createQuery(hql4);
        List employees4 = query4.list();
        
        // WHERE with IN clause
        String hql5 = "FROM Employee e WHERE e.department IN ('IT', 'Sales', 'Marketing')";
        Query query5 = session.createQuery(hql5);
        List employees5 = query5.list();
        
        // WHERE with LIKE
        String hql6 = "FROM Employee e WHERE e.name LIKE 'John%'";
        Query query6 = session.createQuery(hql6);
        List employees6 = query6.list();
        
        // WHERE with IS NULL
        String hql7 = "FROM Employee e WHERE e.manager IS NULL";
        Query query7 = session.createQuery(hql7);
        List employees7 = query7.list();
        
        // WHERE with IS NOT NULL
        String hql8 = "FROM Employee e WHERE e.email IS NOT NULL";
        Query query8 = session.createQuery(hql8);
        List employees8 = query8.list();
        
        // WHERE with logical operators
        String hql9 = "FROM Employee e WHERE e.department = 'IT' OR e.department = 'Sales'";
        Query query9 = session.createQuery(hql9);
        List employees9 = query9.list();
        
        String hql10 = "FROM Employee e WHERE e.department = 'IT' AND e.age > 30 AND e.active = true";
        Query query10 = session.createQuery(hql10);
        List employees10 = query10.list();
        
        session.close();
    }
    
    // HQL queries with parameters
    public void hqlQueriesWithParameters() {
        Session session = sessionFactory.openSession();
        
        // Named parameters
        String hql1 = "FROM Employee e WHERE e.department = :dept";
        Query query1 = session.createQuery(hql1);
        query1.setParameter("dept", "IT");
        List employees1 = query1.list();
        
        // Multiple named parameters
        String hql2 = "FROM Employee e WHERE e.department = :dept AND e.age > :minAge";
        Query<Employee> query2 = session.createQuery(hql2, Employee.class);
        query2.setParameter("dept", "Sales");
        query2.setParameter("minAge", 25);
        List<Employee> employees2 = query2.list();
        
        // Positional parameters (deprecated but still used)
        String hql3 = "FROM Employee e WHERE e.name = ?1 AND e.age > ?2";
        Query query3 = session.createQuery(hql3);
        query3.setParameter(1, "John");
        query3.setParameter(2, 30);
        List employees3 = query3.list();
        
        // Parameter with IN clause
        String hql4 = "FROM Employee e WHERE e.id IN (:ids)";
        Query query4 = session.createQuery(hql4);
        query4.setParameterList("ids", List.of(1, 2, 3, 4, 5));
        List employees4 = query4.list();
        
        // Parameter with LIKE
        String hql5 = "FROM Employee e WHERE e.name LIKE :pattern";
        Query query5 = session.createQuery(hql5);
        query5.setParameter("pattern", "John%");
        List employees5 = query5.list();
        
        session.close();
    }
    
    // HQL queries with ORDER BY
    public void hqlQueriesWithOrderBy() {
        Session session = sessionFactory.openSession();
        
        // ORDER BY ascending
        String hql1 = "FROM Employee e ORDER BY e.name ASC";
        Query query1 = session.createQuery(hql1);
        List employees1 = query1.list();
        
        // ORDER BY descending
        String hql2 = "FROM Employee e ORDER BY e.age DESC";
        Query query2 = session.createQuery(hql2);
        List employees2 = query2.list();
        
        // ORDER BY multiple columns
        String hql3 = "FROM Employee e ORDER BY e.department ASC, e.name ASC, e.age DESC";
        Query query3 = session.createQuery(hql3);
        List employees3 = query3.list();
        
        // ORDER BY with WHERE
        String hql4 = "FROM Employee e WHERE e.active = true ORDER BY e.salary DESC";
        Query query4 = session.createQuery(hql4);
        List employees4 = query4.list();
        
        session.close();
    }
    
    // HQL queries with JOIN
    public void hqlQueriesWithJoin() {
        Session session = sessionFactory.openSession();
        
        // INNER JOIN
        String hql1 = "SELECT e FROM Employee e JOIN e.department d WHERE d.name = 'IT'";
        Query query1 = session.createQuery(hql1);
        List employees1 = query1.list();
        
        // LEFT JOIN
        String hql2 = "SELECT e FROM Employee e LEFT JOIN e.projects p WHERE p.status = 'ACTIVE'";
        Query query2 = session.createQuery(hql2);
        List employees2 = query2.list();
        
        // INNER JOIN with alias
        String hql3 = "SELECT e, d FROM Employee e INNER JOIN e.department d";
        Query query3 = session.createQuery(hql3);
        List results3 = query3.list();
        
        // LEFT OUTER JOIN
        String hql4 = "FROM Employee e LEFT OUTER JOIN e.manager m WHERE m.name = :managerName";
        Query query4 = session.createQuery(hql4);
        query4.setParameter("managerName", "Smith");
        List results4 = query4.list();
        
        // JOIN FETCH
        String hql5 = "SELECT e FROM Employee e JOIN FETCH e.department";
        Query query5 = session.createQuery(hql5);
        List employees5 = query5.list();
        
        // Multiple joins
        String hql6 = "SELECT e FROM Employee e JOIN e.department d JOIN e.projects p WHERE d.name = :dept AND p.status = :status";
        Query query6 = session.createQuery(hql6);
        query6.setParameter("dept", "Engineering");
        query6.setParameter("status", "ACTIVE");
        List employees6 = query6.list();
        
        session.close();
    }
    
    // HQL aggregate functions
    public void hqlAggregateQueries() {
        Session session = sessionFactory.openSession();
        
        // COUNT
        String hql1 = "SELECT COUNT(e) FROM Employee e";
        Query<Long> query1 = session.createQuery(hql1, Long.class);
        Long count = query1.uniqueResult();
        
        // COUNT with WHERE
        String hql2 = "SELECT COUNT(e) FROM Employee e WHERE e.department = :dept";
        Query<Long> query2 = session.createQuery(hql2, Long.class);
        query2.setParameter("dept", "IT");
        Long itCount = query2.getSingleResult();
        
        // SUM
        String hql3 = "SELECT SUM(e.salary) FROM Employee e";
        Query<Double> query3 = session.createQuery(hql3, Double.class);
        Double totalSalary = query3.uniqueResult();
        
        // AVG
        String hql4 = "SELECT AVG(e.age) FROM Employee e";
        Query<Double> query4 = session.createQuery(hql4, Double.class);
        Double avgAge = query4.uniqueResult();
        
        // MIN and MAX
        String hql5 = "SELECT MIN(e.salary), MAX(e.salary) FROM Employee e";
        Query query5 = session.createQuery(hql5);
        Object[] minMax = (Object[]) query5.uniqueResult();
        
        // GROUP BY
        String hql6 = "SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department";
        Query query6 = session.createQuery(hql6);
        List<Object[]> deptCounts = query6.list();
        
        // GROUP BY with HAVING
        String hql7 = "SELECT e.department, AVG(e.salary) FROM Employee e GROUP BY e.department HAVING AVG(e.salary) > :minAvg";
        Query query7 = session.createQuery(hql7);
        query7.setParameter("minAvg", 50000.0);
        List<Object[]> deptAvgSalaries = query7.list();
        
        session.close();
    }
    
    // HQL UPDATE queries
    public void hqlUpdateQueries() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        
        // Simple UPDATE
        String hql1 = "UPDATE Employee SET active = false WHERE age > 65";
        Query query1 = session.createQuery(hql1);
        int updated1 = query1.executeUpdate();
        
        // UPDATE with parameters
        String hql2 = "UPDATE Employee e SET e.salary = e.salary * :multiplier WHERE e.department = :dept";
        Query query2 = session.createQuery(hql2);
        query2.setParameter("multiplier", 1.1);
        query2.setParameter("dept", "IT");
        int updated2 = query2.executeUpdate();
        
        // UPDATE multiple fields
        String hql3 = "UPDATE Employee SET department = :newDept, salary = :newSalary WHERE id = :empId";
        Query query3 = session.createQuery(hql3);
        query3.setParameter("newDept", "Engineering");
        query3.setParameter("newSalary", 75000.0);
        query3.setParameter("empId", 1);
        int updated3 = query3.executeUpdate();
        
        tx.commit();
        session.close();
    }
    
    // HQL DELETE queries
    public void hqlDeleteQueries() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        
        // Simple DELETE
        String hql1 = "DELETE FROM Employee WHERE active = false";
        Query query1 = session.createQuery(hql1);
        int deleted1 = query1.executeUpdate();
        
        // DELETE with parameters
        String hql2 = "DELETE FROM Employee e WHERE e.department = :dept";
        Query query2 = session.createQuery(hql2);
        query2.setParameter("dept", "CLOSED");
        int deleted2 = query2.executeUpdate();
        
        // DELETE with condition
        String hql3 = "DELETE FROM Employee WHERE age > :maxAge AND active = :isActive";
        Query query3 = session.createQuery(hql3);
        query3.setParameter("maxAge", 70);
        query3.setParameter("isActive", false);
        int deleted3 = query3.executeUpdate();
        
        tx.commit();
        session.close();
    }
    
    // HQL with subqueries
    public void hqlSubqueries() {
        Session session = sessionFactory.openSession();
        
        // Subquery in WHERE
        String hql1 = "FROM Employee e WHERE e.salary > (SELECT AVG(emp.salary) FROM Employee emp)";
        Query query1 = session.createQuery(hql1);
        List employees1 = query1.list();
        
        // Subquery with IN
        String hql2 = "FROM Employee e WHERE e.department IN (SELECT d.name FROM Department d WHERE d.location = :loc)";
        Query query2 = session.createQuery(hql2);
        query2.setParameter("loc", "New York");
        List employees2 = query2.list();
        
        // Subquery with EXISTS
        String hql3 = "FROM Employee e WHERE EXISTS (SELECT p FROM Project p WHERE p.employee = e AND p.status = 'ACTIVE')";
        Query query3 = session.createQuery(hql3);
        List employees3 = query3.list();
        
        // Subquery in SELECT
        String hql4 = "SELECT e.name, (SELECT COUNT(p) FROM Project p WHERE p.employee = e) FROM Employee e";
        Query query4 = session.createQuery(hql4);
        List<Object[]> employeeProjectCounts = query4.list();
        
        session.close();
    }
    
    // HQL with DISTINCT
    public void hqlDistinctQueries() {
        Session session = sessionFactory.openSession();
        
        // DISTINCT
        String hql1 = "SELECT DISTINCT e.department FROM Employee e";
        Query<String> query1 = session.createQuery(hql1, String.class);
        List<String> departments = query1.list();
        
        // DISTINCT with entity
        String hql2 = "SELECT DISTINCT e FROM Employee e JOIN e.projects p";
        Query query2 = session.createQuery(hql2);
        List employees = query2.list();
        
        session.close();
    }
    
    // HQL pagination
    public void hqlPaginationQueries() {
        Session session = sessionFactory.openSession();
        
        String hql = "FROM Employee e ORDER BY e.name";
        Query<Employee> query = session.createQuery(hql, Employee.class);
        
        // Set first result (offset)
        query.setFirstResult(0);
        
        // Set max results (limit)
        query.setMaxResults(10);
        
        List<Employee> pagedEmployees = query.list();
        
        session.close();
    }
    
    // HQL with stream
    public void hqlStreamQueries() {
        Session session = sessionFactory.openSession();
        
        String hql = "FROM Employee e WHERE e.active = true";
        Query<Employee> query = session.createQuery(hql, Employee.class);
        
        // Stream results
        Stream<Employee> employeeStream = query.stream();
        employeeStream.forEach(emp -> System.out.println(emp.getName()));
        
        session.close();
    }
    
    // HQL with query hints
    public void hqlQueryHints() {
        Session session = sessionFactory.openSession();
        
        String hql = "FROM Employee e";
        Query<Employee> query = session.createQuery(hql, Employee.class);
        
        // Set cache mode
        query.setCacheable(true);
        query.setCacheRegion("employeeCache");
        
        // Set timeout
        query.setTimeout(30);
        
        // Set fetch size
        query.setFetchSize(50);
        
        // Set read only
        query.setReadOnly(true);
        
        List<Employee> employees = query.list();
        
        session.close();
    }
    
    // Method accepting HQL string
    public List executeHqlQuery(String hqlQuery) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hqlQuery);
        List results = query.list();
        session.close();
        return results;
    }
    
    // Method returning Query object
    public Query createHqlQuery(String hql) {
        return session.createQuery(hql);
    }
    
    // Inner class demonstrating HQL usage
    class EmployeeRepository {
        private Session session;
        
        public EmployeeRepository(Session session) {
            this.session = session;
        }
        
        public List<Employee> findAll() {
            String hql = "FROM Employee";
            return session.createQuery(hql, Employee.class).list();
        }
        
        public List<Employee> findByDepartment(String department) {
            String hql = "FROM Employee e WHERE e.department = :dept";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("dept", department);
            return query.list();
        }
        
        public Employee findById(int id) {
            String hql = "FROM Employee e WHERE e.id = :id";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        }
        
        public Long countByDepartment(String department) {
            String hql = "SELECT COUNT(e) FROM Employee e WHERE e.department = :dept";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("dept", department);
            return query.uniqueResult();
        }
        
        public int updateSalary(String department, double multiplier) {
            String hql = "UPDATE Employee e SET e.salary = e.salary * :mult WHERE e.department = :dept";
            Query query = session.createQuery(hql);
            query.setParameter("mult", multiplier);
            query.setParameter("dept", department);
            return query.executeUpdate();
        }
        
        public int deleteInactive() {
            String hql = "DELETE FROM Employee WHERE active = false";
            return session.createQuery(hql).executeUpdate();
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
    
    // Department entity class
    static class Department {
        private int id;
        private String name;
        private String location;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
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

