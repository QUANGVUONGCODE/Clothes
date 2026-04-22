package com.vuongdev.Storeclothes.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vuongdev.Storeclothes.entity.Payment;
import com.vuongdev.Storeclothes.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;

    @JsonProperty("order_code")
    String orderCode;

    @JsonProperty("user")
    User user;

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("email")
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("address")
    String address;

    @JsonProperty("note")
    String note;

    @JsonProperty("order_date")
    LocalDateTime orderDate;

    @JsonProperty("status")
    String status;

    @JsonProperty("total_money")
    Float totalMoney;

    @JsonProperty("payment")
    Payment payment;

    @JsonProperty("active")
    Boolean active;

}
