package com.example.book_rental_system.controller;

import com.example.book_rental_system.dto.RentalDto;
import com.example.book_rental_system.entity.Rental;
import com.example.book_rental_system.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentBook(@RequestBody RentalDto dto) {
        return ResponseEntity.ok(rentalService.rentBook(dto));
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Rental> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.returnBook(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rental>> getUserRentals(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.getUserRentals(userId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Rental>> getActiveRentals() {
        return ResponseEntity.ok(rentalService.getActiveRentals());
    }
}

