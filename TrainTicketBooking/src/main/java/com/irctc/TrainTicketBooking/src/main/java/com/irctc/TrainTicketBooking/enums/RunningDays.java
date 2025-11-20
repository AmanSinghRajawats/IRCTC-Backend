package com.irctc.TrainTicketBooking.enums;

import jakarta.persistence.*;

import java.util.Set;

public enum RunningDays {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    @ElementCollection
    @CollectionTable(
            name = "train_running_days",
            joinColumns = @JoinColumn(name = "train_id")
    )
    @Column(name = "running_day")
    @Enumerated(EnumType.STRING)
    private Set<RunningDays> runningDays;
}
