package com.vuongdev.Storeclothes.dto.response;

import com.vuongdev.Storeclothes.entity.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InvoiceResponse {
    Long id;

    Long orderId;

    String invoiceCode;
    String orderCode;

    String customerName;
    String customerEmail;
    String customerPhone;
    String shippingAddress;

    BigDecimal totalMoney;

    String paymentMethod;
    String paymentStatus;
    String status;

    LocalDateTime issuedAt;
}
