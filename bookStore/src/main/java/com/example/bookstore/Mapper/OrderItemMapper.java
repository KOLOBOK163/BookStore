package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.OrderItemDTO;
import com.example.bookstore.Entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "title")
    @Mapping(source = "priceAtPurchase", target = "priceAtPurchase")
    @Mapping(source = "priceAtPurchase", target = "discountedPriceAtPurchase")
    OrderItemDTO toDTO(OrderItem orderItem);

    @Mapping(source = "bookId", target = "book", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
