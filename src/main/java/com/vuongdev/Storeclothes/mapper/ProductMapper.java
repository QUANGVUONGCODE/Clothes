package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.ProductRequest;
import com.vuongdev.Storeclothes.dto.request.ProductUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ProductResponse;
import com.vuongdev.Storeclothes.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToProduct(ProductRequest request);
    ProductResponse mapToProductResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromRequest(ProductUpdateRequest request, @MappingTarget Product product);
}
