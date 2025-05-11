package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.WarehouseDTO;
import com.example.bookstore.Entity.Warehouse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T00:29:57+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
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
        warehouseDTO.setCity( warehouse.getCity() );
        warehouseDTO.setStreet( warehouse.getStreet() );
        warehouseDTO.setHouseNumber( warehouse.getHouseNumber() );

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
        warehouse.setCity( warehouseDTO.getCity() );
        warehouse.setStreet( warehouseDTO.getStreet() );
        warehouse.setHouseNumber( warehouseDTO.getHouseNumber() );

        return warehouse;
    }
}
