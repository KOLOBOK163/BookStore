package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.CustomerDTO;
import com.example.bookstore.Entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T01:24:45+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setRoles( rolesToNames( customer.getRoles() ) );
        customerDTO.setId( customer.getId() );
        customerDTO.setLogin( customer.getLogin() );
        customerDTO.setEmail( customer.getEmail() );
        customerDTO.setFullName( customer.getFullName() );

        return customerDTO;
    }

    @Override
    public Customer toEntity(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( customerDTO.getId() );
        customer.setLogin( customerDTO.getLogin() );
        customer.setPassword( customerDTO.getPassword() );
        customer.setEmail( customerDTO.getEmail() );
        customer.setFullName( customerDTO.getFullName() );

        return customer;
    }
}
