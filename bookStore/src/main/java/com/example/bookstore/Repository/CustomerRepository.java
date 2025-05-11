package com.example.bookstore.Repository;

import com.example.bookstore.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByLogin(String login);
    Optional<Customer> findByEmail(String email);
}
