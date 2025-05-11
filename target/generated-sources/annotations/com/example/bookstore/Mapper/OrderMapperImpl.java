package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.OrderDTO;
import com.example.bookstore.DTO.OrderItemDTO;
import com.example.bookstore.Entity.Address;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Entity.Order;
import com.example.bookstore.Entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T01:24:45+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO toDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setCustomerId( orderCustomerId( order ) );
        orderDTO.setDeliveryAddressId( orderDeliveryAddressId( order ) );
        orderDTO.setId( order.getId() );
        orderDTO.setOrderDate( order.getOrderDate() );
        orderDTO.setStatus( order.getStatus() );
        orderDTO.setTotal( order.getTotal() );
        orderDTO.setPaymentType( order.getPaymentType() );
        orderDTO.setPaymentStatus( order.getPaymentStatus() );
        orderDTO.setOrderItems( orderItemListToOrderItemDTOList( order.getOrderItems() ) );

        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Order order = new Order();

        order.setId( orderDTO.getId() );
        order.setOrderDate( orderDTO.getOrderDate() );
        order.setStatus( orderDTO.getStatus() );
        order.setTotal( orderDTO.getTotal() );
        order.setPaymentType( orderDTO.getPaymentType() );
        order.setPaymentStatus( orderDTO.getPaymentStatus() );
        order.setDeliveryAddressId( orderDTO.getDeliveryAddressId() );
        order.setOrderItems( orderItemDTOListToOrderItemList( orderDTO.getOrderItems() ) );

        return order;
    }

    private Long orderCustomerId(Order order) {
        Customer customer = order.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private Long orderDeliveryAddressId(Order order) {
        Address deliveryAddress = order.getDeliveryAddress();
        if ( deliveryAddress == null ) {
            return null;
        }
        return deliveryAddress.getId();
    }

    protected OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setQuantity( orderItem.getQuantity() );
        orderItemDTO.setPriceAtPurchase( orderItem.getPriceAtPurchase() );

        return orderItemDTO;
    }

    protected List<OrderItemDTO> orderItemListToOrderItemDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDTO> list1 = new ArrayList<OrderItemDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemToOrderItemDTO( orderItem ) );
        }

        return list1;
    }

    protected OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO) {
        if ( orderItemDTO == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity( orderItemDTO.getQuantity() );
        orderItem.setPriceAtPurchase( orderItemDTO.getPriceAtPurchase() );

        return orderItem;
    }

    protected List<OrderItem> orderItemDTOListToOrderItemList(List<OrderItemDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemDTO orderItemDTO : list ) {
            list1.add( orderItemDTOToOrderItem( orderItemDTO ) );
        }

        return list1;
    }
}
