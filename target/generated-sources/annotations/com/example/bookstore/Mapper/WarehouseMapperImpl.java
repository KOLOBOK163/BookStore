package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.WarehouseDTO;
import com.example.bookstore.Entity.Warehouse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T01:24:44+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class WarehouseMapperImpl implements WarehouseMapper {

    @Override
    public WarehouseDTO toDTO(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }

        WarehouseDTO warehouseDTO = new WarehouseDTO();

        warehouseDTO.setId( warehouse.getId() );
        warehouseDTO.setName( warehouse.getName() );
        warehouseDTO.setLocation( warehouse.getLocation() );

        return warehouseDTO;
    }

    @Override
    public Warehouse toEntity(WarehouseDTO warehouseDTO) {
        if ( warehouseDTO == null ) {
            return null;
        }

        Warehouse warehouse = new Warehouse();

        warehouse.setId( warehouseDTO.getId() );
        warehouse.setName( warehouseDTO.getName() );
        warehouse.setLocation( warehouseDTO.getLocation() );

        return warehouse;
    }
}
