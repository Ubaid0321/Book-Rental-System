package com.example.book_rental_system;

import com.example.book_rental_system.dto.BookDto;
import com.example.book_rental_system.entity.Book;
import com.example.book_rental_system.exception.BookAlreadyRentedException;
import com.example.book_rental_system.exception.ResourceNotFoundException;
import com.example.book_rental_system.repositry.BookRepository;
import com.example.book_rental_system.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBook() {
        BookDto dto = new BookDto(1L,"Java", "Author", "Programming",true);
        Book savedBook = Book.builder().id(1L).title("Java").author("Author").genre("Programming").isAvailable(true).build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Book result = bookService.addBook(dto);

        assertNotNull(result);
        assertEquals("Java", result.getTitle());
        assertTrue(result.isAvailable());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = List.of(Book.builder().id(1L).build(), Book.builder().id(2L).build());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
    }

    @Test
    void testGetBookFound() {
        Book book = Book.builder().id(1L).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBook(1L);
        assertNotNull(result);
    }

    @Test
    void testGetBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBook(1L));
    }

    @Test
    void testUpdateAvailabilitySuccess() {
        Book book = Book.builder().id(1L).isAvailable(true).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.updateAvailability(1L, false);
        assertFalse(result.isAvailable());
        verify(bookRepository).save(book);
    }

    @Test
    void testUpdateAvailabilityAlreadyRented() {
        Book book = Book.builder().id(1L).isAvailable(false).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BookAlreadyRentedException.class, () -> bookService.updateAvailability(1L, false));
    }

    @Test
    void testDeleteBookSuccess() {
        Book book = Book.builder().id(1L).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);
        verify(bookRepository).delete(book);
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));
    }
}
