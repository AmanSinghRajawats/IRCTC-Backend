package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.irctc.TrainTicketBooking.enums.CoachType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coachId;

    @Column(nullable = false)
    private String coachNumber; // S1, S4 etc.

    @Enumerated(EnumType.STRING)
    private CoachType coachType; // "AC", "Sleeper", etc.

    private Integer totalSeats;
    private Integer availableSeats;

    private Integer nextAvailableSeat = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    @JsonIgnore
    private Train train;
/*
    private Double fixedCharges;

    private Double reservationCharges;

    private Double ratePerKM;

    private Double distance;

    private Double superfastCharges;

 */

    @ElementCollection
    @CollectionTable(
            name = "coach_waiting_list",
            joinColumns = @JoinColumn(name = "coach_id")
    )
    @Column(name = "passenger_id") // Stores passenger IDs
    @JsonIgnore
    private List<Passenger> WaitingList;


    // Generate coach number based on Train's coach map
    @PrePersist
    public void generateCoachNumber() {
        if (this.coachNumber == null && this.coachType != null && this.train != null) {
            // Count existing coaches of this type in the train's map
            long count = this.train.getCoaches().stream()
                    .filter(c -> c.getCoachType() == this.coachType)
                    .count();

            this.coachNumber = this.coachType.getPrefix() + (count + 1);
        }
    }




/*
    // Generate coach number based on Train's coach map
    @PrePersist   // call this method just before saving the entity in db
    public void generateCoachNumber() {
        if (this.coachNumber == null && this.coachType != null && this.train != null) {
            // Count existing coaches of this type in the train's map
            long count = this.train.getCoaches().stream()
                    .filter(c -> c.getCoachType() == this.coachType)
                    .count();

            this.coachNumber = this.coachType.getPrefix() + (count + 1);
        }
    }

    // Called before persisting - copy fare from train's config if available
    @PrePersist
    @PreUpdate
    public void applyTrainCharges() {
        if (this.train != null && this.coachType != null) {
            Charges matched = this.train.getCharges()
                    .stream()
                    .filter(cfg -> cfg.getCoachType() == this.coachType)
                    .findFirst()
                    .orElse(null);


            if (matched != null) {
                this.ratePerKM = matched.getRatePerKM();
                this.reservationCharges = matched.getReservationCharges();
                this.superfastCharges = matched.getSuperfastCharges();
            }
        }
    }

 */

}

