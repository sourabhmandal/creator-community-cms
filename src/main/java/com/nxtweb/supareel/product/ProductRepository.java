package com.nxtweb.supareel.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Query("""
    SELECT product 
    FROM Product product
    WHERE product.createdBy != :userId
    """)
    Page<Product> findAllDisplayableProducts(Pageable pageable, String userId);
}
