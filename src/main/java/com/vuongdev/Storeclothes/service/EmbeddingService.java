package com.vuongdev.Storeclothes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmbeddingService {
    private final String PYTHON_URL = "http://localhost:8000";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EmbeddingService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Double> getEmbedding(MultipartFile file) throws IOException {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                PYTHON_URL + "/embed", request, Map.class
        );

        // ✅ Cast sang Double thay vì Float
        List<Double> embedding = (List<Double>) response.getBody().get("embedding");
        return embedding;
    }

    public double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        double dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < vec1.size(); i++) {
            dot   += vec1.get(i) * vec2.get(i);
            norm1 += vec1.get(i) * vec1.get(i);
            norm2 += vec2.get(i) * vec2.get(i);
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public String toJson(List<Double> embedding) throws JsonProcessingException {
        return objectMapper.writeValueAsString(embedding);
    }

    // ✅ Parse ra List<Double>
    public List<Double> fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<Double>>() {});
    }
}
