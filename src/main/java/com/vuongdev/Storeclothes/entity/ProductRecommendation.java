package com.vuongdev.Storeclothes.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductRecommendation {
    private Long id;
    private String name;
    private String price;
    private String description;
    private String imageUrl;
    private String category;
}