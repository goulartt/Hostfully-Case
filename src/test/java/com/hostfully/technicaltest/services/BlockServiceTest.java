package com.hostfully.technicaltest.services;

import com.hostfully.technicaltest.entities.Block;
import com.hostfully.technicaltest.repositories.BlockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlockServiceTest {


    @InjectMocks
    BlockService blockService;

    @Mock
    BlockRepository blockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBlock() {
        Block block = new Block();
        block.setStartDate(LocalDate.now());
        block.setEndDate(LocalDate.now().plusDays(5));

        when(blockRepository.save(block)).thenReturn(block);
        Block result = blockService.saveBlock(block);
        assertEquals(result, block);
    }

    @Test
    void findById() {
        Block block = new Block();
        block.setId(1L);
        when(blockRepository.findById(1L)).thenReturn(Optional.of(block));
        Optional<Block> result = blockService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), 1L);
    }

    @Test
    void findAll() {
        Block block1 = new Block();
        Block block2 = new Block();
        when(blockRepository.findAll()).thenReturn(Arrays.asList(block1, block2));
        List<Block> result = blockService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void updateBlock() {
        Block oldBlock = new Block();
        oldBlock.setId(1L);
        oldBlock.setStartDate(LocalDate.now());
        oldBlock.setEndDate(LocalDate.now().plusDays(5));

        Block updatedBlock = new Block();
        updatedBlock.setId(1L);
        updatedBlock.setStartDate(LocalDate.now().plusDays(1));
        updatedBlock.setEndDate(LocalDate.now().plusDays(6));

        when(blockRepository.existsById(1L)).thenReturn(true);
        when(blockRepository.save(updatedBlock)).thenReturn(updatedBlock);

        Block result = blockService.updateBlock(1L, updatedBlock);
        assertEquals(result.getStartDate(), updatedBlock.getStartDate());
        assertEquals(result.getEndDate(), updatedBlock.getEndDate());
    }

    @Test
    void deleteBlock() {
        when(blockRepository.existsById(1L)).thenReturn(true);
        blockService.deleteBlock(1L);
        verify(blockRepository, times(1)).deleteById(1L);
    }

    @Test
    void findBlocksInDateRange() {
        Block block1 = new Block();
        block1.setStartDate(LocalDate.now());
        block1.setEndDate(LocalDate.now().plusDays(3));

        Block block2 = new Block();
        block2.setStartDate(LocalDate.now().plusDays(2));
        block2.setEndDate(LocalDate.now().plusDays(5));

        when(blockRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(block1, block2));

        List<Block> result = blockService.findBlocksInDateRange(LocalDate.now(), LocalDate.now().plusDays(4));
        assertEquals(2, result.size());
    }
}
