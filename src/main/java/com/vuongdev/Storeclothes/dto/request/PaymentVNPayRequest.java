package com.vuongdev.Storeclothes.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVNPayRequest {
    @JsonProperty("amount")
    Long amount;

    @JsonProperty("bankCode")
    String bankCode;

    @JsonProperty("language")
    String language;
}

