// @testdata/SpringTransactionalUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Sample code to test rule sql‑java‑167: SpringTransactionalUsage
 */
@Service
public class SpringTransactionalUsageTest {

    private final JdbcTemplate jdbcTemplate;

    public SpringTransactionalUsageTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Violation: method-level @Transactional annotation
    @Transactional // Violation
    public void transactionalMethodViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Alice");
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Bob");
        System.out.println("Method annotated with @Transactional (violation).");
    }

    // Violation: class-level @Transactional annotation
    @Transactional // Violation
    static class TransactionalServiceClass {
        private final JdbcTemplate jdbcTemplate;

        public TransactionalServiceClass(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public void insertData() {
            jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "Charlie");
            System.out.println("Class annotated with @Transactional (violation).");
        }
    }

    // Non-violation: no @Transactional annotation
    public void nonTransactionalMethodNonViolation() {
        jdbcTemplate.update("INSERT INTO users(name) VALUES(?)", "David");
        System.out.println("No @Transactional annotation (non-violation).");
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = null; // placeholder, normally injected
        SpringTransactionalUsageTest test = new SpringTransactionalUsageTest(jdbcTemplate);

        test.transactionalMethodViolation();      // Violation
        test.nonTransactionalMethodNonViolation(); // Non-violation

        TransactionalServiceClass service = new TransactionalServiceClass(jdbcTemplate);
        service.insertData();                     // Violation
    }
}
