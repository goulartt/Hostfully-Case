package com.hostfully.technicaltest.services;


import com.hostfully.technicaltest.dtos.GuestNameUpdateDTO;
import com.hostfully.technicaltest.entities.Block;
import com.hostfully.technicaltest.entities.Booking;
import com.hostfully.technicaltest.repositories.BlockRepository;
import com.hostfully.technicaltest.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BlockService blockService;

    // Create
    public Booking saveBooking(Booking booking) {
        if (isOverlapping(booking.getStartDate(), booking.getEndDate())) {
            throw new RuntimeException("Booking dates overlap with existing bookings or blocks.");
        }
        return bookingRepository.save(booking);
    }

    // Read (by Id)
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    // Read (all)
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    // Update
    public Booking updateBooking(Long id, Booking updatedBooking) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found.");
        }
        if (isOverlapping(updatedBooking.getStartDate(), updatedBooking.getEndDate())) {
            throw new RuntimeException("Updated dates overlap with existing bookings or blocks.");
        }
        return bookingRepository.save(updatedBooking);
    }

    // Delete
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found.");
        }
        bookingRepository.deleteById(id);
    }

    private boolean isOverlapping(LocalDate start, LocalDate end) {
        List<Booking> overlappingBookings = bookingRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(end, start);

        // Example of how you might integrate with BlockService
        List<Block> overlappingBlocks = blockService.findBlocksInDateRange(start, end);  // Assuming you've added this method in BlockService

        return !overlappingBookings.isEmpty() || !overlappingBlocks.isEmpty();
    }

    public Booking updateGuestName(Long id, GuestNameUpdateDTO guestNameUpdate) {
        Booking existingBooking = bookingRepository.findById(id).orElse(null);

        if (existingBooking == null) {
            throw new EntityNotFoundException("Booking not found with id " + id);
        }

        existingBooking.setGuestName(guestNameUpdate.getGuestName());
        return bookingRepository.save(existingBooking);
    }
}
