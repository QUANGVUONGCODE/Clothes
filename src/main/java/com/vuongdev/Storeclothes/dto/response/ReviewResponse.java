package com.vuongdev.Storeclothes.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vuongdev.Storeclothes.entity.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {

    Long id;

    User user;

    Product product;

    ProductVariant productVariant;

    OrderDetail orderDetail;

    Order order;

    Integer rating;

    String comment;

    List<String> tags;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
