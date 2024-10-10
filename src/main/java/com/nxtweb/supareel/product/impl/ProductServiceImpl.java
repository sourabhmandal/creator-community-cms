package com.nxtweb.supareel.product.impl;

import com.nxtweb.supareel.common.PageResponse;
import com.nxtweb.supareel.product.Product;
import com.nxtweb.supareel.product.ProductMapper;
import com.nxtweb.supareel.product.ProductRepository;
import com.nxtweb.supareel.product.ProductService;
import com.nxtweb.supareel.product.dto.CreateProductRequest;
import com.nxtweb.supareel.product.dto.CreateProductResponse;
import com.nxtweb.supareel.product.dto.ProductByIdResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.nxtweb.supareel.product.ProductSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository repository;

    @Override
    public CreateProductResponse create(CreateProductRequest productRequest, Authentication connectedUser) {
        Product product = productMapper.toProduct(productRequest, connectedUser.getName());
        Product savedProduct = repository.save(product);
        return productMapper.toCreateProductResponse(savedProduct);
    }

    @Override
    public ProductByIdResponse findById(UUID id) {
        return repository.findById(id)
                .map(productMapper::toProductByIdResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + id.toString()));
    }

    @Override
    public PageResponse<ProductByIdResponse> findAllProducts(int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        Page<Product> products = repository.findAllDisplayableProducts(pageable, connectedUser.getName());
        List<ProductByIdResponse> productResponses = products.stream()
                .map(productMapper::toProductByIdResponse)
                .toList();
        return new PageResponse<>(
                productResponses,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isFirst(),
                products.isLast());
    }

    @Override
    public PageResponse<ProductByIdResponse> findAllProductsByOwner(int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        Page<Product> products = repository.findAll(withOwnerId(connectedUser.getName()), pageable);
        List<ProductByIdResponse> productResponses = products.stream()
                .map(productMapper::toProductByIdResponse)
                .toList();

        return new PageResponse<>(
                productResponses,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isFirst(),
                products.isLast());
    }
}