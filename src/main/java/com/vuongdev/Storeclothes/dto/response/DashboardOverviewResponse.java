package com.vuongdev.Storeclothes.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardOverviewResponse {
    long totalProducts;
    long totalOrders;
    long pendingOrders;
    long completedOrders;
    long totalUsers;
    BigDecimal totalRevenue;
    BigDecimal todayRevenue;
    BigDecimal monthRevenue;
}