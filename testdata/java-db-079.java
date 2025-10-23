// @testdata/SpringDataJpaRepositoryUsageTest.java
package com.example.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Sample code to test rule sql-java-079: SpringDataJpaRepositoryUsage
 */
public class SpringDataJpaRepositoryUsageTest {

    // Non-violation: interface extending CrudRepository (not JpaRepository)
    public interface UserCrudRepository extends CrudRepository<User, Long> {
        List<User> findByName(String name);
    }

    // Violation: interface extending JpaRepository
    public interface UserJpaRepository extends JpaRepository<User, Long> {
        List<User> findByStatus(String status);
    }

    // Violation: annotated and extending JpaRepository
    @Repository
    public interface OrderJpaRepository extends JpaRepository<Order, Integer> {
        List<Order> findByAmountGreaterThan(Double amt);
    }

    // Non-violation: manually implemented repository class (not interface extending JpaRepository)
    public static class ManualRepository {
        public User getUserById(Long id) {
            // custom implementation
            return null;
        }
        public void persistUser(User user) {
            // custom implementation
        }
    }

    // Violation: another repository extending JpaRepository with custom interface
    public interface ProductJpaRepository extends JpaRepository<Product, String>, CustomProductRepository {
        List<Product> findByCategory(String cat);
    }

    // Custom repository marker interface — non-violation in itself
    public interface CustomProductRepository {
        void customMethod();
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
