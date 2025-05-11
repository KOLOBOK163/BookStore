package com.example.bookstore.Repository;

import com.example.bookstore.Entity.BookWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookWarehouseRepository extends JpaRepository<BookWarehouse, Long> {
    List<BookWarehouse> findByBookId(Long bookId);
    List<BookWarehouse> findByWarehouseId(Long warehouseId);
    Optional<BookWarehouse> findByBookIdAndWarehouseId(Long bookId, Long warehouseId);
    void deleteByBookIdAndWarehouseId(Long bookId, Long warehouseId);
}
