package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.Route;
import com.irctc.TrainTicketBooking.entities.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

}
