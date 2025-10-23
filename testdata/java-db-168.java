// @testdata/SpringTransactionTemplateUsageTest.java
package com.example.test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Sample code to test rule sql‑java‑168: SpringTransactionTemplateUsage
 */
public class SpringTransactionTemplateUsageTest {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public SpringTransactionTemplateUsageTest(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    // Violation: programmatic transaction using TransactionTemplate
    public void programmaticTransactionViolation() {
        transactionTemplate.execute(new TransactionCallback<Void>() { // Violation
            @Override
            public Void doInTransaction(TransactionStatus status) {
                jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
                jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
                System.out.println("Programmatic transaction via TransactionTemplate (violation).");
                return null;
            }
        });
    }

    // Non-violation: using standard JDBC operations without TransactionTemplate
    public void standardJdbcNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
        System.out.println("Standard JDBC operation, no TransactionTemplate (non-violation).");
    }

    // Non-violation: Spring declarative @Transactional usage
    @org.springframework.transaction.annotation.Transactional
    public void transactionalAnnotationNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Declarative @Transactional, not a TransactionTemplate usage (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        TransactionTemplate transactionTemplate = null; // placeholder
        SpringTransactionTemplateUsageTest test = new SpringTransactionTemplateUsageTest(jdbcTemplate, transactionTemplate);

        test.programmaticTransactionViolation(); // Violation
        test.standardJdbcNonViolation();          // Non-violation
        test.transactionalAnnotationNonViolation(); // Non-violation
    }
}
