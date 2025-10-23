// @testdata/MyBatisExceptionHandlingTest.java
package com.example.test;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sample code to test rule sql‑java‑106: MyBatisExceptionHandling
 */
public class MyBatisExceptionHandlingTest {

    @Repository
    public class UserRepository {

        private final SqlSessionFactory sqlSessionFactory;

        public UserRepository(SqlSessionFactory sqlSessionFactory) {
            this.sqlSessionFactory = sqlSessionFactory;
        }

        // Non-violation: proper handling of MyBatis PersistenceException
        public void addUser(User user) {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                session.insert("UserMapper.insertUser", user);
                session.commit();
            } catch (PersistenceException e) {
                // Proper handling: log and wrap in custom exception
                System.err.println("MyBatis exception occurred: " + e.getMessage());
                throw new CustomMyBatisException("Failed to add user due to MyBatis exception", e);
            }
        }

        // Violation: inadequate handling, just printing exception
        public void updateUser(User user) {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                session.update("UserMapper.updateUser", user);
                session.commit();
            } catch (PersistenceException e) {
                // Violation: only printing the exception
                System.out.println("MyBatis exception: " + e.getMessage());
            }
        }

        // Non-violation: proper handling with commit/rollback logic
        public void deleteUser(User user) {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                session.delete("UserMapper.deleteUser", user);
                session.commit();
            } catch (PersistenceException e) {
                System.err.println("MyBatis exception during delete: " + e.getMessage());
                throw new CustomMyBatisException("Delete failed due to MyBatis exception", e);
            }
        }

        // Non-violation: retrieving users with proper exception handling
        public List<User> getUsers() {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                return session.selectList("UserMapper.getUsers");
            } catch (PersistenceException e) {
                System.err.println("MyBatis exception during query: " + e.getMessage());
                throw new CustomMyBatisException("Failed to fetch users due to MyBatis exception", e);
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
    public static class CustomMyBatisException extends RuntimeException {
        public CustomMyBatisException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
