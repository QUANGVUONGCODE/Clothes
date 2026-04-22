package com.vuongdev.Storeclothes.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopProductResponse {
    Long productId;
    String productName;
    Long soldQuantity;
}