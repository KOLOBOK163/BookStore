// src/main/java/com/example/bookstore/DTO/CustomerDTO.java
package com.example.bookstore.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerDTO {
    private Long id;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 8, message = "Логин должен содержать минимум 8 символов")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    private String fullName;

    private List<String> roles;

    private String jwt;

    private BigDecimal balance;
}

