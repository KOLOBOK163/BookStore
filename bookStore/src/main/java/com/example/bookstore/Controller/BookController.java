package com.example.bookstore.Controller;

import com.example.bookstore.DTO.BookDTO;
import com.example.bookstore.DTO.DiscountDTO;
import com.example.bookstore.Service.BookService;
import com.example.bookstore.Service.BookWarehouseService;
import com.example.bookstore.Service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookWarehouseService bookWarehouseService;

    @Autowired
    private DiscountService discountService;

    @Operation(summary = "Get all books")
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Get book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Get books by category")
    @GetMapping("/category")
    public List<BookDTO> getBooksByCategory(@RequestParam String category) {
        return bookService.getBooksByCategory(category);
    }

    @Operation(summary = "Search books by title")
    @GetMapping("/search")
    public List<BookDTO> searchBooks(@RequestParam String title) {
        return bookService.searchBooks(title);
    }

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BookDTO createBook(@RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    @Operation(summary = "Update an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @Operation(summary = "Distribute book to warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book distributed successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/distribute")
    @PreAuthorize("hasRole('ADMIN')")
    public void distributeBook(@RequestParam Long bookId, @RequestParam Long warehouseId, @RequestParam Integer stock) {
        bookWarehouseService.distributeBook(bookId, warehouseId, stock);
    }

    @Operation(summary = "Deduct stock for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock deducted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/deduct-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public void deductStock(@RequestParam Long bookId, @RequestParam Integer quantity) {
        bookService.deductStock(bookId, quantity);
    }

    @Operation(summary = "Create a new discount for a book")
    @PostMapping("/{bookId}/discounts")
    @PreAuthorize("hasRole('ADMIN')")
    public DiscountDTO createDiscount(@PathVariable Long bookId, @RequestBody DiscountDTO discountDTO) {
        discountDTO.setBookId(bookId);
        return discountService.createDiscount(discountDTO);
    }

    @Operation(summary = "Update an existing discount")
    @PutMapping("/discounts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DiscountDTO updateDiscount(@PathVariable Long id, @RequestBody DiscountDTO discountDTO) {
        return discountService.updateDiscount(id, discountDTO);
    }

    @Operation(summary = "Delete a discount")
    @DeleteMapping("/discounts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
    }

    @Operation(summary = "Get all discounts for a book")
    @GetMapping("/{bookId}/discounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DiscountDTO> getDiscountsForBook(@PathVariable Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        return discountService.getDiscountsForBook(bookId);
    }
}