package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.Charges;
import com.irctc.TrainTicketBooking.enums.CoachType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChargesRepository extends JpaRepository<Charges, Long> {
    Optional<Charges> findByCoachType(CoachType coachType);
}
