package com.example.bookstore.Service;

import com.example.bookstore.DTO.WarehouseDTO;
import com.example.bookstore.Entity.Warehouse;
import com.example.bookstore.Mapper.WarehouseMapper;
import com.example.bookstore.Repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseMapper warehouseMapper;

    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(warehouseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse.setCreatedAt(LocalDateTime.now());
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDTO(savedWarehouse);
    }

    public WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        warehouse.setName(warehouseDTO.getName());
        warehouse.setCity(warehouseDTO.getCity());
        warehouse.setStreet(warehouseDTO.getStreet());
        warehouse.setHouseNumber(warehouseDTO.getHouseNumber());
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDTO(updatedWarehouse);
    }

    public void deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        warehouseRepository.delete(warehouse);
    }

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
    }
}
