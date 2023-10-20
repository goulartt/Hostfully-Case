package com.hostfully.technicaltest.services;


import com.hostfully.technicaltest.entities.Block;
import com.hostfully.technicaltest.repositories.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BlockService {

    @Autowired
    private BlockRepository blockRepository;

    // Create
    public Block saveBlock(Block block) {
        return blockRepository.save(block);
    }

    // Read (by Id)
    public Optional<Block> findById(Long id) {
        return blockRepository.findById(id);
    }

    // Read (all)
    public List<Block> findAll() {
        return blockRepository.findAll();
    }

    // Update
    public Block updateBlock(Long id, Block updatedBlock) {
        if (!blockRepository.existsById(id)) {
            throw new RuntimeException("Block not found.");
        }
        return blockRepository.save(updatedBlock);
    }

    // Delete
    public void deleteBlock(Long id) {
        if (!blockRepository.existsById(id)) {
            throw new RuntimeException("Block not found.");
        }
        blockRepository.deleteById(id);
    }

    public List<Block> findBlocksInDateRange(LocalDate startDate, LocalDate endDate) {
        return blockRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
    }
}
