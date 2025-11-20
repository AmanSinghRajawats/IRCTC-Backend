package com.irctc.TrainTicketBooking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.irctc.TrainTicketBooking.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

//    @Column( nullable = false)
    private String firstName;

//    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String passwordInText;

//    @Column(nullable = false)
    private String gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> ticketHistory;

    private LocalDate dateOfBirth;

//    @Column(unique = true, nullable = false)
    private Long phoneNo;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

}
