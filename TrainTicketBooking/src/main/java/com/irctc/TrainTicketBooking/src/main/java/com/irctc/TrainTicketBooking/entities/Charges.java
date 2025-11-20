package com.irctc.TrainTicketBooking.entities;

import com.irctc.TrainTicketBooking.enums.CoachType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Charges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chargesId;

    private Double fixedCharges;
    private Double reservationCharges;
    private Double ratePerKM;
    private Double superfastCharges;

    @Enumerated(EnumType.STRING)
    private CoachType coachType;

    // 🔥 Important: charges belong to a train
    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;


//            Class        Fixed Charge (₹)	Rate per km (₹)
//        SL (Sleeper)	        20	                1.5
//        3A (AC 3-Tier)	    50	                3
//        2A (AC 2-Tier)	    100	                4
//        1A (AC First)	        250	                6
//        CC (Chair Car)	     40	                2.5
//        EC (Exec. Chair Car)	60	                3.5

// 115KM - Agra = SL-180    3A-560   2A-765   1A-1260   UR-70

//            Reservation Charge (₹)  Superfast Charges (₹)
//            SL	40                  SL	30
//            3A	60                  3A	70
//            2A	80                  2A	100
//            1A	150                 1A	200

}

