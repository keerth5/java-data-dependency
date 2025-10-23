// @testdata/HibernateExceptionHandlingTest.java
package com.example.test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑099: HibernateExceptionHandling
 */
public class HibernateExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final SessionFactory sessionFactory;

        public UserRepository(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        // Non-violation: proper handling of HibernateException
        public void addUser(User user) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                session.save(user);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                // Proper handling: log and throw custom exception
                System.err.println("Hibernate exception occurred: " + e.getMessage());
                throw new CustomHibernateException("Failed to add user", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUser(User user) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                session.update(user);
                tx.commit();
            } catch (HibernateException e) {
                // Violation: only printing the exception
                System.out.println("Hibernate exception: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with rollback and rethrow
        public void deleteUser(User user) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                session.delete(user);
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                System.err.println("Hibernate exception during delete: " + e.getMessage());
                throw new CustomHibernateException("Delete failed", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try (Session session = sessionFactory.openSession()) {
                return session.createQuery("FROM User", User.class).list();
            } catch (HibernateException e) {
                System.err.println("Hibernate exception during query: " + e.getMessage());
                throw new CustomHibernateException("Failed to fetch users", e);
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
    public static class CustomHibernateException extends RuntimeException {
        public CustomHibernateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
