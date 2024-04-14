package com.azry.library_management_system.service.impl;

import com.azry.library_management_system.dto.BookDTO;
import com.azry.library_management_system.exception.BookAlreadyBorrowedException;
import com.azry.library_management_system.exception.BookAlreadyReturnedException;
import com.azry.library_management_system.exception.BookNotFoundException;
import com.azry.library_management_system.exception.UserNotFoundException;
import com.azry.library_management_system.model.Book;
import com.azry.library_management_system.model.BookStatus;
import com.azry.library_management_system.repository.BookRepository;
import com.azry.library_management_system.repository.UserRepository;
import com.azry.library_management_system.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(BookDTO::new).toList();
    }

    @Override
    public BookDTO getBookById(Long id) {
        return new BookDTO(bookRepository.findById(id).orElseThrow(() ->  new BookNotFoundException("Book not found")));
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setStatus(BookStatus.AVAILABLE);

        return new BookDTO(bookRepository.save(book));
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElseThrow(() ->  new BookNotFoundException("Book not found"));
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());

        return new BookDTO(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        bookRepository.delete(book);
    }

    @Override
    public List<BookDTO> searchBooks(String query) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContaining(query, query, query);
        return books.stream().map(BookDTO::new).collect(Collectors.toList());
    }

    @Override
    public void borrowBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));
        if (book.getStatus() == BookStatus.AVAILABLE) {
            book.setStatus(BookStatus.BORROWED);
            book.setUser(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found")));
            bookRepository.save(book);
            bookRepository.save(book);
        } else {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }
    }

    @Override
    public void returnBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));
        if (book.getStatus() == BookStatus.BORROWED) {
            book.setStatus(BookStatus.AVAILABLE);
            book.setUser(null);
            bookRepository.save(book);
        } else {
            throw new BookAlreadyReturnedException("Book is already returned");
        }
    }
}