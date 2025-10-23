// @testdata/CannotAcquireLockExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Sample code to test rule sql‑java‑095: CannotAcquireLockExceptionHandling
 */
public class CannotAcquireLockExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: proper handling of CannotAcquireLockException
        public void addUser(String username, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", username, email);
            } catch (CannotAcquireLockException e) {
                // Proper handling: log and throw custom exception
                System.err.println("Cannot acquire lock: " + e.getMessage());
                throw new CustomLockException("Failed to acquire database lock while adding user", e);
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
                throw new RuntimeException("User insertion failed", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUserEmail(String username, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE username = ?", email, username);
            } catch (CannotAcquireLockException e) {
                // Violation: only printing the exception
                System.out.println("Cannot acquire lock: " + e.getMessage());
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch and rethrow CannotAcquireLockException properly
        public void deleteUser(String username) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
            } catch (CannotAcquireLockException e) {
                System.err.println("Cannot acquire lock during delete: " + e.getMessage());
                throw new CustomLockException("Delete failed due to lock acquisition failure", e);
            }
        }
    }

    // Custom exception for proper handling
    public static class CustomLockException extends RuntimeException {
        public CustomLockException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
