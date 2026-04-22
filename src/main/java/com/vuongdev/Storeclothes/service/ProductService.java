package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ProductRequest;
import com.vuongdev.Storeclothes.dto.request.ProductUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ProductResponse;

import com.vuongdev.Storeclothes.entity.Product;
import com.vuongdev.Storeclothes.entity.SubCategory;
import com.vuongdev.Storeclothes.enums.ProductStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.ProductMapper;
import com.vuongdev.Storeclothes.repository.ProductRepository;
import com.vuongdev.Storeclothes.repository.SubCategoryRepository;
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
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    SubCategoryRepository subCategoryRepository;

    public ProductResponse createProduct(ProductRequest request){
        if(productRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.PRODUCT_EXISTS);
        }

        SubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_SUB_CATEGORY)
        );
        validateProductRequest(request);

        Product product = productMapper.mapToProduct(request);
        product.setSubCategory(subCategory);
        product.setDiscountPercent(normalizeDiscountPercent(request.getDiscountPercent()));
        product.setSalePrice(calculateSalePrice(request.getPrice(), product.getDiscountPercent()));
        product.setStatus(ProductStatus.ACTIVE.name());
        productRepository.save(product);
        return productMapper.mapToProductResponse(product);
    }

    public ProductResponse getProductByid(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );
        return productMapper.mapToProductResponse(product);
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream().map(productMapper::mapToProductResponse).toList();
    }





    public List<ProductResponse> getAllProductsBySubCategoryId(Long subCategoryId){
        return productRepository.findAllActiveBySubCategoryId(subCategoryId)
                .stream()
                .map(productMapper::mapToProductResponse)
                .toList();
    }

    public Page<ProductResponse> searchProductsBySubCategoryId(Long subCategoryId, String keyword, Pageable pageable) {
        return productRepository.searchProductsBySubCategoryId(subCategoryId, keyword, pageable)
                .map(productMapper::mapToProductResponse);
    }


    public Page<ProductResponse> searchProductsBySubCategoryId(String keyword, Pageable pageable) {
        return productRepository.searchActiveProducts(keyword, pageable).map(productMapper::mapToProductResponse);
    }

    public Page<ProductResponse> searchProductsByCategoryId(Long categoryId, String keyword, Pageable pageable){
        return productRepository.searchProductsByCategoryId(categoryId, keyword, pageable)
                .map(productMapper::mapToProductResponse);
    }


    public Page<ProductResponse> searchProductsByDepartmentId(Long departmentId, String keyword, Pageable pageable) {
        return productRepository.searchProductsByDepartmentId(departmentId, keyword, pageable)
                .map(productMapper::mapToProductResponse);
    }



    public ProductResponse getProductsById(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );
        return productMapper.mapToProductResponse(product);
    }

    public ProductResponse updateProductById(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        if(request.getPrice() != null){
            if(request.getPrice().compareTo(BigDecimal.ZERO) <= 0){
                throw new AppException(ErrorCode.INVALID_PRICE);
            }
            product.setPrice(request.getPrice());
        }

        if (request.getDiscountPercent() != null) {
            if (request.getDiscountPercent() < 0 || request.getDiscountPercent() > 100) {
                throw new AppException(ErrorCode.INVALID_DISCOUNT_PERCENT);
            }
            product.setDiscountPercent(request.getDiscountPercent());
        }


        productMapper.updateProductFromRequest(request, product);

        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }

        product.setSalePrice(
                calculateSalePrice(
                        product.getPrice(),
                        normalizeDiscountPercent(product.getDiscountPercent())
                )
        );

        return productMapper.mapToProductResponse(productRepository.save(product));

    }


    public void updateProductThumbnail(Long id, String thumbnail){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );
        product.setThumbnail(thumbnail);
        productRepository.save(product);
    }


    public void deletedProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );
        product.setStatus(ProductStatus.INACTIVE.name());
        productRepository.save(product);
    }


    private void validateProductRequest(ProductRequest request) {
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INVALID_PRICE);
        }

        Integer discountPercent = normalizeDiscountPercent(request.getDiscountPercent());
        if (discountPercent < 0 || discountPercent > 100) {
            throw new AppException(ErrorCode.INVALID_DISCOUNT_PERCENT);
        }
    }

    private Integer normalizeDiscountPercent(Integer discountPercent) {
        return discountPercent == null ? 0 : discountPercent;
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


    public Long countProducts() {
        return productRepository.count();
    }

}
