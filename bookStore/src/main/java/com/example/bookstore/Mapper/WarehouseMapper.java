package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.WarehouseDTO;
import com.example.bookstore.Entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);


    WarehouseDTO toDTO(Warehouse warehouse);
    Warehouse toEntity(WarehouseDTO warehouseDTO);
}
