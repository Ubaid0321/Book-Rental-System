package com.example.book_rental_system.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")

    @Email(message = "Invalid email")
    private String email;

    private String role;
}
