// sql-java-083: SpringDataTransactionalUsage
// Detects @Transactional annotation on repository methods
// This file tests detection of @Transactional usage in Spring Data repositories

package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;
import java.util.List;
import java.util.Optional;

@Repository
// VIOLATION: @Transactional at class level on repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // VIOLATION: @Transactional on repository query method
    @Transactional
    @Query("SELECT o FROM Order o WHERE o.customerId = ?1")
    List<Order> findByCustomerId(Long customerId);
    
    // VIOLATION: @Transactional with readOnly flag
    @Transactional(readOnly = true)
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // VIOLATION: @Transactional with propagation setting
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("UPDATE Order o SET o.status = ?1 WHERE o.id = ?2")
    void updateOrderStatus(String status, Long id);
    
    // VIOLATION: @Transactional with isolation level
    @Transactional(isolation = Isolation.SERIALIZABLE)
    List<Order> findByStatusAndAmount(String status, double amount);
    
    // VIOLATION: @Transactional with timeout
    @Transactional(timeout = 30)
    @Query("SELECT o FROM Order o WHERE o.createdDate > ?1")
    List<Order> findRecentOrders(java.time.LocalDate date);
    
    // VIOLATION: @Transactional with rollbackFor
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("DELETE FROM Order o WHERE o.status = 'CANCELLED' AND o.createdDate < ?1")
    void cleanupCancelledOrders(java.time.LocalDate cutoffDate);
    
    // VIOLATION: @Transactional with noRollbackFor
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    @Modifying
    @Query("UPDATE Order o SET o.processedDate = CURRENT_TIMESTAMP WHERE o.id = ?1")
    void markOrderProcessed(Long orderId);
    
    // VIOLATION: @Transactional with multiple settings
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        timeout = 60,
        readOnly = false
    )
    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.total > ?1")
    List<Order> findHighValueOrders(double minTotal);
    
    // VIOLATION: @Transactional on derived delete method
    @Transactional
    void deleteByStatus(String status);
    
    // VIOLATION: Read-only transaction on custom query
    @Transactional(readOnly = true, timeout = 10)
    @Query("SELECT o FROM Order o WHERE o.customerId = ?1 ORDER BY o.createdDate DESC")
    List<Order> findCustomerOrderHistory(Long customerId);
    
    // NON-VIOLATION: Repository method without @Transactional
    List<Order> findByStatus(String status);
    
    // NON-VIOLATION: Derived query method without explicit transaction
    Optional<Order> findByIdAndCustomerId(Long id, Long customerId);
    
    // NON-VIOLATION: Simple @Query without @Transactional
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PENDING'")
    long countPendingOrders();
    
    // NON-VIOLATION: Standard repository method (inherited)
    // Optional<Order> findById(Long id);
    
    // NON-VIOLATION: Derived method without transaction annotation
    List<Order> findByCustomerIdAndStatus(Long customerId, String status);
}

// Additional non-repository interface without @Transactional (NON-VIOLATION)
@Repository
interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // VIOLATION: @Transactional on this repository method
    @Transactional(propagation = Propagation.MANDATORY)
    void deleteByOrderId(Long orderId);
    
    // NON-VIOLATION: No @Transactional annotation
    List<Payment> findByOrderId(Long orderId);
    
    // VIOLATION: @Transactional with modifying query
    @Transactional
    @Modifying
    @Query("UPDATE Payment p SET p.status = ?1 WHERE p.orderId = ?2")
    int updatePaymentStatus(String status, Long orderId);
}

class Order {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String status;
    private double total;
}

class Payment {
    private Long id;
    private Long orderId;
    private String status;
}

