package com.vuongdev.Storeclothes.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vuongdev.Storeclothes.entity.Departments;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Long id;
    String name;
    Departments departments;
}
