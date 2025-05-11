package com.example.bookstore.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long bookId;
    private String title;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal discountedPriceAtPurchase;
}
