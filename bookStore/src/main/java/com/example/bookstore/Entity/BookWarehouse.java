package com.example.bookstore.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "book_warehouse")
public class BookWarehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false)
    private Integer stock;
}
