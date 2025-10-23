package com.example.database;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;
import java.util.Map;

/**
 * Test file for sql-java-075: MyBatisMapperUsage
 * Detects MyBatis Mapper interface usage
 * MyBatis mappers affect SQL mapping architecture
 */

// Basic Mapper interface
@Mapper
public interface EmployeeMapper {
    
    // Basic CRUD operations
    @Select("SELECT * FROM employees WHERE id = #{id}")
    Employee findById(@Param("id") Long id);
    
    @Select("SELECT * FROM employees")
    List<Employee> findAll();
    
    @Insert("INSERT INTO employees (name, department, age) VALUES (#{name}, #{department}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Employee employee);
    
    @Update("UPDATE employees SET name = #{name}, department = #{department}, age = #{age} WHERE id = #{id}")
    int update(Employee employee);
    
    @Delete("DELETE FROM employees WHERE id = #{id}")
    int delete(@Param("id") Long id);
}

// Mapper with complex queries
@Mapper
interface DepartmentMapper {
    
    @Select("SELECT * FROM departments WHERE id = #{id}")
    Department findById(@Param("id") Long id);
    
    @Select("SELECT * FROM departments WHERE name = #{name}")
    Department findByName(@Param("name") String name);
    
    @Select("SELECT d.*, COUNT(e.id) as employee_count FROM departments d " +
            "LEFT JOIN employees e ON d.id = e.department_id " +
            "GROUP BY d.id")
    List<DepartmentWithCount> findAllWithEmployeeCount();
    
    @Insert("INSERT INTO departments (name, location) VALUES (#{name}, #{location})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Department department);
    
    @Update("UPDATE departments SET name = #{name}, location = #{location} WHERE id = #{id}")
    int update(Department department);
    
    @Delete("DELETE FROM departments WHERE id = #{id}")
    int delete(@Param("id") Long id);
}

// Mapper with result mapping
@Mapper
interface ProductMapper {
    
    @Select("SELECT * FROM products WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "product_name"),
        @Result(property = "price", column = "price"),
        @Result(property = "category", column = "category_id", 
                one = @One(select = "com.example.mapper.CategoryMapper.findById"))
    })
    Product findByIdWithCategory(@Param("id") Long id);
    
    @Select("SELECT * FROM products WHERE category_id = #{categoryId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "product_name"),
        @Result(property = "price", column = "price")
    })
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Select("SELECT p.*, c.name as category_name FROM products p " +
            "JOIN categories c ON p.category_id = c.id " +
            "WHERE p.price BETWEEN #{minPrice} AND #{maxPrice}")
    List<ProductWithCategory> findByPriceRange(@Param("minPrice") Double minPrice, 
                                               @Param("maxPrice") Double maxPrice);
}

// Mapper with dynamic SQL
@Mapper
interface CustomerMapper {
    
    @Select("<script>" +
            "SELECT * FROM customers WHERE 1=1" +
            "<if test='name != null'> AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='email != null'> AND email = #{email}</if>" +
            "<if test='city != null'> AND city = #{city}</if>" +
            "<if test='minAge != null'> AND age >= #{minAge}</if>" +
            "<if test='maxAge != null'> AND age <= #{maxAge}</if>" +
            "</script>")
    List<Customer> findByCriteria(CustomerSearchCriteria criteria);
    
    @Select("<script>" +
            "SELECT * FROM customers WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Customer> findByIds(@Param("ids") List<Long> ids);
    
    @Update("<script>" +
            "UPDATE customers" +
            "<set>" +
            "<if test='name != null'>name = #{name},</if>" +
            "<if test='email != null'>email = #{email},</if>" +
            "<if test='phone != null'>phone = #{phone},</if>" +
            "<if test='city != null'>city = #{city},</if>" +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    int updateSelective(Customer customer);
}

// Mapper with batch operations
@Mapper
interface OrderMapper {
    
    @Insert("<script>" +
            "INSERT INTO orders (customer_id, order_date, total_amount) VALUES " +
            "<foreach item='order' collection='orders' separator=','>" +
            "(#{order.customerId}, #{order.orderDate}, #{order.totalAmount})" +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("orders") List<Order> orders);
    
    @Update("<script>" +
            "<foreach item='order' collection='orders' separator=';'>" +
            "UPDATE orders SET status = #{order.status} WHERE id = #{order.id}" +
            "</foreach>" +
            "</script>")
    int updateBatch(@Param("orders") List<Order> orders);
    
    @Delete("<script>" +
            "DELETE FROM orders WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteBatch(@Param("ids") List<Long> ids);
}

// Mapper with stored procedures
@Mapper
interface ReportMapper {
    
    @Select("{CALL GetEmployeeReport(#{department, mode=IN, jdbcType=VARCHAR}, " +
            "#{startDate, mode=IN, jdbcType=DATE}, " +
            "#{endDate, mode=IN, jdbcType=DATE}, " +
            "#{result, mode=OUT, jdbcType=CURSOR, javaType=java.sql.ResultSet, resultMap=EmployeeReportResult})}")
    @Options(statementType = StatementType.CALLABLE)
    List<EmployeeReport> getEmployeeReport(@Param("department") String department,
                                          @Param("startDate") java.util.Date startDate,
                                          @Param("endDate") java.util.Date endDate);
    
    @Select("{CALL CalculateTotalSales(#{year, mode=IN, jdbcType=INTEGER}, " +
            "#{total, mode=OUT, jdbcType=DECIMAL})}")
    @Options(statementType = StatementType.CALLABLE)
    void calculateTotalSales(@Param("year") Integer year, @Param("total") java.math.BigDecimal total);
}

// Mapper with type handlers
@Mapper
interface UserMapper {
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "username", column = "username"),
        @Result(property = "roles", column = "roles", 
                typeHandler = com.example.typehandler.StringListTypeHandler.class)
    })
    User findById(@Param("id") Long id);
    
    @Insert("INSERT INTO users (username, roles) VALUES (#{username}, #{roles, typeHandler=com.example.typehandler.StringListTypeHandler})")
    int insert(User user);
}

// Mapper with cache configuration
@Mapper
@CacheNamespace(implementation = org.apache.ibatis.cache.impl.PerpetualCache.class, 
                eviction = org.apache.ibatis.cache.decorators.LruCache.class,
                flushInterval = 60000,
                size = 512,
                readWrite = true)
interface CachedEmployeeMapper {
    
    @Select("SELECT * FROM employees WHERE id = #{id}")
    @Options(useCache = true)
    Employee findById(@Param("id") Long id);
    
    @Select("SELECT * FROM employees WHERE department = #{department}")
    @Options(useCache = true)
    List<Employee> findByDepartment(@Param("department") String department);
    
    @Update("UPDATE employees SET name = #{name} WHERE id = #{id}")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    int updateName(@Param("id") Long id, @Param("name") String name);
}

// Mapper with custom SQL provider
@Mapper
interface DynamicQueryMapper {
    
    @SelectProvider(type = EmployeeSqlProvider.class, method = "findByCriteria")
    List<Employee> findByCriteria(EmployeeSearchCriteria criteria);
    
    @UpdateProvider(type = EmployeeSqlProvider.class, method = "updateSelective")
    int updateSelective(Employee employee);
    
    @InsertProvider(type = EmployeeSqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(Employee employee);
}

// SQL Provider class
class EmployeeSqlProvider {
    
    public String findByCriteria(EmployeeSearchCriteria criteria) {
        StringBuilder sql = new StringBuilder("SELECT * FROM employees WHERE 1=1");
        
        if (criteria.getName() != null) {
            sql.append(" AND name LIKE CONCAT('%', #{name}, '%')");
        }
        if (criteria.getDepartment() != null) {
            sql.append(" AND department = #{department}");
        }
        if (criteria.getMinAge() != null) {
            sql.append(" AND age >= #{minAge}");
        }
        if (criteria.getMaxAge() != null) {
            sql.append(" AND age <= #{maxAge}");
        }
        
        return sql.toString();
    }
    
    public String updateSelective(Employee employee) {
        StringBuilder sql = new StringBuilder("UPDATE employees SET ");
        boolean first = true;
        
        if (employee.getName() != null) {
            if (!first) sql.append(", ");
            sql.append("name = #{name}");
            first = false;
        }
        if (employee.getDepartment() != null) {
            if (!first) sql.append(", ");
            sql.append("department = #{department}");
            first = false;
        }
        if (employee.getAge() != null) {
            if (!first) sql.append(", ");
            sql.append("age = #{age}");
            first = false;
        }
        
        sql.append(" WHERE id = #{id}");
        return sql.toString();
    }
    
    public String insertSelective(Employee employee) {
        StringBuilder sql = new StringBuilder("INSERT INTO employees (");
        StringBuilder values = new StringBuilder(" VALUES (");
        boolean first = true;
        
        if (employee.getName() != null) {
            if (!first) {
                sql.append(", ");
                values.append(", ");
            }
            sql.append("name");
            values.append("#{name}");
            first = false;
        }
        if (employee.getDepartment() != null) {
            if (!first) {
                sql.append(", ");
                values.append(", ");
            }
            sql.append("department");
            values.append("#{department}");
            first = false;
        }
        if (employee.getAge() != null) {
            if (!first) {
                sql.append(", ");
                values.append(", ");
            }
            sql.append("age");
            values.append("#{age}");
            first = false;
        }
        
        sql.append(")").append(values).append(")");
        return sql.toString();
    }
}

// Mapper with multiple result sets
@Mapper
interface MultiResultSetMapper {
    
    @Select("SELECT * FROM employees WHERE department = #{department}; " +
            "SELECT * FROM departments WHERE name = #{department}")
    @Results({
        @Result(property = "employees", column = "department", 
                many = @Many(select = "com.example.mapper.EmployeeMapper.findByDepartment"))
    })
    DepartmentWithEmployees getDepartmentWithEmployees(@Param("department") String department);
}

// Mapper with enum handling
@Mapper
interface StatusMapper {
    
    @Select("SELECT * FROM orders WHERE status = #{status}")
    List<Order> findByStatus(@Param("status") OrderStatus status);
    
    @Insert("INSERT INTO orders (customer_id, status, order_date) VALUES (#{customerId}, #{status}, #{orderDate})")
    int insert(Order order);
    
    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") OrderStatus status);
}

// Mapper with date/time handling
@Mapper
interface TimeBasedMapper {
    
    @Select("SELECT * FROM events WHERE event_date BETWEEN #{startDate} AND #{endDate}")
    List<Event> findEventsByDateRange(@Param("startDate") java.util.Date startDate, 
                                      @Param("endDate") java.util.Date endDate);
    
    @Select("SELECT * FROM logs WHERE created_at >= #{since}")
    List<LogEntry> findLogsSince(@Param("since") java.time.LocalDateTime since);
    
    @Insert("INSERT INTO events (name, event_date, created_at) VALUES (#{name}, #{eventDate}, #{createdAt})")
    int insertEvent(Event event);
}

// Mapper with pagination
@Mapper
interface PaginatedMapper {
    
    @Select("SELECT * FROM products ORDER BY name LIMIT #{offset}, #{limit}")
    List<Product> findProductsWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM products")
    int countProducts();
    
    @Select("SELECT * FROM customers ORDER BY name LIMIT #{pageSize} OFFSET #{offset}")
    List<Customer> findCustomersWithPagination(@Param("offset") int offset, @Param("pageSize") int pageSize);
}

// Mapper with joins
@Mapper
interface JoinMapper {
    
    @Select("SELECT e.*, d.name as department_name FROM employees e " +
            "LEFT JOIN departments d ON e.department_id = d.id " +
            "WHERE e.id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "department", column = "department_name")
    })
    EmployeeWithDepartment findEmployeeWithDepartment(@Param("id") Long id);
    
    @Select("SELECT o.*, c.name as customer_name, c.email as customer_email " +
            "FROM orders o " +
            "JOIN customers c ON o.customer_id = c.id " +
            "WHERE o.order_date >= #{startDate}")
    List<OrderWithCustomer> findOrdersWithCustomers(@Param("startDate") java.util.Date startDate);
}

// Entity classes
class Employee {
    private Long id;
    private String name;
    private String department;
    private Integer age;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}

class Department {
    private Long id;
    private String name;
    private String location;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

class Product {
    private Long id;
    private String name;
    private Double price;
    private Long categoryId;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

class Customer {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String city;
    private Integer age;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

class Order {
    private Long id;
    private Long customerId;
    private java.util.Date orderDate;
    private Double totalAmount;
    private OrderStatus status;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}

enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}

// Search criteria classes
class EmployeeSearchCriteria {
    private String name;
    private String department;
    private Integer minAge;
    private Integer maxAge;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }
    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
}

class CustomerSearchCriteria {
    private String name;
    private String email;
    private String city;
    private Integer minAge;
    private Integer maxAge;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }
    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
}

// Result classes
class DepartmentWithCount {
    private Long id;
    private String name;
    private String location;
    private Integer employeeCount;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(Integer employeeCount) { this.employeeCount = employeeCount; }
}

class ProductWithCategory {
    private Long id;
    private String name;
    private Double price;
    private String categoryName;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}

class EmployeeReport {
    private String employeeName;
    private String department;
    private Integer totalHours;
    private Double totalSales;
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}

class User {
    private Long id;
    private String username;
    private List<String> roles;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}

class Event {
    private Long id;
    private String name;
    private java.util.Date eventDate;
    private java.time.LocalDateTime createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public java.util.Date getEventDate() { return eventDate; }
    public void setEventDate(java.util.Date eventDate) { this.eventDate = eventDate; }
}

class LogEntry {
    private Long id;
    private String message;
    private java.time.LocalDateTime createdAt;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

class EmployeeWithDepartment {
    private Long id;
    private String name;
    private String department;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}

class OrderWithCustomer {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private java.util.Date orderDate;
    private Double totalAmount;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}

class DepartmentWithEmployees {
    private Long id;
    private String name;
    private List<Employee> employees;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
