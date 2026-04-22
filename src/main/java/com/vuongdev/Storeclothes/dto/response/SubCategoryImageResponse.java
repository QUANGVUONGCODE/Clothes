package com.vuongdev.Storeclothes.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vuongdev.Storeclothes.entity.SubCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryImageResponse {
    private Long id;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("sub_category")
    SubCategory subCategory;
}
