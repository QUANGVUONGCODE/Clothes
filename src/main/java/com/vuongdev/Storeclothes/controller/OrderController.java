package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.OrderRequest;
import com.vuongdev.Storeclothes.dto.request.OrderUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.OrderListResponse;
import com.vuongdev.Storeclothes.dto.response.OrderResponse;
import com.vuongdev.Storeclothes.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable Long id){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderById(id))
                .build();
    }


    @GetMapping
    ApiResponse<OrderListResponse> getAllOrders(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("orderDate").descending()
        );
        Page<OrderResponse> orderPage = orderService.getAllOrders(keyword, pageRequest);
        List<OrderResponse> orders = orderPage.getContent();
        int totalPages = orderPage.getNumber() + 1;
        return ApiResponse.<OrderListResponse>builder()
                .result(OrderListResponse.builder()
                        .orders(orders)
                        .page(totalPages)
                        .build())
                .build();
    }

    @GetMapping("/user/{id}")
    ApiResponse<List<OrderResponse>> getOrderByUserId(@PathVariable Long id){
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrderByUserId(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateRequest request){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrder(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ApiResponse.<String>builder()
                .result("Order deleted successfully")
                .build();
    }

    @GetMapping("/today")
    public ApiResponse<List<OrderResponse>> getOrdersForToday() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrdersForToday())
                .build();
    }
}
