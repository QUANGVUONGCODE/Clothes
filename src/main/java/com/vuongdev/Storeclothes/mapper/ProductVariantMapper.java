package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.ProductVariantRequest;
import com.vuongdev.Storeclothes.dto.request.ProductVariantUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ProductVariantResponse;
import com.vuongdev.Storeclothes.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariant mapToProductVariant(ProductVariantRequest request);
    ProductVariantResponse mapToProductVariantResponse(ProductVariant productVariant);
    void updateProductVariantFromRequest(ProductVariantUpdateRequest request, @MappingTarget ProductVariant productVariant);
}
