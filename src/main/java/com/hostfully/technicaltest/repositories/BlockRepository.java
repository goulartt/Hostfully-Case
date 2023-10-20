package com.hostfully.technicaltest.repositories;

import com.hostfully.technicaltest.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate end, LocalDate start);
}