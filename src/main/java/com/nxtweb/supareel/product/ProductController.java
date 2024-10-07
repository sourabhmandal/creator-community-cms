package com.nxtweb.supareel.product;

import com.nxtweb.supareel.product.dto.CreateProductRequest;
import com.nxtweb.supareel.product.dto.CreateProductResponse;
import com.nxtweb.supareel.product.dto.ProductByIdResponse;
import com.nxtweb.supareel.common.PageResponse;
import com.nxtweb.supareel.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(name = "products")
@RequiredArgsConstructor
@Tag(name = "Book")
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.create(request, connectedUser));
    }

    @GetMapping("{product-id}")
    public ResponseEntity<ProductByIdResponse> findProductById(
            @PathVariable("product-id") UUID productId
    ) {
        return ResponseEntity.ok(service.findById(productId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductByIdResponse>> findAllProducts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllProducts(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<ProductByIdResponse>> findAllProductsByOwner(
        @RequestParam(name = "page", defaultValue = "0", required = false) int page,
        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
        Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllProductsByOwner(page, size, connectedUser));
    }


}
