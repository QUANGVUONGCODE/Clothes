package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.ChatRequest;
import com.vuongdev.Storeclothes.dto.response.ChatResponse;
import com.vuongdev.Storeclothes.service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${api.prefix}/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    public Mono<ResponseEntity<ChatResponse>> chat(@Valid @RequestBody ChatRequest request) {
        return chatbotService.processMessage(request)
                .map(ResponseEntity::ok);
    }
}