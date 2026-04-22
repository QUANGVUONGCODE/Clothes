package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.ProductRequest;
import com.vuongdev.Storeclothes.dto.request.ProductUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.ProductListResponse;
import com.vuongdev.Storeclothes.dto.response.ProductResponse;
import com.vuongdev.Storeclothes.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/count")
    ApiResponse<Long> countProduct(){
        return ApiResponse.<Long>builder()
                .result(productService.countProducts())
                .build();
    }

    @GetMapping("/news/{id}")
    ApiResponse<ProductResponse> getProductById(@PathVariable Long id){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductByid(id))
                .build();
    }


    @GetMapping("/search")
    ApiResponse<ProductListResponse> getAllProductByKeyWord(
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit
        );
        Page<ProductResponse> productResponsePage = productService.searchProductsBySubCategoryId(keyword, pageRequest);
        List<ProductResponse> productResponses = productResponsePage.getContent();
        int totalPage = productResponsePage.getNumber();
        return ApiResponse.<ProductListResponse>builder()
                .result(ProductListResponse.builder()
                        .productResponseLists(productResponses)
                        .page(totalPage)
                        .build())
                .build();
    }


    @GetMapping("/categoryId")
    ApiResponse<ProductListResponse> getAllProductByCategoryId(
            @RequestParam(defaultValue = "",name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit
        );
        Page<ProductResponse> productResponsePage = productService.searchProductsByCategoryId(categoryId,keyword,pageRequest);
        List<ProductResponse> productResponses = productResponsePage.getContent();
        int totalPage = productResponsePage.getNumber();
        return ApiResponse.<ProductListResponse>builder()
                .result(ProductListResponse.builder()
                        .productResponseLists(productResponses)
                        .page(totalPage)
                        .build())
                .build();
    }

    @GetMapping("/department")
    ApiResponse<ProductListResponse> getAllProductByDepartmentId(
            @RequestParam(defaultValue = "",name = "department_id") Long departmentId,
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit
        );
        Page<ProductResponse> productResponsePage = productService.searchProductsByDepartmentId(departmentId,keyword,pageRequest);
        List<ProductResponse> productResponses = productResponsePage.getContent();
        int totalPage = productResponsePage.getNumber();
        return ApiResponse.<ProductListResponse>builder()
                .result(ProductListResponse.builder()
                        .productResponseLists(productResponses)
                        .page(totalPage)
                        .build())
                .build();
    }


    @GetMapping("/subCategory")
    ApiResponse<ProductListResponse> getAllProductBysubcategoryId(
            @RequestParam(defaultValue = "",name = "subcategory_id") Long subcategoryId,
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit
        );
        Page<ProductResponse> productResponsePage = productService.searchProductsBySubCategoryId(subcategoryId,keyword,pageRequest);
        List<ProductResponse> productResponses = productResponsePage.getContent();
        int totalPage = productResponsePage.getNumber();
        return ApiResponse.<ProductListResponse>builder()
                .result(ProductListResponse.builder()
                        .productResponseLists(productResponses)
                        .page(totalPage)
                        .build())
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
