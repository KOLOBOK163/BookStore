package com.example.bookstore.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private BigDecimal price;

    private String category;

    private String description;
    private String publisher;
    private int publicationYear;
    private String isbn;
    private int stock;

    private String coverImage;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts;

    @Column(name = "discount_percentage", nullable = true)
    private BigDecimal discountPercentage;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookWarehouse> bookWarehouses;
}