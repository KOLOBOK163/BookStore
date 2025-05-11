package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.AddressDTO;
import com.example.bookstore.Entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    AddressDTO toDTO(Address address);

    @Mapping(source = "customerId", target = "customer", ignore = true)
    Address toEntity(AddressDTO addressDTO);
}
