package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.ProductVariantRequest;
import com.vuongdev.Storeclothes.dto.request.ProductVariantUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.ProductVariantListResponse;
import com.vuongdev.Storeclothes.dto.response.ProductVariantResponse;
import com.vuongdev.Storeclothes.entity.ProductVariant;
import com.vuongdev.Storeclothes.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/product-variants")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantController {
    ProductVariantService productVariantService;

    @PostMapping
    ApiResponse<ProductVariantResponse> createProductVariant(@RequestBody ProductVariantRequest request){
        return ApiResponse.<ProductVariantResponse>builder()
                .result(productVariantService.createProductVariant(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductVariantResponse>> getAllProductVariant(){
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .result(productVariantService.getAllProductVariant())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ProductVariantResponse> getProductVariantById(@PathVariable Long id){
        return ApiResponse.<ProductVariantResponse>builder()
                .result(productVariantService.getProductVariantById(id))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<ProductVariantResponse>> getAllProductVariantByProductId(@PathVariable Long productId){
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .result(productVariantService.getAllProductVariantByProductId(productId))
                .build();
    }


    @PutMapping("/{id}")
    ApiResponse<ProductVariantResponse> updateProductVariant(@PathVariable Long id, @RequestBody ProductVariantUpdateRequest request){
        return ApiResponse.<ProductVariantResponse>builder()
                .result(productVariantService.updateProductVariant(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    ApiResponse<String> deleteProductVariant(@PathVariable Long id){
        productVariantService.deleteProductVariant(id);
        return ApiResponse.<String>builder()
                .result("Product variant deleted successfully")
                .build();
    }

    @GetMapping("/search")
    ApiResponse<ProductVariantListResponse> getAllFoods(
            @RequestParam(defaultValue = "", name = "product_id") Long productId,
            @RequestParam(defaultValue = "", name = "color_id") Long colorId,
            @RequestParam(defaultValue = "", name = "size_id") Long sizeId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("id").ascending()
        );
        Page<ProductVariantResponse> productVariantPage = productVariantService.searchProductVariants(pageRequest, productId, colorId, sizeId);
        List<ProductVariantResponse> productVariantResponses = productVariantPage.getContent();
        int totalPages = productVariantPage.getNumber();
        return ApiResponse.<ProductVariantListResponse>builder()
                .result(ProductVariantListResponse.builder()
                        .productVariantResponseList(productVariantResponses)
                        .page(totalPages)
                        .build())
                .build();
    }


    @GetMapping("/by-ids")
    ApiResponse<?> findProductVariats(@RequestParam String ids){
        try {
            List<Long> productVariantIds = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
            return ApiResponse.<List<ProductVariant>>builder()
                    .result(productVariantService.getProductVariantsByIds(productVariantIds))
                    .build();
        }catch (Exception e){
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }
    }
}
