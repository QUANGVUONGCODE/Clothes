package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.entity.ProductImage;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.service.ProductImageService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/product-images")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageController {
    ProductImageService productImageService;

    @NonFinal
    @Value("${app.upload-dir}")
    String uploadDir;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadProductImages(
            @RequestParam("productId") Long productId,
            @RequestParam("colorId") Long colorId,
            @RequestPart("files") List<MultipartFile> files
    ) throws IOException {
        try{
            return ApiResponse.<List<ProductImage>>builder()
                    .result(productImageService.uploadProductImage(files, productId, colorId))
                    .build();
        }catch (AppException e){
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }
    }

    @GetMapping
    public ApiResponse<List<ProductImage>> getImagesByProductAndColor(
            @RequestParam("productId") Long productId,
            @RequestParam("colorId") Long colorId
    ) {
        return ApiResponse.<List<ProductImage>>builder()
                .result(productImageService.getImagesByProductIdAndColorId(productId, colorId))
                .build();
    }

    @DeleteMapping
    public ApiResponse<String> deleteeImagesByProductAndColor(
            @RequestParam("productId") Long productId,
            @RequestParam("colorId") Long colorId
    ){
        productImageService.deleteProductImage(productId, colorId);
        return ApiResponse.<String>builder()
                .result("Delete successfully image")
                .build();
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename){
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if(!resource.exists() || !resource.isReadable()){
                throw new AppException(ErrorCode.INVALID_IMAGE);
            }
            String contentType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }catch (Exception e){
            throw new AppException(ErrorCode.INVALID_IMAGE);
        }
    }

}
