package com.example.book_rental_system.repositry;


import com.example.book_rental_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

