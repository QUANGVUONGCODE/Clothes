package com.vuongdev.Storeclothes.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vuongdev.Storeclothes.enums.OrderStatus;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("full_name")
    String fullName;

    String email;

    @JsonProperty("phone_number")
    @Size(min = 10, max = 10, message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    String note;

    @JsonProperty("status")
    OrderStatus status;

    @JsonProperty("payment_id")
    Long paymentId;

    @JsonProperty("cart_items")
    List<CartRequest> cartItems;

}
