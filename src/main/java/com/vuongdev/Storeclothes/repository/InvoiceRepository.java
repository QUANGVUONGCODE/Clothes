package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrder_Id(Long orderId);

    Optional<Invoice> findByOrderCode(String orderCode);
    boolean existsByOrder_Id(Long orderId);
}
