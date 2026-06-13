package com.vuongdev.Storeclothes.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseObject {
    @JsonProperty("message")
    String message;

    @JsonProperty("status")
    HttpStatus status;

    @JsonProperty("data")
    Object data;
}
