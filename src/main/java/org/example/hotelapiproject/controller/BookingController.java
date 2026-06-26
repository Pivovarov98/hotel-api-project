package org.example.hotelapiproject.controller;

import org.example.hotelapiproject.dto.booking_dto.BookingCreateDTO;
import org.example.hotelapiproject.dto.booking_dto.BookingResponseDTO;
import org.example.hotelapiproject.entity.Booking;
import org.example.hotelapiproject.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(dto));
    }

    @DeleteMapping("/{book_id}")
    public ResponseEntity<Booking> deleteBookById(@PathVariable Long book_id){
        bookingService.deleteBookingByID(book_id);
        return ResponseEntity.noContent().build();
    }
}
