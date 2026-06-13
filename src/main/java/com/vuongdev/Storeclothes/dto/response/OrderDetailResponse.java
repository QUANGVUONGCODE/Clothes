package com.vuongdev.Storeclothes.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vuongdev.Storeclothes.entity.Order;
import com.vuongdev.Storeclothes.entity.ProductVariant;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OrderDetailResponse {
    Long id;

    @JsonProperty("order")
    Order order;

    @JsonProperty("product_variant")
    ProductVariant productVariant;


    BigDecimal price;

    Integer quantity;

    Boolean active;

    @JsonProperty("total_money")
    BigDecimal totalMoney;
}
