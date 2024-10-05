package com.nxtweb.supareel.payment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tab_payment_gateway")
@EntityListeners(AuditingEntityListener.class)
public class PaymentGateway {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentGatewayId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider providerName;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum Provider {
        STRIPE, PAYPAL
    }

    public enum Currency {
        USD, EUR
    }

    public enum Status {
        SUCCESSFUL, FAILED
    }
}
