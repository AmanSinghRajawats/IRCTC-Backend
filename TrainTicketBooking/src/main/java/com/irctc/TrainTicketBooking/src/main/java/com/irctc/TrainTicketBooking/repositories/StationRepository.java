package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    // case-insensitive
    Optional<Station> findByNameIgnoreCase(String name);
}
