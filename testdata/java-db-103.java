// @testdata/OptimisticLockExceptionHandlingTest.java
package com.example.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑103: OptimisticLockExceptionHandling
 */
public class OptimisticLockExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final EntityManager entityManager;

        public UserRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        // Non-violation: proper handling of OptimisticLockException
        public void updateUserEmail(User user, String newEmail) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                user.setEmail(newEmail);
                entityManager.merge(user);
                tx.commit();
            } catch (OptimisticLockException e) {
                if (tx.isActive()) tx.rollback();
                // Proper handling: log and throw custom exception
                System.err.println("Optimistic lock exception: " + e.getMessage());
                throw new CustomOptimisticLockException("Failed to update user due to optimistic lock", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUserName(User user, String newName) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                user.setName(newName);
                entityManager.merge(user);
                tx.commit();
            } catch (OptimisticLockException e) {
                // Violation: only printing the exception
                System.out.println("Optimistic lock occurred: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with rollback and rethrow
        public void deleteUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.remove(user);
                tx.commit();
            } catch (OptimisticLockException e) {
                if (tx.isActive()) tx.rollback();
                System.err.println("Optimistic lock exception during delete: " + e.getMessage());
                throw new CustomOptimisticLockException("Delete failed due to optimistic lock", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try {
                return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
            } catch (OptimisticLockException e) {
                System.err.println("Optimistic lock exception during query: " + e.getMessage());
                throw new CustomOptimisticLockException("Failed to fetch users due to optimistic lock", e);
            }
        }
    }

    // Sample entity stub
    public static class User {
        private Long id;
        private String name;
        private String email;

        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        // getters omitted
    }

    // Custom exception for proper handling
    public static class CustomOptimisticLockException extends RuntimeException {
        public CustomOptimisticLockException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
