package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.OrderDetailRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.OrderDetailResponse;
import com.vuongdev.Storeclothes.service.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("${api.prefix}/order-details")
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @PostMapping
    ApiResponse<OrderDetailResponse> createOrderDetail(@RequestBody OrderDetailRequest request){
        return ApiResponse.<OrderDetailResponse>builder()
                .result(orderDetailService.createOrderDetail(request))
                .build();
    }

    @GetMapping("orders/{id}")
    ApiResponse<List<OrderDetailResponse>> getOrderDetailsByOrderId(@PathVariable Long id){
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailService.getOrderDetailByOrderId(id))
                .build();
    }

    @DeleteMapping("orders/{id}")
    ApiResponse<String> deleteOrderDetail(@PathVariable Long id){
        orderDetailService.deleteOrderDetail(id);
        return ApiResponse.<String>builder()
                .result("Delete order detail successfully")
                .build();
    }
}
