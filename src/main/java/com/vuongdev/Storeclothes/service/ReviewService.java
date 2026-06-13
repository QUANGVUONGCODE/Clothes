package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ReviewRequest;
import com.vuongdev.Storeclothes.dto.response.ReviewResponse;
import com.vuongdev.Storeclothes.entity.*;
import com.vuongdev.Storeclothes.enums.OrderStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.ReviewMapper;
import com.vuongdev.Storeclothes.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    UserRepository userRepository;
    ProductRepository productRepository;
    OrderDetailRepository orderDetailRepository;
    ProductVariantRepository productVariantRepository;
    OrderRepository orderRepository;

    public ReviewResponse createReview(ReviewRequest request) {
        User user =  userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_USER_ID)
        );

        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        OrderDetail orderDetail = orderDetailRepository.findById(request.getOrderDetailId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );

        ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT)
        );

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );

        if (!order.getStatus().equals(OrderStatus.COMPLETED.name())) {
            throw new AppException(ErrorCode.ORDER_NOT_COMPLETED);
        }

        if (Boolean.TRUE.equals(orderDetail.getActive())) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        orderDetail.setActive(true);
        orderDetailRepository.save(orderDetail);


        Review review = reviewMapper.mapToReview(request);
        review.setUser(user);
        review.setProduct(product);
        review.setOrder(order);
        review.setOrderDetail(orderDetail);
        review.setProductVariant(productVariant);

        reviewRepository.save(review);
        return reviewMapper.mapToReviewResponse(review);

    }


    public Page<ReviewResponse> getReviewByProductIdAndOrderId(Long productId, Long orderId , Pageable pageable){
        return reviewRepository.findByProductIdAndOrderId(productId,orderId, pageable)
                .map(reviewMapper::mapToReviewResponse);
    }

    public List<ReviewResponse> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::mapToReviewResponse)
                .toList();
    }


    public Map<String, Object> getRatingSummary(Long productId) {
        Double average = reviewRepository.findAverageRatingByProductId(productId);
        List<Object[]> rawDist = reviewRepository.countRatingDistributionByProductId(productId);

        // Khởi tạo map với tất cả rating từ 1-5 = 0
        Map<Integer, Long> distribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) distribution.put(i, 0L);

        for (Object[] row : rawDist) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            distribution.put(rating, count);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("average", Math.round(average * 10.0) / 10.0);
        result.put("distribution", distribution);
        return result;
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        return reviewMapper.mapToReviewResponse(review);
    }

    public ReviewResponse getReviewByOrderDetailId(Long orderDetailId) {
        Review review = reviewRepository.findByOrderDetailId(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        return reviewMapper.mapToReviewResponse(review);
    }



    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));


        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setTags(request.getTags());

        reviewRepository.save(review);
        return reviewMapper.mapToReviewResponse(review);
    }


    public void deleteReview(Long id){
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_REVIEW)
        );
        reviewRepository.delete(review);
    }
}
