package com.nxtweb.supareel.product;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductSpecification {

    public static Specification<Product> withOwnerId(UUID ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner"), ownerId);
    }

}
