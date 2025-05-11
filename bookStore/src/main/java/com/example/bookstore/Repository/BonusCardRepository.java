package com.example.bookstore.Repository;

import com.example.bookstore.Entity.BonusCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonusCardRepository extends JpaRepository<BonusCard, Long> {
    Optional<BonusCard> findByCardNumber(String cardNumber);
    Optional<BonusCard> findByCustomerId(Long customerId);
}
