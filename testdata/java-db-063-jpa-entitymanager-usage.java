package com.example.database;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;

/**
 * Test file for sql-java-063: JpaEntityManagerUsage
 * Detects JPA EntityManager interface usage
 * EntityManager affects JPA persistence patterns
 */
public class JpaEntityManagerExample {
    
    // Field declaration with EntityManager
    private EntityManager entityManager;
    
    private EntityManagerFactory entityManagerFactory;
    
    // Constructor with EntityManager initialization
    public JpaEntityManagerExample() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }
    
    // Setter for EntityManager
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    // Getter returning EntityManager
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    // Basic CRUD operations with EntityManager
    public void basicCrudOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            // Persist (create) operation
            Employee employee = new Employee();
            employee.setName("John Doe");
            employee.setAge(30);
            employee.setDepartment("IT");
            em.persist(employee);
            
            // Find (read) operation
            Employee foundEmployee = em.find(Employee.class, 1L);
            
            // Get reference operation
            Employee reference = em.getReference(Employee.class, 1L);
            
            // Merge (update) operation
            Employee detachedEmployee = new Employee();
            detachedEmployee.setId(1L);
            detachedEmployee.setName("Jane Doe");
            Employee mergedEmployee = em.merge(detachedEmployee);
            
            // Remove (delete) operation
            Employee toDelete = em.find(Employee.class, 2L);
            if (toDelete != null) {
                em.remove(toDelete);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    // Query operations with EntityManager
    public void queryOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        try {
            // Create JPQL query
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.department = :dept");
            query.setParameter("dept", "IT");
            List results = query.getResultList();
            
            // Create typed query
            TypedQuery<Employee> typedQuery = em.createQuery(
                "SELECT e FROM Employee e WHERE e.age > :age", 
                Employee.class
            );
            typedQuery.setParameter("age", 25);
            List<Employee> employees = typedQuery.getResultList();
            
            // Create named query
            Query namedQuery = em.createNamedQuery("Employee.findByDepartment");
            namedQuery.setParameter("dept", "Sales");
            List namedResults = namedQuery.getResultList();
            
            // Create typed named query
            TypedQuery<Employee> typedNamedQuery = em.createNamedQuery(
                "Employee.findAll", 
                Employee.class
            );
            List<Employee> allEmployees = typedNamedQuery.getResultList();
            
            // Create native query
            Query nativeQuery = em.createNativeQuery(
                "SELECT * FROM employees WHERE department = ?1"
            );
            nativeQuery.setParameter(1, "Marketing");
            List nativeResults = nativeQuery.getResultList();
            
            // Create native query with result class
            Query nativeTypedQuery = em.createNativeQuery(
                "SELECT * FROM employees WHERE age > ?1", 
                Employee.class
            );
            nativeTypedQuery.setParameter(1, 30);
            List<Employee> nativeEmployees = nativeTypedQuery.getResultList();
            
            // Create native query with result set mapping
            Query mappedQuery = em.createNativeQuery(
                "SELECT e.id, e.name FROM employees e",
                "EmployeeResultMapping"
            );
            List mappedResults = mappedQuery.getResultList();
            
            // Create named native query
            Query namedNativeQuery = em.createNamedQuery("Employee.nativeFindAll");
            List namedNativeResults = namedNativeQuery.getResultList();
            
        } finally {
            em.close();
        }
    }
    
    // Criteria API operations with EntityManager
    public void criteriaApiOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        try {
            // Get CriteriaBuilder from EntityManager
            CriteriaBuilder cb = em.getCriteriaBuilder();
            
            // Create CriteriaQuery
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            javax.persistence.criteria.Root<Employee> root = cq.from(Employee.class);
            cq.select(root).where(cb.equal(root.get("department"), "IT"));
            
            // Execute criteria query
            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> employees = query.getResultList();
            
            // Criteria query for count
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            countQuery.select(cb.count(countQuery.from(Employee.class)));
            Long count = em.createQuery(countQuery).getSingleResult();
            
        } finally {
            em.close();
        }
    }
    
    // Transaction management with EntityManager
    public void transactionManagement() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        try {
            // Get transaction from EntityManager
            EntityTransaction tx = em.getTransaction();
            
            // Begin transaction
            tx.begin();
            
            // Check if transaction is active
            boolean isActive = tx.isActive();
            
            // Perform operations
            Employee emp = new Employee();
            emp.setName("Transaction Test");
            em.persist(emp);
            
            // Flush changes to database
            em.flush();
            
            // Commit transaction
            tx.commit();
            
            // Rollback example
            EntityTransaction tx2 = em.getTransaction();
            tx2.begin();
            try {
                // Operations that might fail
                em.persist(new Employee());
                tx2.commit();
            } catch (Exception e) {
                if (tx2.isActive()) {
                    tx2.rollback();
                }
            }
            
            // Set rollback only
            EntityTransaction tx3 = em.getTransaction();
            tx3.begin();
            tx3.setRollbackOnly();
            boolean rollbackOnly = tx3.getRollbackOnly();
            tx3.rollback();
            
        } finally {
            em.close();
        }
    }
    
    // Entity state management with EntityManager
    public void entityStateManagement() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            Employee emp = new Employee();
            emp.setName("State Test");
            
            // Check if entity contains
            boolean contains = em.contains(emp);
            
            // Persist entity
            em.persist(emp);
            
            // Detach entity
            em.detach(emp);
            
            // Merge entity back
            Employee managed = em.merge(emp);
            
            // Refresh entity from database
            em.refresh(managed);
            
            // Refresh with lock mode
            em.refresh(managed, LockModeType.PESSIMISTIC_WRITE);
            
            // Refresh with properties
            Map<String, Object> properties = new java.util.HashMap<>();
            properties.put("javax.persistence.lock.timeout", 5000);
            em.refresh(managed, LockModeType.PESSIMISTIC_READ, properties);
            
            // Clear persistence context
            em.clear();
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }
    
    // Locking operations with EntityManager
    public void lockingOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            Employee emp = em.find(Employee.class, 1L);
            
            // Optimistic locking
            em.lock(emp, LockModeType.OPTIMISTIC);
            
            // Optimistic force increment
            em.lock(emp, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            
            // Pessimistic read lock
            em.lock(emp, LockModeType.PESSIMISTIC_READ);
            
            // Pessimistic write lock
            em.lock(emp, LockModeType.PESSIMISTIC_WRITE);
            
            // Pessimistic force increment
            em.lock(emp, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            
            // Lock with properties
            Map<String, Object> props = new java.util.HashMap<>();
            props.put("javax.persistence.lock.timeout", 10000);
            em.lock(emp, LockModeType.PESSIMISTIC_WRITE, props);
            
            // Find with lock mode
            Employee lockedEmp = em.find(Employee.class, 2L, LockModeType.PESSIMISTIC_READ);
            
            // Find with lock mode and properties
            Employee lockedEmp2 = em.find(Employee.class, 3L, LockModeType.PESSIMISTIC_WRITE, props);
            
            // Get lock mode
            LockModeType lockMode = em.getLockMode(emp);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }
    
    // Flush operations with EntityManager
    public void flushOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            
            // Manual flush
            Employee emp = new Employee();
            emp.setName("Flush Test");
            em.persist(emp);
            em.flush();
            
            // Get flush mode
            FlushModeType flushMode = em.getFlushMode();
            
            // Set flush mode to AUTO
            em.setFlushMode(FlushModeType.AUTO);
            
            // Set flush mode to COMMIT
            em.setFlushMode(FlushModeType.COMMIT);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }
    
    // EntityManager metadata operations
    public void metadataOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        try {
            // Get metamodel
            Metamodel metamodel = em.getMetamodel();
            
            // Get entity manager factory
            EntityManagerFactory emf = em.getEntityManagerFactory();
            
            // Get delegate
            Object delegate = em.getDelegate();
            
            // Check if open
            boolean isOpen = em.isOpen();
            
            // Check if joinedToTransaction
            boolean isJoinedToTransaction = em.isJoinedToTransaction();
            
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
    
    // EntityManager properties operations
    public void propertiesOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        try {
            // Get properties
            Map<String, Object> properties = em.getProperties();
            
            // Set property
            em.setProperty("hibernate.show_sql", "true");
            em.setProperty("javax.persistence.query.timeout", 5000);
            
            // Create query with hint
            Query query = em.createQuery("SELECT e FROM Employee e");
            query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
            query.setHint("javax.persistence.query.timeout", 3000);
            
        } finally {
            em.close();
        }
    }
    
    // Join/Detach transaction operations
    public void transactionJoinOperations() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        try {
            // Join transaction
            em.joinTransaction();
            
            // Check if joined to transaction
            boolean joined = em.isJoinedToTransaction();
            
        } finally {
            em.close();
        }
    }
    
    // Method accepting EntityManager as parameter
    public void processWithEntityManager(EntityManager em) {
        em.getTransaction().begin();
        Employee emp = em.find(Employee.class, 1L);
        em.getTransaction().commit();
    }
    
    // Method returning EntityManager
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    // Static method with EntityManager
    public static List<Employee> findAllEmployees(EntityManager entityManager) {
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e", 
            Employee.class
        );
        return query.getResultList();
    }
    
    // Inner class demonstrating EntityManager usage
    class EmployeeRepository {
        private EntityManager em;
        
        public EmployeeRepository(EntityManager em) {
            this.em = em;
        }
        
        public void save(Employee employee) {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
        }
        
        public Employee findById(Long id) {
            return em.find(Employee.class, id);
        }
        
        public List<Employee> findAll() {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e", 
                Employee.class
            );
            return query.getResultList();
        }
        
        public void update(Employee employee) {
            em.getTransaction().begin();
            em.merge(employee);
            em.getTransaction().commit();
        }
        
        public void delete(Long id) {
            em.getTransaction().begin();
            Employee emp = em.find(Employee.class, id);
            if (emp != null) {
                em.remove(emp);
            }
            em.getTransaction().commit();
        }
        
        public List<Employee> findByDepartment(String department) {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.department = :dept", 
                Employee.class
            );
            query.setParameter("dept", department);
            return query.getResultList();
        }
        
        public Long count() {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(Employee.class)));
            return em.createQuery(cq).getSingleResult();
        }
    }
    
    // EntityManager wrapper class
    static class EntityManagerWrapper {
        private final EntityManager delegate;
        
        public EntityManagerWrapper(EntityManager delegate) {
            this.delegate = delegate;
        }
        
        public EntityManager getDelegate() {
            return delegate;
        }
        
        public void persist(Object entity) {
            delegate.persist(entity);
        }
        
        public <T> T find(Class<T> entityClass, Object primaryKey) {
            return delegate.find(entityClass, primaryKey);
        }
        
        public <T> T merge(T entity) {
            return delegate.merge(entity);
        }
        
        public void remove(Object entity) {
            delegate.remove(entity);
        }
        
        public void close() {
            if (delegate.isOpen()) {
                delegate.close();
            }
        }
    }
    
    // Employee entity class
    @javax.persistence.Entity
    @javax.persistence.Table(name = "employees")
    static class Employee {
        @javax.persistence.Id
        @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @javax.persistence.Column(name = "name")
        private String name;
        
        @javax.persistence.Column(name = "age")
        private Integer age;
        
        @javax.persistence.Column(name = "department")
        private String department;
        
        public Employee() {}
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
}

