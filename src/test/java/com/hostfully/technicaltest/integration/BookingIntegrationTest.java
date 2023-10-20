package com.hostfully.technicaltest.integration;


import com.hostfully.technicaltest.dtos.GuestNameUpdateDTO;
import com.hostfully.technicaltest.entities.Block;
import com.hostfully.technicaltest.entities.Booking;
import com.hostfully.technicaltest.repositories.BlockRepository;
import com.hostfully.technicaltest.repositories.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BlockRepository blockRepository;

    @BeforeEach
    public void setup() {
        // setup data or clean repositories if needed
    }

    @Test
    public void testCreateBookingWithoutOverlaps() {
        Booking booking = Booking.builder()
                .guestName("Joao")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusDays(5)).build();

        ResponseEntity<Booking> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings", booking, Booking.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testCreateBookingWithBookingOverlap() {

        this.testCreateBookingWithoutOverlaps();

        Booking booking = Booking.builder()
                .guestName("Pedro")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusDays(3)).build();

        ResponseEntity<Booking> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings", booking, Booking.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreatingBlock() {
        Block block = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusDays(5)).build();

        ResponseEntity<Block> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/blocks", block, Block.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testCreateBookingWithBlockOverlap() {

        this.testCreatingBlock();

        Booking booking = Booking.builder()
                .guestName("Pedro")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusDays(3)).build();

        ResponseEntity<Booking> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings", booking, Booking.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreatingOverlappingBlocks() {
        Block block1 = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusDays(5)).build();

        ResponseEntity<Block> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/blocks", block1, Block.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());

        Block block2 = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusDays(3)).build();

        ResponseEntity<Block> response2= restTemplate.postForEntity(
                "http://localhost:" + port + "/api/blocks", block2, Block.class);

        assertNotNull(response2.getBody());
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        assertNotNull(response2.getBody().getId());
    }

    @Test
    public void testUpdateGuestName() {
        // 1. Create a booking to be updated
        Booking booking = Booking.builder()
                .guestName("Initial Name")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .build();

        ResponseEntity<Booking> createdBookingResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings", booking, Booking.class);

        Booking createdBooking = createdBookingResponse.getBody();
        assertNotNull(createdBooking);

        Long bookingId = createdBooking.getId();

        // 2. Prepare the update data
        GuestNameUpdateDTO updateDTO = new GuestNameUpdateDTO();
        updateDTO.setGuestName("Updated Name");

        // 3. Send PUT request to update the guest name
        restTemplate.put("http://localhost:" + port + "/api/bookings/" + bookingId, updateDTO);

        // 4. Retrieve the updated booking
        ResponseEntity<Booking> updatedBookingResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/bookings/" + bookingId, Booking.class);

        // 5. Assert the name was updated
        assertEquals("Updated Name", updatedBookingResponse.getBody().getGuestName());
    }

    @Test
    public void testCancelBooking() {
        Booking booking = Booking.builder()
                .guestName("Joao")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .build();
        booking = bookingRepository.save(booking);

        restTemplate.delete("http://localhost:" + port + "/api/bookings/" + booking.getId());

        Booking canceledBooking = bookingRepository.findById(booking.getId()).orElse(null);
        assertNull(canceledBooking);
    }

    @Test
    public void testRebookBooking() {
        // 1. Create an initial booking
        Booking initialBooking = Booking.builder()
                .guestName("John Doe")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build();

        ResponseEntity<Booking> initialBookingResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings", initialBooking, Booking.class);

        Booking createdBooking = initialBookingResponse.getBody();
        assertNotNull(createdBooking);

        Long initialBookingId = createdBooking.getId();

        // 2. Prepare the new booking data
        Booking newBooking = Booking.builder()
                .guestName("Jane Smith")
                .startDate(LocalDate.now().plusDays(4))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        // 3. Send POST request to rebook
        ResponseEntity<Booking> rebookResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings/" + initialBookingId + "/rebook", newBooking, Booking.class);

        Booking rebookedBooking = rebookResponse.getBody();
        assertNotNull(rebookedBooking);

        // 4. Assert that the initial booking was deleted
        ResponseEntity<Booking> deletedBookingResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/bookings/" + initialBookingId, Booking.class);
        assertEquals(HttpStatus.NOT_FOUND, deletedBookingResponse.getStatusCode());

        // 5. Assert the new booking was created
        ResponseEntity<Booking> fetchedRebookedBookingResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/bookings/" + rebookedBooking.getId(), Booking.class);
        assertEquals(HttpStatus.OK, fetchedRebookedBookingResponse.getStatusCode());
        assertEquals("Jane Smith", fetchedRebookedBookingResponse.getBody().getGuestName());
    }

    @Test
    public void testRebookNonExistentBooking() {
        // 1. Use a non-existent booking ID
        Long nonExistentBookingId = 9999L; // Assuming this ID doesn't exist in the test database

        // 2. Prepare the new booking data
        Booking newBooking = Booking.builder()
                .guestName("Jane Smith")
                .startDate(LocalDate.now().plusDays(4))
                .endDate(LocalDate.now().plusDays(7))
                .build();

        // 3. Send POST request to rebook and expect a RuntimeException
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bookings/" + nonExistentBookingId + "/rebook", newBooking, String.class);

        // 4. Assert that the response contains the exception message and has the expected status code
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Booking not found."));
    }


    @AfterEach
    public void tearDown() {
        // Clean up data after each test
        bookingRepository.deleteAll();
        blockRepository.deleteAll();
    }
}