package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.entity.Invoice;
import com.vuongdev.Storeclothes.entity.Order;
import com.vuongdev.Storeclothes.repository.InvoiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;
    @Transactional
    public Invoice createInvoiceFromOrder(Order order) {

        return invoiceRepository.findByOrder_Id(order.getId())
                .orElseGet(() -> {
                    Invoice invoice = Invoice.builder()
                            .invoiceCode(generateInvoiceCode(order))
                            .order(order)
                            .orderCode(order.getOrderCode())
                            .customerName(order.getFullName())
                            .customerEmail(order.getEmail())
                            .customerPhone(order.getPhoneNumber())
                            .shippingAddress(order.getAddress())
                            .totalMoney(order.getTotalMoney())
                            .paymentMethod(getPaymentMethod(order))
                            .paymentStatus(getPaymentStatus(order))
                            .status("ISSUED")
                            .build();

                    return invoiceRepository.save(invoice);
                });
    }

    private String generateInvoiceCode(Order order) {
        return "INV-" + LocalDate.now().toString().replace("-", "") + "-" + order.getId();
    }

    private String getPaymentMethod(Order order) {
        if (order.getPayment() == null) {
            return "COD";
        }

        // Tạm thời để ONLINE.
        // Nếu Payment entity của bạn có field name/method thì sửa ở đây.
        return "ONLINE";
    }

    private String getPaymentStatus(Order order) {
        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            return "PAID";
        }

        if ("COD_PENDING".equalsIgnoreCase(order.getStatus())) {
            return "UNPAID";
        }

        return order.getStatus();
    }
}
