package com.nxtweb.supareel.product;

import com.nxtweb.supareel.common.PageResponse;
import com.nxtweb.supareel.product.dto.CreateProductRequest;
import com.nxtweb.supareel.product.dto.CreateProductResponse;
import com.nxtweb.supareel.product.dto.ProductByIdResponse;
import com.nxtweb.supareel.user.User;
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
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository repository;

    public CreateProductResponse create(CreateProductRequest productRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Product product = productMapper.toProduct(productRequest, user);
        Product savedProduct = repository.save(product);
        return productMapper.toCreateProductResponse(savedProduct);
    }

    public ProductByIdResponse findById(UUID id) {
        return repository.findById(id)
                .map(productMapper::toProductByIdResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: " + id.toString()));
    }

    public PageResponse<ProductByIdResponse> findAllProducts(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        Page<Product> products = repository.findAllDisplayableProducts(pageable, user.getId());
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

    public PageResponse<ProductByIdResponse> findAllProductsByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
        Page<Product> products = repository.findAll(withOwnerId(user.getId()), pageable);
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