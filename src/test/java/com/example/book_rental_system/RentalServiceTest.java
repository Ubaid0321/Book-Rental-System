package com.example.book_rental_system;

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
import com.example.book_rental_system.service.RentalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentalService rentalService;

    private RentalDto createRentalDto(Long userId, Long bookId) {
        return new RentalDto(
                null,       // id
                userId,     // userId
                bookId,     // bookId
                null,       // rentalDate
                null,       // returnDate
                null        // status
        );
    }

    @Test
    void testRentBookSuccess() {
        RentalDto dto = new RentalDto(
                null,
                1L,
                1L,
                LocalDate.now(),
                null,
                "RENTED"
        );
        User user = User.builder().id(1L).build();
        Book book = Book.builder().id(1L).isAvailable(true).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(rentalRepository.countByUserIdAndStatus(1L, "RENTED")).thenReturn(0L);

        Rental rental = Rental.builder().id(1L).user(user).book(book).status("RENTED").rentalDate(LocalDate.now()).build();
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);
        when(bookRepository.save(book)).thenReturn(book);

        Rental result = rentalService.rentBook(dto);
        assertNotNull(result);
        assertEquals("RENTED", result.getStatus());
        assertFalse(book.isAvailable());
    }

    @Test
    void testRentBookUserNotFound() {
        RentalDto dto = createRentalDto(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> rentalService.rentBook(dto));
    }

    @Test
    void testRentBookBookNotFound() {
        RentalDto dto = createRentalDto(1L, 1L);
        User user = User.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.rentBook(dto));
    }

    @Test
    void testRentBookAlreadyRented() {
        RentalDto dto = createRentalDto(1L, 1L);
        User user = User.builder().id(1L).build();
        Book book = Book.builder().id(1L).isAvailable(false).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookAlreadyRentedException.class, () -> rentalService.rentBook(dto));
    }

    @Test
    void testRentBookMaxLimitExceeded() {
        RentalDto dto = createRentalDto(1L, 1L);
        User user = User.builder().id(1L).build();
        Book book = Book.builder().id(1L).isAvailable(true).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(rentalRepository.countByUserIdAndStatus(1L, "RENTED")).thenReturn(5L);

        assertThrows(InvalidRequestException.class, () -> rentalService.rentBook(dto));
    }

    @Test
    void testReturnBookSuccess() {
        Book book = Book.builder().id(1L).isAvailable(false).build();
        Rental rental = Rental.builder().id(1L).book(book).status("RENTED").build();

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(bookRepository.save(book)).thenReturn(book);
        when(rentalRepository.save(rental)).thenReturn(rental);

        Rental result = rentalService.returnBook(1L);

        assertEquals("RETURNED", result.getStatus());
        assertTrue(book.isAvailable());

        verify(rentalRepository).findById(1L);
        verify(bookRepository).save(book);
        verify(rentalRepository).save(rental);
    }

    @Test
    void testReturnBookNotFound() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> rentalService.returnBook(1L));
    }
}
