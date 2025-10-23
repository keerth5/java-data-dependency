package com.example.database;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Test file for sql-java-071: JpaTableAnnotationUsage
 * Detects @Table annotation usage
 * Table mapping affects database schema coupling
 */

// Basic @Table annotation
@Entity
@Table(name = "employees")
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

// @Table with schema
@Entity
@Table(name = "departments", schema = "hr")
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

// @Table with catalog
@Entity
@Table(name = "products", catalog = "inventory_db")
class Product {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private Double price;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @Table with schema and catalog
@Entity
@Table(name = "customers", schema = "sales", catalog = "company_db")
class Customer {
    @Id
    @GeneratedValue
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}

// @Table with indexes
@Entity
@Table(name = "orders",
       indexes = {
           @Index(name = "idx_customer_id", columnList = "customer_id"),
           @Index(name = "idx_order_date", columnList = "order_date"),
           @Index(name = "idx_status", columnList = "status"),
           @Index(name = "idx_customer_status", columnList = "customer_id, status")
       })
class Order {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    
    private String status;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}

// @Table with unique constraints
@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"username"}),
           @UniqueConstraint(columnNames = {"email"}),
           @UniqueConstraint(name = "uk_phone", columnNames = {"phone_number"}),
           @UniqueConstraint(columnNames = {"username", "email"})
       })
class User {
    @Id
    @GeneratedValue
    private Long id;
    
    private String username;
    private String email;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

// @Table with both indexes and unique constraints
@Entity
@Table(name = "invoices",
       indexes = {
           @Index(name = "idx_invoice_number", columnList = "invoice_number"),
           @Index(name = "idx_customer_id", columnList = "customer_id"),
           @Index(name = "idx_due_date", columnList = "due_date")
       },
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"invoice_number"}),
           @UniqueConstraint(name = "uk_customer_invoice", columnNames = {"customer_id", "invoice_number"})
       })
class Invoice {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "invoice_number")
    private String invoiceNumber;
    
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    
    private Double amount;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
}

// @Table with complex index definitions
@Entity
@Table(name = "transactions",
       indexes = {
           @Index(name = "idx_transaction_date", columnList = "transaction_date"),
           @Index(name = "idx_account_id", columnList = "account_id"),
           @Index(name = "idx_transaction_type", columnList = "transaction_type"),
           @Index(name = "idx_date_account", columnList = "transaction_date, account_id"),
           @Index(name = "idx_type_amount", columnList = "transaction_type, amount")
       })
class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "account_id")
    private Long accountId;
    
    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    
    @Column(name = "transaction_type")
    private String transactionType;
    
    private Double amount;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
}

// @Table with multiple unique constraints
@Entity
@Table(name = "books",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"isbn"}),
           @UniqueConstraint(columnNames = {"title", "author"}),
           @UniqueConstraint(name = "uk_publisher_edition", columnNames = {"publisher", "edition"}),
           @UniqueConstraint(columnNames = {"barcode"})
       })
class Book {
    @Id
    @GeneratedValue
    private Long id;
    
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String edition;
    private String barcode;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}

// @Table with inheritance - single table strategy
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
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
@DiscriminatorValue("CAR")
@Table(name = "cars")
class Car extends Vehicle {
    private Integer numberOfDoors;
    private String transmission;
    
    public Integer getNumberOfDoors() { return numberOfDoors; }
    public void setNumberOfDoors(Integer numberOfDoors) { this.numberOfDoors = numberOfDoors; }
}

// @Table with inheritance - joined table strategy
@Entity
@Table(name = "payment_base")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Payment {
    @Id
    @GeneratedValue
    private Long id;
    
    private Double amount;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}

@Entity
@Table(name = "credit_card_payments")
class CreditCardPayment extends Payment {
    private String cardNumber;
    private String cardType;
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
}

@Entity
@Table(name = "bank_transfer_payments")
class BankTransferPayment extends Payment {
    private String accountNumber;
    private String bankCode;
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}

// @Table with secondary table
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
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @Table with collection table
@Entity
@Table(name = "authors")
class Author {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ElementCollection
    @CollectionTable(name = "author_phone_numbers", 
                     joinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "phone_number")
    private List<String> phoneNumbers;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// @Table with join table for many-to-many
@Entity
@Table(name = "students")
class Student {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ManyToMany
    @JoinTable(
        name = "student_course_enrollment",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@Entity
@Table(name = "courses")
class Course {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    private String description;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

// @Table with audit fields
@Entity
@Table(name = "audited_entities")
class AuditedEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    private String data;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}

// @Table with versioning
@Entity
@Table(name = "versioned_entities")
class VersionedEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}

// @Table with complex naming
@Entity
@Table(name = "TBL_EMPLOYEE_MASTER", 
       schema = "HR_SCHEMA", 
       catalog = "COMPANY_DATABASE")
class EmployeeMaster {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "EMP_FIRST_NAME")
    private String firstName;
    
    @Column(name = "EMP_LAST_NAME")
    private String lastName;
    
    @Column(name = "EMP_EMAIL_ADDRESS")
    private String email;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}

// @Table with all possible attributes
@Entity
@Table(name = "complete_entities",
       schema = "test_schema",
       catalog = "test_catalog",
       indexes = {
           @Index(name = "idx_name", columnList = "name"),
           @Index(name = "idx_code", columnList = "code"),
           @Index(name = "idx_name_code", columnList = "name, code")
       },
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"code"}),
           @UniqueConstraint(name = "uk_name_type", columnNames = {"name", "type"})
       })
class CompleteEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    private String code;
    private String type;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
