package com.vuongdev.Storeclothes.repository;


import com.vuongdev.Storeclothes.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);
    List<OrderDetail> findByOrder_Status(String status);
}
