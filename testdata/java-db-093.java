// @testdata/DataIntegrityViolationExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Sample code to test rule sql‑java‑093: DataIntegrityViolationExceptionHandling
 */
public class DataIntegrityViolationExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: specific handling of DataIntegrityViolationException
        public void addUser(String username, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", username, email);
            } catch (DataIntegrityViolationException e) {
                // Proper handling: log and wrap in custom exception
                System.err.println("Data integrity violation: " + e.getMessage());
                throw new CustomIntegrityException("User insertion failed due to integrity violation", e);
            } catch (DataAccessException e) {
                // Other data access errors
                System.err.println("Data access error: " + e.getMessage());
                throw new RuntimeException("User insertion failed", e);
            }
        }

        // Violation: generic catch of DataIntegrityViolationException with only print
        public void updateUserEmail(String username, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE username = ?", email, username);
            } catch (DataIntegrityViolationException e) {
                // Inadequate handling: just printing the exception
                System.out.println("Data integrity violation occurred: " + e.getMessage());
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch and rethrow DataIntegrityViolationException properly
        public void deleteUser(String username) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
            } catch (DataIntegrityViolationException e) {
                System.err.println("Data integrity violation while deleting: " + e.getMessage());
                throw new CustomIntegrityException("Delete operation failed due to integrity violation", e);
            }
        }
    }

    // Custom exception for proper handling
    public static class CustomIntegrityException extends RuntimeException {
        public CustomIntegrityException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
