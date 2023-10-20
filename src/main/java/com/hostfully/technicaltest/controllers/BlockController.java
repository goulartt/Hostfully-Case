package com.hostfully.technicaltest.controllers;

import com.hostfully.technicaltest.entities.Block;
import com.hostfully.technicaltest.services.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @PostMapping
    public ResponseEntity<Block> createBlock(@RequestBody Block block) {
        return new ResponseEntity<>(blockService.saveBlock(block), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Block> getBlockById(@PathVariable Long id) {
        return ResponseEntity.of(blockService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        return new ResponseEntity<>(blockService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Block> updateBlock(@PathVariable Long id, @RequestBody Block block) {
        return new ResponseEntity<>(blockService.updateBlock(id, block), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        blockService.deleteBlock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}