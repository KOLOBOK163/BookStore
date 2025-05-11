// src/main/java/com/example/bookstore/Mapper/CustomerMapper.java
package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.CustomerDTO;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToNames")
    @Mapping(target = "balance", source = "balance")
    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "balance", source = "balance")
    Customer toEntity(CustomerDTO customerDTO);

    @Named("rolesToNames")
    default List<String> rolesToNames(List<Role> roles) {
        if (roles == null) {
            return Collections.emptyList(); // Возвращаем пустой список вместо null
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}