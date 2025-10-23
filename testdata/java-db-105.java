// @testdata/TransactionRollbackExceptionHandlingTest.java
package com.example.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑105: TransactionRollbackExceptionHandling
 */
public class TransactionRollbackExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final EntityManager entityManager;

        public UserRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        // Non-violation: proper handling of RollbackException
        public void addUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.persist(user);
                tx.commit();
            } catch (RollbackException e) {
                if (tx.isActive()) tx.rollback();
                // Proper handling: log and wrap in custom exception
                System.err.println("Transaction rollback exception: " + e.getMessage());
                throw new CustomTransactionRollbackException("Failed to add user due to transaction rollback", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.merge(user);
                tx.commit();
            } catch (RollbackException e) {
                // Violation: only printing the exception
                System.out.println("Transaction rollback occurred: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with rollback and rethrow
        public void deleteUser(User user) {
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.remove(user);
                tx.commit();
            } catch (RollbackException e) {
                if (tx.isActive()) tx.rollback();
                System.err.println("Transaction rollback during delete: " + e.getMessage());
                throw new CustomTransactionRollbackException("Delete failed due to transaction rollback", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try {
                return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
            } catch (RollbackException e) {
                System.err.println("Transaction rollback during query: " + e.getMessage());
                throw new CustomTransactionRollbackException("Failed to fetch users due to transaction rollback", e);
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
    public static class CustomTransactionRollbackException extends RuntimeException {
        public CustomTransactionRollbackException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
