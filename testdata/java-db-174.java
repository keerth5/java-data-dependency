// @testdata/TransactionRollbackForUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample code to test rule sql‑java‑174: TransactionRollbackForUsage
 */
@Service
public class TransactionRollbackForUsageTest {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRollbackForUsageTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Violation: method-level @Transactional with rollbackFor specified
    @Transactional(rollbackFor = {RuntimeException.class}) // Violation
    public void rollbackForTransactionViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
        System.out.println("Method with rollbackFor=RuntimeException (violation).");
    }

    // Violation: multiple exceptions in rollbackFor
    @Transactional(rollbackFor = {RuntimeException.class, IllegalArgumentException.class}) // Violation
    public void rollbackForMultipleExceptionsViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
        System.out.println("Method with rollbackFor multiple exceptions (violation).");
    }

    // Non-violation: standard @Transactional without rollbackFor
    @Transactional // Non-violation
    public void defaultTransactionNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
        System.out.println("Method with default @Transactional (non-violation).");
    }

    // Non-violation: no @Transactional
    public void noTransactionalNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("Method without @Transactional (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        TransactionRollbackForUsageTest test = new TransactionRollbackForUsageTest(jdbcTemplate);

        test.rollbackForTransactionViolation();           // Violation
        test.rollbackForMultipleExceptionsViolation();    // Violation
        test.defaultTransactionNonViolation();            // Non-violation
        test.noTransactionalNonViolation();               // Non-violation
    }
}
