package com.example.database;

import javax.persistence.*;
import javax.ejb.Stateless;
import javax.ejb.Stateful;
import javax.inject.Inject;
import java.util.List;

/**
 * Test file for sql-java-065: JpaPersistenceContextUsage
 * Detects @PersistenceContext annotation usage
 * PersistenceContext affects JPA dependency injection
 */

// Basic @PersistenceContext usage
@Stateless
public class EmployeeService {
    
    // Field injection with @PersistenceContext
    @PersistenceContext
    private EntityManager entityManager;
    
    // @PersistenceContext with unit name
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager namedEntityManager;
    
    // @PersistenceContext with type TRANSACTION (default)
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager transactionalEntityManager;
    
    // @PersistenceContext with type EXTENDED
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager extendedEntityManager;
    
    // @PersistenceContext with synchronization SYNCHRONIZED (default)
    @PersistenceContext(synchronization = SynchronizationType.SYNCHRONIZED)
    private EntityManager synchronizedEntityManager;
    
    // @PersistenceContext with synchronization UNSYNCHRONIZED
    @PersistenceContext(synchronization = SynchronizationType.UNSYNCHRONIZED)
    private EntityManager unsynchronizedEntityManager;
    
    // @PersistenceContext with properties
    @PersistenceContext(
        unitName = "customPU",
        type = PersistenceContextType.EXTENDED,
        properties = {
            @PersistenceProperty(name = "hibernate.show_sql", value = "true"),
            @PersistenceProperty(name = "hibernate.format_sql", value = "true"),
            @PersistenceProperty(name = "javax.persistence.query.timeout", value = "5000")
        }
    )
    private EntityManager customEntityManager;
    
    // Multiple @PersistenceContext declarations
    @PersistenceContext(unitName = "primaryPU")
    private EntityManager primaryEntityManager;
    
    @PersistenceContext(unitName = "secondaryPU")
    private EntityManager secondaryEntityManager;
    
    // CRUD operations using injected EntityManager
    public void createEmployee(Employee employee) {
        entityManager.persist(employee);
    }
    
    public Employee findEmployee(Long id) {
        return entityManager.find(Employee.class, id);
    }
    
    public void updateEmployee(Employee employee) {
        entityManager.merge(employee);
    }
    
    public void deleteEmployee(Long id) {
        Employee employee = entityManager.find(Employee.class, id);
        if (employee != null) {
            entityManager.remove(employee);
        }
    }
    
    public List<Employee> findAllEmployees() {
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e", 
            Employee.class
        );
        return query.getResultList();
    }
    
    public List<Employee> findEmployeesByDepartment(String department) {
        TypedQuery<Employee> query = namedEntityManager.createQuery(
            "SELECT e FROM Employee e WHERE e.department = :dept", 
            Employee.class
        );
        query.setParameter("dept", department);
        return query.getResultList();
    }
    
    // Operations with transactional entity manager
    public void performTransactionalOperation() {
        Employee emp = new Employee();
        emp.setName("Transactional Employee");
        transactionalEntityManager.persist(emp);
        transactionalEntityManager.flush();
    }
    
    // Operations with extended entity manager
    public void performExtendedOperation() {
        Employee emp = new Employee();
        emp.setName("Extended Employee");
        extendedEntityManager.persist(emp);
        // Extended persistence context spans multiple transactions
    }
}

// Stateful session bean with extended persistence context
@Stateful
class ShoppingCartService {
    
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
    
    private Order currentOrder;
    
    public void createOrder() {
        currentOrder = new Order();
        entityManager.persist(currentOrder);
    }
    
    public void addItem(Item item) {
        currentOrder.addItem(item);
        entityManager.persist(item);
    }
    
    public void checkout() {
        currentOrder.setStatus("COMPLETED");
        entityManager.flush();
    }
}

// Multiple persistence contexts in one class
@Stateless
class MultiDatabaseService {
    
    @PersistenceContext(unitName = "orderDB")
    private EntityManager orderEntityManager;
    
    @PersistenceContext(unitName = "inventoryDB")
    private EntityManager inventoryEntityManager;
    
    @PersistenceContext(unitName = "customerDB")
    private EntityManager customerEntityManager;
    
    public void processOrder(Long orderId) {
        Order order = orderEntityManager.find(Order.class, orderId);
        // Process order using multiple databases
    }
    
    public void checkInventory(Long productId) {
        Product product = inventoryEntityManager.find(Product.class, productId);
        // Check inventory
    }
    
    public void updateCustomer(Long customerId) {
        Customer customer = customerEntityManager.find(Customer.class, customerId);
        // Update customer
    }
}

// Setter injection with @PersistenceContext
@Stateless
class SetterInjectionService {
    
    private EntityManager entityManager;
    
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public void saveEmployee(Employee employee) {
        entityManager.persist(employee);
    }
}

// Constructor injection (not directly supported, but shown for completeness)
class ConstructorInjectionService {
    
    private final EntityManager entityManager;
    
    @Inject
    public ConstructorInjectionService(@PersistenceContext EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public void processEmployee(Employee employee) {
        entityManager.persist(employee);
    }
}

// Repository pattern with @PersistenceContext
@Stateless
class EmployeeRepository {
    
    @PersistenceContext(unitName = "hrDB")
    private EntityManager em;
    
    public void save(Employee employee) {
        em.persist(employee);
    }
    
    public Employee findById(Long id) {
        return em.find(Employee.class, id);
    }
    
    public List<Employee> findAll() {
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }
    
    public List<Employee> findByName(String name) {
        TypedQuery<Employee> query = em.createQuery(
            "SELECT e FROM Employee e WHERE e.name LIKE :name", 
            Employee.class
        );
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
    
    public void update(Employee employee) {
        em.merge(employee);
    }
    
    public void delete(Long id) {
        Employee employee = em.find(Employee.class, id);
        if (employee != null) {
            em.remove(employee);
        }
    }
    
    public Long count() {
        return em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult();
    }
    
    public List<Employee> findWithPagination(int pageNumber, int pageSize) {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e ORDER BY e.id", Employee.class);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
}

// DAO pattern with @PersistenceContext
@Stateless
class GenericDao<T> {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private Class<T> entityClass;
    
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void persist(T entity) {
        entityManager.persist(entity);
    }
    
    public T findById(Long id) {
        return entityManager.find(entityClass, id);
    }
    
    public List<T> findAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
            .getResultList();
    }
    
    public T merge(T entity) {
        return entityManager.merge(entity);
    }
    
    public void remove(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
}

// Service layer with @PersistenceContext
@Stateless
class DepartmentService {
    
    @PersistenceContext
    private EntityManager em;
    
    public void createDepartment(Department department) {
        em.persist(department);
    }
    
    public Department getDepartment(Long id) {
        return em.find(Department.class, id);
    }
    
    public List<Department> getAllDepartments() {
        return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
    }
    
    public void updateDepartment(Department department) {
        em.merge(department);
    }
    
    public void deleteDepartment(Long id) {
        Department dept = em.find(Department.class, id);
        if (dept != null) {
            em.remove(dept);
        }
    }
    
    public List<Employee> getEmployeesByDepartment(Long deptId) {
        TypedQuery<Employee> query = em.createQuery(
            "SELECT e FROM Employee e WHERE e.department.id = :deptId", 
            Employee.class
        );
        query.setParameter("deptId", deptId);
        return query.getResultList();
    }
}

// Transaction management with @PersistenceContext
@Stateless
class TransactionService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void executeInTransaction(Runnable operation) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            operation.run();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
    public void batchInsert(List<Employee> employees) {
        for (int i = 0; i < employees.size(); i++) {
            entityManager.persist(employees.get(i));
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}

// Named queries with @PersistenceContext
@Stateless
class NamedQueryService {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Employee> findEmployeesByNamedQuery(String department) {
        TypedQuery<Employee> query = em.createNamedQuery("Employee.findByDepartment", Employee.class);
        query.setParameter("dept", department);
        return query.getResultList();
    }
    
    public List<Employee> findAllByNamedQuery() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }
}

// Criteria API with @PersistenceContext
@Stateless
class CriteriaService {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Employee> findByCriteria(String department, Integer minAge) {
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
    }
}

// Async operations with @PersistenceContext
@Stateless
class AsyncService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void asyncSave(Employee employee) {
        entityManager.persist(employee);
        entityManager.flush();
    }
}

// Entity classes
@javax.persistence.Entity
@javax.persistence.Table(name = "employees")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @javax.persistence.NamedQuery(name = "Employee.findByDepartment", query = "SELECT e FROM Employee e WHERE e.department = :dept")
})
class Employee {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer age;
    private String department;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
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
    
    @javax.persistence.OneToMany
    private List<Item> items;
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

@javax.persistence.Entity
class Item {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue
    private Long id;
    
    private String name;
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

