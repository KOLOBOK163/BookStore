package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.OrderDTO;
import com.example.bookstore.Entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;



@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "orderNumber", target = "orderNumber")
    OrderDTO toDTO(Order order);

    @Mapping(source = "customerId", target = "customer", ignore = true)
    @Mapping(source = "deliveryAddressId", target = "deliveryAddress", ignore = true)
    @Mapping(source = "orderItems", target = "orderItems", ignore = true)
    @Mapping(source = "orderNumber", target = "orderNumber")
    Order toEntity(OrderDTO orderDTO);
}
