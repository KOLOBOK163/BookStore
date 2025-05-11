package com.example.bookstore.Controller;

import com.example.bookstore.DTO.CustomerDTO;
import com.example.bookstore.Exception.ResourceNotFoundException;
import com.example.bookstore.Service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Top up balance for the authenticated customer using card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance topped up successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amount or card details"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/topup-with-card")
    public ResponseEntity<CustomerDTO> topUpBalanceWithCard(@RequestBody TopUpWithCardRequest request) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerDTO customer = customerService.findByLogin(login);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть больше 0");
        }

        // Validate card details
        if (!isValidCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Неверный номер карты");
        }
        if (!isValidExpiryDate(request.getExpiryDate())) {
            throw new IllegalArgumentException("Неверная дата окончания срока действия");
        }
        if (!isValidCVV(request.getCvv())) {
            throw new IllegalArgumentException("Неверный CVV");
        }

        // Simulate payment processing (in a real app, integrate with a payment gateway)
        customerService.updateBalance(customer.getId(), request.getAmount());
        CustomerDTO updatedCustomer = customerService.findByLogin(login);
        return ResponseEntity.ok(updatedCustomer);
    }

    // Basic card validation methods
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            return false;
        }
        try {
            Long.parseLong(cardNumber); // Ensure all digits
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        String[] parts = expiryDate.split("/");
        if (parts.length != 2) return false;
        try {
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);
            int currentYear = java.time.Year.now().getValue() % 100; // Last two digits
            int currentMonth = java.time.MonthDay.now().getMonthValue();
            return month >= 1 && month <= 12 && year >= currentYear && (year > currentYear || (year == currentYear && month >= currentMonth));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidCVV(String cvv) {
        if (cvv == null || cvv.length() != 3 || !cvv.matches("\\d+")) {
            return false;
        }
        try {
            Integer.parseInt(cvv); // Ensure all digits
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Operation(summary = "Get profile of the authenticated customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> getCustomerProfile() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        CustomerDTO customer = customerService.findByLogin(login);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with login: " + login);
        }
        return ResponseEntity.ok(customer);
    }
}

class TopUpWithCardRequest {
    private BigDecimal amount;
    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}