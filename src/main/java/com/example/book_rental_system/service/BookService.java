package com.example.book_rental_system.service;

import com.example.book_rental_system.dto.BookDto;
import com.example.book_rental_system.entity.Book;
import com.example.book_rental_system.exception.BookAlreadyRentedException;
import com.example.book_rental_system.exception.ResourceNotFoundException;
import com.example.book_rental_system.repositry.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Add a new book
    public Book addBook(BookDto dto) {
        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .isAvailable(true)
                .build();
        return bookRepository.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by ID
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));
    }

    // Update book availability
    public Book updateAvailability(Long id, boolean available) {
        Book book = getBook(id);

        // Optional: If trying to mark as unavailable but already rented
        if (!available && !book.isAvailable()) {
            throw new BookAlreadyRentedException("Book '" + book.getTitle() + "' is already rented");
        }

        book.setAvailable(available);
        return bookRepository.save(book);
    }

    // Delete a book
    public void deleteBook(Long id) {
        // Throws exception if book not found
        Book book = getBook(id);
        bookRepository.delete(book);
    }
}
