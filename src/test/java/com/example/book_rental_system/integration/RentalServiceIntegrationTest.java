package com.example.book_rental_system.integration;

import com.example.book_rental_system.dto.RentalDto;
import com.example.book_rental_system.entity.Book;
import com.example.book_rental_system.entity.Rental;
import com.example.book_rental_system.entity.User;
import com.example.book_rental_system.exception.BookAlreadyRentedException;
import com.example.book_rental_system.exception.ResourceNotFoundException;
import com.example.book_rental_system.repositry.BookRepository;
import com.example.book_rental_system.repositry.RentalRepository;
import com.example.book_rental_system.repositry.UserRepository;
import com.example.book_rental_system.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")  // ✅ Uses application-test.properties with H2
@Transactional            // ✅ Rollback after each test for clean DB
public class RentalServiceIntegrationTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentalRepository rentalRepository;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setup() {
        // Clean database before each test
        rentalRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(User.builder()
                .name("Ali")
                .email("ali@example.com")
                .role("STUDENT")
                .build());

        testBook = bookRepository.save(Book.builder()
                .title("Java Basics")
                .author("James Gosling")
                .genre("Programming")
                .isAvailable(true)
                .build());
    }

    @Test
    void rentBook_success() {
        RentalDto dto = RentalDto.builder()
                .userId(testUser.getId())
                .bookId(testBook.getId())
                .build();

        Rental rental = rentalService.rentBook(dto);

        assertThat(rental.getId()).isNotNull();
        assertThat(rental.getStatus()).isEqualTo("RENTED");
        assertThat(rental.getBook().isAvailable()).isFalse();
    }

    @Test
    void rentBook_bookAlreadyRented_shouldThrow() {
        // First rent succeeds
        RentalDto dto = RentalDto.builder()
                .userId(testUser.getId())
                .bookId(testBook.getId())
                .build();
        rentalService.rentBook(dto);

        // Second rent should fail
        assertThrows(BookAlreadyRentedException.class, () -> rentalService.rentBook(dto));
    }

    @Test
    void returnBook_success() {
        RentalDto dto = RentalDto.builder()
                .userId(testUser.getId())
                .bookId(testBook.getId())
                .build();
        Rental rental = rentalService.rentBook(dto);

        Rental returned = rentalService.returnBook(rental.getId());

        assertThat(returned.getStatus()).isEqualTo("RETURNED");
        assertThat(returned.getReturnDate()).isEqualTo(LocalDate.now());
        assertThat(returned.getBook().isAvailable()).isTrue();
    }

    @Test
    void rentBook_userNotFound_shouldThrow() {
        RentalDto dto = RentalDto.builder()
                .userId(999L)  // non-existing
                .bookId(testBook.getId())
                .build();

        assertThrows(ResourceNotFoundException.class, () -> rentalService.rentBook(dto));
    }

    @Test
    void rentBook_bookNotFound_shouldThrow() {
        RentalDto dto = RentalDto.builder()
                .userId(testUser.getId())
                .bookId(999L)  // non-existing
                .build();

        assertThrows(ResourceNotFoundException.class, () -> rentalService.rentBook(dto));
    }
}
