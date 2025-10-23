package com.example.database;

import javax.persistence.*;
import java.util.Date;

/**
 * Test file for sql-java-072: JpaColumnAnnotationUsage
 * Detects @Column annotation usage
 * Column mapping affects field-database binding
 */

@Entity
@Table(name = "employees")
class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Basic @Column annotation
    @Column(name = "first_name")
    private String firstName;
    
    // @Column with length
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    // @Column with nullable
    @Column(name = "email", nullable = false)
    private String email;
    
    // @Column with unique
    @Column(name = "employee_id", unique = true)
    private String employeeId;
    
    // @Column with insertable
    @Column(name = "created_at", insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    // @Column with updatable
    @Column(name = "updated_at", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    // @Column with precision and scale
    @Column(name = "salary", precision = 10, scale = 2)
    private Double salary;
    
    // @Column with columnDefinition
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    
    // @Column with table (for secondary table)
    @Column(name = "profile_picture", table = "employee_details")
    private byte[] profilePicture;
    
    // @Column with all attributes
    @Column(name = "phone_number", 
            length = 20, 
            nullable = false, 
            unique = false,
            insertable = true,
            updatable = true,
            columnDefinition = "VARCHAR(20)",
            table = "employee_contacts")
    private String phoneNumber;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

@Entity
@Table(name = "products")
class Product {
    
    @Id
    @GeneratedValue
    private Long id;
    
    // @Column with different data types
    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;
    
    @Column(name = "product_code", length = 20, unique = true)
    private String productCode;
    
    @Column(name = "price", precision = 8, scale = 2)
    private Double price;
    
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "weight", precision = 6, scale = 3)
    private Float weight;
    
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;
    
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}

@Entity
@Table(name = "orders")
class Order {
    
    @Id
    @GeneratedValue
    private Long id;
    
    // @Column with temporal types
    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    
    @Column(name = "order_time")
    @Temporal(TemporalType.TIME)
    private Date orderTime;
    
    @Column(name = "created_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;
    
    // @Column with enum
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    @Column(name = "priority_level")
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
    
    // @Column with LOB
    @Column(name = "order_notes")
    @Lob
    private String orderNotes;
    
    @Column(name = "order_document")
    @Lob
    private byte[] orderDocument;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}

enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}

enum Priority {
    LOW, MEDIUM, HIGH, URGENT
}

@Entity
@Table(name = "customers")
class Customer {
    
    @Id
    @GeneratedValue
    private Long id;
    
    // @Column with constraints
    @Column(name = "customer_name", length = 100, nullable = false)
    private String customerName;
    
    @Column(name = "email_address", length = 255, unique = true, nullable = false)
    private String emailAddress;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "registration_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    
    @Column(name = "last_login", updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    
    @Column(name = "is_verified")
    private Boolean isVerified;
    
    @Column(name = "credit_limit", precision = 12, scale = 2)
    private Double creditLimit;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}

@Entity
@Table(name = "invoices")
class Invoice {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "invoice_number", length = 20, unique = true, nullable = false)
    private String invoiceNumber;
    
    @Column(name = "invoice_date")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    
    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private Double totalAmount;
    
    @Column(name = "tax_amount", precision = 15, scale = 2)
    private Double taxAmount;
    
    @Column(name = "discount_amount", precision = 15, scale = 2)
    private Double discountAmount;
    
    @Column(name = "payment_status", length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_by", length = 50, insertable = true, updatable = false)
    private String createdBy;
    
    @Column(name = "updated_by", length = 50, insertable = false, updatable = true)
    private String updatedBy;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
}

enum PaymentStatus {
    PENDING, PAID, OVERDUE, CANCELLED
}

@Entity
@Table(name = "transactions")
class Transaction {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "transaction_id", length = 30, unique = true, nullable = false)
    private String transactionId;
    
    @Column(name = "account_number", length = 20, nullable = false)
    private String accountNumber;
    
    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private Double amount;
    
    @Column(name = "transaction_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    @Column(name = "balance_after", precision = 15, scale = 2)
    private Double balanceAfter;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}

enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, REFUND
}

@Entity
@Table(name = "users")
class User {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;
    
    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;
    
    @Column(name = "email", length = 255, unique = true, nullable = false)
    private String email;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "is_verified")
    private Boolean isVerified;
    
    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

@Entity
@Table(name = "addresses")
class Address {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "street_address", length = 255, nullable = false)
    private String streetAddress;
    
    @Column(name = "city", length = 100, nullable = false)
    private String city;
    
    @Column(name = "state_province", length = 50)
    private String stateProvince;
    
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    
    @Column(name = "country", length = 50, nullable = false)
    private String country;
    
    @Column(name = "address_type", length = 20)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    
    @Column(name = "is_primary")
    private Boolean isPrimary;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private Double latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private Double longitude;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
}

enum AddressType {
    HOME, WORK, BILLING, SHIPPING
}

@Entity
@Table(name = "documents")
class Document {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "document_name", length = 255, nullable = false)
    private String documentName;
    
    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "content_type", length = 100)
    private String contentType;
    
    @Column(name = "file_content")
    @Lob
    private byte[] fileContent;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    
    @Column(name = "uploaded_by", length = 50)
    private String uploadedBy;
    
    @Column(name = "is_public")
    private Boolean isPublic;
    
    @Column(name = "version", length = 10)
    private String version;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }
}

@Entity
@Table(name = "audit_logs")
class AuditLog {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "entity_name", length = 100, nullable = false)
    private String entityName;
    
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    @Column(name = "action", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;
    
    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues;
    
    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues;
    
    @Column(name = "changed_by", length = 50)
    private String changedBy;
    
    @Column(name = "change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
}

enum AuditAction {
    CREATE, UPDATE, DELETE, READ
}

@Entity
@Table(name = "configurations")
class Configuration {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "config_key", length = 100, unique = true, nullable = false)
    private String configKey;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(name = "config_type", length = 20)
    @Enumerated(EnumType.STRING)
    private ConfigType configType;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "is_encrypted")
    private Boolean isEncrypted;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @Column(name = "created_by", length = 50)
    private String createdBy;
    
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
}

enum ConfigType {
    STRING, INTEGER, BOOLEAN, JSON, XML, BINARY
}
