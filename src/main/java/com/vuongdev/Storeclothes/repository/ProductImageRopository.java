package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductImageRopository extends JpaRepository<ProductImage, Long> {
    List<ProductImage>  findByProductIdAndColorId(Long productId, Long colorId);

    Optional<ProductImage> findByImageUrl(String imageUrl);

    @Query("SELECT p FROM ProductImage p WHERE p.embedding IS NOT NULL")
    List<ProductImage> findAllByEmbeddingIsNotNull();
}
