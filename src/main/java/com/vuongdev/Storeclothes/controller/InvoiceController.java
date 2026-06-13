package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.InvoiceResponse;
import com.vuongdev.Storeclothes.entity.Invoice;
import com.vuongdev.Storeclothes.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/order-code/{orderCode}")
    public ApiResponse<InvoiceResponse> getInvoiceByOrderCode(@PathVariable String orderCode) {
        Invoice invoice = invoiceRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        InvoiceResponse response = InvoiceResponse.builder()
                .id(invoice.getId())
                .orderId(invoice.getOrder().getId())
                .invoiceCode(invoice.getInvoiceCode())
                .orderCode(invoice.getOrderCode())
                .customerName(invoice.getCustomerName())
                .customerEmail(invoice.getCustomerEmail())
                .customerPhone(invoice.getCustomerPhone())
                .shippingAddress(invoice.getShippingAddress())
                .totalMoney(invoice.getTotalMoney())
                .paymentMethod(invoice.getPaymentMethod())
                .paymentStatus(invoice.getPaymentStatus())
                .status(invoice.getStatus())
                .issuedAt(invoice.getIssuedAt())
                .build();

        return ApiResponse.<InvoiceResponse>builder()
                .code(0)
                .message("Lấy hóa đơn thành công")
                .result(response)
                .build();
    }
}
