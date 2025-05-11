package com.example.bookstore.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal total;
    private String paymentType;
    private String paymentStatus;
    private String orderNumber;
    private Long deliveryAddressId;
    private AddressDTO deliveryAddress;
    private List<OrderItemDTO> orderItems;
}
