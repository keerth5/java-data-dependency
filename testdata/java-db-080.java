// @testdata/SpringDataQueryMethodUsageTest.java
package com.example.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Sample code to test rule sql-java-080: SpringDataQueryMethodUsage
 */
public class SpringDataQueryMethodUsageTest {

    public interface UserRepository extends JpaRepository<User, Long> {

        // Violation: derived query method naming pattern — findByName
        List<User> findByName(String name);

        // Violation: derived query method naming pattern — countByStatus
        long countByStatus(String status);

        // Violation: derived query method naming pattern — existsByEmailAddress
        boolean existsByEmailAddress(String email);

        // Violation: derived query method naming pattern — findTop3ByAgeGreaterThan
        List<User> findTop3ByAgeGreaterThan(int age);

        // Non-violation: explicit @Query annotation used (not purely derived naming)
        @Query("SELECT u FROM User u WHERE u.department = ?1")
        List<User> queryByDepartment(String dept);

        // Non-violation: method name doesn’t follow derived naming pattern (custom method)
        List<User> customFindUsers(String filter);

        // Violation: derived query method naming pattern — deleteByActiveFalse
        long deleteByActiveFalse();

        // Non-violation: method uses @Query for delete, so not a derived naming violation
        @Query("DELETE FROM User u WHERE u.registrationDate < ?1")
        int removeInactiveUsersOlderThan(java.time.LocalDate date);
    }

    // Sample entity stub
    public static class User {
        private Long id;
        private String name;
        private String emailAddress;
        private String status;
        private int age;
        private boolean active;
        private String department;
        private java.time.LocalDate registrationDate;
        // getters/setters omitted
    }
}
