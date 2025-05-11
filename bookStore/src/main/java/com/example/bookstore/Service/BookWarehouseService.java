package com.example.bookstore.Service;

import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.BookWarehouse;
import com.example.bookstore.Entity.Warehouse;
import com.example.bookstore.Repository.BookRepository;
import com.example.bookstore.Repository.BookWarehouseRepository;
import com.example.bookstore.Repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookWarehouseService {
    @Autowired
    private BookWarehouseRepository bookWarehouseRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    public void distributeBook(Long bookId, Long warehouseId, Integer stock) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        BookWarehouse bookWarehouse = new BookWarehouse();
        bookWarehouse.setBook(book);
        bookWarehouse.setWarehouse(warehouse);
        bookWarehouse.setStock(stock);

        bookWarehouseRepository.save(bookWarehouse);
    }

    public List<BookWarehouse> getWarehouseStockForBook(Long bookId) {
        return bookWarehouseRepository.findByBookId(bookId);
    }

    public void updateStock(Long bookId, Long warehouseId, Integer stock) {
        BookWarehouse bookWarehouse = bookWarehouseRepository.findById(
                        bookWarehouseRepository.findAll().stream()
                                .filter(bw -> bw.getBook().getId().equals(bookId) && bw.getWarehouse().getId().equals(warehouseId))
                                .findFirst().get().getId())
                .orElseThrow(() -> new RuntimeException("BookWarehouse not found"));
        bookWarehouse.setStock(stock);
        bookWarehouseRepository.save(bookWarehouse);
    }

    public void deductStock(Long bookId, Integer quantity) {
        List<BookWarehouse> bookWarehouses = bookWarehouseRepository.findByBookId(bookId);
        int remaining = quantity;
        for (BookWarehouse bw : bookWarehouses) {
            if (remaining <= 0) break;
            int newStock = Math.max(0, bw.getStock() - remaining);
            remaining -= bw.getStock();
            bw.setStock(newStock);
            bookWarehouseRepository.save(bw);
        }
    }
}
