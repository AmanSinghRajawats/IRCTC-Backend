package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.BookingResponse;
import com.irctc.TrainTicketBooking.entities.BookingRequest;
import com.irctc.TrainTicketBooking.entities.User;
import com.irctc.TrainTicketBooking.exceptions.UserNotFoundException;
import com.irctc.TrainTicketBooking.repositories.UserRepository;
import com.sun.source.tree.LambdaExpressionTree;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    private UserRepository userRepository;
    private TrainService trainService;

    public UserService(UserRepository userRepository, TrainService trainService) {
        this.userRepository = userRepository;
        this.trainService = trainService;
    }

    //    create new user
    public ResponseEntity<User> createUser(User user) {
        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    // book ticket
    public ResponseEntity<BookingResponse> bookTicket(BookingRequest bookingRequest) {
        return trainService.bookTicket(bookingRequest);
    }

    // cancel ticket
    public ResponseEntity<BookingResponse> cancelTicket(String pnr) {
        return trainService.cancelTicket(pnr);
    }

    //    find all users
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok().body(allUsers);
    }


    //    find user by userId
    public ResponseEntity<User> getUserById(Long userId) {
        User allUsers = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with userId : " + userId));
        return ResponseEntity.ok().body(allUsers);
    }


    //    update user
    public ResponseEntity<User> updateUser(Long userId, User user) {
        User userById = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found with userId : " + userId + " to update user."));
        userById.setEmail(user.getEmail() != null ? user.getEmail() : userById.getEmail());
        userById.setGender(user.getGender() != null ? user.getGender() : userById.getGender());
        userById.setPassword(user.getPassword() != null ? user.getPassword() : userById.getPassword());
        userById.setPasswordInText(user.getPassword() != null ? user.getPassword() : userById.getPasswordInText());
        userById.setFirstName(user.getFirstName() != null ? user.getFirstName() : userById.getFirstName());
        userById.setLastName(user.getLastName() != null ? user.getLastName() : userById.getLastName());
        userById.setUsername(user.getUsername() != null ? user.getUsername() : userById.getUsername());
        userById.setPhoneNo(user.getPhoneNo() != null ? user.getPhoneNo() : userById.getPhoneNo());
        userById.setDateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth() : userById.getDateOfBirth());
        userRepository.save(userById);
        return ResponseEntity.ok().body(userById);
    }

    //    delete user
    public ResponseEntity<User> deleteUser(Long userId) {
        User userById = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with userId : " + userId + " to delete."));
        userRepository.deleteById(userId);
        return ResponseEntity.ok().body(userById);
    }


}