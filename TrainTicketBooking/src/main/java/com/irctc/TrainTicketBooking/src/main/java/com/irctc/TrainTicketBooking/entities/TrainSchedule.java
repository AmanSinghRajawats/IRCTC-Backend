package com.irctc.TrainTicketBooking.entities;

import com.irctc.TrainTicketBooking.enums.RunningDays;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "route_id", nullable = false)
//    private Route route;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "schedule_running_days",
            joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "running_day", nullable = false)
    @Size(min = 1, message = "At least one running day must be specified")
    private Set<RunningDays> runningDays;

    @ElementCollection
    @CollectionTable(name = "schedule_cancellation_dates",
            joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "cancellation_date", nullable = false)
    private Set<LocalDate> cancellationDates;


    /*
    // Derived properties
    @Transient
    public Station getFromStation() {
        return route.getFirstStop().getStation();
    }

    @Transient
    public Station getToStation() {
        return route.getLastStop().getStation();
    }

    @Transient
    public LocalTime getDepartureTime() {
        return route.getFirstStop().getDepartureTime();
    }

    @Transient
    public LocalTime getArrivalTime() {
        return route.getLastStop().getArrivalTime();
    }
*/
    // Business logic
    public boolean isRunningOn(LocalDate date) {
        return !date.isBefore(startDate) &&
                !date.isAfter(endDate) &&
                runningDays.contains(date.getDayOfWeek()) &&
                !cancellationDates.contains(date);
    }
}