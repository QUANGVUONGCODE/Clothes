package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.ProductImageRequest;
import com.vuongdev.Storeclothes.dto.request.SubCategoryImageRequest;
import com.vuongdev.Storeclothes.entity.*;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.repository.*;
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
public class SubCategoryImageService {

    SubCategoryRepository subCategoryRepository;
    ProductImageService productImageService;
    SubCategoryService subCategoryService;
    @NonFinal
    @Value("${app.upload-dir}")
    String uploadRoot;

    public SubCategoryImage uploadSubCategoryImage(Long subCategoryId, MultipartFile files) throws IOException {

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_SUB_CATEGORY));

        if (files == null || files.getSize() == 0) {
            throw new AppException(ErrorCode.INVALID_IMAGE);
        }

        if (files.getSize() > 1024 * 1024 * 5) {
            throw new AppException(ErrorCode.INVALID_SIZE_IMAGE);
        }

        String contentType = files.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.INVALID_IMAGE_URL);
        }

        String fileName = productImageService.storeFile(files);

        SubCategoryImage subCategoryImage = subCategoryService.createSubCategoryImage(
                subCategoryId,
                SubCategoryImageRequest.builder()
                        .imageUrl(fileName)
                        .build()
                );

        subCategoryService.updatethumbnailSubCategory(subCategoryId, fileName);
        return subCategoryImage;
    }

}
