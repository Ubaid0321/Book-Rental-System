package com.example.book_rental_system.repository;

import com.example.book_rental_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // useful for login/checking uniqueness
}
