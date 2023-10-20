package com.hostfully.technicaltest.controllers;

import com.hostfully.technicaltest.dtos.GuestNameUpdateDTO;
import com.hostfully.technicaltest.entities.Booking;
import com.hostfully.technicaltest.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return new ResponseEntity<>(bookingService.saveBooking(booking), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.of(bookingService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateGuestName(@PathVariable Long id, @RequestBody GuestNameUpdateDTO guestNameUpdate) {
        return new ResponseEntity<>(bookingService.updateGuestName(id, guestNameUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/rebook")
    public ResponseEntity<Booking> rebook(@PathVariable Long id, @RequestBody Booking newBooking) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(bookingService.saveBooking(newBooking), HttpStatus.CREATED);
    }
}