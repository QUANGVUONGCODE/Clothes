package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.entity.Conversation;
import com.vuongdev.Storeclothes.entity.Message;
import com.vuongdev.Storeclothes.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Transactional
    public Conversation getOrCreateConversation(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        final String finalSessionId = sessionId;

        return conversationRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Conversation conv = Conversation.builder()
                            .sessionId(finalSessionId)
                            .title("Cuộc trò chuyện mới")
                            .build();
                    return conversationRepository.save(conv);
                });
    }

    @Transactional
    public void addMessage(Conversation conversation, String role, String content) {
        // ====================== SỬA LỖI NULL Ở ĐÂY ======================
        if (conversation.getMessages() == null) {
            conversation.setMessages(new ArrayList<>());
        }

        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();

        conversation.getMessages().add(message);

        // Lưu vào database
        conversationRepository.save(conversation);
    }
}