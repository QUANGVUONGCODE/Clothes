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
public class ProductVariantRequest {
    @JsonProperty("product_id")
    Long productId;

    @JsonProperty("color_id")
    Long colorId;

    @JsonProperty("size_id")
    Long sizeId;

    String sku;

    @JsonProperty("stock_quantity")
    Integer stockQuantity;


}
