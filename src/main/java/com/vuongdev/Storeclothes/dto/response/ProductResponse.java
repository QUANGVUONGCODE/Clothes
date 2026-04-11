package com.vuongdev.Storeclothes.dto.response;

import com.vuongdev.Storeclothes.entity.SubCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    SubCategory subCategory;
    String name;
    String slug;
    String description;
    String shortDescription;
    BigDecimal price;
    Integer discountPercent;
    BigDecimal salePrice;
    String thumbnail;
    String status;
    String material;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
