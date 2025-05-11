package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.AddressDTO;
import com.example.bookstore.DTO.OrderDTO;
import com.example.bookstore.DTO.OrderItemDTO;
import com.example.bookstore.Entity.Address;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Entity.Order;
import com.example.bookstore.Entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T00:29:57+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderDTO toDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setCustomerId( orderCustomerId( order ) );
        orderDTO.setDeliveryAddressId( orderDeliveryAddressId( order ) );
        orderDTO.setDeliveryAddress( addressToAddressDTO( order.getDeliveryAddress() ) );
        orderDTO.setOrderItems( orderItemListToOrderItemDTOList( order.getOrderItems() ) );
        orderDTO.setOrderNumber( order.getOrderNumber() );
        orderDTO.setId( order.getId() );
        orderDTO.setOrderDate( order.getOrderDate() );
        orderDTO.setStatus( order.getStatus() );
        orderDTO.setTotal( order.getTotal() );
        orderDTO.setPaymentType( order.getPaymentType() );
        orderDTO.setPaymentStatus( order.getPaymentStatus() );

        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Order order = new Order();

        order.setOrderNumber( orderDTO.getOrderNumber() );
        order.setId( orderDTO.getId() );
        order.setOrderDate( orderDTO.getOrderDate() );
        order.setStatus( orderDTO.getStatus() );
        order.setTotal( orderDTO.getTotal() );
        order.setPaymentType( orderDTO.getPaymentType() );
        order.setPaymentStatus( orderDTO.getPaymentStatus() );

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

    protected AddressDTO addressToAddressDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setId( address.getId() );
        addressDTO.setCity( address.getCity() );
        addressDTO.setStreet( address.getStreet() );
        addressDTO.setHouse( address.getHouse() );
        addressDTO.setApartment( address.getApartment() );

        return addressDTO;
    }

    protected List<OrderItemDTO> orderItemListToOrderItemDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDTO> list1 = new ArrayList<OrderItemDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemMapper.toDTO( orderItem ) );
        }

        return list1;
    }
}
