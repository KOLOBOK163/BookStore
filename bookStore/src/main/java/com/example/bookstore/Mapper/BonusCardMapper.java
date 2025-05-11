package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.BonusCardDTO;
import com.example.bookstore.Entity.BonusCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BonusCardMapper {
    BonusCardMapper INSTANCE = Mappers.getMapper(BonusCardMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    BonusCardDTO toDTO(BonusCard bonusCard);

    @Mapping(target = "customer", ignore = true)
    BonusCard toEntity(BonusCardDTO bonusCardDTO);
}
