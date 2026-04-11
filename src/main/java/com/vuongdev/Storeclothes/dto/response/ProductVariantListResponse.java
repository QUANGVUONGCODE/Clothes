package com.vuongdev.Storeclothes.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantListResponse {
    List<ProductVariantResponse> productVariantResponseList;
    int page;
}
