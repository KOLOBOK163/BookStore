package com.example.bookstore.Service;

import com.example.bookstore.DTO.CustomerDTO;
import com.example.bookstore.Entity.BonusCard;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Entity.Role;
import com.example.bookstore.Exception.BusinessException;
import com.example.bookstore.Mapper.CustomerMapper;
import com.example.bookstore.Repository.BonusCardRepository;
import com.example.bookstore.Repository.CustomerRepository;
import com.example.bookstore.Repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BonusCardRepository bonusCardRepository;

    @Transactional
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        if (customerRepository.findByLogin(customerDTO.getLogin()).isPresent()) {
            throw new BusinessException("Логин уже занят");
        }
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new BusinessException("Email уже занят");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });

        Customer customer = new Customer();
        customer.setLogin(customerDTO.getLogin());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setEmail(customerDTO.getEmail());
        customer.setFullName(customerDTO.getFullName());
        customer.setBalance(BigDecimal.ZERO);
        customer.setRoles(Collections.singletonList(userRole));

        Customer savedCustomer = customerRepository.save(customer);

        BonusCard bonusCard = new BonusCard();
        bonusCard.setCustomer(savedCustomer);
        bonusCard.setCardNumber(generateUniqueCardNumber());
        bonusCard.setPoints(0);

        try {
            bonusCardRepository.save(bonusCard);
        } catch (Exception e) {
            logger.error("Failed to save bonus card for customer {}: {}", savedCustomer.getId(), e.getMessage());
            throw new BusinessException("Ошибка при создании бонусной карты. Пожалуйста, попробуйте позже.");
        }

        return customerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO findByLogin(String login) {
        Customer customer = customerRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with login: " + login));
        return customerMapper.toDTO(customer);
    }

    public void updateBalance(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with id: " + customerId));
        customer.setBalance(customer.getBalance().add(amount));
        customerRepository.save(customer);
    }

    private String generateUniqueCardNumber() {
        Random random = new Random();
        String number;
        do {
            number = String.format("%010d", random.nextInt(1000000000));
        } while (bonusCardRepository.findByCardNumber(number).isPresent());
        return number;
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElse(null);
    }
}