package com.vuongdev.Storeclothes.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    NOT_BLANK_NAME(1000, "Name must not be blank", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTS(1001, "Category already exists", HttpStatus.BAD_REQUEST),
    DEPARTMENT_EXISTS(1002, "Department already exists", HttpStatus.BAD_REQUEST),
    INVALID_ID_DEPARTMENT(1003, "Invalid id department", HttpStatus.BAD_REQUEST),
    INVALID_ID_CATEGORY(1004, "Invalid id category", HttpStatus.BAD_REQUEST),
    SUB_CATEGORY_EXISTS(1005, "Sub category already exists", HttpStatus.BAD_REQUEST),
    INVALID_ID_SUB_CATEGORY(1006, "Invalid id sub category", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTS(1007, "Product already exists", HttpStatus.BAD_REQUEST),
    INVALID_ID_PRODUCT(1008, "Invalid id product", HttpStatus.BAD_REQUEST),
    COLOR_EXISTS(1009, "Color already exists", HttpStatus.BAD_REQUEST),
    INVALID_ID_COLOR(1010, "Invalid id color", HttpStatus.BAD_REQUEST),
    SIZE_EXISTS(1011, "Size already exists", HttpStatus.BAD_REQUEST),
    INVALID_ID_SIZE(1012, "Invalid id size", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_EXISTS(1013, "Product variant already exists", HttpStatus.BAD_REQUEST),
    SKU_EXISTS(1014, "Sku already exists", HttpStatus.BAD_REQUEST),
    INVALID_DISCOUNT_PERCENT(1015, "Invalid discount percent", HttpStatus.BAD_REQUEST),
    INVALID_ID_PRODUCT_VARIANT(1016, "Invalid id product variant", HttpStatus.BAD_REQUEST),
    INVALID_PRICE(1017, "Invalid price", HttpStatus.BAD_REQUEST),
    INVALID_STOCK_QUANTITY(1018, "Invalid stock quantity", HttpStatus.BAD_REQUEST),
    MAXIMUM_IMAGE_COUNT_EXCEEDED(1019, "Maximum image count exceeded", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE(1020, "Invalid image", HttpStatus.BAD_REQUEST),
    INVALID_SIZE_IMAGE(1021, "Invalid size image", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_URL(1022, "Invalid image url", HttpStatus.BAD_REQUEST),
    ROLE_EXISTS(1023, "Role already exists", HttpStatus.BAD_REQUEST),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
