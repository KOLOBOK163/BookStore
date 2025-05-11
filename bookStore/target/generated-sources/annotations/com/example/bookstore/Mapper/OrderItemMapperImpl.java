package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.OrderItemDTO;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.OrderItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T12:36:30+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemDTO toDTO(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setBookId( orderItemBookId( orderItem ) );
        orderItemDTO.setTitle( orderItemBookTitle( orderItem ) );
        orderItemDTO.setPriceAtPurchase( orderItem.getPriceAtPurchase() );
        orderItemDTO.setDiscountedPriceAtPurchase( orderItem.getPriceAtPurchase() );
        orderItemDTO.setQuantity( orderItem.getQuantity() );

        return orderItemDTO;
    }

    @Override
    public OrderItem toEntity(OrderItemDTO orderItemDTO) {
        if ( orderItemDTO == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity( orderItemDTO.getQuantity() );
        orderItem.setPriceAtPurchase( orderItemDTO.getPriceAtPurchase() );

        return orderItem;
    }

    private Long orderItemBookId(OrderItem orderItem) {
        Book book = orderItem.getBook();
        if ( book == null ) {
            return null;
        }
        return book.getId();
    }

    private String orderItemBookTitle(OrderItem orderItem) {
        Book book = orderItem.getBook();
        if ( book == null ) {
            return null;
        }
        return book.getTitle();
    }
}
