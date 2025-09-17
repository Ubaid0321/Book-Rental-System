package com.example.book_rental_system.service;

import com.example.book_rental_system.dto.RentalDto;
import com.example.book_rental_system.entity.Book;
import com.example.book_rental_system.entity.Rental;
import com.example.book_rental_system.entity.User;
import com.example.book_rental_system.exception.BookAlreadyRentedException;
import com.example.book_rental_system.exception.InvalidRequestException;
import com.example.book_rental_system.exception.ResourceNotFoundException;
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

        // 1️⃣ Validate user
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + dto.getUserId() + " not found"));

        // 2️⃣ Validate book
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + dto.getBookId() + " not found"));

        // 3️⃣ Check if book is already rented
        if (!book.isAvailable()) {
            throw new BookAlreadyRentedException("Book '" + book.getTitle() + "' is already rented");
        }

        // 4️⃣ Optional: Check user rental limit (max 5 books)
        long rentedCount = rentalRepository.countByUserIdAndStatus(user.getId(), "RENTED");
        if (rentedCount >= 5) {
            throw new InvalidRequestException("User cannot rent more than 5 books at a time");
        }

        // 5️⃣ Update book availability
        book.setAvailable(false);
        bookRepository.save(book);

        // 6️⃣ Create rental record
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
                .orElseThrow(() -> new ResourceNotFoundException("Rental with ID " + rentalId + " not found"));

        rental.setReturnDate(LocalDate.now());
        rental.setStatus("RETURNED");

        // Update book availability
        Book book = rental.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return rentalRepository.save(rental);
    }

    // Get all rentals for a specific user
    public List<Rental> getUserRentals(Long userId) {
        // Validate user existence
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

        return rentalRepository.findByUserId(userId);
    }

    // Get all currently rented books
    public List<Rental> getActiveRentals() {
        return rentalRepository.findAll().stream()
                .filter(r -> "RENTED".equals(r.getStatus()))
                .toList();
    }
}
