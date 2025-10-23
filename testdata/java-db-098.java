// @testdata/ConnectionTimeoutExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.CannotGetJdbcConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Sample code to test rule sql‑java‑098: ConnectionTimeoutExceptionHandling
 */
public class ConnectionTimeoutExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: proper handling of connection timeout
        public void addUser(String username, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", username, email);
            } catch (CannotGetJdbcConnectionException e) {
                // Proper handling: log and wrap in custom exception
                System.err.println("Connection timeout: " + e.getMessage());
                throw new CustomConnectionTimeoutException("Failed to add user due to connection timeout", e);
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
                throw new RuntimeException("User insertion failed", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUserEmail(String username, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE username = ?", email, username);
            } catch (CannotGetJdbcConnectionException e) {
                // Violation: only printing the exception
                System.out.println("Connection timeout occurred: " + e.getMessage());
            } catch (DataAccessException e) {
                System.err.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch and rethrow connection timeout properly
        public void deleteUser(String username) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
            } catch (CannotGetJdbcConnectionException e) {
                System.err.println("Connection timeout during delete: " + e.getMessage());
                throw new CustomConnectionTimeoutException("Delete operation failed due to connection timeout", e);
            }
        }
    }

    // Custom exception for proper handling
    public static class CustomConnectionTimeoutException extends RuntimeException {
        public CustomConnectionTimeoutException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
