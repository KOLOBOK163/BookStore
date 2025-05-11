package com.example.bookstore.DTO;

import lombok.Data;

@Data
public class BookWarehouseDTO {
    private Long bookId;
    private String bookTitle;
    private Integer stock;
}
