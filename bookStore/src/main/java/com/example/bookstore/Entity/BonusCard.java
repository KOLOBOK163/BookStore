package com.example.bookstore.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bonus_cards")
public class BonusCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(unique = true, nullable = false, length = 10)
    private String cardNumber;

    @Column(nullable = false)
    private int points;

    public BonusCard() {
        this.points = 0;
    }
}
