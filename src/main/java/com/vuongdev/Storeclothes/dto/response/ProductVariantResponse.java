package com.vuongdev.Storeclothes.dto.response;

import com.vuongdev.Storeclothes.entity.Color;
import com.vuongdev.Storeclothes.entity.Product;
import com.vuongdev.Storeclothes.entity.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    Long id;
    Product product;
    Color color;
    Size size;
    String sku;
    Integer stockQuantity;
    String status;
}
