package com.example.book_rental_system.repositry;

import com.example.book_rental_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

