// @testdata/SpringBootStarterDataJpaUsageTest.java
package com.example.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

/**
 * Sample code to test rule sql‑java‑177: SpringBootStarterDataJpaUsage
 */
public class SpringBootStarterDataJpaUsageTest {

    // Violation: main class with Spring Boot + JPA repositories
    @SpringBootApplication
    @EnableJpaRepositories(basePackages = "com.example.repositories") // Violation
    static class JpaStarterApplication {
    }

    // Violation: repository interface extending JpaRepository
    interface UserRepository extends JpaRepository<User, Long> { // Violation
    }

    // Violation: another repository interface
    interface ProductRepository extends JpaRepository<Product, Integer> { // Violation
    }

    // Non-violation: simple configuration class
    @Configuration // Non-violation
    static class RegularConfiguration {
    }

    // Non-violation: simple component
    @Component // Non-violation
    static class PlainComponent {
    }

    // Dummy entity classes
    static class User {
        private Long id;
        private String name;
    }

    static class Product {
        private Integer id;
        private String title;
    }

    public static void main(String[] args) {
        System.out.println("Test Spring Boot Data JPA starter usage.");
    }
}
