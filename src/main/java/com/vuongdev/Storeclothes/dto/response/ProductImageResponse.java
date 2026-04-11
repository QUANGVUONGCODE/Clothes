package com.vuongdev.Storeclothes.dto.response;

import com.vuongdev.Storeclothes.entity.Color;
import com.vuongdev.Storeclothes.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {
    Long id;
    Product product;
    Color color;
    String imageUrl;
    Boolean isMain;
}
