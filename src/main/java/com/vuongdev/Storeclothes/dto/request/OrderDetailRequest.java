package com.vuongdev.Storeclothes.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    @JsonProperty("order_id")
    Long orderId;

    @JsonProperty("product_variant_id")
    Long productVariantId;

    Integer quantity;

}
