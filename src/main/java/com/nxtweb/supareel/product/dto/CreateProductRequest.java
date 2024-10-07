package com.nxtweb.supareel.product.dto;

import com.nxtweb.supareel.product.Product;
import jakarta.persistence.Column;
import com.nxtweb.supareel.product.dto.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateProductRequest(
        UUID id,
        @NotNull(message = "100") @NotBlank(message = "100") @NotEmpty(message = "100")
        String title,
        String description,
        @NotNull(message = "101") @NotBlank(message = "101") @NotEmpty(message = "101")
        BigDecimal price,
        @NotNull(message = "102") @NotBlank(message = "102") @NotEmpty(message = "102")
        Currency currency,
        @NotNull(message = "103") @NotBlank(message = "103") @NotEmpty(message = "103")
        UUID user_id
) {
}
