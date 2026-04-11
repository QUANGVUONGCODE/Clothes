package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.PaymentRequest;
import com.vuongdev.Storeclothes.dto.response.PaymentResponse;
import com.vuongdev.Storeclothes.entity.Payment;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.PaymentMapper;
import com.vuongdev.Storeclothes.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;



    public PaymentResponse createPayment(PaymentRequest paymentRequest){
        if(paymentRepository.existsByName(paymentRequest.getName())){
            throw new AppException(ErrorCode.PAYMENT_EXISTS);
        }
        Payment payment = paymentMapper.mapToPayment(paymentRequest);
        return paymentMapper.mapToPaymentResponse(paymentRepository.save(payment));
    }

    public List<PaymentResponse> getAllPayments(){
        return paymentRepository.findAll().stream().map(paymentMapper::mapToPaymentResponse).toList();
    }

    public PaymentResponse getPaymentById(Long id){
        return paymentMapper.mapToPaymentResponse(paymentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_PAYMENT_ID)
        ));
    }

    public PaymentResponse updatePayment(Long id, PaymentRequest paymentRequest){
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_PAYMENT_ID)
        );
        paymentMapper.updatePayment(payment, paymentRequest);
        return paymentMapper.mapToPaymentResponse(paymentRepository.save(payment));
    }

    public void deletePayment(Long id){
        if(!paymentRepository.existsById(id)){
            throw new AppException(ErrorCode.INVALID_PAYMENT_ID);
        }
        paymentRepository.deleteById(id);
    }
}
