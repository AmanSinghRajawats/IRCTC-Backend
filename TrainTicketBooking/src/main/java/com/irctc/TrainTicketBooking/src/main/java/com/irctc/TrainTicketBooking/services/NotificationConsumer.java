package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.enums.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @Autowired
    private EmailService emailService;   // 👈 Your existing EmailService

    @KafkaListener(topics = "notifications", groupId = "notify-group")
    public void consume(NotificationEvent event) {

        System.out.println("📩 Received Event: " + event);

        if (event == null) {
            System.err.println("Received null NotificationEvent");
            return;
        }

//        if (event.getType() == BookingStatus.CONFIRMED) {

        // calling existing EmailService
        emailService.sendTicketEmail(
                event.getEmail(),
                event.getSubject(),
                event.getMessage()
        );
    }
}

