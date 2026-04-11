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
public class ProductVariantUpdateRequest {
    String sku;
    BigDecimal price;

    @JsonProperty("discount_percent")
    Integer discountPercent;

    @JsonProperty("stock_quantity")
    Integer stockQuantity;


    String status;
}
