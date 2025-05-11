package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.AddressDTO;
import com.example.bookstore.Entity.Address;
import com.example.bookstore.Entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T00:29:57+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDTO toDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setCustomerId( addressCustomerId( address ) );
        addressDTO.setId( address.getId() );
        addressDTO.setCity( address.getCity() );
        addressDTO.setStreet( address.getStreet() );
        addressDTO.setHouse( address.getHouse() );
        addressDTO.setApartment( address.getApartment() );

        return addressDTO;
    }

    @Override
    public Address toEntity(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Address address = new Address();

        address.setId( addressDTO.getId() );
        address.setCity( addressDTO.getCity() );
        address.setStreet( addressDTO.getStreet() );
        address.setHouse( addressDTO.getHouse() );
        address.setApartment( addressDTO.getApartment() );

        return address;
    }

    private Long addressCustomerId(Address address) {
        Customer customer = address.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }
}
