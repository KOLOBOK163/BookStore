package com.example.bookstore.Controller;

import com.example.bookstore.DTO.BonusCardDTO;
import com.example.bookstore.DTO.OrderDTO;
import com.example.bookstore.Entity.BonusCard;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Exception.ResourceNotFoundException;
import com.example.bookstore.Mapper.BonusCardMapper;
import com.example.bookstore.Repository.BonusCardRepository;
import com.example.bookstore.Repository.CustomerRepository;
import com.example.bookstore.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BonusCardRepository bonusCardRepository;

    @Autowired
    private BonusCardMapper bonusCardMapper;

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer or address not found")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Authenticated user login: " + login); // Отладка
        Customer customer = customerRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with login: " + login));
        orderDTO.setCustomerId(customer.getId()); // Устанавливаем реальный customerId
        System.out.println("Setting customerId: " + customer.getId()); // Отладка
        OrderDTO savedOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(savedOrder);
    }

    @Operation(summary = "Get orders for the authenticated customer")
    @GetMapping
    public List<OrderDTO> getUserOrder() {
        System.out.println("SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Authenticated user: " + userDetails.getUsername() + ", roles: " + userDetails.getAuthorities());
        Customer customer = customerRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with login: " + userDetails.getUsername()));
        System.out.println("Customer ID: " + customer.getId());
        return orderService.getOrdersByCustomer(customer.getId());
    }

    @Operation(summary = "Get bonus card for the authenticated customer")
    @GetMapping("/bonus-cards")
    public ResponseEntity<BonusCardDTO> getBonusCard() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with login: " + userDetails.getUsername()));
        BonusCard bonusCard = bonusCardRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Bonus card not found for customer: " + customer.getId()));
        return ResponseEntity.ok(bonusCardMapper.toDTO(bonusCard));
    }
}
