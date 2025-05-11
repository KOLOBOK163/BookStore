package com.example.bookstore.DTO;

import lombok.Data;

@Data
public class BonusCardDTO {
    private Long id;
    private Long customerId;
    private String cardNumber;
    private int points;
}
