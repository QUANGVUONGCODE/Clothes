package com.vuongdev.Storeclothes.dto.response;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private String response;
    private String sessionId;
}
