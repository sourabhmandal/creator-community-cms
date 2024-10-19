package com.nxtweb.supareel.product;

import com.nxtweb.supareel.common.PageResponse;
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

public interface ProductService {

    CreateProductResponse create(CreateProductRequest productRequest, Authentication connectedUser);

    ProductByIdResponse findById(UUID id);

    PageResponse<ProductByIdResponse> findAllProducts(int page, int size, Authentication connectedUser);

    PageResponse<ProductByIdResponse> findAllProductsByOwner(int page, int size, Authentication connectedUser);
}