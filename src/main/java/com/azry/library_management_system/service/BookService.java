package com.azry.library_management_system.service;

import com.azry.library_management_system.dto.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    BookDTO addBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);
    List<BookDTO> searchBooks(String query);
    void borrowBook(Long userId, Long bookId);
    void returnBook(Long userId, Long bookId);
}