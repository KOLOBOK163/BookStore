package com.example.bookstore.Service;

import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with login: " + username));
        List<SimpleGrantedAuthority> authorities = customer.getRoles() != null
                ? customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // Добавляем префикс ROLE_
                .collect(Collectors.toList())
                : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // Роль по умолчанию с префиксом
        return new org.springframework.security.core.userdetails.User(
                customer.getLogin(),
                customer.getPassword(),
                authorities
        );
    }
}

