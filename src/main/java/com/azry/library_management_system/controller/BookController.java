package com.azry.library_management_system.controller;

import com.azry.library_management_system.dto.BookDTO;
import com.azry.library_management_system.messaging.BookBorrowEvent;
import com.azry.library_management_system.messaging.BookReturnEvent;
import com.azry.library_management_system.messaging.JmsEventProducer;
import com.azry.library_management_system.rate_limiting.RateLimitProtection;
import com.azry.library_management_system.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Endpoints for managing books")
public class BookController {

    private final BookService bookService;

    private final JmsEventProducer jmsEventProducer;


    @GetMapping
    @RateLimitProtection
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @RateLimitProtection
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDTO> getBookById(@PathVariable @NotNull Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new book")
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO newBook = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book details")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody BookDTO bookDTO
    ) {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete book by ID")
    public ResponseEntity<Void> deleteBook(@PathVariable @NotNull Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @RateLimitProtection
    @GetMapping("/search")
    @Operation(summary = "Search books by query")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam @NotBlank String query) {
        List<BookDTO> books = bookService.searchBooks(query);
        return ResponseEntity.ok(books);
    }

    @RateLimitProtection
    @PostMapping("/{bookId}/borrow/{userId}")
    @Operation(summary = "Borrow a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed"),
            @ApiResponse(responseCode = "404", description = "Book or user not found")
    })
    public ResponseEntity<Void> borrowBook(
            @PathVariable @NotNull Long userId,
            @PathVariable @NotNull Long bookId
    ) {
        bookService.borrowBook(userId, bookId);
        jmsEventProducer.sendBorrowEvent(new BookBorrowEvent(userId, bookId));
        return ResponseEntity.ok().build();
    }

    @RateLimitProtection
    @PostMapping("/{bookId}/return/{userId}")
    @Operation(summary = "Return a borrowed book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned"),
            @ApiResponse(responseCode = "404", description = "Book or user not found")
    })
    public ResponseEntity<Void> returnBook(
            @PathVariable @NotNull Long userId,
            @PathVariable @NotNull Long bookId
    ) {
        bookService.returnBook(userId, bookId);
        jmsEventProducer.sendReturnEvent(new BookReturnEvent(userId, bookId));
        return ResponseEntity.ok().build();
    }
}
