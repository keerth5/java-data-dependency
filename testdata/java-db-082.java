// sql-java-082: SpringDataModifyingUsage
// Detects @Modifying annotation usage in Spring Data
// This file tests detection of @Modifying annotation patterns

package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // VIOLATION: Using @Modifying for UPDATE query
    @Modifying
    @Query("UPDATE Product p SET p.price = :price WHERE p.id = :id")
    int updatePrice(@Param("id") Long id, @Param("price") double price);
    
    // VIOLATION: Using @Modifying with @Transactional for DELETE
    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.category = :category")
    void deleteByCategory(@Param("category") String category);
    
    // VIOLATION: @Modifying with clearAutomatically flag
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id = :id")
    int decrementStock(@Param("id") Long id, @Param("quantity") int quantity);
    
    // VIOLATION: @Modifying with flushAutomatically flag
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Product p SET p.active = false WHERE p.expiryDate < CURRENT_DATE")
    int deactivateExpiredProducts();
    
    // VIOLATION: @Modifying for batch UPDATE
    @Modifying
    @Query("UPDATE Product p SET p.discount = :discount WHERE p.category IN :categories")
    int applyDiscountToCategories(@Param("discount") double discount, @Param("categories") java.util.List<String> categories);
    
    // VIOLATION: Native query with @Modifying
    @Modifying
    @Query(value = "UPDATE products SET status = 'INACTIVE' WHERE last_updated < DATE_SUB(NOW(), INTERVAL 1 YEAR)", 
           nativeQuery = true)
    int archiveOldProducts();
    
    // VIOLATION: @Modifying for INSERT operation (native)
    @Modifying
    @Query(value = "INSERT INTO product_audit (product_id, action, timestamp) VALUES (:id, :action, NOW())", 
           nativeQuery = true)
    void insertAuditLog(@Param("id") Long id, @Param("action") String action);
    
    // VIOLATION: Complex UPDATE with @Modifying
    @Modifying
    @Query("UPDATE Product p SET p.price = p.price * (1 + :percentage / 100) WHERE p.supplierId = :supplierId")
    int increasePricesBySupplier(@Param("supplierId") Long supplierId, @Param("percentage") double percentage);
    
    // VIOLATION: @Modifying with multiple conditions
    @Modifying
    @Query("UPDATE Product p SET p.featured = true WHERE p.rating >= :minRating AND p.stock > :minStock")
    int markFeaturedProducts(@Param("minRating") double minRating, @Param("minStock") int minStock);
    
    // VIOLATION: DELETE with @Modifying and native query
    @Modifying
    @Query(value = "DELETE FROM products WHERE created_date < :date AND sold_count = 0", nativeQuery = true)
    int removeUnsoldOldProducts(@Param("date") java.time.LocalDate date);
    
    // NON-VIOLATION: Simple read query without @Modifying
    @Query("SELECT p FROM Product p WHERE p.category = :category")
    java.util.List<Product> findByCategory(@Param("category") String category);
    
    // NON-VIOLATION: Derived delete query (without @Modifying)
    void deleteByName(String name);
    
    // NON-VIOLATION: Standard save operation (inherited from JpaRepository)
    // Product save(Product product);
    
    // NON-VIOLATION: Read query with custom projection
    @Query("SELECT p.name, p.price FROM Product p WHERE p.active = true")
    java.util.List<Object[]> findActiveProductDetails();
    
    // NON-VIOLATION: Derived query method
    java.util.List<Product> findByPriceLessThan(double price);
    
    // NON-VIOLATION: Count query without modification
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stock = 0")
    long countOutOfStock();
}

class Product {
    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
    private boolean active;
    private double discount;
}

