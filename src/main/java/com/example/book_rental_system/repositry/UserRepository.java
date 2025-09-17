package com.example.book_rental_system.repositry;


import com.example.book_rental_system.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters") @Email(message = "Invalid email") String email);
}

