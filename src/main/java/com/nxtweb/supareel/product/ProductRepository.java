package com.nxtweb.supareel.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
    SELECT product FROM Product product
    """)
    Page<Product> findAllDisplayableProducts(Pageable pageable);

}
