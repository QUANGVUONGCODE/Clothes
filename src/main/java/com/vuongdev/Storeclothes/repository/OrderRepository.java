package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Order;
import com.vuongdev.Storeclothes.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long id);

    @Query("SELECT o FROM Order o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "o.fullName LIKE %:keyword% OR o.phoneNumber LIKE %:keyword% OR o.status LIKE %:keyword% OR o.orderCode LIKE %:keyword%)")
    Page<Order> findByKeyword(@Param("keyword") String keyword, Pageable pageable);




    boolean existsByIdAndUser_Id(Long orderId, Long userId);

    boolean existsById(Long orderId);

    List<Order> findByStatusIn(List<String> status);

    List<Order> findByOrderDateBetween(LocalDateTime startOfToday, LocalDateTime endOfToday);

    Optional<Order> findByOrderCode(String orderCode);
    Optional<Order> findByVnpTxnRef(String txnRef);

    long countByStatus(String status);

    List<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Order> findByStatusAndCreatedAtBetweenOrderByCreatedAtAsc(
            String status,
            LocalDateTime from,
            LocalDateTime to
    );

    @Query("""
        SELECT COALESCE(SUM(o.totalMoney), 0)
        FROM Order o
        WHERE o.status = :status
    """)
    BigDecimal sumTotalMoneyByStatus(@Param("status") String status);

    @Query("""
        SELECT COALESCE(SUM(o.totalMoney), 0)
        FROM Order o
        WHERE o.status = :status
          AND o.createdAt >= :from
          AND o.createdAt < :to
    """)
    BigDecimal sumTotalMoneyByStatusAndCreatedAtBetween(
            @Param("status") String status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Optional<Order> findByOrderCodeAndPhoneNumber(String orderCode, String phoneNumber);
}

