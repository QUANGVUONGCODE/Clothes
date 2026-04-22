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
    PHONE_NUMBER_LENGTH(1024, "Phone number length must be 10", HttpStatus.BAD_REQUEST),
    NOT_BLANK_EMAIL(1025, "Email must not be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1026, "Password invalid", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTS(1027, "Phone number already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1028, "Email already exists", HttpStatus.BAD_REQUEST),
    RETYPE_PASSWORD_WRONG(1029, "Retype password wrong", HttpStatus.BAD_REQUEST),
    INVALID_USER_ID(1030, "Invalid user id", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1031, "Unauthorized", HttpStatus.UNAUTHORIZED),
    PHONE_NUMBER_INVALID(1032, "Phone number invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1033, "User not exists", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1034, "Invalid Token", HttpStatus.BAD_REQUEST),
    USER_EXISTS(1035, "User already exists", HttpStatus.BAD_REQUEST),
    PAYMENT_EXISTS(1036, "Payment already exists", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_ID(1037, "Invalid payment id", HttpStatus.BAD_REQUEST),
    USER_ID_REQUIRED(1038, "User id required", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_ID(1039, "Invalid order id", HttpStatus.BAD_REQUEST),


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
