package com.example.book_rental_system.service;

import com.example.book_rental_system.dto.UserDto;
import com.example.book_rental_system.entity.User;
import com.example.book_rental_system.exception.ResourceNotFoundException;
import com.example.book_rental_system.repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Create a new user
    public User createUser(UserDto dto) {
        // Optional: you can add a check if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email '" + dto.getEmail() + "' is already in use");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();

        return userRepository.save(user);
    }

    // Get a user by ID
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
