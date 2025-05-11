package com.example.bookstore.Service;

import com.example.bookstore.Exception.ResourceNotFoundException;
import com.example.bookstore.DTO.AddressDTO;
import com.example.bookstore.Entity.Address;
import com.example.bookstore.Entity.Customer;
import com.example.bookstore.Mapper.AddressMapper;
import com.example.bookstore.Repository.AddressRepository;
import com.example.bookstore.Repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressMapper addressMapper;

    public List<AddressDTO> getAddressesByCustomer(Long customerId) {
        logger.info("Fetching addresses for customer id: {}", customerId);
        return addressRepository.findByCustomerId(customerId).stream()
                .map(addressMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AddressDTO createAddress(AddressDTO addressDTO) {
        logger.info("Creating address for customer id: {}", addressDTO.getCustomerId());
        Customer customer = customerRepository.findById(addressDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + addressDTO.getCustomerId()));
        Address address = addressMapper.toEntity(addressDTO);
        address.setCustomer(customer);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDTO(savedAddress);
    }
}
