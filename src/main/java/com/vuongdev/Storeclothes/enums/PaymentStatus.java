package com.vuongdev.Storeclothes.enums;

public enum PaymentStatus {
    UNPAID,           // chưa thanh toán
    PENDING_PAYMENT,  // đang chờ thanh toán VNPay
    PAID,             // đã thanh toán
    PAYMENT_FAILED,   // thanh toán thất bại
    REFUNDED          // đã hoàn tiền nếu có
}
