package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.ReviewRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.ReviewListResponse;
import com.vuongdev.Storeclothes.dto.response.ReviewResponse;
import com.vuongdev.Storeclothes.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping
    ApiResponse<ReviewResponse> createReview(@RequestBody ReviewRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(request))
                .build();
    }


    @GetMapping("/all")
    ApiResponse<ReviewListResponse> getAllReviewProduct(
            @RequestParam(defaultValue = "", name = "product_id") Long productId,
            @RequestParam(defaultValue = "", name = "order_id") Long orderId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ){
        PageRequest pageRequest =  PageRequest.of(
                page,
                limit
        );
        Page<ReviewResponse> reviewResponsePage = reviewService.getReviewByProductIdAndOrderId(productId,orderId, pageRequest);
        List<ReviewResponse> reviewResponseList = reviewResponsePage.getContent();
        int totalPage = reviewResponsePage.getNumber();
        return ApiResponse.<ReviewListResponse>builder()
                .result(ReviewListResponse.builder()
                        .reviews(reviewResponseList)
                        .page(totalPage)
                        .build())
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ReviewResponse>> getReviewsByUser(@PathVariable Long userId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getReviewsByUser(userId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ReviewResponse> getReviewByID(@PathVariable Long id){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.getReviewById(id))
                .build();
    }

    @GetMapping("/one")
    ApiResponse<ReviewResponse> getReviewByProductVariantIdAndOrderId(
            @RequestParam(defaultValue = "", name = "order_detail_id") Long orderDetailId) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.getReviewByOrderDetailId(orderDetailId))
                .build();
    }


    @GetMapping("/product/{productId}/summary")
    public ApiResponse<Map<String, Object>> getRatingSummary(@PathVariable Long productId) {
        return ApiResponse.<Map<String, Object>>builder()
                .code(1000)
                .result(reviewService.getRatingSummary(productId))
                .build();
    }



    @PutMapping("/{id}")
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .message("Review updated successfully")
                .result(reviewService.updateReview(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ApiResponse.<String>builder()
                .result("Review deleted successfully")
                .build();
    }
}
