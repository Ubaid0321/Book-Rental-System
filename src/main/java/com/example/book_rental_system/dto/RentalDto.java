package com.example.book_rental_system.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
    private Long id;
    @NotNull(message = "User ID is required")

    private Long userId;
    @NotNull(message = "Book ID is required")

    private Long bookId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private String status;
}
