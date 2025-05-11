package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.DiscountDTO;
import com.example.bookstore.Entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);

    @Mapping(source = "book.id", target = "bookId")
    DiscountDTO toDTO(Discount discount);

    @Mapping(target = "book", ignore = true)
    Discount toEntity(DiscountDTO discountDTO);
}
