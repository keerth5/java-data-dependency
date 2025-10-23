// @testdata/QueryTimeoutExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Sample code to test rule sql‑java‑097: QueryTimeoutExceptionHandling
 */
public class QueryTimeoutExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: proper handling of QueryTimeoutException
        public void addUser(String username, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", username, email);
            } catch (QueryTimeoutException e) {
                // Proper handling: log and wrap in custom exception
                System.err.println("Query timed out: " + e.getMessage());
                throw new CustomTimeoutException("Failed to add user due to query timeout", e);
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
                throw new RuntimeException("User insertion failed", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUserEmail(String username, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE username = ?", email, username);
            } catch (QueryTimeoutException e) {
                // Violation: only printing the exception
                System.out.println("Query timeout occurred: " + e.getMessage());
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch and rethrow QueryTimeoutException properly
        public void deleteUser(String username) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
            } catch (QueryTimeoutException e) {
                System.err.println("Query timed out during delete: " + e.getMessage());
                throw new CustomTimeoutException("Delete operation failed due to query timeout", e);
            }
        }
    }

    // Custom exception for proper handling
    public static class CustomTimeoutException extends RuntimeException {
        public CustomTimeoutException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
