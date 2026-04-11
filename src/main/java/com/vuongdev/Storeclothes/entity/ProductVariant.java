package com.vuongdev.Storeclothes.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Table(name = "product_variants")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    Color color;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    Size size;

    @Column(name = "sku")
    String sku;

    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity;

    @Column(name = "status", length = 20)
    String status;
}
