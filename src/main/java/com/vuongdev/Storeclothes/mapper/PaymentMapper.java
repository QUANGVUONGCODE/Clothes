package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.PaymentRequest;
import com.vuongdev.Storeclothes.dto.response.PaymentResponse;
import com.vuongdev.Storeclothes.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment mapToPayment(PaymentRequest request);
    PaymentResponse mapToPaymentResponse(Payment payment);
    void updatePayment(Payment payment, @MappingTarget PaymentRequest request);
}
