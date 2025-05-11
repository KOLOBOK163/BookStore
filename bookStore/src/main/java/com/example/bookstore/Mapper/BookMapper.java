package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.BookDTO;
import com.example.bookstore.DTO.DiscountDTO;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "coverImage", target = "coverImage")
    @Mapping(source = "discountPercentage", target = "discountPercentage")
    BookDTO toDTO(Book book);

    @Mapping(source = "coverImage", target = "coverImage")
    @Mapping(source = "discountPercentage", target = "discountPercentage")
    Book toEntity(BookDTO bookDTO);


}
