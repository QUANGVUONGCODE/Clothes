package com.vuongdev.Storeclothes.dto.response;

import com.vuongdev.Storeclothes.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubCategoryResponse {
    Long id;
    String name;
    Category category;
    String thumbnail;
}
