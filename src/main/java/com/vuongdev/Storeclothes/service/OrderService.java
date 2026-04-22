package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.CartRequest;
import com.vuongdev.Storeclothes.dto.request.OrderRequest;
import com.vuongdev.Storeclothes.dto.request.OrderUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.OrderResponse;
import com.vuongdev.Storeclothes.entity.Order;
import com.vuongdev.Storeclothes.entity.OrderDetail;
import com.vuongdev.Storeclothes.entity.Payment;
import com.vuongdev.Storeclothes.entity.Product;
import com.vuongdev.Storeclothes.entity.ProductVariant;
import com.vuongdev.Storeclothes.entity.User;
import com.vuongdev.Storeclothes.enums.OrderStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.OrderDetailMapper;
import com.vuongdev.Storeclothes.mapper.OrderMapper;
import com.vuongdev.Storeclothes.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    PaymentRepository paymentRepository;
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    OrderDetailMapper orderDetailMapper;
    OrderDetailRepository orderDetailRepository;
    OrderDetailService orderDetailService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_USER_ID)
        );

        Payment payment = paymentRepository.findById(request.getPaymentId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_PAYMENT_ID)
        );

        Order order = orderMapper.mapToOrder(request);
        order.setUser(user);
        order.setPayment(payment);
        order.setAddress(request.getAddress());
        order.setStatus(OrderStatus.PENDING.name());
        order.setOrderDate(
                request.getOrderDate() != null ? request.getOrderDate() : LocalDateTime.now()
        );
        order = orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal totalMoney = BigDecimal.ZERO;

        for (var cartItem : request.getCartItems()) {
            ProductVariant productVariant = productVariantRepository.findById(cartItem.getProductVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT));

            Product product = productRepository.findById(productVariant.getProduct().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_PRODUCT));

            BigDecimal price = product.getPrice();
            Integer quantity = cartItem.getQuantity();

            OrderDetail orderDetail = orderDetailMapper.mapToOrderDetail2(cartItem);
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(productVariant);
            orderDetail.setPrice(price);
            orderDetail.setTotalMoney(price.multiply(BigDecimal.valueOf(quantity)));
            totalMoney = totalMoney.add(orderDetail.getTotalMoney());
            orderDetailRepository.save(orderDetail);
            orderDetailMapper.mapToOrderDetailResponse(orderDetail);
        }

        order.setTotalMoney(totalMoney);
        orderRepository.save(order);
        return orderMapper.mapToOrderResponse(order);
    }
    
    public OrderResponse getOrderById(Long id){
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );
        return orderMapper.mapToOrderResponse(order);
    }

    public Page<OrderResponse> getAllOrders(String keyword, Pageable pageable){
        return orderRepository.findByKeyword(keyword,pageable).map(orderMapper::mapToOrderResponse);
    }


    public List<OrderResponse> getOrderByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_USER_ID)
        );
        return orderRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .map(orderMapper::mapToOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ORDER_ID));

        if (request.getPaymentId() != null) {
            Payment payment = paymentRepository.findById(request.getPaymentId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_PAYMENT_ID));
            order.setPayment(payment);
        }

        orderMapper.updateOrder(request, order);



        if (request.getCartItems() != null && !request.getCartItems().isEmpty()) {
            BigDecimal totalMoney = BigDecimal.ZERO;

            orderDetailService.deleteOrderDetail(orderId);

            List<OrderDetail> newOrderDetails = new ArrayList<>();

            for (CartRequest cartRequest : request.getCartItems()) {


                ProductVariant productVariant = productVariantRepository.findById(cartRequest.getProductVariantId())
                        .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT));

                Product product = productVariant.getProduct();
                if (product == null) {
                    throw new AppException(ErrorCode.INVALID_ID_PRODUCT);
                }

                BigDecimal price = product.getPrice();
                if (price == null) {
                    throw new AppException(ErrorCode.INVALID_PRICE);
                }

                Integer quantity = cartRequest.getQuantity();
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

                OrderDetail orderDetail = orderDetailMapper.mapToOrderDetail2(cartRequest);
                orderDetail.setOrder(order);
                orderDetail.setProductVariant(productVariant);
                orderDetail.setPrice(price);
                orderDetail.setQuantity(quantity);
                orderDetail.setTotalMoney(itemTotal);

                orderDetail = orderDetailRepository.save(orderDetail);

                newOrderDetails.add(orderDetail);
                totalMoney = totalMoney.add(itemTotal);
            }
            order.setTotalMoney(totalMoney);
        }
        if(request.getStatus() != null){
            order.setStatus(request.getStatus().name());
            if(request.getStatus() == OrderStatus.CANCELLED){
                order.setActive(false);
            }
        }

        orderRepository.save(order);
        return orderMapper.mapToOrderResponse(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );
        order.setStatus(OrderStatus.CANCELLED.name());
        order.setActive(false);

        orderRepository.save(order);
    }

    public List<OrderResponse> getOrdersForToday() {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1);

        List<Order> orders = orderRepository.findByOrderDateBetween(startOfToday, endOfToday);
        return orders.stream()
                .map(orderMapper::mapToOrderResponse)
                .collect(Collectors.toList());
    }


}
