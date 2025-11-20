package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.irctc.TrainTicketBooking.enums.CoachType;
import com.irctc.TrainTicketBooking.enums.RunningDays;
import com.irctc.TrainTicketBooking.enums.TrainType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainId;

    @Column(unique = true, nullable = false)
    private Long trainNumber;
    @Column(nullable = false, unique = true)
    private String trainName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainType trainType;  // Passenger, Superfast, Express

    @ElementCollection
    @CollectionTable(
            name = "train_running_days",
            joinColumns = @JoinColumn(name = "train_id")
    )
    @Column(name = "running_day")
    @Enumerated(EnumType.STRING)
    private Set<RunningDays> runningDays;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<Coach> coaches = new ArrayList<>();

    @OneToMany(mappedBy = "train", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Route> routes;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<Charges> charges = new ArrayList<>();

}

