package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    boolean existsBySku(String sku);
    boolean existsByProduct_IdAndColor_IdAndSize_Id(Long productId, Long colorId, Long sizeId);

    List<ProductVariant> findByProduct_Id(Long productId);

    boolean existsBySkuAndIdNot(String sku, Long id);

    Optional<ProductVariant> findByProductIdAndColorId(Long productId, Long colorId);


    @Query(value = """
            select pv
            from ProductVariant pv
            where (:productId is null or :productId = 0 or pv.product.id = :productId)
              and (:colorId is null or :colorId = 0 or pv.color.id = :colorId)
              and (:sizeId is null or :sizeId = 0 or pv.size.id = :sizeId)
            """,
            countQuery = """
            select count(pv.id)
            from ProductVariant pv
            where (:productId is null or :productId = 0 or pv.product.id = :productId)
              and (:colorId is null or :colorId = 0 or pv.color.id = :colorId)
              and (:sizeId is null or :sizeId = 0 or pv.size.id = :sizeId)
            """)
    Page<ProductVariant> searchProductVariants(Pageable pageable,
                                               @Param("productId") Long productId,
                                               @Param("colorId") Long colorId,
                                               @Param("sizeId") Long sizeId);


    List<ProductVariant> findByIdIn(List<Long> productVariantIds);
}

