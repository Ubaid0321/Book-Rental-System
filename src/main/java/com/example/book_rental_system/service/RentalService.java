package com.example.book_rental_system.service;

import com.example.book_rental_system.dto.RentalDto;
import com.example.book_rental_system.entity.Book;
import com.example.book_rental_system.entity.Rental;
import com.example.book_rental_system.entity.User;
import com.example.book_rental_system.repositry.BookRepository;
import com.example.book_rental_system.repositry.RentalRepository;
import com.example.book_rental_system.repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // Rent a book
    public Rental rentBook(RentalDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is already rented");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        Rental rental = Rental.builder()
                .user(user)
                .book(book)
                .rentalDate(LocalDate.now())
                .status("RENTED")
                .build();

        return rentalRepository.save(rental);
    }

    // Return a book
    public Rental returnBook(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setReturnDate(LocalDate.now());
        rental.setStatus("RETURNED");

        Book book = rental.getBook(); // ✅ use the book reference
        book.setAvailable(true);
        bookRepository.save(book);

        return rentalRepository.save(rental);
    }

    // Get all rentals for a specific user
    public List<Rental> getUserRentals(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    // Get all currently rented books
    public List<Rental> getActiveRentals() {
        return rentalRepository.findAll().stream()
                .filter(r -> "RENTED".equals(r.getStatus()))
                .toList();
    }
}
