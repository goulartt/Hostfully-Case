package com.hostfully.technicaltest.services;

import com.hostfully.technicaltest.dtos.GuestNameUpdateDTO;
import com.hostfully.technicaltest.entities.Block;
import com.hostfully.technicaltest.entities.Booking;
import com.hostfully.technicaltest.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @InjectMocks
    BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    BlockService blockService;  // Using BlockService now instead of BlockRepository

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBooking() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(5));

        when(bookingRepository.save(booking)).thenReturn(booking);
        Booking result = bookingService.saveBooking(booking);
        assertEquals(result, booking);
    }

    @Test
    void findById() {
        Booking booking = new Booking();
        booking.setId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Optional<Booking> result = bookingService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), 1L);
    }


    @Test
    void findAll() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));
        List<Booking> result = bookingService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void updateBooking() {
        Booking oldBooking = new Booking();
        oldBooking.setId(1L);
        oldBooking.setStartDate(LocalDate.now());
        oldBooking.setEndDate(LocalDate.now().plusDays(5));

        Booking updatedBooking = new Booking();
        updatedBooking.setId(1L);
        updatedBooking.setStartDate(LocalDate.now().plusDays(1));
        updatedBooking.setEndDate(LocalDate.now().plusDays(6));

        when(bookingRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.save(updatedBooking)).thenReturn(updatedBooking);

        Booking result = bookingService.updateBooking(1L, updatedBooking);
        assertEquals(result.getStartDate(), updatedBooking.getStartDate());
        assertEquals(result.getEndDate(), updatedBooking.getEndDate());
    }

    @Test
    void deleteBooking() {
        when(bookingRepository.existsById(1L)).thenReturn(true);
        bookingService.deleteBooking(1L);
        verify(bookingRepository, times(1)).deleteById(1L);
    }

    @Test
    void saveBookingWithOverlap() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(5));

        when(blockService.findBlocksInDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(new Block()));

        assertThrows(RuntimeException.class, () -> bookingService.saveBooking(booking));
    }


    @Test
    public void updateGuestName_ExistingBooking_UpdatesName() {
        Long id = 1L;
        Booking booking = new Booking();
        booking.setId(id);
        booking.setGuestName("Old Name");

        GuestNameUpdateDTO updateDTO = new GuestNameUpdateDTO();
        updateDTO.setGuestName("New Name");

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking updatedBooking = bookingService.updateGuestName(id, updateDTO);

        assertEquals("New Name", updatedBooking.getGuestName());
        verify(bookingRepository).save(booking);
    }


    @Test
    public void updateGuestName_NonExistingBooking_ThrowsException() {
        Long id = 1L;
        GuestNameUpdateDTO updateDTO = new GuestNameUpdateDTO();
        updateDTO.setGuestName("New Name");

        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            bookingService.updateGuestName(id, updateDTO);
        });
    }
}
