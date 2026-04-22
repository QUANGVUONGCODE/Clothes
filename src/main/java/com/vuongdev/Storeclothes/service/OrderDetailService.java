package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.OrderDetailRequest;
import com.vuongdev.Storeclothes.dto.response.OrderDetailResponse;
import com.vuongdev.Storeclothes.entity.Order;
import com.vuongdev.Storeclothes.entity.OrderDetail;
import com.vuongdev.Storeclothes.entity.Product;
import com.vuongdev.Storeclothes.entity.ProductVariant;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.OrderDetailMapper;
import com.vuongdev.Storeclothes.repository.OrderDetailRepository;
import com.vuongdev.Storeclothes.repository.OrderRepository;
import com.vuongdev.Storeclothes.repository.ProductRepository;
import com.vuongdev.Storeclothes.repository.ProductVariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;
    OrderRepository orderRepository;
    ProductVariantRepository productVariantRepository;
    ProductRepository productRepository;

    public OrderDetailResponse createOrderDetail(OrderDetailRequest orderDetailRequest){
        Order order = orderRepository.findById(orderDetailRequest.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );

        ProductVariant productVariant = productVariantRepository.findById(orderDetailRequest.getProductVariantId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT_VARIANT)
        );

        Product product = productRepository.findById(productVariant.getProduct().getId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_PRODUCT)
        );

        OrderDetail orderDetail = orderDetailMapper.mapToOrderDetail(orderDetailRequest);
        orderDetail.setOrder(order);
        orderDetail.setProductVariant(productVariant);

        orderDetail.setPrice(product.getPrice());
        orderDetailRepository.save(orderDetail);
        return orderDetailMapper.mapToOrderDetailResponse(orderDetail);
    }


    public List<OrderDetailResponse> getOrderDetailByOrderId(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ORDER_ID)
        );
        return orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(orderDetailMapper::mapToOrderDetailResponse)
                .toList();
    }

    public void deleteOrderDetail(Long orderId){
        if(!orderRepository.existsById(orderId)){
            throw new AppException(ErrorCode.INVALID_ORDER_ID);
        }
        orderDetailRepository.deleteByOrderId(orderId);
    }
}
