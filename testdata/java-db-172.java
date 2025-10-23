// @testdata/TransactionTimeoutUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample code to test rule sql‑java‑172: TransactionTimeoutUsage
 */
@Service
public class TransactionTimeoutUsageTest {

    private final JdbcTemplate jdbcTemplate;

    public TransactionTimeoutUsageTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Violation: method-level @Transactional with timeout
    @Transactional(timeout = 30) // Violation
    public void transactionWithTimeoutViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
        System.out.println("Method with timeout=30 (violation).");
    }

    // Violation: another method with timeout specified
    @Transactional(timeout = 60) // Violation
    public void transactionWithLongTimeoutViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
        System.out.println("Method with timeout=60 (violation).");
    }

    // Non-violation: default timeout (no explicit timeout)
    @Transactional // Non-violation
    public void defaultTimeoutNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
        System.out.println("Method with default timeout (non-violation).");
    }

    // Non-violation: no @Transactional
    public void noTransactionalNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Method without @Transactional (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        TransactionTimeoutUsageTest test = new TransactionTimeoutUsageTest(jdbcTemplate);

        test.transactionWithTimeoutViolation();       // Violation
        test.transactionWithLongTimeoutViolation();   // Violation
        test.defaultTimeoutNonViolation();            // Non-violation
        test.noTransactionalNonViolation();           // Non-violation
    }
}
