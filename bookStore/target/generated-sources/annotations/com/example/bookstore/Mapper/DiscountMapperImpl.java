package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.DiscountDTO;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.Discount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T13:13:22+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class DiscountMapperImpl implements DiscountMapper {

    @Override
    public DiscountDTO toDTO(Discount discount) {
        if ( discount == null ) {
            return null;
        }

        DiscountDTO discountDTO = new DiscountDTO();

        discountDTO.setBookId( discountBookId( discount ) );
        discountDTO.setId( discount.getId() );
        discountDTO.setDiscountPercentage( discount.getDiscountPercentage() );
        discountDTO.setStartDate( discount.getStartDate() );
        discountDTO.setEndDate( discount.getEndDate() );
        discountDTO.setActive( discount.isActive() );

        return discountDTO;
    }

    @Override
    public Discount toEntity(DiscountDTO discountDTO) {
        if ( discountDTO == null ) {
            return null;
        }

        Discount discount = new Discount();

        discount.setId( discountDTO.getId() );
        discount.setDiscountPercentage( discountDTO.getDiscountPercentage() );
        discount.setStartDate( discountDTO.getStartDate() );
        discount.setEndDate( discountDTO.getEndDate() );
        discount.setActive( discountDTO.isActive() );

        return discount;
    }

    private Long discountBookId(Discount discount) {
        Book book = discount.getBook();
        if ( book == null ) {
            return null;
        }
        return book.getId();
    }
}
