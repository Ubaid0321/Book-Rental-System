package com.example.book_rental_system.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;
    @Size(min = 2, max = 50, message = "Author name must be between 2 and 50 characters")

    @NotBlank(message = "Author is required")
    private String author;

    private String genre;
    private boolean available;
}
