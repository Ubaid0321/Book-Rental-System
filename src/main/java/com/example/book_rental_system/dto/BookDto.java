package com.example.book_rental_system.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;

    @NotBlank
    private String title;

    private String author;
    private String genre;
    private boolean isAvailable;
}