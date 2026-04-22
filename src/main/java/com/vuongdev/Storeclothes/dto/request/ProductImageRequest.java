package com.vuongdev.Storeclothes.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.logging.Level;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageRequest {

    @JsonProperty("product_id")
    Long productId;

    @JsonProperty("color_id")
    Long colorId;

    @JsonProperty(value = "image_url")
    String imageUrl;

    @JsonProperty(value = "is_main")
    Boolean isMain;

    @JsonProperty(value = "embedding")
    String embedding;
}
