// @testdata/PessimisticLockExceptionHandlingTest.java
package com.example.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PessimisticLockException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑104: PessimisticLockExceptionHandling
 */
public class PessimisticLockExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final EntityManager entityManager;

        public UserRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        // Non-violation: proper handling of PessimisticLockException
        public void updateUserEmail(User user, String newEmail) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                user.setEmail(newEmail);
                entityManager.merge(user);
                tx.commit();
            } catch (PessimisticLockException e) {
                if (tx.isActive()) tx.rollback();
                // Proper handling: log and throw custom exception
                System.err.println("Pessimistic lock exception: " + e.getMessage());
                throw new CustomPessimisticLockException("Failed to update user due to pessimistic lock", e);
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
            } catch (PessimisticLockException e) {
                // Violation: only printing the exception
                System.out.println("Pessimistic lock occurred: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with rollback and rethrow
        public void deleteUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.remove(user);
                tx.commit();
            } catch (PessimisticLockException e) {
                if (tx.isActive()) tx.rollback();
                System.err.println("Pessimistic lock exception during delete: " + e.getMessage());
                throw new CustomPessimisticLockException("Delete failed due to pessimistic lock", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try {
                return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
            } catch (PessimisticLockException e) {
                System.err.println("Pessimistic lock exception during query: " + e.getMessage());
                throw new CustomPessimisticLockException("Failed to fetch users due to pessimistic lock", e);
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
    public static class CustomPessimisticLockException extends RuntimeException {
        public CustomPessimisticLockException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
