package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    @Column(unique = true, nullable = false)
    private String stationCode;

    @Column(nullable = false)
    private String name;

    private String city;

    private String state;

    @OneToMany(mappedBy = "station")
    @JsonIgnore
    private List<RouteStop> routeStops = new ArrayList<>();

}
