package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.BonusCardDTO;
import com.example.bookstore.Entity.BonusCard;
import com.example.bookstore.Entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T00:29:57+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class BonusCardMapperImpl implements BonusCardMapper {

    @Override
    public BonusCardDTO toDTO(BonusCard bonusCard) {
        if ( bonusCard == null ) {
            return null;
        }

        BonusCardDTO bonusCardDTO = new BonusCardDTO();

        bonusCardDTO.setCustomerId( bonusCardCustomerId( bonusCard ) );
        bonusCardDTO.setId( bonusCard.getId() );
        bonusCardDTO.setCardNumber( bonusCard.getCardNumber() );
        bonusCardDTO.setPoints( bonusCard.getPoints() );

        return bonusCardDTO;
    }

    @Override
    public BonusCard toEntity(BonusCardDTO bonusCardDTO) {
        if ( bonusCardDTO == null ) {
            return null;
        }

        BonusCard bonusCard = new BonusCard();

        bonusCard.setId( bonusCardDTO.getId() );
        bonusCard.setCardNumber( bonusCardDTO.getCardNumber() );
        bonusCard.setPoints( bonusCardDTO.getPoints() );

        return bonusCard;
    }

    private Long bonusCardCustomerId(BonusCard bonusCard) {
        Customer customer = bonusCard.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }
}
