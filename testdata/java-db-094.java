// @testdata/DuplicateKeyExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Sample code to test rule sql‑java‑094: DuplicateKeyExceptionHandling
 */
public class DuplicateKeyExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: proper handling of DuplicateKeyException
        public void addUser(String username, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", username, email);
            } catch (DuplicateKeyException e) {
                // Proper handling: log and throw custom exception
                System.err.println("Duplicate key violation: " + e.getMessage());
                throw new CustomDuplicateKeyException("User already exists", e);
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
                throw new RuntimeException("User insertion failed", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUserEmail(String username, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE username = ?", email, username);
            } catch (DuplicateKeyException e) {
                // Violation: only printing the exception
                System.out.println("Duplicate key encountered: " + e.getMessage());
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch and rethrow DuplicateKeyException properly
        public void deleteUser(String username) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
            } catch (DuplicateKeyException e) {
                System.err.println("Duplicate key violation during delete: " + e.getMessage());
                throw new CustomDuplicateKeyException("Delete failed due to duplicate key", e);
            }
        }
    }

    // Custom exception for proper handling
    public static class CustomDuplicateKeyException extends RuntimeException {
        public CustomDuplicateKeyException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
