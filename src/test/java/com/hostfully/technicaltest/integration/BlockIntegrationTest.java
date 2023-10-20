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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlockIntegrationTest {

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
    public void testCreateBlock() {
        Block block = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build();

        ResponseEntity<Block> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/blocks", block, Block.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testGetBlockById() {
        Block block = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build();
        block = blockRepository.save(block);

        ResponseEntity<Block> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/blocks/" + block.getId(), Block.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(block.getId(), response.getBody().getId());
    }

    @Test
    public void testGetAllBlocks() {
        blockRepository.save(Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build());
        blockRepository.save(Block.builder()
                .startDate(LocalDate.now().plusDays(4))
                .endDate(LocalDate.now().plusDays(6))
                .build());

        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/blocks", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateBlock() {
        Block block = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build();
        block = blockRepository.save(block);

        block.setEndDate(LocalDate.now().plusDays(5));

        restTemplate.put("http://localhost:" + port + "/api/blocks/" + block.getId(), block);

        Block updatedBlock = blockRepository.findById(block.getId()).orElse(null);

        assertEquals(LocalDate.now().plusDays(5), updatedBlock.getEndDate());
    }

    @Test
    public void testDeleteBlock() {
        Block block = Block.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .build();
        block = blockRepository.save(block);

        restTemplate.delete("http://localhost:" + port + "/api/blocks/" + block.getId());

        assertFalse(blockRepository.existsById(block.getId()));
    }



    @AfterEach
    public void tearDown() {
        // Clean up data after each test
        bookingRepository.deleteAll();
        blockRepository.deleteAll();
    }
}