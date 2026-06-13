package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ChatRequest;
import com.vuongdev.Storeclothes.dto.response.ChatResponse;
import com.vuongdev.Storeclothes.entity.Conversation;
import com.vuongdev.Storeclothes.entity.ProductRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final GeminiService geminiService;
    private final ConversationService conversationService;
    private final ProductService productService;

    public Mono<ChatResponse> processMessage(ChatRequest request) {
        Conversation conversation = conversationService.getOrCreateConversation(request.getSessionId());
        conversationService.addMessage(conversation, "user", request.getMessage());

        List<ProductRecommendation> products = safeSearchProducts(request.getMessage());

        String prompt = buildIntelligentPrompt(request.getMessage(), products);

        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", prompt))
        ));

        for (var msg : conversation.getMessages()) {
            String role = msg.getRole().equals("assistant") ? "model" : "user";
            contents.add(Map.of("role", role, "parts", List.of(Map.of("text", msg.getContent()))));
        }

        return geminiService.getChatCompletion(contents)
                .map(aiResponse -> {
                    conversationService.addMessage(conversation, "assistant", aiResponse);
                    return ChatResponse.builder()
                            .response(aiResponse)
                            .sessionId(conversation.getSessionId())
                            .build();
                });
    }

    // ==================== TÌM KIẾM TỔNG QUÁT ====================
    private List<ProductRecommendation> safeSearchProducts(String userMessage) {
        try {
            String keyword = cleanKeyword(userMessage);

            if (keyword.length() < 2) {
                return List.of();
            }

            var pageable = PageRequest.of(0, 6);
            var page = productService.searchProductsBySubCategoryId(keyword, pageable);

            // Nếu không tìm thấy, thử tìm theo tên sản phẩm chung
            if (page.getContent().isEmpty() && keyword.length() > 3) {
                page = productService.searchProductsBySubCategoryId(keyword, pageable);
            }

            return page.getContent().stream()
                    .map(p -> ProductRecommendation.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .price(p.getSalePrice() != null ? p.getSalePrice().toString() :
                                    p.getPrice() != null ? p.getPrice().toString() : "0")
                            .description(p.getDescription())
                            .imageUrl(p.getThumbnail())
                            .category(p.getSubCategory() != null ? p.getSubCategory().getName() : "")
                            .build())
                    .toList();

        } catch (Exception e) {
            System.err.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
            return List.of();
        }
    }

    // Làm sạch từ khóa (loại bỏ từ thừa)
    private String cleanKeyword(String message) {
        return message.toLowerCase()
                .replace("tìm giúp tôi", "")
                .replace("bạn tìm", "")
                .replace("cho tôi", "")
                .replace("tìm", "")
                .replace("gợi ý", "")
                .trim();
    }

    // Prompt thông minh và tổng quát
    private String buildIntelligentPrompt(String userMessage, List<ProductRecommendation> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bạn là trợ lý bán hàng thời trang rất dễ thương, nhiệt tình và am hiểu sản phẩm.\n");
        sb.append("Khách hàng hỏi: \"").append(userMessage).append("\"\n\n");

        if (!products.isEmpty()) {
            sb.append("Tôi đã tìm thấy một số sản phẩm phù hợp:\n");
            for (ProductRecommendation p : products) {
                sb.append("• ").append(p.getName())
                        .append(" - Giá: ").append(p.getPrice()).append("đ\n");
            }
            sb.append("\nHãy giới thiệu sản phẩm phù hợp nhất, tư vấn size/màu/chất liệu và khuyến khích khách mua hàng.");
        } else {
            sb.append("Hiện tại shop chưa tìm thấy sản phẩm nào khớp với yêu cầu. ");
            sb.append("Hãy hỏi thêm thông tin khách hàng (màu sắc, size, giá tiền, phong cách...) để tư vấn chính xác hơn.");
        }

        return sb.toString();
    }
}