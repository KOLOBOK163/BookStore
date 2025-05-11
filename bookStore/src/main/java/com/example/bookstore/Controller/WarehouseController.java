package com.example.bookstore.Controller;

import com.example.bookstore.DTO.BookWarehouseDTO;
import com.example.bookstore.DTO.WarehouseDTO;
import com.example.bookstore.Entity.BookWarehouse;
import com.example.bookstore.Repository.BookWarehouseRepository;
import com.example.bookstore.Service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = "http://localhost:3000")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private BookWarehouseRepository bookWarehouseRepository;

    @Operation(summary = "Get all warehouses")
    @GetMapping
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }

    @Operation(summary = "Create a new warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouse created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public WarehouseDTO createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.createWarehouse(warehouseDTO);
    }

    @Operation(summary = "Update an existing warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouse updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public WarehouseDTO updateWarehouse(@PathVariable Long id, @RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.updateWarehouse(id, warehouseDTO);
    }

    @Operation(summary = "Delete a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Warehouse deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get books in a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found")
    })
    @GetMapping("/{id}/books")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<BookWarehouseDTO> getBooksInWarehouse(@PathVariable Long id) {
        List<BookWarehouse> bookWarehouses = bookWarehouseRepository.findByWarehouseId(id);
        return bookWarehouses.stream().map(bw -> {
            BookWarehouseDTO dto = new BookWarehouseDTO();
            dto.setBookId(bw.getBook().getId());
            dto.setBookTitle(bw.getBook().getTitle());
            dto.setStock(bw.getStock());
            return dto;
        }).collect(Collectors.toList());
    }

    @Operation(summary = "Update stock of a book in a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "BookWarehouse not found")
    })
    @PutMapping("/{warehouseId}/books/{bookId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookWarehouseDTO> updateBookStock(@PathVariable Long warehouseId, @PathVariable Long bookId, @RequestBody BookWarehouseDTO bookWarehouseDTO) {
        BookWarehouse bookWarehouse = bookWarehouseRepository.findByBookIdAndWarehouseId(bookId, warehouseId)
                .orElseThrow(() -> new RuntimeException("BookWarehouse not found"));
        bookWarehouse.setStock(bookWarehouseDTO.getStock());
        bookWarehouseRepository.save(bookWarehouse);
        bookWarehouseDTO.setBookId(bookWarehouse.getBook().getId());
        bookWarehouseDTO.setBookTitle(bookWarehouse.getBook().getTitle());
        return ResponseEntity.ok(bookWarehouseDTO);
    }

    @Operation(summary = "Delete a book from a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book removed from warehouse successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description =  "BookWarehouse not found")
    })
    @DeleteMapping("/{warehouseId}/books/{bookId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBookFromWarehouse(@PathVariable Long warehouseId, @PathVariable Long bookId) {
        bookWarehouseRepository.deleteByBookIdAndWarehouseId(bookId, warehouseId);
        return ResponseEntity.ok().build();
    }
}
