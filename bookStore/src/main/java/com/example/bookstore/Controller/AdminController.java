package com.example.bookstore.Controller;

import com.example.bookstore.DTO.BookDTO;
import com.example.bookstore.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    @Autowired
    private BookService bookService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Operation(summary = "Add a new book with cover (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping(value = "/books", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> addBook(
            @RequestPart("book") String bookJson,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        System.out.println("Received book JSON: " + bookJson);
        if (bookJson == null || bookJson.trim().isEmpty()) {
            throw new IllegalArgumentException("Book data is required");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        BookDTO bookDTO;
        try {
            bookDTO = objectMapper.readValue(bookJson, BookDTO.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid book JSON format: " + e.getMessage(), e);
        }

        System.out.println("Parsed bookDTO: " + bookDTO);

        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            System.out.println("Created directory: " + uploadDir);
        }

        if (coverImage != null && !coverImage.isEmpty()) {
            System.out.println("Processing cover image: " + coverImage.getOriginalFilename());
            String fileName = UUID.randomUUID() + "_" + coverImage.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, coverImage.getBytes());
            bookDTO.setCoverImage("/uploads/covers/" + fileName);
            System.out.println("Saved cover image at: " + filePath);
        }

        BookDTO savedBook = bookService.createBook(bookDTO);
        return ResponseEntity.ok(savedBook);
    }

    @Operation(summary = "Update an existing book with cover (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping(value = "/books/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @RequestPart("book") String bookJson,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        System.out.println("Received book JSON: " + bookJson);
        if (bookJson == null || bookJson.trim().isEmpty()) {
            throw new IllegalArgumentException("Book data is required");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        BookDTO bookDTO;
        try {
            bookDTO = objectMapper.readValue(bookJson, BookDTO.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid book JSON format: " + e.getMessage(), e);
        }

        System.out.println("Parsed bookDTO: " + bookDTO);

        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            System.out.println("Created directory: " + uploadDir);
        }

        BookDTO existingBook = bookService.getBookById(id);
        String oldCoverImagePath = existingBook.getCoverImage();

        if (coverImage != null && !coverImage.isEmpty()) {
            System.out.println("Processing cover image: " + coverImage.getOriginalFilename());
            String fileName = UUID.randomUUID() + "_" + coverImage.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, coverImage.getBytes());
            bookDTO.setCoverImage("/uploads/covers/" + fileName);
            System.out.println("Saved cover image at: " + filePath);

            if (oldCoverImagePath != null && !oldCoverImagePath.isEmpty()) {
                String oldFileName = oldCoverImagePath.replace("/uploads/covers/", "");
                Path oldFilePath = Paths.get(uploadDir, oldFileName);
                try {
                    Files.deleteIfExists(oldFilePath);
                    System.out.println("Deleted old cover image at: " + oldFilePath);
                } catch (IOException e) {
                    System.out.println("Failed to delete old cover image: " + e.getMessage());
                }
            }
        } else {
            bookDTO.setCoverImage(oldCoverImagePath);
        }

        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}