package com.example.book_rental_system.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
    private Long id;

    @NotNull
    private LocalDate rentalDate;

    private LocalDate returnDate;
    private String status;

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;
}

