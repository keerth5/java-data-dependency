// @testdata/HibernateJdbcExceptionHandlingTest.java
package com.example.test;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑100: HibernateJdbcExceptionHandling
 */
public class HibernateJdbcExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final SessionFactory sessionFactory;

        public UserRepository(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        // Non-violation: proper handling of JDBCException
        public void addUser(User user) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                session.save(user);
                tx.commit();
            } catch (JDBCException e) {
                if (tx != null) tx.rollback();
                // Proper handling: log and wrap in custom exception
                System.err.println("JDBC exception occurred: " + e.getMessage());
                throw new CustomJdbcException("Failed to add user due to JDBC exception", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUser(User user) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                session.update(user);
                tx.commit();
            } catch (JDBCException e) {
                // Violation: only printing the exception
                System.out.println("JDBC exception: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with rollback and rethrow
        public void deleteUser(User user) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                session.delete(user);
                tx.commit();
            } catch (JDBCException e) {
                if (tx != null) tx.rollback();
                System.err.println("JDBC exception during delete: " + e.getMessage());
                throw new CustomJdbcException("Delete failed due to JDBC exception", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try (Session session = sessionFactory.openSession()) {
                return session.createQuery("FROM User", User.class).list();
            } catch (JDBCException e) {
                System.err.println("JDBC exception during query: " + e.getMessage());
                throw new CustomJdbcException("Failed to fetch users due to JDBC exception", e);
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
    public static class CustomJdbcException extends RuntimeException {
        public CustomJdbcException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
