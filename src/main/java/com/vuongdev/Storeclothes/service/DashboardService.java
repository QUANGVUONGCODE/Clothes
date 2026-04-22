package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.response.*;
import com.vuongdev.Storeclothes.entity.Order;
import com.vuongdev.Storeclothes.entity.OrderDetail;
import com.vuongdev.Storeclothes.repository.OrderDetailRepository;
import com.vuongdev.Storeclothes.repository.OrderRepository;
import com.vuongdev.Storeclothes.repository.ProductRepository;
import com.vuongdev.Storeclothes.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardService {

    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    UserRepository userRepository;

    private static final String PENDING = "PENDING";
    private static final String CONFIRMED = "CONFIRMED";
    private static final String SHIPPING = "SHIPPING";
    private static final String COMPLETED = "COMPLETED";
    private static final String CANCELLED = "CANCELLED";

    public DashboardOverviewResponse getOverview() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();

        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime nextMonthStart = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        return DashboardOverviewResponse.builder()
                .totalProducts(productRepository.count())
                .totalOrders(orderRepository.count())
                .pendingOrders(orderRepository.countByStatus(PENDING))
                .completedOrders(orderRepository.countByStatus(COMPLETED))
                .totalUsers(userRepository.count())
                .totalRevenue(defaultZero(orderRepository.sumTotalMoneyByStatus(COMPLETED)))
                .todayRevenue(defaultZero(orderRepository.sumTotalMoneyByStatusAndCreatedAtBetween(
                        COMPLETED, todayStart, tomorrowStart
                )))
                .monthRevenue(defaultZero(orderRepository.sumTotalMoneyByStatusAndCreatedAtBetween(
                        COMPLETED, monthStart, nextMonthStart
                )))
                .build();
    }

    public OrderStatusStatsResponse getOrderStatusStats() {
        return OrderStatusStatsResponse.builder()
                .pending(orderRepository.countByStatus(PENDING))
                .confirmed(orderRepository.countByStatus(CONFIRMED))
                .shipping(orderRepository.countByStatus(SHIPPING))
                .completed(orderRepository.countByStatus(COMPLETED))
                .cancelled(orderRepository.countByStatus(CANCELLED))
                .build();
    }

    public List<RecentOrderResponse> getRecentOrders(int limit) {
        return orderRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit))
                .stream()
                .map(order -> RecentOrderResponse.builder()
                        .id(order.getId())
                        // đổi lại field này theo entity thật của bạn nếu cần
                        .customerName(order.getUser() != null ? order.getUser().getFullName() : null)
                        .totalMoney(order.getTotalMoney())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }

    public List<RevenuePointResponse> getRevenue(LocalDate from, LocalDate to) {
        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.plusDays(1).atStartOfDay();

        List<Order> completedOrders = orderRepository
                .findByStatusAndCreatedAtBetweenOrderByCreatedAtAsc(COMPLETED, fromDateTime, toDateTime);

        Map<LocalDate, BigDecimal> revenueByDate = new LinkedHashMap<>();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            revenueByDate.put(date, BigDecimal.ZERO);
        }

        for (Order order : completedOrders) {
            LocalDate date = order.getCreatedAt().toLocalDate();
            revenueByDate.put(
                    date,
                    revenueByDate.getOrDefault(date, BigDecimal.ZERO).add(defaultZero(order.getTotalMoney()))
            );
        }

        return revenueByDate.entrySet().stream()
                .map(entry -> RevenuePointResponse.builder()
                        .date(entry.getKey())
                        .revenue(entry.getValue())
                        .build())
                .toList();
    }

    public List<TopProductResponse> getTopProducts(int limit) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_Status(COMPLETED);

        Map<Long, TopProductResponse> productMap = new HashMap<>();

        for (OrderDetail od : orderDetails) {
            // nếu entity của bạn là productVariant thì đổi lại:
            // Product product = od.getProductVariant().getProduct();
            var product = od.getProductVariant().getProduct();

            if (product == null) continue;

            TopProductResponse current = productMap.get(product.getId());

            long quantity = od.getQuantity() == null ? 0L : od.getQuantity().longValue();

            if (current == null) {
                productMap.put(product.getId(), TopProductResponse.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .soldQuantity(quantity)
                        .build());
            } else {
                current.setSoldQuantity(current.getSoldQuantity() + quantity);
            }
        }

        return productMap.values().stream()
                .sorted(Comparator.comparing(TopProductResponse::getSoldQuantity).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}