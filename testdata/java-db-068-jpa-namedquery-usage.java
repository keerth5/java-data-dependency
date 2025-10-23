package com.example.database;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Test file for sql-java-068: JpaNamedQueryUsage
 * Detects @NamedQuery annotation usage
 * Named queries affect static query management
 */

// Single @NamedQuery annotation
@Entity
@Table(name = "employees")
@NamedQuery(
    name = "Employee.findAll",
    query = "SELECT e FROM Employee e"
)
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer age;
    private String department;
    private Boolean active;
    private Double salary;
    
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
}

// Multiple @NamedQuery annotations using @NamedQueries
@Entity
@Table(name = "departments")
@NamedQueries({
    @NamedQuery(
        name = "Department.findAll",
        query = "SELECT d FROM Department d"
    ),
    @NamedQuery(
        name = "Department.findByName",
        query = "SELECT d FROM Department d WHERE d.name = :name"
    ),
    @NamedQuery(
        name = "Department.findByLocation",
        query = "SELECT d FROM Department d WHERE d.location = :location"
    ),
    @NamedQuery(
        name = "Department.countEmployees",
        query = "SELECT d, COUNT(e) FROM Department d LEFT JOIN d.employees e GROUP BY d"
    )
})
class Department {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String location;
    
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}

// @NamedQuery with hints
@Entity
@Table(name = "products")
@NamedQuery(
    name = "Product.findActive",
    query = "SELECT p FROM Product p WHERE p.active = true",
    hints = {
        @QueryHint(name = "javax.persistence.cache.retrieveMode", value = "USE"),
        @QueryHint(name = "javax.persistence.cache.storeMode", value = "USE"),
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "productCache"),
        @QueryHint(name = "org.hibernate.fetchSize", value = "50"),
        @QueryHint(name = "org.hibernate.readOnly", value = "true")
    }
)
class Product {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private Boolean active;
    private Double price;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}

// @NamedQuery with lock mode
@Entity
@Table(name = "orders")
@NamedQuery(
    name = "Order.findByStatus",
    query = "SELECT o FROM Order o WHERE o.status = :status",
    lockMode = LockModeType.PESSIMISTIC_WRITE
)
class Order {
    @Id
    @GeneratedValue
    private Long id;
    
    private String status;
    private Date orderDate;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}

// Complex @NamedQueries with various query types
@Entity
@Table(name = "customers")
@NamedQueries({
    @NamedQuery(
        name = "Customer.findAll",
        query = "SELECT c FROM Customer c ORDER BY c.name"
    ),
    @NamedQuery(
        name = "Customer.findByEmail",
        query = "SELECT c FROM Customer c WHERE c.email = :email"
    ),
    @NamedQuery(
        name = "Customer.findByStatus",
        query = "SELECT c FROM Customer c WHERE c.status = :status"
    ),
    @NamedQuery(
        name = "Customer.findActiveCustomers",
        query = "SELECT c FROM Customer c WHERE c.active = true AND c.status = 'ACTIVE'"
    ),
    @NamedQuery(
        name = "Customer.countByStatus",
        query = "SELECT COUNT(c) FROM Customer c WHERE c.status = :status"
    ),
    @NamedQuery(
        name = "Customer.findWithOrders",
        query = "SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.active = true"
    ),
    @NamedQuery(
        name = "Customer.findByRegistrationDate",
        query = "SELECT c FROM Customer c WHERE c.registrationDate BETWEEN :startDate AND :endDate"
    ),
    @NamedQuery(
        name = "Customer.updateStatus",
        query = "UPDATE Customer c SET c.status = :newStatus WHERE c.id = :customerId"
    ),
    @NamedQuery(
        name = "Customer.deleteInactive",
        query = "DELETE FROM Customer c WHERE c.active = false AND c.lastLoginDate < :cutoffDate"
    )
})
class Customer {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String email;
    private String status;
    private Boolean active;
    
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;
    
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

// @NamedQuery with JOIN operations
@Entity
@Table(name = "projects")
@NamedQueries({
    @NamedQuery(
        name = "Project.findAll",
        query = "SELECT p FROM Project p"
    ),
    @NamedQuery(
        name = "Project.findWithEmployees",
        query = "SELECT p FROM Project p LEFT JOIN FETCH p.employees"
    ),
    @NamedQuery(
        name = "Project.findByDepartment",
        query = "SELECT p FROM Project p JOIN p.department d WHERE d.name = :deptName"
    ),
    @NamedQuery(
        name = "Project.findByStatus",
        query = "SELECT p FROM Project p WHERE p.status = :status ORDER BY p.startDate DESC"
    )
})
class Project {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String status;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @ManyToOne
    private Department department;
    
    @ManyToMany
    private List<Employee> employees;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

// @NamedQuery with aggregate functions
@Entity
@Table(name = "invoices")
@NamedQueries({
    @NamedQuery(
        name = "Invoice.findAll",
        query = "SELECT i FROM Invoice i"
    ),
    @NamedQuery(
        name = "Invoice.totalAmount",
        query = "SELECT SUM(i.amount) FROM Invoice i WHERE i.status = 'PAID'"
    ),
    @NamedQuery(
        name = "Invoice.averageAmount",
        query = "SELECT AVG(i.amount) FROM Invoice i"
    ),
    @NamedQuery(
        name = "Invoice.countByStatus",
        query = "SELECT i.status, COUNT(i) FROM Invoice i GROUP BY i.status"
    ),
    @NamedQuery(
        name = "Invoice.findOverdue",
        query = "SELECT i FROM Invoice i WHERE i.dueDate < CURRENT_DATE AND i.status = 'PENDING'"
    )
})
class Invoice {
    @Id
    @GeneratedValue
    private Long id;
    
    private Double amount;
    private String status;
    
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

// @NamedQuery with subqueries
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u"
    ),
    @NamedQuery(
        name = "User.findWithAboveAverageSalary",
        query = "SELECT u FROM User u WHERE u.salary > (SELECT AVG(u2.salary) FROM User u2)"
    ),
    @NamedQuery(
        name = "User.findInDepartments",
        query = "SELECT u FROM User u WHERE u.department IN (SELECT d FROM Department d WHERE d.location = :location)"
    )
})
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    private Double salary;
    private String department;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}

// @NamedQuery with DISTINCT
@Entity
@Table(name = "items")
@NamedQueries({
    @NamedQuery(
        name = "Item.findDistinctCategories",
        query = "SELECT DISTINCT i.category FROM Item i"
    ),
    @NamedQuery(
        name = "Item.findByCategory",
        query = "SELECT i FROM Item i WHERE i.category = :category"
    )
})
class Item {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String category;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

// @NamedQuery with LIKE operator
@Entity
@Table(name = "books")
@NamedQueries({
    @NamedQuery(
        name = "Book.findByTitlePattern",
        query = "SELECT b FROM Book b WHERE b.title LIKE :pattern"
    ),
    @NamedQuery(
        name = "Book.findByAuthorName",
        query = "SELECT b FROM Book b WHERE b.author LIKE :authorName"
    )
})
class Book {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    private String author;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}

// @NamedQuery with BETWEEN operator
@Entity
@Table(name = "transactions")
@NamedQueries({
    @NamedQuery(
        name = "Transaction.findByDateRange",
        query = "SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate"
    ),
    @NamedQuery(
        name = "Transaction.findByAmountRange",
        query = "SELECT t FROM Transaction t WHERE t.amount BETWEEN :minAmount AND :maxAmount"
    )
})
class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    
    private Double amount;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}

// @NamedQuery with IN operator
@Entity
@Table(name = "employees_detailed")
@NamedQueries({
    @NamedQuery(
        name = "EmployeeDetailed.findByDepartments",
        query = "SELECT e FROM EmployeeDetailed e WHERE e.department IN :deptList"
    ),
    @NamedQuery(
        name = "EmployeeDetailed.findByIds",
        query = "SELECT e FROM EmployeeDetailed e WHERE e.id IN :ids"
    )
})
class EmployeeDetailed {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String department;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @NamedQuery with CASE expression
@Entity
@Table(name = "sales")
@NamedQuery(
    name = "Sale.findWithPriority",
    query = "SELECT s, CASE WHEN s.amount > 10000 THEN 'HIGH' WHEN s.amount > 5000 THEN 'MEDIUM' ELSE 'LOW' END " +
            "FROM Sale s WHERE s.status = :status"
)
class Sale {
    @Id
    @GeneratedValue
    private Long id;
    
    private Double amount;
    private String status;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}

