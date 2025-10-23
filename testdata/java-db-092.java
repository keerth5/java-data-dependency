// @testdata/DataAccessExceptionHandlingTest.java
package com.example.test;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

/**
 * Sample code to test rule sql‑java‑092: DataAccessExceptionHandling
 */
public class DataAccessExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final JdbcTemplate jdbcTemplate;

        public UserRepository(DataSource dataSource) {
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }

        // Non-violation: catch specific exceptions and rethrow as custom exceptions
        public void addUser(String name, String email) {
            try {
                jdbcTemplate.update("INSERT INTO users (name, email) VALUES (?, ?)", name, email);
            } catch (DataIntegrityViolationException e) {
                throw new CustomDataIntegrityException("Data integrity violation while adding user", e);
            } catch (DataAccessException e) {
                throw new CustomDataAccessException("Data access error while adding user", e);
            }
        }

        // Violation: catch generic DataAccessException without specific handling
        public void updateUser(String name, String email) {
            try {
                jdbcTemplate.update("UPDATE users SET email = ? WHERE name = ?", email, name);
            } catch (DataAccessException e) {
                // Inadequate handling: just printing the exception
                System.out.println("Data access error: " + e.getMessage());
            }
        }

        // Non-violation: catch specific exceptions and log the error
        public void deleteUser(String name) {
            try {
                jdbcTemplate.update("DELETE FROM users WHERE name = ?", name);
            } catch (DataRetrievalFailureException e) {
                // Log the error and rethrow as a custom exception
                System.err.println("Data retrieval failure: " + e.getMessage());
                throw new CustomDataRetrievalException("Failed to retrieve data while deleting user", e);
            } catch (DataAccessException e) {
                // Log the error and rethrow as a custom exception
                System.err.println("Data access error: " + e.getMessage());
                throw new CustomDataAccessException("Data access error while deleting user", e);
            }
        }

        // Non-violation: catch specific exceptions and handle accordingly
        public List<String> getUserEmails() {
            try {
                return jdbcTemplate.queryForList("SELECT email FROM users", String.class);
            } catch (DataRetrievalFailureException e) {
                // Handle the exception and return an empty list
                System.err.println("Data retrieval failure: " + e.getMessage());
                return List.of();
            } catch (DataAccessException e) {
                // Handle the exception and return an empty list
                System.err.println("Data access error: " + e.getMessage());
                return List.of();
            }
        }
    }

    // Custom exception classes
    public static class CustomDataAccessException extends RuntimeException {
        public CustomDataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class CustomDataIntegrityException extends RuntimeException {
        public CustomDataIntegrityException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class CustomDataRetrievalException extends RuntimeException {
        public CustomDataRetrievalException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
