package com.example.database;

import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Index;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * Test file for sql-java-062: HibernateAnnotationUsage
 * Detects Hibernate annotations (@Entity, @Table, etc.)
 * Hibernate annotations affect entity mapping strategy
 */

// Basic entity annotations
@Entity
@Table(name = "employees", 
       schema = "hr",
       catalog = "company_db",
       indexes = {
           @Index(name = "idx_dept", columnList = "department"),
           @Index(name = "idx_name", columnList = "last_name, first_name")
       },
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"email"}),
           @UniqueConstraint(columnNames = {"employee_code"})
       })
@org.hibernate.annotations.Table(appliesTo = "employees",
    indexes = {
        @org.hibernate.annotations.Index(name = "idx_salary", columnNames = {"salary"})
    })
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Immutable
@Polymorphism(type = PolymorphismType.EXPLICIT)
@BatchSize(size = 10)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "employeeCache")
@Proxy(lazy = true)
@Where(clause = "active = true")
@Filter(name = "activeEmployeeFilter", condition = "active = :active")
@FilterDef(name = "activeEmployeeFilter", parameters = @ParamDef(name = "active", type = "boolean"))
@SQLDelete(sql = "UPDATE employees SET deleted = true WHERE id = ?")
@SQLInsert(sql = "INSERT INTO employees (name, age, department, id) VALUES (?, ?, ?, ?)")
@Loader(namedQuery = "findEmployeeById")
@NamedQuery(name = "findEmployeeById", query = "FROM Employee WHERE id = :id")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "FROM Employee"),
    @NamedQuery(name = "Employee.findByDept", query = "FROM Employee WHERE department = :dept")
})
@NamedNativeQuery(name = "Employee.nativeFindAll", query = "SELECT * FROM employees", resultClass = Employee.class)
@NamedNativeQueries({
    @NamedNativeQuery(name = "Employee.nativeCount", query = "SELECT COUNT(*) FROM employees"),
    @NamedNativeQuery(name = "Employee.nativeByDept", query = "SELECT * FROM employees WHERE department = ?")
})
public class Employee {
    
    // Primary key annotations
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @GenericGenerator(name = "employee_id", strategy = "native")
    private Long id;
    
    // Basic column annotations
    @Column(name = "first_name", 
            length = 50, 
            nullable = false, 
            unique = false,
            insertable = true,
            updatable = true,
            columnDefinition = "VARCHAR(50)")
    @NotNull
    @Formula("CONCAT(first_name, ' ', last_name)")
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    @Index(name = "idx_lastname")
    private String lastName;
    
    @Column(name = "email", unique = true)
    @NaturalId
    private String email;
    
    @Column(name = "employee_code")
    @NaturalId(mutable = false)
    private String employeeCode;
    
    // Numeric fields
    @Column(name = "age")
    @Check(constraints = "age >= 18 AND age <= 100")
    private Integer age;
    
    @Column(name = "salary", precision = 10, scale = 2)
    @ColumnTransformer(
        read = "salary / 100",
        write = "? * 100"
    )
    private Double salary;
    
    // Boolean field
    @Column(name = "active")
    @Type(type = "yes_no")
    private Boolean active;
    
    // Date/Time fields
    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date hireDate;
    
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdated;
    
    @Temporal(TemporalType.TIME)
    private Date startTime;
    
    // Enumerated field
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;
    
    @Enumerated(EnumType.ORDINAL)
    private EmployeeType type;
    
    // LOB fields
    @Lob
    @Column(name = "profile_picture")
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePicture;
    
    @Lob
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    
    // Transient field
    @Transient
    private String tempData;
    
    // Version field for optimistic locking
    @Version
    @Column(name = "version")
    private Integer version;
    
    // Embedded object
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "city", column = @Column(name = "address_city")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "address_zip"))
    })
    private Address address;
    
    // One-to-One relationship
    @OneToOne(mappedBy = "employee", 
              cascade = CascadeType.ALL, 
              fetch = FetchType.LAZY,
              orphanRemoval = true)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @LazyToOne(LazyToOneOption.PROXY)
    @NotFound(action = NotFoundAction.IGNORE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Fetch(FetchMode.JOIN)
    private EmployeeProfile profile;
    
    // Many-to-One relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @Fetch(FetchMode.SELECT)
    @LazyToOne(LazyToOneOption.PROXY)
    @NotFound(action = NotFoundAction.EXCEPTION)
    private Department department;
    
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private Employee manager;
    
    // One-to-Many relationship
    @OneToMany(mappedBy = "employee", 
               cascade = CascadeType.ALL, 
               fetch = FetchType.LAZY,
               orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 10)
    @Where(clause = "status = 'ACTIVE'")
    @OrderBy("startDate DESC")
    @LazyCollection(LazyCollectionOption.TRUE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Project> projects;
    
    @OneToMany(mappedBy = "manager")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OrderColumn(name = "subordinate_order")
    private List<Employee> subordinates;
    
    // Many-to-Many relationship
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "employee_skills",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "skill_id"})
    )
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size = 20)
    private Set<Skill> skills;
    
    @ManyToMany
    @JoinTable(name = "employee_certifications")
    @MapKey(name = "certificationName")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Map<String, Certification> certifications;
    
    // Collection with element collection
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "employee_phone_numbers", 
                     joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "phone_number")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @BatchSize(size = 10)
    private Set<String> phoneNumbers;
    
    @ElementCollection
    @CollectionTable(name = "employee_addresses")
    @MapKeyColumn(name = "address_type")
    @MapKeyEnumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Map<AddressType, Address> addresses;
    
    // Any relationship
    @Any(metaColumn = @Column(name = "contact_type"), fetch = FetchType.LAZY)
    @AnyMetaDef(
        idType = "long",
        metaType = "string",
        metaValues = {
            @MetaValue(value = "E", targetEntity = Email.class),
            @MetaValue(value = "P", targetEntity = Phone.class)
        }
    )
    @JoinColumn(name = "contact_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Object primaryContact;
    
    // Custom type
    @Type(type = "com.example.CustomPhoneNumberType")
    @Columns(columns = {
        @Column(name = "phone_country_code"),
        @Column(name = "phone_number")
    })
    private PhoneNumber phoneNumber;
    
    // Formula field (computed)
    @Formula("YEAR(CURRENT_DATE) - YEAR(hire_date)")
    private Integer yearsOfService;
    
    // Generated value
    @Generated(GenerationTime.ALWAYS)
    @Column(name = "full_name", insertable = false, updatable = false)
    private String fullName;
    
    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at")
    private Date createdAt;
    
    // Constructors
    public Employee() {}
    
    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    
    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        // Code to execute before persist
    }
    
    @PostPersist
    protected void afterCreate() {
        // Code to execute after persist
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Code to execute before update
    }
    
    @PostUpdate
    protected void afterUpdate() {
        // Code to execute after update
    }
    
    @PreRemove
    protected void onDelete() {
        // Code to execute before remove
    }
    
    @PostRemove
    protected void afterDelete() {
        // Code to execute after remove
    }
    
    @PostLoad
    protected void onLoad() {
        // Code to execute after load
    }
}

// Embeddable class
@Embeddable
@Access(AccessType.FIELD)
class Address {
    @Column(name = "street", length = 100)
    private String street;
    
    @Column(name = "city", length = 50)
    private String city;
    
    @Column(name = "state", length = 2)
    private String state;
    
    @Column(name = "zip_code", length = 10)
    private String zipCode;
    
    // Getters and setters
}

// Supporting entity classes
@Entity
@Table(name = "departments")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", unique = true)
    private String name;
    
    @OneToMany(mappedBy = "department")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Employee> employees;
}

@Entity
@Table(name = "projects")
class Project {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;
}

@Entity
@Table(name = "skills")
class Skill {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @ManyToMany(mappedBy = "skills")
    private Set<Employee> employees;
}

@Entity
@Table(name = "certifications")
class Certification {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "name")
    private String certificationName;
}

@Entity
@Table(name = "employee_profiles")
class EmployeeProfile {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @Lob
    private String biography;
}

@Entity
class Email {
    @Id
    @GeneratedValue
    private Long id;
    
    private String emailAddress;
}

@Entity
class Phone {
    @Id
    @GeneratedValue
    private Long id;
    
    private String phoneNumber;
}

// Enumerations
enum EmployeeStatus {
    ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
}

enum EmployeeType {
    FULL_TIME, PART_TIME, CONTRACT, INTERN
}

enum AddressType {
    HOME, WORK, BILLING
}

// Custom type class
class PhoneNumber {
    private String countryCode;
    private String number;
}

