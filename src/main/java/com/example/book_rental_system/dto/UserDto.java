package com.example.book_rental_system.dto;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String role;
}