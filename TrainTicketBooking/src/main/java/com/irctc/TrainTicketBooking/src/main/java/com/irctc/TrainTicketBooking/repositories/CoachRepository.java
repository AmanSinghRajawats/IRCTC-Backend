package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.Coach;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoachRepository extends JpaRepository<Coach, Long> {

//    List<Coach> availableSeats(Long trainId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE coach SET next_available_seat = next_available_seat + 1 WHERE coach_id = :id", nativeQuery = true)
    void incrementSeat(@Param("id") Long id);

    @Query(value = "SELECT next_available_seat FROM coach WHERE coach_id = :id", nativeQuery = true)
    Integer getNextAvailableSeat(@Param("id") Long id);

    // Ye lock actual coach row ko DB me lock karega
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coach c WHERE c.coachId = :coachId")
    Coach findCoachForUpdate(@Param("coachId") Long coachId);

}
