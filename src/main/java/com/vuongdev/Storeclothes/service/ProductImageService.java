package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ProductImageRequest;
import com.vuongdev.Storeclothes.entity.Color;
import com.vuongdev.Storeclothes.entity.Product;
import com.vuongdev.Storeclothes.entity.ProductImage;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.repository.ColorRepository;
import com.vuongdev.Storeclothes.repository.ProductImageRopository;
import com.vuongdev.Storeclothes.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageService {
    ProductImageRopository productImageRopository;
    ProductVariantService productVariantService;
    ProductRepository productRepository;
    ColorRepository colorRepository;
    ProductService productService;

    @NonFinal
    @Value("${app.upload-dir}")
    String uploadRoot;

    public void deleteProductImage(Long productId, Long colorId){
        List<ProductImage> productImageList = productImageRopository.findByProductIdAndColorId(productId, colorId);
        if(!productImageList.isEmpty()){
            productImageRopository.deleteAll(productImageList);
        }
    }


    public List<ProductImage> uploadProductImage(List<MultipartFile> files, Long productId, Long colorId) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        Color color = colorRepository.findById(colorId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_COLOR)
        );

        files = files == null ? new ArrayList<>() : files;

        if(files.size() > ProductImage.MAX_IMAGE_COUNT){
            throw new AppException(ErrorCode.MAXIMUM_IMAGE_COUNT_EXCEEDED);
        }
        if(files.isEmpty() || files.stream().allMatch(file -> file.getSize() == 0)){
            throw new AppException(ErrorCode.INVALID_IMAGE);
        }

        List<ProductImage> productImages = new ArrayList<>();
        for(MultipartFile file : files){
            if(file.getSize() == 0){
                continue;
            }
            if(file.getSize() > 1024*1024*5){
                throw new AppException(ErrorCode.INVALID_SIZE_IMAGE);
            }

            String contenType = file.getContentType();
            if(contenType == null || !contenType.startsWith("image/")){
                throw new AppException(ErrorCode.INVALID_IMAGE_URL);
            }

            String fileName = storeFile(file);
            ProductImage productImage = productVariantService.createProductImage(
                    productId,
                    colorId,
                    ProductImageRequest.builder()
                            .imageUrl(fileName)
                            .build()
            );

            productImages.add(productImage);
            if(productImages.size() == 1){
                productService.updateProductThumbnail(productId, fileName);
            }
        }
        return productImages;
    }


    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) && file.getOriginalFilename() == null){
            throw new AppException(ErrorCode.INVALID_IMAGE_URL);
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get(uploadRoot);
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destinationFile = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public List<ProductImage> getImagesByProductIdAndColorId(Long productId, Long colorId){
        productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        colorRepository.findById(colorId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_COLOR)
        );

        return productImageRopository.findByProductIdAndColorId(productId, colorId);
    }
}
