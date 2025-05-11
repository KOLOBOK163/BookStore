package com.example.bookstore.Controller;

import com.example.bookstore.DTO.AddressDTO;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Exception.ResourceNotFoundException;
import com.example.bookstore.Repository.CustomerRepository;
import com.example.bookstore.Service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:3000")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerRepository customerRepository;

    @Operation(summary = "Get addresses for the authenticated customer")
    @GetMapping
    public List<AddressDTO> getUserAddresses() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return addressService.getAddressesByCustomer(customer.getId());
    }

    @Operation(summary = "Add a new address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = userDetails.getUsername();
        System.out.println("Authenticated user login: " + login); // Отладка
        Customer customer = customerRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with login: " + login));
        addressDTO.setCustomerId(customer.getId()); // Устанавливаем реальный customerId
        System.out.println("Setting customerId: " + customer.getId()); // Отладка
        AddressDTO savedAddress = addressService.createAddress(addressDTO);
        return ResponseEntity.ok(savedAddress);
    }
}