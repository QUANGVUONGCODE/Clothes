package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ProductImageRequest;
import com.vuongdev.Storeclothes.dto.request.ProductVariantRequest;
import com.vuongdev.Storeclothes.dto.request.ProductVariantUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ProductVariantResponse;
import com.vuongdev.Storeclothes.entity.*;
import com.vuongdev.Storeclothes.enums.ProductVariantStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.ProductVariantMapper;
import com.vuongdev.Storeclothes.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantService {
    ProductVariantRepository productVariantRepository;
    ProductVariantMapper productVariantMapper;
    ProductRepository productRepository;
    ColorRepository colorRepository;
    SizeRepository sizeRepository;
    ProductImageRopository productImageRepository;


    public ProductVariantResponse createProductVariant(ProductVariantRequest request){
        if(productVariantRepository.existsByProduct_IdAndColor_IdAndSize_Id(
                request.getProductId(),
                request.getColorId(),
                request.getSizeId())){
            throw new AppException(ErrorCode.PRODUCT_VARIANT_EXISTS);}

        if(productVariantRepository.existsBySku(request.getSku())){
            throw new AppException(ErrorCode.SKU_EXISTS);
        }

        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        Color color = colorRepository.findById(request.getColorId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_COLOR)
        );

        Size size = sizeRepository.findById(request.getSizeId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_SIZE)
        );

        ProductVariant productVariant = productVariantMapper.mapToProductVariant(request);
        productVariant.setProduct(product);
        productVariant.setColor(color);
        productVariant.setSize(size);
        productVariant.setStatus(ProductVariantStatus.ACTIVE.name());
        return productVariantMapper.mapToProductVariantResponse(productVariantRepository.save(productVariant));
    }

    public List<ProductVariantResponse> getAllProductVariant(){
        return productVariantRepository.findAll()
                .stream()
                .map(productVariantMapper::mapToProductVariantResponse)
                .toList();

    }

    public ProductVariantResponse getProductVariantById(Long id){
        return productVariantRepository.findById(id)
                .map(productVariantMapper::mapToProductVariantResponse)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT));
    }

    public List<ProductVariantResponse> getAllProductVariantByProductId(Long productId){
        return productVariantRepository.findByProduct_Id(productId)
                .stream()
                .map(productVariantMapper::mapToProductVariantResponse)
                .toList();
    }


    public ProductVariantResponse updateProductVariant(Long id, ProductVariantUpdateRequest request){
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT)
        );

        if(request.getSku() != null && !request.getSku().equals((productVariant.getSku()))){
            if(productVariantRepository.existsBySkuAndIdNot(request.getSku(), id)){
                throw new AppException(ErrorCode.SKU_EXISTS);
            }
            productVariant.setSku(request.getSku());
        }


        if (request.getStockQuantity() != null) {
            if (request.getStockQuantity() < 0) {
                throw new AppException(ErrorCode.INVALID_STOCK_QUANTITY);
            }
            productVariant.setStockQuantity(request.getStockQuantity());
        }

        if (request.getStatus() != null) {
            productVariant.setStatus(request.getStatus());
        }


        return productVariantMapper.mapToProductVariantResponse(
                productVariantRepository.save(productVariant)
        );
    }


    public Page<ProductVariantResponse> searchProductVariants(Pageable pageable,
                                                              Long productId,
                                                              Long colorId,
                                                              Long sizeId) {
        return productVariantRepository.searchProductVariants(pageable, productId, colorId, sizeId)
                .map(productVariantMapper::mapToProductVariantResponse);
    }



    public List<ProductVariant> getProductVariantsByIds(List<Long> productVariantIds){
        return productVariantRepository.findByIdIn(productVariantIds);
    }

    public void deleteProductVariant(Long id){
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT)
        );
        productVariantRepository.delete(productVariant);
    }





    public ProductImage createProductImage(Long productId, Long colorId, ProductImageRequest request){
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        Color color = colorRepository.findById(colorId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_COLOR)
        );

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .color(color)
                .embedding(request.getEmbedding())
                .imageUrl(request.getImageUrl())
                .build();

        int size = productImageRepository.findByProductIdAndColorId(productId, colorId).size();

        if (size >= ProductImage.MAX_IMAGE_COUNT) {
            throw new AppException(ErrorCode.MAXIMUM_IMAGE_COUNT_EXCEEDED);
        }
        return productImageRepository.save(productImage);
    }


    private BigDecimal calculateSalePrice(BigDecimal price, Integer discountPercent) {
        if (discountPercent == null || discountPercent <= 0) {
            return price;
        }

        return price.subtract(
                price.multiply(BigDecimal.valueOf(discountPercent))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        );
    }

    private Integer normalizeDiscountPercent(Integer discountPercent) {
        return discountPercent == null ? 0 : discountPercent;
    }
}
