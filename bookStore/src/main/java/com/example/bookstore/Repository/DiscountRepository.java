package com.example.bookstore.Repository;

import com.example.bookstore.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByBookIdAndActiveTrueAndStartDateBeforeAndEndDateAfter(
            Long bookId,
            LocalDateTime startDateBefore,
            LocalDateTime endDateAfter
    );

    List<Discount> findByBookId(Long bookId);
}
