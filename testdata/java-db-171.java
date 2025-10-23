// @testdata/TransactionPropagationUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample code to test rule sql‑java‑171: TransactionPropagationUsage
 */
@Service
public class TransactionPropagationUsageTest {

    private final JdbcTemplate jdbcTemplate;

    public TransactionPropagationUsageTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Violation: method-level @Transactional with propagation setting
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW) // Violation
    public void requiresNewTransactionViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
        System.out.println("Method with propagation=REQUIRES_NEW (violation).");
    }

    // Violation: method-level @Transactional with nested propagation
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.NESTED) // Violation
    public void nestedTransactionViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
        System.out.println("Method with propagation=NESTED (violation).");
    }

    // Non-violation: standard @Transactional with default propagation
    @Transactional // Non-violation
    public void defaultPropagationNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
        System.out.println("Method with default propagation (non-violation).");
    }

    // Non-violation: no @Transactional
    public void noTransactionalNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Method without @Transactional (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        TransactionPropagationUsageTest test = new TransactionPropagationUsageTest(jdbcTemplate);

        test.requiresNewTransactionViolation();   // Violation
        test.nestedTransactionViolation();        // Violation
        test.defaultPropagationNonViolation();    // Non-violation
        test.noTransactionalNonViolation();       // Non-violation
    }
}
