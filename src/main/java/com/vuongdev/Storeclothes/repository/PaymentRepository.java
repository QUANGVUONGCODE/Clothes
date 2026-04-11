package com.vuongdev.Storeclothes.repository;


import com.vuongdev.Storeclothes.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByName(String name);
}
