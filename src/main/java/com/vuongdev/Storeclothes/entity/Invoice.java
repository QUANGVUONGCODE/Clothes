package com.vuongdev.Storeclothes.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "invoices")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "invoice_code", nullable = false, unique = true, length = 50)
    String invoiceCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    Order order;

    @Column(name = "order_code", nullable = false, length = 50)
    String orderCode;

    @Column(name = "customer_name", nullable = false, length = 255)
    String customerName;

    @Column(name = "customer_email", length = 100)
    String customerEmail;

    @Column(name = "customer_phone", length = 10)
    String customerPhone;

    @Column(name = "shipping_address", nullable = false, length = 255)
    String shippingAddress;

    @Column(name = "total_money")
    BigDecimal totalMoney;

    @Column(name = "payment_method")
    String paymentMethod;

    @Column(name = "payment_status")
    String paymentStatus;

    @Column(name = "status")
    String status;

    @Column(name = "issued_at")
    LocalDateTime issuedAt;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;

        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = "ISSUED";
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
