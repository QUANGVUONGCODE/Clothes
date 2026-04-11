package com.vuongdev.Storeclothes.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;

    @JsonProperty("subcategory_id")
    Long subCategoryId;

    String slug;

    String description;

    String material;

    BigDecimal price;

    @JsonProperty("discount_percent")
    Integer discountPercent;

    @JsonProperty("short_description")
    String shortDescription;


    String thumbnail;

    String status;
}
