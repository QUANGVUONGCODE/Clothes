package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndProductVariantIdAndOrderId(
            Long userId, Long productVariantId, Long orderId);
    @Query("SELECT r FROM Review r WHERE " +
            "(:productId IS NULL OR r.product.id = :productId) AND " +
            "(:orderId IS NULL OR r.order.id = :orderId)")
    Page<Review> findByProductIdAndOrderId(Long productId,Long orderId, Pageable pageable);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByOrderDetailId(Long orderDetailId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.rating ORDER BY r.rating")
    List<Object[]> countRatingDistributionByProductId(@Param("productId") Long productId);
}
