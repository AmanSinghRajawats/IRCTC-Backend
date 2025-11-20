package com.irctc.TrainTicketBooking.entities;

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
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    private Double fixedCharges;
    private Double reservationCharges;
    private Double ratePerKM;
    private Double distance;
    private Double superfastCharges;
    private Double totalFare;

    @OneToOne
    @JoinColumn(name = "ticket_id")   // FK in invoice table
    private Ticket ticket;

}
