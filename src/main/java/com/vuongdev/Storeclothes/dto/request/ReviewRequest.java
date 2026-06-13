package com.vuongdev.Storeclothes.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {

    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("product_id")
    Long productId;

    @JsonProperty("order_id")
    Long orderId;

    @JsonProperty("product_variant_id")
    Long productVariantId;

    @JsonProperty("orderDetail_id")
    Long orderDetailId;

    @JsonProperty("rating")
    @Min(1) @Max(5)
    Integer rating;

    @JsonProperty("comment")
    String comment;

    @JsonProperty("tags")
    List<String> tags;
}
