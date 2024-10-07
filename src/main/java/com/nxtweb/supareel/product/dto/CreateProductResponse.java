package com.nxtweb.supareel.product.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductResponse {
        private UUID id;
        private String title;
        private String message;
}
