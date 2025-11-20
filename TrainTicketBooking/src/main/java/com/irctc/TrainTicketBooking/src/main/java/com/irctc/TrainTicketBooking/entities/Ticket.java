package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;  // like PNR

//    PNR = [Train Zone][Last 5 digits of epoch time][Random 3 digits]
    @Column(unique = true)
    private String pnr;

// generating pnr no :
    public String generatePNR() {
        long time = System.currentTimeMillis() % 100000;  // 5 digits
        int random = (int)(Math.random() * 900) + 100;    // 3 digits
        int zone = 22; // sample

        return zone + String.format("%05d%03d", time, random);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    @JsonIgnore
    private Train train;

    @ManyToOne
    @JoinColumn(name = "user_id")   // FK inside ticket table
    private User user;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Invoice invoice;


//    No Need, because passenger contains bookTicket status
//    @Enumerated(EnumType.STRING)
//    private BookingStatus bookingStatus;  // booked, confirm, waiting list, RAC, cancelled

    @Column(precision = 10, scale = 2)
    private BigDecimal totalFare;

    private String boardingStation;

    private String destinationStation;

    @Column(updatable = false)
    private LocalDateTime bookingDate;

    @NotNull
    private LocalDateTime journeyDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")  // Owns the relationship
    private List<Passenger> passengers; // multiple passengers

    @Column(length = 500)
    private String remarks;

}

