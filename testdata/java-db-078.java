// @testdata/SpringDataRepositoryUsageTest.java
package com.example.test;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql-java-078: SpringDataRepositoryUsage
 */
public class SpringDataRepositoryUsageTest {

    // Non-violation: a plain DAO interface not extending Spring Data repository
    public interface UserDao {
        List<User> findByName(String name);
        void save(User user);
    }

    // Violation: Spring Data repository interface extending CrudRepository
    public interface UserRepository extends CrudRepository<User, Long> {
        List<User> findByStatus(String status);
    }

    // Violation: Spring Data repository interface extending JpaRepository
    @Repository  // annotation is present but still violation because of heritage
    public interface OrderRepository extends JpaRepository<Order, Integer> {
        List<Order> findByAmountGreaterThan(Double amt);
    }

    // Non-violation: manually implemented repository class (not an interface extending Spring Data)
    public static class ManualRepository {
        public User getUserById(Long id) {
            // custom implementation
            return null;
        }

        public void persistUser(User user) {
            // custom implementation
        }
    }

    // Violation: another repository interface extending CrudRepository
    public interface ProductRepository extends org.springframework.data.repository.PagingAndSortingRepository<Product, String> {
        Iterable<Product> findByCategory(String cat);
    }

    // Sample entity stubs
    public static class User {
        private Long id;
        private String name;
        private String status;
        // getters / setters omitted
    }

    public static class Order {
        private Integer id;
        private Double amount;
        // getters / setters omitted
    }

    public static class Product {
        private String id;
        private String category;
        // getters / setters omitted
    }
}
