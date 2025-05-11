package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.BookDTO;
import com.example.bookstore.Entity.Book;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T13:23:10+0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDTO toDTO(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();

        bookDTO.setCoverImage( book.getCoverImage() );
        bookDTO.setDiscountPercentage( book.getDiscountPercentage() );
        bookDTO.setId( book.getId() );
        bookDTO.setTitle( book.getTitle() );
        bookDTO.setAuthor( book.getAuthor() );
        bookDTO.setPrice( book.getPrice() );
        bookDTO.setCategory( book.getCategory() );
        bookDTO.setDescription( book.getDescription() );
        bookDTO.setPublisher( book.getPublisher() );
        bookDTO.setPublicationYear( book.getPublicationYear() );
        bookDTO.setIsbn( book.getIsbn() );
        bookDTO.setStock( book.getStock() );

        return bookDTO;
    }

    @Override
    public Book toEntity(BookDTO bookDTO) {
        if ( bookDTO == null ) {
            return null;
        }

        Book book = new Book();

        book.setCoverImage( bookDTO.getCoverImage() );
        book.setDiscountPercentage( bookDTO.getDiscountPercentage() );
        book.setId( bookDTO.getId() );
        book.setTitle( bookDTO.getTitle() );
        book.setAuthor( bookDTO.getAuthor() );
        book.setPrice( bookDTO.getPrice() );
        book.setCategory( bookDTO.getCategory() );
        book.setDescription( bookDTO.getDescription() );
        book.setPublisher( bookDTO.getPublisher() );
        if ( bookDTO.getPublicationYear() != null ) {
            book.setPublicationYear( bookDTO.getPublicationYear() );
        }
        book.setIsbn( bookDTO.getIsbn() );
        if ( bookDTO.getStock() != null ) {
            book.setStock( bookDTO.getStock() );
        }

        return book;
    }
}
