package com.example.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.io.Serializable;

/**
 * Test file for sql-java-057: HibernateSessionUsage
 * Detects Hibernate Session interface usage
 * Hibernate Session affects ORM architecture decisions
 */
public class HibernateSessionExample {
    
    // Field declaration with Session
    private Session session;
    
    private SessionFactory sessionFactory;
    
    // Constructor initializing Session
    public HibernateSessionExample() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.session = sessionFactory.openSession();
    }
    
    // Method to open Session
    public Session openSession() {
        return sessionFactory.openSession();
    }
    
    // Method to get current Session
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    // Setter for Session
    public void setSession(Session session) {
        this.session = session;
    }
    
    // Getter returning Session
    public Session getSession() {
        return session;
    }
    
    // Basic CRUD operations with Session
    public void basicCrudOperations() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Save operation
            Employee employee = new Employee();
            employee.setName("John Doe");
            employee.setAge(30);
            employee.setDepartment("IT");
            Serializable id = session.save(employee);
            
            // Persist operation
            Employee employee2 = new Employee();
            employee2.setName("Jane Smith");
            employee2.setAge(28);
            session.persist(employee2);
            
            // Load operation
            Employee loadedEmp = (Employee) session.load(Employee.class, id);
            
            // Get operation
            Employee retrievedEmp = (Employee) session.get(Employee.class, id);
            
            // Update operation
            retrievedEmp.setAge(31);
            session.update(retrievedEmp);
            
            // SaveOrUpdate operation
            Employee employee3 = new Employee();
            employee3.setId(100);
            employee3.setName("Bob Johnson");
            session.saveOrUpdate(employee3);
            
            // Merge operation
            Employee detachedEmp = new Employee();
            detachedEmp.setId(1);
            detachedEmp.setName("Updated Name");
            Employee mergedEmp = (Employee) session.merge(detachedEmp);
            
            // Delete operation
            session.delete(employee3);
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // Query operations with Session
    public void queryOperations() {
        Session session = sessionFactory.openSession();
        
        try {
            // HQL query
            String hql = "FROM Employee WHERE age > :age";
            Query query = session.createQuery(hql);
            query.setParameter("age", 25);
            List<Employee> employees = query.list();
            
            // Named query
            Query namedQuery = session.getNamedQuery("Employee.findByDepartment");
            namedQuery.setParameter("dept", "IT");
            List<Employee> itEmployees = namedQuery.list();
            
            // SQL query
            String sql = "SELECT * FROM employees WHERE department = :dept";
            Query sqlQuery = session.createSQLQuery(sql).addEntity(Employee.class);
            sqlQuery.setParameter("dept", "Sales");
            List<Employee> salesEmployees = sqlQuery.list();
            
            // Unique result
            String uniqueHql = "FROM Employee WHERE id = :id";
            Query uniqueQuery = session.createQuery(uniqueHql);
            uniqueQuery.setParameter("id", 1);
            Employee uniqueEmp = (Employee) uniqueQuery.uniqueResult();
            
            // Pagination
            Query paginatedQuery = session.createQuery("FROM Employee");
            paginatedQuery.setFirstResult(0);
            paginatedQuery.setMaxResults(10);
            List<Employee> pagedEmployees = paginatedQuery.list();
            
        } finally {
            session.close();
        }
    }
    
    // Criteria API operations with Session
    public void criteriaOperations() {
        Session session = sessionFactory.openSession();
        
        try {
            // Basic criteria
            Criteria criteria = session.createCriteria(Employee.class);
            List<Employee> allEmployees = criteria.list();
            
            // Criteria with restrictions
            Criteria restrictedCriteria = session.createCriteria(Employee.class);
            restrictedCriteria.add(Restrictions.gt("age", 25));
            restrictedCriteria.add(Restrictions.eq("department", "IT"));
            List<Employee> filteredEmployees = restrictedCriteria.list();
            
            // Criteria with unique result
            Criteria uniqueCriteria = session.createCriteria(Employee.class);
            uniqueCriteria.add(Restrictions.eq("id", 1));
            Employee employee = (Employee) uniqueCriteria.uniqueResult();
            
        } finally {
            session.close();
        }
    }
    
    // Transaction management with Session
    public void transactionManagement() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            // Begin transaction
            tx = session.beginTransaction();
            
            // Get current transaction
            Transaction currentTx = session.getTransaction();
            
            // Perform operations
            Employee emp = new Employee();
            emp.setName("Transaction Test");
            session.save(emp);
            
            // Flush session
            session.flush();
            
            // Clear session
            session.clear();
            
            // Commit transaction
            tx.commit();
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
    
    // Session state management
    public void sessionStateManagement() {
        Session session = sessionFactory.openSession();
        
        try {
            Employee emp = new Employee();
            emp.setName("State Test");
            
            // Check if object contains the instance
            boolean contains = session.contains(emp);
            
            // Evict object from session
            session.save(emp);
            session.evict(emp);
            
            // Refresh object
            session.refresh(emp);
            
            // Lock object
            session.lock(emp, org.hibernate.LockMode.NONE);
            
            // Check if session is open
            boolean isOpen = session.isOpen();
            
            // Check if session is connected
            boolean isConnected = session.isConnected();
            
            // Reconnect session
            if (!isConnected) {
                session.reconnect();
            }
            
            // Disconnect session
            session.disconnect();
            
        } finally {
            session.close();
        }
    }
    
    // Batch processing with Session
    public void batchProcessing() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            for (int i = 0; i < 100; i++) {
                Employee emp = new Employee();
                emp.setName("Batch Employee " + i);
                emp.setAge(20 + (i % 40));
                session.save(emp);
                
                if (i % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // Session filter operations
    public void sessionFilters() {
        Session session = sessionFactory.openSession();
        
        try {
            // Enable filter
            session.enableFilter("activeEmployees")
                   .setParameter("active", true);
            
            List<Employee> activeEmployees = session.createQuery("FROM Employee").list();
            
            // Disable filter
            session.disableFilter("activeEmployees");
            
            // Get enabled filter
            org.hibernate.Filter filter = session.getEnabledFilter("activeEmployees");
            
        } finally {
            session.close();
        }
    }
    
    // Method accepting Session as parameter
    public void processWithSession(Session hibernateSession) {
        hibernateSession.beginTransaction();
        Employee emp = (Employee) hibernateSession.get(Employee.class, 1);
        hibernateSession.getTransaction().commit();
    }
    
    // Method returning Session
    public Session createSession() {
        return sessionFactory.openSession();
    }
    
    // Static method with Session
    public static void staticSessionOperation(Session session) {
        session.createQuery("FROM Employee").list();
    }
    
    // Inner class demonstrating Session usage
    class EmployeeDao {
        private Session session;
        
        public EmployeeDao(Session session) {
            this.session = session;
        }
        
        public void save(Employee employee) {
            session.save(employee);
        }
        
        public Employee findById(int id) {
            return (Employee) session.get(Employee.class, id);
        }
        
        public List<Employee> findAll() {
            return session.createQuery("FROM Employee").list();
        }
        
        public void update(Employee employee) {
            session.update(employee);
        }
        
        public void delete(Employee employee) {
            session.delete(employee);
        }
    }
    
    // Nested class with Session field
    static class SessionManager {
        private Session managedSession;
        
        public Session getManagedSession() {
            return managedSession;
        }
        
        public void setManagedSession(Session session) {
            this.managedSession = session;
        }
        
        public void executeInSession(SessionCallback callback) {
            callback.doInSession(managedSession);
        }
    }
    
    // Callback interface for Session
    interface SessionCallback {
        void doInSession(Session session);
    }
    
    // Employee entity class
    static class Employee {
        private int id;
        private String name;
        private int age;
        private String department;
        
        public Employee() {}
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
}

