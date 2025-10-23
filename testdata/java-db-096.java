// @testdata/DeadlockLoserDataAccessExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Sample code to test rule sql‑java‑096: DeadlockLoserDataAccessExceptionHandling
 */
public class DeadlockLoserDataAccessExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: proper handling of DeadlockLoserDataAccessException
        public void addUser(String username, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", username, email);
            } catch (DeadlockLoserDataAccessException e) {
                // Proper handling: log and throw custom exception or retry
                System.err.println("Deadlock occurred: " + e.getMessage());
                throw new CustomDeadlockException("Failed due to deadlock while adding user", e);
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
                throw new RuntimeException("User insertion failed", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUserEmail(String username, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE username = ?", email, username);
            } catch (DeadlockLoserDataAccessException e) {
                // Violation: only printing the exception
                System.out.println("Deadlock detected: " + e.getMessage());
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch and rethrow DeadlockLoserDataAccessException properly
        public void deleteUser(String username) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
            } catch (DeadlockLoserDataAccessException e) {
                System.err.println("Deadlock during delete: " + e.getMessage());
                throw new CustomDeadlockException("Delete failed due to deadlock", e);
            }
        }
    }

    // Custom exception for proper handling
    public static class CustomDeadlockException extends RuntimeException {
        public CustomDeadlockException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
