package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.RouteStop;
import com.irctc.TrainTicketBooking.entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByTrainNumber(Long trainNumber);
}
