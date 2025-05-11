// src/main/java/com/example/bookstore/Controller/AuthController.java
package com.example.bookstore.Controller;

import com.example.bookstore.DTO.CustomerDTO;
import com.example.bookstore.Service.CustomerService;
import com.example.bookstore.Utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Operation(summary = "Register a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO registeredCustomer = customerService.registerCustomer(customerDTO);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(customerDTO.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, registeredCustomer));
    }

    @Operation(summary = "Login a customer and return JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody CustomerDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthenticationResponse(null, null));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails);
        CustomerDTO customer = customerService.findByLogin(loginRequest.getLogin());
        return ResponseEntity.ok(new AuthenticationResponse(jwt, customer));
    }
}

class AuthenticationResponse {
    private final String jwt;
    private final CustomerDTO customer;

    public AuthenticationResponse(String jwt, CustomerDTO customer) {
        this.jwt = jwt;
        this.customer = customer;
    }

    public String getJwt() { return jwt; }
    public CustomerDTO getCustomer() { return customer; }
}