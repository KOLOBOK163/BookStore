package com.example.bookstore.Service;

import com.example.bookstore.DTO.DiscountDTO;
import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.Discount;
import com.example.bookstore.Exception.ResourceNotFoundException;
import com.example.bookstore.Mapper.DiscountMapper;
import com.example.bookstore.Repository.BookRepository;
import com.example.bookstore.Repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DiscountMapper discountMapper;

    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        Book book = bookRepository.findById(discountDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + discountDTO.getBookId()));
        Discount discount = discountMapper.toEntity(discountDTO);
        discount.setBook(book);
        discount.setActive(true);
        return discountMapper.toDTO(discountRepository.save(discount));
    }

    public DiscountDTO updateDiscount(Long id, DiscountDTO discountDTO) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
        existingDiscount.setDiscountPercentage(discountDTO.getDiscountPercentage());
        existingDiscount.setStartDate(discountDTO.getStartDate());
        existingDiscount.setEndDate(discountDTO.getEndDate());
        existingDiscount.setActive(discountDTO.isActive());
        return discountMapper.toDTO(discountRepository.save(existingDiscount));
    }

    public void deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
        discountRepository.delete(discount);
    }

    public DiscountDTO getActiveDiscountForBook(Long bookId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByBookIdAndActiveTrueAndStartDateBeforeAndEndDateAfter(bookId, now, now)
                .map(discountMapper::toDTO)
                .orElse(null);
    }

    public List<DiscountDTO> getDiscountsForBook(Long bookId) {
        return discountRepository.findByBookId(bookId).stream()
                .map(discountMapper::toDTO)
                .collect(Collectors.toList());
    }
}
