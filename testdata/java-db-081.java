// sql-java-081: SpringDataQueryAnnotationUsage
// Detects @Query annotation usage in Spring Data repositories
// This file tests detection of @Query annotation patterns

package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // VIOLATION: Using @Query annotation with JPQL
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
    
    // VIOLATION: Using @Query annotation with native SQL
    @Query(value = "SELECT * FROM users WHERE status = :status", nativeQuery = true)
    List<User> findByStatus(@Param("status") String status);
    
    // VIOLATION: Using @Query with named parameters
    @Query("SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    List<User> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
    // VIOLATION: Complex @Query with JOIN
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    // VIOLATION: @Query with multiple conditions
    @Query("SELECT u FROM User u WHERE u.age > :minAge AND u.country = :country")
    List<User> findUsersByAgeAndCountry(@Param("minAge") int minAge, @Param("country") String country);
    
    // VIOLATION: Native query with complex SQL
    @Query(value = "SELECT u.* FROM users u LEFT JOIN orders o ON u.id = o.user_id WHERE o.total > ?1", 
           nativeQuery = true)
    List<User> findUsersWithHighValueOrders(double minTotal);
    
    // VIOLATION: @Query with subquery
    @Query("SELECT u FROM User u WHERE u.id IN (SELECT o.userId FROM Order o WHERE o.status = 'COMPLETED')")
    List<User> findUsersWithCompletedOrders();
    
    // VIOLATION: @Query with COUNT function
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countActiveUsers();
    
    // VIOLATION: @Query with custom projection
    @Query("SELECT new com.example.dto.UserDTO(u.id, u.name, u.email) FROM User u WHERE u.department = :dept")
    List<Object> findUserDTOsByDepartment(@Param("dept") String department);
    
    // NON-VIOLATION: Method query (derived query) - no @Query annotation
    Optional<User> findByUsername(String username);
    
    // NON-VIOLATION: Another derived query method
    List<User> findByLastNameAndFirstName(String lastName, String firstName);
    
    // NON-VIOLATION: Simple derived query
    List<User> findByActiveTrue();
    
    // NON-VIOLATION: Derived query with ordering
    List<User> findByAgeGreaterThanOrderByAgeDesc(int age);
    
    // NON-VIOLATION: Exists query derived method
    boolean existsByEmail(String email);
    
    // NON-VIOLATION: Count derived query
    long countByActive(boolean active);
    
    // NON-VIOLATION: Delete derived query
    void deleteByUsername(String username);
    
    // NON-VIOLATION: Standard JpaRepository methods (inherited)
    // findById(), save(), delete(), findAll() etc.
}

class User {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private String country;
    private boolean active;
    private String department;
}

