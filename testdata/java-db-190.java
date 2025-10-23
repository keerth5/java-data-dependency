// sql-java-190: MicronautDataUsage
// Detects Micronaut Data framework usage
// This file tests detection of Micronaut Data patterns

package com.example.micronaut;

import io.micronaut.data.annotation.*;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.repository.PageableRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import java.util.List;
import java.util.Optional;

// VIOLATION: Micronaut Data JDBC Repository
@JdbcRepository(dialect = Dialect.MYSQL)
public interface UserRepository extends CrudRepository<User, Long> {
    
    // VIOLATION: Micronaut Data query method
    List<User> findByEmail(String email);
    
    // VIOLATION: Micronaut Data with @Query annotation
    @Query("SELECT * FROM users WHERE age > :age")
    List<User> findUsersOlderThan(@QueryParam("age") int age);
    
    // VIOLATION: Using @Where annotation
    @Where("@.active = true")
    List<User> findActiveUsers();
}

// VIOLATION: Micronaut Data JPA Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategory(String category);
    
    // VIOLATION: @EntityGraph usage
    @EntityGraph(attributePaths = {"reviews", "supplier"})
    Optional<Product> findById(Long id);
    
    // VIOLATION: Custom query with Micronaut Data
    @Query("FROM Product p WHERE p.price < :maxPrice")
    List<Product> findAffordableProducts(double maxPrice);
}

// VIOLATION: Micronaut Data with async operations
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface OrderRepository extends CrudRepository<Order, Long> {
    
    // VIOLATION: Async query method
    @Async
    CompletableFuture<List<Order>> findByCustomerId(Long customerId);
    
    // VIOLATION: Reactive query
    @Reactive
    Mono<Order> findByOrderNumber(String orderNumber);
}

// VIOLATION: PageableRepository usage
@JdbcRepository(dialect = Dialect.H2)
public interface CustomerRepository extends PageableRepository<Customer, Long> {
    
    // VIOLATION: Pageable query
    Page<Customer> findByCountry(String country, Pageable pageable);
    
    // VIOLATION: Slice query
    Slice<Customer> findByCity(String city, Pageable pageable);
}

// VIOLATION: Using @Id annotation (Micronaut Data entity)
@MappedEntity("users")
public class User {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "user_name")
    private String username;
    
    private String email;
    
    @DateCreated
    private LocalDateTime createdDate;
    
    @DateUpdated
    private LocalDateTime updatedDate;
    
    @Version
    private Long version;
}

// VIOLATION: Micronaut Data embedded entity
@MappedEntity
public class Product {
    
    @Id
    private Long id;
    
    private String name;
    
    @Embedded
    private Price price;
    
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "product")
    private List<Review> reviews;
}

// VIOLATION: Micronaut Data repository with custom methods
@JdbcRepository(dialect = Dialect.MYSQL)
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    
    // VIOLATION: @Query with update
    @Query("UPDATE payments SET status = :status WHERE id = :id")
    void updateStatus(Long id, String status);
    
    // VIOLATION: Delete query
    @Query("DELETE FROM payments WHERE created_date < :date")
    int deleteOldPayments(LocalDate date);
    
    // VIOLATION: Native query
    @Query(value = "SELECT * FROM payments WHERE amount > :amount", nativeQuery = true)
    List<Payment> findHighValuePayments(double amount);
}

// VIOLATION: Using @RepositoryConfiguration
@RepositoryConfiguration(
    queryBuilder = io.micronaut.data.model.query.builder.sql.SqlQueryBuilder.class,
    implicitQueries = false
)
@JdbcRepository(dialect = Dialect.ORACLE)
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    
    List<Invoice> findByStatus(String status);
}

// VIOLATION: Micronaut Data DTO projection
@JdbcRepository(dialect = Dialect.MYSQL)
public interface ReportRepository extends CrudRepository<Report, Long> {
    
    // VIOLATION: DTO projection
    @Query("SELECT new com.example.dto.UserSummary(u.id, u.name, COUNT(o.id)) " +
           "FROM User u LEFT JOIN u.orders o GROUP BY u.id, u.name")
    List<UserSummary> getUserSummaries();
}

// VIOLATION: Using @Join annotation
@JdbcRepository(dialect = Dialect.MYSQL)
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long> {
    
    @Join("product")
    @Join("order")
    Optional<OrderDetail> findById(Long id);
    
    @Join(value = "product", type = Join.Type.LEFT)
    List<OrderDetail> findByOrderId(Long orderId);
}

// VIOLATION: Criteria API with Micronaut Data
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface DynamicRepository extends CrudRepository<Entity, Long> {
    
    @QueryHint(name = "fetch_size", value = "100")
    @Query("FROM Entity e WHERE e.field = :value")
    List<Entity> findWithHint(String value);
}

// VIOLATION: Transactional repository method
@JdbcRepository(dialect = Dialect.MYSQL)
public interface TransactionalRepository extends CrudRepository<Account, Long> {
    
    @Transactional
    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :id")
    void addBalance(Long id, double amount);
}

// VIOLATION: Micronaut Data specification
@JdbcRepository(dialect = Dialect.H2)
public interface SpecificationRepository extends JpaSpecificationExecutor<User> {
    
    List<User> findAll(Specification<User> spec);
}

// NON-VIOLATION: Spring Data repository
@org.springframework.data.jpa.repository.JpaRepository
public interface SpringUserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Long> {
    List<User> findByUsername(String username);
}

// NON-VIOLATION: Plain DAO interface
public interface UserDao {
    User findById(Long id);
    void save(User user);
    void delete(Long id);
}

// NON-VIOLATION: JDBC Template usage
public class JdbcUserRepository {
    
    private JdbcTemplate jdbcTemplate;
    
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            new Object[]{id},
            new UserRowMapper()
        );
    }
}

// NON-VIOLATION: MyBatis mapper
public interface MyBatisUserMapper {
    @org.apache.ibatis.annotations.Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(Long id);
}

// Helper classes and imports
class Order { }
class Customer { }
class Payment { }
class Invoice { }
class Report { }
class OrderDetail { }
class Entity { }
class Account { }
class Review { }
class Price { }
class UserSummary { }
class LocalDateTime { }
class LocalDate { }
class CompletableFuture<T> { }
class Mono<T> { }
class Slice<T> { }
class Specification<T> { }
interface JpaSpecificationExecutor<T> { }
class JdbcTemplate { 
    <T> T queryForObject(String sql, Object[] args, RowMapper<T> mapper) { return null; }
}
interface RowMapper<T> { }
class UserRowMapper implements RowMapper<User> { }
@interface Async { }
@interface Reactive { }
@interface Transactional { }
@interface QueryHint { String name(); String value(); }

