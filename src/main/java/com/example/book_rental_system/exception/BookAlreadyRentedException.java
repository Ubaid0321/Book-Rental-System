package com.example.book_rental_system.exception;


public class BookAlreadyRentedException extends RuntimeException {

    public BookAlreadyRentedException(String message) {
        super(message);
    }

    public BookAlreadyRentedException(String message, Throwable cause) {
        super(message, cause);
    }
}
