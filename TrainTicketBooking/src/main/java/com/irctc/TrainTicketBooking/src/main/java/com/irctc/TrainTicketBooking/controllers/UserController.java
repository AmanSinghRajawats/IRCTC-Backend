package com.irctc.TrainTicketBooking.controllers;

import com.irctc.TrainTicketBooking.dtos.BookingResponse;
import com.irctc.TrainTicketBooking.entities.BookingRequest;
import com.irctc.TrainTicketBooking.entities.User;
import com.irctc.TrainTicketBooking.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST - create user
    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping({"/bookTicket", "/book", "/bookTicket"})
    public ResponseEntity<BookingResponse> bookTicket(@RequestBody BookingRequest bookingRequest) {
        return userService.bookTicket(bookingRequest);
    }

    @PostMapping("/cancel/{PNR}")
    public ResponseEntity<BookingResponse> bookTicket(@PathVariable String PNR) {
        return userService.cancelTicket(PNR);
    }


    // GET - find all users
    @GetMapping({"", "/get-all-users", "/all"})
    public ResponseEntity<List<User>> getAllUser() {
        return userService.getAllUsers();
    }

    // GET - find user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    // PUT - update user by userId
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserByUserId(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    // DELETE - delete user by userId
    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUserByUserId(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

}
