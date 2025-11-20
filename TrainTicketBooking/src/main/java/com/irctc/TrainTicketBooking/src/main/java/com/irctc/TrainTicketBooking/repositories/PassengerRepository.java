package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
