package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    @JsonIgnore // *Imp* This prevents serialization of the route when serializing
    private Train train;

    @Column(nullable = false)
    private String routeName;  // e.g., "Delhi-Mumbai Central Route"

    @OneToMany(mappedBy = "route", orphanRemoval = true)
    @OrderBy("sequenceNumber ASC")
    private List<RouteStop> stops = new ArrayList<>(); // List of Stations (STOPPAGES) in a Route. RouteStop = “Delhi”, “Mathura”, “Agra”, “Morena”, “Gwalior”, "Jhansi"

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    // Helper methods to manage bidirectional relationship
    public void addStop(RouteStop stop) {
        stops.add(stop);
        stop.setRoute(this);
    }

    public void removeStop(RouteStop stop) {
        stops.remove(stop);
        stop.setRoute(null);
    }

}

