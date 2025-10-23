// @testdata/ReadOnlyTransactionUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample code to test rule sql‑java‑173: ReadOnlyTransactionUsage
 */
@Service
public class ReadOnlyTransactionUsageTest {

    private final JdbcTemplate jdbcTemplate;

    public ReadOnlyTransactionUsageTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Violation: method-level @Transactional with readOnly=true
    @Transactional(readOnly = true) // Violation
    public void readOnlyTransactionViolation() {
        jdbcTemplate.queryForList("SELECT * FROM users");
        System.out.println("Method with readOnly=true (violation).");
    }

    // Violation: another method explicitly read-only
    @Transactional(readOnly = true) // Violation
    public void anotherReadOnlyTransactionViolation() {
        jdbcTemplate.queryForList("SELECT * FROM products");
        System.out.println("Another read-only transaction (violation).");
    }

    // Non-violation: standard @Transactional without readOnly
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
        ReadOnlyTransactionUsageTest test = new ReadOnlyTransactionUsageTest(jdbcTemplate);

        test.readOnlyTransactionViolation();          // Violation
        test.anotherReadOnlyTransactionViolation();   // Violation
        test.defaultTransactionNonViolation();        // Non-violation
        test.noTransactionalNonViolation();           // Non-violation
    }
}
