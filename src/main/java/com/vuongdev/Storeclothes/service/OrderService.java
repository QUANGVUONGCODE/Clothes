package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.CartRequest;
import com.vuongdev.Storeclothes.dto.request.OrderRequest;
import com.vuongdev.Storeclothes.dto.request.OrderUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.OrderResponse;
import com.vuongdev.Storeclothes.entity.*;
import com.vuongdev.Storeclothes.enums.OrderStatus;
import com.vuongdev.Storeclothes.enums.PaymentStatus;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.OrderDetailMapper;
import com.vuongdev.Storeclothes.mapper.OrderMapper;
import com.vuongdev.Storeclothes.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    EmailService emailService;
    InvoiceService invoiceService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
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

        // Phân biệt COD và E_WALLET
        if (isEWalletPayment(payment)) {
            order.setPaymentStatus(PaymentStatus.PENDING_PAYMENT.name());
        } else {
            order.setPaymentStatus(PaymentStatus.UNPAID.name());
        }

        order = orderRepository.save(order);

        BigDecimal totalMoney = BigDecimal.ZERO;

        for (var cartItem : request.getCartItems()) {
            ProductVariant productVariant = productVariantRepository.findById(cartItem.getProductVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT));

            Product product = productRepository.findById(productVariant.getProduct().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_ID_PRODUCT));

            BigDecimal price = product.getPrice();
            Integer quantity = cartItem.getQuantity();

            if (productVariant.getStockQuantity() == 0 || productVariant.getStockQuantity() < quantity) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            productVariant.setStockQuantity(productVariant.getStockQuantity() - quantity);
            productVariantRepository.save(productVariant);

            OrderDetail orderDetail = orderDetailMapper.mapToOrderDetail2(cartItem);
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(productVariant);
            orderDetail.setPrice(price);
            orderDetail.setTotalMoney(price.multiply(BigDecimal.valueOf(quantity)));

            totalMoney = totalMoney.add(orderDetail.getTotalMoney());

            orderDetailRepository.save(orderDetail);
        }

        order.setTotalMoney(totalMoney);
        order = orderRepository.save(order);

        // COD: gửi mail xác nhận đơn hàng
        // Nếu mail lỗi thì chỉ log, không làm rollback order
        if (isCodPayment(payment)) {
            try {
                emailService.sendCodOrderConfirmationEmail(order);
            } catch (Exception e) {
                System.out.println("Gửi email xác nhận COD thất bại: " + e.getMessage());
            }
        }

        // E_WALLET: KHÔNG gửi mail ở đây
        // Đợi VNPay thanh toán thành công rồi mới gửi hóa đơn ở updateOrderWithVNPayTxnRef()

        return orderMapper.mapToOrderResponse(order);
    }

    private boolean isCodPayment(Payment payment) {
        return payment != null
                && payment.getName() != null
                && payment.getName().equalsIgnoreCase("COD");
    }

    private boolean isEWalletPayment(Payment payment) {
        return payment != null
                && payment.getName() != null
                && payment.getName().equalsIgnoreCase("E_WALLET");
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

    public OrderResponse findByOrderCodeAndPhoneNumber(String orderCode, String phoneNumber) {

        Order order = orderRepository.findByOrderCodeAndPhoneNumber(orderCode, phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND_CODE_PHONE));
        return orderMapper.mapToOrderResponse(order);
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

    public OrderResponse updateStatusOrder(OrderUpdateRequest request, Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );
        OrderStatus newStatus = request.getStatus();

        order.setStatus(newStatus.name());

        /*
         * Nếu admin bấm "Hoàn thành"
         * - COD: xem như đã thu tiền
         * - E_WALLET: thường đã PAID từ VNPay trước đó
         */
        if (newStatus == OrderStatus.COMPLETED) {

            // Nếu đơn chưa PAID thì set PAID
            // Đặc biệt dùng cho COD
            if (!PaymentStatus.PAID.name().equals(order.getPaymentStatus())) {
                order.setPaymentStatus(PaymentStatus.PAID.name());
            }

            Order savedOrder = orderRepository.save(order);

            // Tạo hóa đơn nếu chưa có
            Invoice invoice = invoiceService.createInvoiceFromOrder(savedOrder);

            // Gửi mail hóa đơn
            try {
                emailService.sendPaymentSuccessEmail(savedOrder, invoice);
            } catch (Exception e) {
                System.out.println("Gửi email hóa đơn khi hoàn thành đơn thất bại: " + e.getMessage());
            }

            return orderMapper.mapToOrderResponse(savedOrder);
        }

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapToOrderResponse(savedOrder);
    }


    public Page<OrderResponse> getOrdersByStatus(OrderStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

        Page<Order> orderPage = orderRepository.findByStatus(status, pageable);

        return orderPage.map(orderMapper::mapToOrderResponse);
    }

}
