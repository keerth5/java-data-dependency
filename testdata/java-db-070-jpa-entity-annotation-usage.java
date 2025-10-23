package com.example.database;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.io.Serializable;

/**
 * Test file for sql-java-070: JpaEntityAnnotationUsage
 * Detects JPA @Entity annotation usage
 * Entity annotations affect JPA entity mapping
 */

// Basic @Entity annotation
@Entity
public class SimpleEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @Entity with @Table annotation
@Entity
@Table(name = "employees", 
       schema = "hr", 
       catalog = "company_db")
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer age;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}

// @Entity with explicit name
@Entity(name = "DeptEntity")
@Table(name = "departments")
class Department {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String location;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @Entity with indexes
@Entity
@Table(name = "customers",
       indexes = {
           @Index(name = "idx_email", columnList = "email"),
           @Index(name = "idx_name", columnList = "last_name, first_name"),
           @Index(name = "idx_city", columnList = "city")
       })
class Customer {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private String email;
    private String city;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

// @Entity with unique constraints
@Entity
@Table(name = "products",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"sku"}),
           @UniqueConstraint(columnNames = {"barcode"}),
           @UniqueConstraint(name = "uk_product_code", columnNames = {"product_code"})
       })
class Product {
    @Id
    @GeneratedValue
    private Long id;
    
    private String sku;
    private String barcode;
    
    @Column(name = "product_code")
    private String productCode;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

// @Entity with inheritance - single table
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
abstract class Person {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String email;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
@DiscriminatorValue("EMPLOYEE")
class EmployeePerson extends Person {
    private String employeeId;
    private String department;
}

@Entity
@DiscriminatorValue("CONTRACTOR")
class Contractor extends Person {
    private String contractorId;
    private Date contractEndDate;
}

// @Entity with inheritance - joined table
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Vehicle {
    @Id
    @GeneratedValue
    private Long id;
    
    private String manufacturer;
    private String model;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
@Table(name = "cars")
class Car extends Vehicle {
    private Integer numberOfDoors;
    private String transmission;
}

@Entity
@Table(name = "trucks")
class Truck extends Vehicle {
    private Double loadCapacity;
    private Boolean fourWheelDrive;
}

// @Entity with inheritance - table per class
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
    private Double amount;
    private Date paymentDate;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
class CreditCardPayment extends Payment {
    private String cardNumber;
    private String cardType;
}

@Entity
class BankTransferPayment extends Payment {
    private String accountNumber;
    private String bankCode;
}

// @Entity with composite primary key using @IdClass
@Entity
@IdClass(OrderItemId.class)
class OrderItem {
    @Id
    private Long orderId;
    
    @Id
    private Long productId;
    
    private Integer quantity;
    private Double price;
}

class OrderItemId implements Serializable {
    private Long orderId;
    private Long productId;
}

// @Entity with composite primary key using @EmbeddedId
@Entity
class BookRental {
    @EmbeddedId
    private BookRentalId id;
    
    private Date rentalDate;
    private Date returnDate;
}

@Embeddable
class BookRentalId implements Serializable {
    private Long bookId;
    private Long customerId;
}

// @Entity with relationships
@Entity
class Order {
    @Id
    @GeneratedValue
    private Long id;
    
    private String orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

// @Entity with @OneToOne relationship
@Entity
class UserAccount {
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private UserProfile profile;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
class UserProfile {
    @Id
    @GeneratedValue
    private Long id;
    
    private String bio;
    private String avatarUrl;
    
    @OneToOne
    @JoinColumn(name = "account_id")
    private UserAccount account;
}

// @Entity with @ManyToMany relationship
@Entity
class Student {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
class Course {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students;
}

// @Entity with @ElementCollection
@Entity
class Author {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ElementCollection
    @CollectionTable(name = "author_phone_numbers")
    @Column(name = "phone_number")
    private Set<String> phoneNumbers;
    
    @ElementCollection
    @CollectionTable(name = "author_addresses")
    private List<Address> addresses;
}

@Embeddable
class Address {
    private String street;
    private String city;
    private String zipCode;
}

// @Entity with temporal fields
@Entity
class Event {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    
    @Temporal(TemporalType.DATE)
    private Date eventDate;
    
    @Temporal(TemporalType.TIME)
    private Date eventTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

// @Entity with LOB fields
@Entity
class Document {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    
    @Lob
    private byte[] content;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
}

// @Entity with enumerated fields
@Entity
class Task {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
}

enum TaskStatus {
    PENDING, IN_PROGRESS, COMPLETED, CANCELLED
}

enum Priority {
    LOW, MEDIUM, HIGH, CRITICAL
}

// @Entity with transient field
@Entity
class Invoice {
    @Id
    @GeneratedValue
    private Long id;
    
    private Double amount;
    
    @Transient
    private Double calculatedTax;
    
    public Double getCalculatedTax() {
        return amount * 0.1;
    }
}

// @Entity with versioning (optimistic locking)
@Entity
class BankAccount {
    @Id
    @GeneratedValue
    private Long id;
    
    private String accountNumber;
    private Double balance;
    
    @Version
    private Integer version;
}

// @Entity with named queries
@Entity
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findByStatus", query = "SELECT p FROM Project p WHERE p.status = :status")
})
class Project {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String status;
}

// @Entity with secondary table
@Entity
@Table(name = "employees_main")
@SecondaryTable(name = "employees_details", 
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "employee_id"))
class EmployeeDetailed {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @Column(table = "employees_details")
    private String bio;
    
    @Column(table = "employees_details")
    private byte[] photo;
}

// @Entity with attribute overrides
@Entity
class Company {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "office_street")),
        @AttributeOverride(name = "city", column = @Column(name = "office_city")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "office_zip"))
    })
    private Address officeAddress;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "billing_street")),
        @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip"))
    })
    private Address billingAddress;
}

// @Entity with lifecycle callbacks
@Entity
@EntityListeners(AuditListener.class)
class AuditedEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    private String data;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = new Date();
    }
}

class AuditListener {
    @PrePersist
    public void setCreatedDate(AuditedEntity entity) {
        // Audit logic
    }
    
    @PreUpdate
    public void setLastModifiedDate(AuditedEntity entity) {
        // Audit logic
    }
}

// @Entity with cacheable annotation
@Entity
@Cacheable
class CachedProduct {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private Double price;
}

// @Entity with access type
@Entity
@Access(AccessType.FIELD)
class FieldAccessEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
}

@Entity
@Access(AccessType.PROPERTY)
class PropertyAccessEntity {
    private Long id;
    private String name;
    
    @Id
    @GeneratedValue
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @Entity with converter
@Entity
class EncryptedData {
    @Id
    @GeneratedValue
    private Long id;
    
    @Convert(converter = EncryptionConverter.class)
    private String sensitiveData;
}

@Converter
class EncryptionConverter implements AttributeConverter<String, String> {
    public String convertToDatabaseColumn(String attribute) {
        // Encryption logic
        return attribute;
    }
    
    public String convertToEntityAttribute(String dbData) {
        // Decryption logic
        return dbData;
    }
}

// @Entity with mapped superclass
@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
class ExtendedEntity extends BaseEntity {
    private String name;
    private String description;
}

