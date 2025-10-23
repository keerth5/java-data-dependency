// @testdata/PersistenceExceptionHandlingTest.java
package com.example.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑102: PersistenceExceptionHandling
 */
public class PersistenceExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final EntityManager entityManager;

        public UserRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        // Non-violation: proper handling of PersistenceException
        public void addUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.persist(user);
                tx.commit();
            } catch (PersistenceException e) {
                if (tx.isActive()) tx.rollback();
                // Proper handling: log and wrap in custom exception
                System.err.println("Persistence exception: " + e.getMessage());
                throw new CustomPersistenceException("Failed to add user", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.merge(user);
                tx.commit();
            } catch (PersistenceException e) {
                // Violation: only printing the exception
                System.out.println("Persistence exception occurred: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with rollback and rethrow
        public void deleteUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.remove(user);
                tx.commit();
            } catch (PersistenceException e) {
                if (tx.isActive()) tx.rollback();
                System.err.println("Persistence exception during delete: " + e.getMessage());
                throw new CustomPersistenceException("Delete failed", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try {
                return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
            } catch (PersistenceException e) {
                System.err.println("Persistence exception during query: " + e.getMessage());
                throw new CustomPersistenceException("Failed to fetch users", e);
            }
        }
    }

    // Sample entity stub
    public static class User {
        private Long id;
        private String name;
        private String email;
        // getters and setters omitted
    }

    // Custom exception for proper handling
    public static class CustomPersistenceException extends RuntimeException {
        public CustomPersistenceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
