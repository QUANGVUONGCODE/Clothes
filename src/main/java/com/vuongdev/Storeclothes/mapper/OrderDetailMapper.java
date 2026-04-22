package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.CartRequest;
import com.vuongdev.Storeclothes.dto.request.OrderDetailRequest;
import com.vuongdev.Storeclothes.dto.response.OrderDetailResponse;
import com.vuongdev.Storeclothes.entity.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail mapToOrderDetail(OrderDetailRequest orderDetailRequest);
    OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail);
    OrderDetail mapToOrderDetail2(CartRequest cartRequest);
}