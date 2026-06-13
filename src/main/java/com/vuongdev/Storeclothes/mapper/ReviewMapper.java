package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.ReviewRequest;
import com.vuongdev.Storeclothes.dto.response.ReviewResponse;
import com.vuongdev.Storeclothes.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review mapToReview(ReviewRequest request);

    ReviewResponse mapToReviewResponse(Review review);

    void updateReview(@MappingTarget Review review, ReviewRequest request);
}
