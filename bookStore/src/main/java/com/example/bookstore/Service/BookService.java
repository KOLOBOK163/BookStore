package com.example.bookstore.Service;

import com.example.bookstore.DTO.BookDTO;
import com.example.bookstore.DTO.DiscountDTO;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Mapper.BookMapper;
import com.example.bookstore.Repository.BookRepository;
import com.example.bookstore.Exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookWarehouseService bookWarehouseService;

    @Autowired
    private DiscountService discountService;

    private BookDTO applyDiscount(BookDTO bookDTO) {
        DiscountDTO activeDiscount = discountService.getActiveDiscountForBook(bookDTO.getId());
        if (activeDiscount != null) {
            bookDTO.setActiveDiscount(activeDiscount);
            BigDecimal price = bookDTO.getPrice();
            BigDecimal discountPercentage = activeDiscount.getDiscountPercentage();
            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));
            BigDecimal discountedPrice = price.multiply(discountMultiplier).setScale(2, BigDecimal.ROUND_HALF_UP);
            bookDTO.setDiscountedPrice(discountedPrice);
        } else if (bookDTO.getDiscountPercentage() != null && bookDTO.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal price = bookDTO.getPrice();
            BigDecimal discountPercentage = bookDTO.getDiscountPercentage();
            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100)));
            BigDecimal discountedPrice = price.multiply(discountMultiplier).setScale(2, BigDecimal.ROUND_HALF_UP);
            bookDTO.setDiscountedPrice(discountedPrice);
        } else {
            bookDTO.setDiscountedPrice(bookDTO.getPrice());
        }
        return bookDTO;
    }

    public List<BookDTO> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAllWithDiscounts().stream()
                .map(bookMapper::toDTO)
                .map(this::applyDiscount)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        logger.info("Fetching book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        BookDTO bookDTO = bookMapper.toDTO(book);
        return applyDiscount(bookDTO);
    }

    public List<BookDTO> searchBooks(String title) {
        logger.info("Searching books with title: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(bookMapper::toDTO)
                .map(this::applyDiscount)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByCategory(String category) {
        logger.info("Fetching books by category: {}", category);
        return bookRepository.findByCategory(category).stream()
                .map(bookMapper::toDTO)
                .map(this::applyDiscount)
                .collect(Collectors.toList());
    }

    public BookDTO createBook(BookDTO bookDTO) {
        logger.info("Creating new book: {}", bookDTO.getTitle());
        Book book = bookMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        BookDTO createdBookDTO = bookMapper.toDTO(savedBook);
        return applyDiscount(createdBookDTO);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        logger.info("Updating book with id: {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setPrice(bookDTO.getPrice());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setPublisher(bookDTO.getPublisher());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setStock(bookDTO.getStock());
        existingBook.setCategory(bookDTO.getCategory());
        existingBook.setCoverImage(bookDTO.getCoverImage());
        existingBook.setDiscountPercentage(bookDTO.getDiscountPercentage());
        Book updatedBook = bookRepository.save(existingBook);
        BookDTO updatedBookDTO = bookMapper.toDTO(updatedBook);
        return applyDiscount(updatedBookDTO);
    }

    public void deleteBook(Long id) {
        logger.info("Deleting book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    public void deductStock(Long bookId, Integer quantity) {
        logger.info("Deducting stock for bookId: {}, quantity: {}", bookId, quantity);
        bookWarehouseService.deductStock(bookId, quantity);
    }
}