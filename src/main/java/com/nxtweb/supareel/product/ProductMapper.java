package com.nxtweb.supareel.product;

import com.nxtweb.supareel.product.dto.CreateProductRequest;
import com.nxtweb.supareel.product.dto.CreateProductResponse;
import com.nxtweb.supareel.product.dto.ProductByIdResponse;
import com.nxtweb.supareel.user.User;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(CreateProductRequest createProductRequest, User creator) {
        return Product.builder()
                .id(createProductRequest.id())
                .title(createProductRequest.title())
                .description(createProductRequest.description())
                .price(createProductRequest.price())
                .currency(createProductRequest.currency())
                .owner(creator)
                .createdBy(creator.getEmail())
                .lastModifiedBy( creator.getEmail())
                .build();
    }

    public CreateProductResponse toCreateProductResponse(Product product) {
        return CreateProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .message("New product created successfully")
                .build();
    }

    public ProductByIdResponse toProductByIdResponse(Product product) {
        return ProductByIdResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .user(product.getOwner())
                .build();
    }

}
