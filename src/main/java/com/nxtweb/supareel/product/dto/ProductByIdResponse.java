package com.nxtweb.supareel.product.dto;

import com.nxtweb.supareel.order.Order;
import com.nxtweb.supareel.product.Attachment;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductByIdResponse {
        private UUID id;
        private String title;
        private String description;
        private BigDecimal price;
        private Currency currency;
        private String userId;
        private Set<Attachment> files;
        private Set<Order> orders;
}
