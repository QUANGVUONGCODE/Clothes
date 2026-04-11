package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.ProductRequest;
import com.vuongdev.Storeclothes.dto.request.ProductUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.ProductResponse;
import com.vuongdev.Storeclothes.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductResponse>> getAllProducts(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{subCategoryId}")
    ApiResponse<List<ProductResponse>> getAllProductsBySubCategoryId(@PathVariable Long subCategoryId){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProductsBySubCategoryId(subCategoryId))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ProductResponse> updateProductById(@PathVariable Long id, @RequestBody ProductUpdateRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProductById(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deletedProduct(@PathVariable Long id){
        productService.deletedProduct(id);
        return ApiResponse.<String>builder()
                .result("Product deleted successfully")
                .build();
    }

}
