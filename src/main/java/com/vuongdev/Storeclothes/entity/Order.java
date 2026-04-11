package com.vuongdev.Storeclothes.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Table(name = "orders")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true, length = 50)
    String orderCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "full_name", nullable = false, length = 255)
    String fullName;

    @Column(name = "email", length = 100)
    String email;

    @Column(name = "phone_number", nullable = false, length = 10)
    String phoneNumber;

    @Column(name = "address", nullable = false, length = 255)
    String address;

    @Column(name = "order_date", nullable = false)
    LocalDateTime orderDate;

    @Column(name = "note", length = 255)
    String note;

    @Column(name = "total_money", nullable = false, precision = 12, scale = 2)
    BigDecimal totalMoney;

    @Column(name = "status")
    String status;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (this.orderCode == null || this.orderCode.isEmpty()) {
            this.orderCode = generateUniqueOrderCode();
        }
    }

    @PreUpdate
    public  void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateUniqueOrderCode() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = LocalDateTime.now().format(formatter);
        String randomSuffix = String.format("%03d", (int) (Math.random() * 1000000));

        return "ORD-" + timestamp + randomSuffix;
    }

}
