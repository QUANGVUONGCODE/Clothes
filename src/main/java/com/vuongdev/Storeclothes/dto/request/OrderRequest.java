package com.vuongdev.Storeclothes.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @JsonProperty("user_id")
    @NotNull(message = "USER_ID_REQUIRED")
    Long userId;

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("email")
    String email;

    @JsonProperty("address")
    String address;

    @JsonProperty("phone_number")
    @Size(min = 10, max = 10, message = "PHONE_NUMBER_LENGTH")
    String phoneNumber;

    String note;

    @JsonProperty("order_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime orderDate;

    @JsonProperty("cart_items")
    List<CartRequest> cartItems;

    @JsonProperty("payment_id")
    Long paymentId;

    @JsonProperty("vnp_txn_ref")
    String vnpTxnRef;
}
