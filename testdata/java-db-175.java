// @testdata/NestedTransactionUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample code to test rule sql‑java‑175: NestedTransactionUsage
 */
@Service
public class NestedTransactionUsageTest {

    private final JdbcTemplate jdbcTemplate;

    public NestedTransactionUsageTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Violation: method-level @Transactional with nested propagation
    @Transactional(propagation = Propagation.NESTED) // Violation
    public void nestedTransactionViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
        System.out.println("Method with nested propagation (violation).");
    }

    // Violation: another nested transaction example
    @Transactional(propagation = Propagation.NESTED) // Violation
    public void anotherNestedTransactionViolation() {
        jdbcTemplate.update("INSERT INTO orders(order_id) VALUES(?)", 101);
        System.out.println("Another nested transaction (violation).");
    }

    // Non-violation: default propagation (REQUIRED)
    @Transactional // Non-violation
    public void defaultTransactionNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
        System.out.println("Method with default propagation (non-violation).");
    }

    // Non-violation: no @Transactional
    public void noTransactionalNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
        System.out.println("Method without @Transactional (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        NestedTransactionUsageTest test = new NestedTransactionUsageTest(jdbcTemplate);

        test.nestedTransactionViolation();           // Violation
        test.anotherNestedTransactionViolation();    // Violation
        test.defaultTransactionNonViolation();       // Non-violation
        test.noTransactionalNonViolation();          // Non-violation
    }
}
