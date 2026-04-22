package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.OrderRequest;
import com.vuongdev.Storeclothes.dto.request.OrderUpdateRequest;
import com.vuongdev.Storeclothes.dto.response.OrderResponse;
import com.vuongdev.Storeclothes.entity.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order mapToOrder(OrderRequest orderRequest);
    OrderResponse mapToOrderResponse(Order order);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrder(OrderUpdateRequest orderRequest, @MappingTarget Order order);
}
