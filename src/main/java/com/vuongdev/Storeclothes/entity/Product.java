package com.vuongdev.Storeclothes.entity;

import com.vuongdev.Storeclothes.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "products")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    SubCategory subCategory;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "slug", nullable = false, unique = true)
    String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "short_description", columnDefinition = "TEXT")
    String shortDescription;

    @Column(name = "price")
    BigDecimal price;

    @Column(name = "discount_percent")
    Integer discountPercent;

    @Column(name = "sale_price")
    BigDecimal salePrice;

    @Column(name = "material")
    String material;

    @Column(name = "thumbnail")
    String thumbnail;

    @Column(name = "status", length = 255)
    String status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
