package com.example.database;

import javax.persistence.*;
import javax.ejb.Stateless;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Test file for sql-java-066: JpaPersistenceUnitUsage
 * Detects @PersistenceUnit annotation usage
 * PersistenceUnit affects JPA configuration management
 */

// Basic @PersistenceUnit usage
@Stateless
public class EmployeeServiceWithFactory {
    
    // Field injection with @PersistenceUnit
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    // @PersistenceUnit with unit name
    @PersistenceUnit(unitName = "myPersistenceUnit")
    private EntityManagerFactory namedEntityManagerFactory;
    
    // @PersistenceUnit with different unit names
    @PersistenceUnit(unitName = "primaryPU")
    private EntityManagerFactory primaryFactory;
    
    @PersistenceUnit(unitName = "secondaryPU")
    private EntityManagerFactory secondaryFactory;
    
    @PersistenceUnit(unitName = "readOnlyPU")
    private EntityManagerFactory readOnlyFactory;
    
    // CRUD operations using injected EntityManagerFactory
    public void createEmployee(Employee employee) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public Employee findEmployee(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }
    
    public void updateEmployee(Employee employee) {
        EntityManager em = namedEntityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void deleteEmployee(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, id);
            if (employee != null) {
                em.remove(employee);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public java.util.List<Employee> findAllEmployees() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

// Setter injection with @PersistenceUnit
@Stateless
class SetterInjectionFactoryService {
    
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }
    
    public void saveEmployee(Employee employee) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}

// Multiple EntityManagerFactory instances
@Stateless
class MultiDatabaseFactoryService {
    
    @PersistenceUnit(unitName = "orderDB")
    private EntityManagerFactory orderFactory;
    
    @PersistenceUnit(unitName = "inventoryDB")
    private EntityManagerFactory inventoryFactory;
    
    @PersistenceUnit(unitName = "customerDB")
    private EntityManagerFactory customerFactory;
    
    @PersistenceUnit(unitName = "analyticsDB")
    private EntityManagerFactory analyticsFactory;
    
    public void processOrder(Long orderId) {
        EntityManager em = orderFactory.createEntityManager();
        try {
            Order order = em.find(Order.class, orderId);
            // Process order
        } finally {
            em.close();
        }
    }
    
    public void checkInventory(Long productId) {
        EntityManager em = inventoryFactory.createEntityManager();
        try {
            Product product = em.find(Product.class, productId);
            // Check inventory
        } finally {
            em.close();
        }
    }
    
    public void updateCustomer(Long customerId) {
        EntityManager em = customerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Customer customer = em.find(Customer.class, customerId);
            // Update customer
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public void recordAnalytics() {
        EntityManager em = analyticsFactory.createEntityManager();
        try {
            // Record analytics
        } finally {
            em.close();
        }
    }
}

// Singleton service managing EntityManagerFactory
@Singleton
class SingletonFactoryService {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void performBatchOperation(java.util.List<Employee> employees) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (int i = 0; i < employees.size(); i++) {
                em.persist(employees.get(i));
                if (i % 50 == 0) {
                    em.flush();
                    em.clear();
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}

// Repository pattern with @PersistenceUnit
@Stateless
class EmployeeRepository {
    
    @PersistenceUnit(unitName = "hrDB")
    private EntityManagerFactory emf;
    
    public void save(Employee employee) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(employee);
            tx.commit();
        } finally {
            em.close();
        }
    }
    
    public Employee findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }
    
    public java.util.List<Employee> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public void update(Employee employee) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(employee);
            tx.commit();
        } finally {
            em.close();
        }
    }
    
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, id);
            if (employee != null) {
                em.remove(employee);
            }
            tx.commit();
        } finally {
            em.close();
        }
    }
}

// Service with EntityManager creation from EntityManagerFactory
@Stateless
class DepartmentService {
    
    @PersistenceUnit
    private EntityManagerFactory factory;
    
    public void createDepartment(Department department) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(department);
            tx.commit();
        } finally {
            em.close();
        }
    }
    
    public Department getDepartment(Long id) {
        EntityManager em = factory.createEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }
    
    public java.util.List<Employee> getEmployeesByDepartment(Long deptId) {
        EntityManager em = factory.createEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.department.id = :deptId", 
                Employee.class
            );
            query.setParameter("deptId", deptId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

// EntityManagerFactory with custom properties
@Stateless
class CustomPropertiesService {
    
    @PersistenceUnit(unitName = "customPU")
    private EntityManagerFactory emf;
    
    public EntityManager createCustomEntityManager() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true");
        properties.put("javax.persistence.query.timeout", 5000);
        return emf.createEntityManager(properties);
    }
    
    public void executeWithCustomSettings() {
        EntityManager em = createCustomEntityManager();
        try {
            em.getTransaction().begin();
            // Execute operations
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}

// Generic DAO with @PersistenceUnit
@Stateless
class GenericDao<T> {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    private Class<T> entityClass;
    
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void persist(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } finally {
            em.close();
        }
    }
    
    public T findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    
    public java.util.List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
        } finally {
            em.close();
        }
    }
}

// Criteria API with @PersistenceUnit
@Stateless
class CriteriaService {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public java.util.List<Employee> findByCriteria(String department, Integer minAge) {
        EntityManager em = emf.createEntityManager();
        try {
            javax.persistence.criteria.CriteriaBuilder cb = em.getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            javax.persistence.criteria.Root<Employee> root = cq.from(Employee.class);
            
            cq.select(root).where(
                cb.and(
                    cb.equal(root.get("department"), department),
                    cb.ge(root.get("age"), minAge)
                )
            );
            
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}

// Transaction management with @PersistenceUnit
@Stateless
class TransactionService {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public void executeInTransaction(Runnable operation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operation.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}

// Cache operations with @PersistenceUnit
@Stateless
class CacheService {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public void evictCache() {
        Cache cache = emf.getCache();
        cache.evictAll();
    }
    
    public void evictEntity(Class<?> entityClass, Object id) {
        Cache cache = emf.getCache();
        cache.evict(entityClass, id);
    }
    
    public boolean isInCache(Class<?> entityClass, Object id) {
        Cache cache = emf.getCache();
        return cache.contains(entityClass, id);
    }
}

// Metamodel operations with @PersistenceUnit
@Stateless
class MetamodelService {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public void inspectMetamodel() {
        javax.persistence.metamodel.Metamodel metamodel = emf.getMetamodel();
        java.util.Set<javax.persistence.metamodel.EntityType<?>> entities = metamodel.getEntities();
        // Inspect entities
    }
}

// EntityManagerFactory lifecycle management
@Stateless
class FactoryLifecycleService {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    public boolean isOpen() {
        return emf.isOpen();
    }
    
    public Map<String, Object> getProperties() {
        return emf.getProperties();
    }
    
    public javax.persistence.criteria.CriteriaBuilder getCriteriaBuilder() {
        return emf.getCriteriaBuilder();
    }
}

// Entity classes
@javax.persistence.Entity
@javax.persistence.Table(name = "employees")
class Employee {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer age;
    
    @javax.persistence.ManyToOne
    private Department department;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}

@javax.persistence.Entity
class Department {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue
    private Long id;
    
    private String name;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@javax.persistence.Entity
class Order {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue
    private Long id;
    
    private String status;
}

@javax.persistence.Entity
class Product {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue
    private Long id;
    
    private String name;
}

@javax.persistence.Entity
class Customer {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue
    private Long id;
    
    private String name;
}

