package com.vuongdev.Storeclothes.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    @NotBlank(message = "INVALID_CHAT_REQUEST")
    private String message;

    private String sessionId;   // Nếu không truyền sẽ tự tạo
}
