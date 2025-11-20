package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.RouteStop;
import com.irctc.TrainTicketBooking.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

}
