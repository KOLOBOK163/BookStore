package com.example.bookstore.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookDTO {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String title;

    @NotBlank(message = "Автор не может быть пустым")
    private String author;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    private BigDecimal price;

    @NotBlank(message = "Категория не может быть пустой")
    private String category; // Оставляем как строку

    private String description;
    private String publisher;

    @Min(value = 0, message = "Год издания должен быть больше 0")
    private Integer publicationYear;

    @Size(min = 13, max = 13, message = "ISBN должен содержать 13 символов")
    private String isbn;

    @NotNull(message = "Количество на складе не может быть пустым")
    @Min(value = 0, message = "Количество на складе не может быть меньше 0")
    private Integer stock;

    private String coverImage;

    private BigDecimal discountPercentage;

    private DiscountDTO activeDiscount;

    private BigDecimal discountedPrice;
}