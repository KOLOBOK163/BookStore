package com.example.bookstore.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private Long customerId;

    @NotBlank(message = "Город не может быть пустым")
    private String city;

    @NotBlank(message = "Улица не может быть пустой")
    private String street;

    @NotBlank(message = "Дом не может быть пустым")
    private String house;

    private String apartment;
}
