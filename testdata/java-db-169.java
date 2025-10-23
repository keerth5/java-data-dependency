// @testdata/SpringPlatformTransactionManagerUsageTest.java
package com.example.test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Sample code to test rule sql‑java‑169: SpringPlatformTransactionManagerUsage
 */
public class SpringPlatformTransactionManagerUsageTest {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    public SpringPlatformTransactionManagerUsageTest(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    // Violation: programmatic transaction using PlatformTransactionManager
    public void programmaticTransactionViolation() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition()); // Violation
        try {
            jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
            jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
            transactionManager.commit(status); // Violation
            System.out.println("Programmatic transaction via PlatformTransactionManager (violation).");
        } catch (Exception e) {
            transactionManager.rollback(status); // Violation
            e.printStackTrace();
        }
    }

    // Non-violation: using standard JDBC operations
    public void standardJdbcNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
        System.out.println("Standard JDBC operation, no PlatformTransactionManager (non-violation).");
    }

    // Non-violation: declarative @Transactional usage
    @org.springframework.transaction.annotation.Transactional
    public void transactionalAnnotationNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Declarative @Transactional, not PlatformTransactionManager usage (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        PlatformTransactionManager transactionManager = null; // placeholder
        SpringPlatformTransactionManagerUsageTest test =
                new SpringPlatformTransactionManagerUsageTest(jdbcTemplate, transactionManager);

        test.programmaticTransactionViolation(); // Violation
        test.standardJdbcNonViolation();          // Non-violation
        test.transactionalAnnotationNonViolation(); // Non-violation
    }
}
