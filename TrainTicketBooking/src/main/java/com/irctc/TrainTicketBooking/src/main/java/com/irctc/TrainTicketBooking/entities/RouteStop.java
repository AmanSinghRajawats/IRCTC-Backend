package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopId;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonIgnore // *Imp* This prevents serialization of the route when serializing RouteStop
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(nullable = false)
    private int sequenceNumber;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private int haltDuration;  // in minutes

    @Column(nullable = false)
    private int distanceFromOrigin;  // in km

}

