package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.response.*;
import com.vuongdev.Storeclothes.service.DashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/dashboard")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/overview")
    public ApiResponse<DashboardOverviewResponse> getOverview() {
        return ApiResponse.<DashboardOverviewResponse>builder()
                .result(dashboardService.getOverview())
                .build();
    }

    @GetMapping("/order-status")
    public ApiResponse<OrderStatusStatsResponse> getOrderStatus() {
        return ApiResponse.<OrderStatusStatsResponse>builder()
                .result(dashboardService.getOrderStatusStats())
                .build();
    }

    @GetMapping("/recent-orders")
    public ApiResponse<List<RecentOrderResponse>> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ApiResponse.<List<RecentOrderResponse>>builder()
                .result(dashboardService.getRecentOrders(limit))
                .build();
    }

    @GetMapping("/revenue")
    public ApiResponse<List<RevenuePointResponse>> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ApiResponse.<List<RevenuePointResponse>>builder()
                .result(dashboardService.getRevenue(from, to))
                .build();
    }

    @GetMapping("/top-products")
    public ApiResponse<List<TopProductResponse>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ApiResponse.<List<TopProductResponse>>builder()
                .result(dashboardService.getTopProducts(limit))
                .build();
    }
}