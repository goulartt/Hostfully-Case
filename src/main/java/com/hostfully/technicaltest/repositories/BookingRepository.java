package com.hostfully.technicaltest.repositories;

import com.hostfully.technicaltest.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate end, LocalDate start);
}