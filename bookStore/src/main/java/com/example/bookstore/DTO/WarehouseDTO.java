package com.example.bookstore.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseDTO {
    private Long id;

    @NotBlank(message = "Название склада не может быть пустым")
    private String name;

    @NotBlank(message = "Город не может быть пустым")
    private String city;

    @NotBlank(message = "Улица не может быть пустой")
    private String street;

    @NotBlank(message = "Номер дома не может быть пустым")
    private String houseNumber;
}
