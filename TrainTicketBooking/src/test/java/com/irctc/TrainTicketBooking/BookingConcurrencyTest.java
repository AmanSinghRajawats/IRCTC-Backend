package com.irctc.TrainTicketBooking;

import com.irctc.TrainTicketBooking.dtos.StationDTO;
import com.irctc.TrainTicketBooking.entities.BookingRequest;
import com.irctc.TrainTicketBooking.entities.Passenger;
import com.irctc.TrainTicketBooking.entities.Station;
import com.irctc.TrainTicketBooking.entities.Ticket;
import com.irctc.TrainTicketBooking.enums.CoachType;
import com.irctc.TrainTicketBooking.repositories.StationRepository;
import com.irctc.TrainTicketBooking.repositories.TicketRepository;
import com.irctc.TrainTicketBooking.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingConcurrencyTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private StationRepository stationRepository;

    @Test
    @Transactional
    void testConcurrentTicketBooking() throws Exception {

        int threadCount = 100;  // simulate 20 users bookTicket at same time
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int userIndex = i;

            executor.submit(() -> {
                try {
                    BookingRequest request = createRequest(userIndex);
                    userService.bookTicket(request);
                } catch (Exception e) {
                    System.out.println("Error bookTicket ticket: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Wait for all threads to finish
        executor.shutdown();

        // Verification
        List<Ticket> tickets = ticketRepository.findAll();
        Set<String> seatNumbers = new HashSet<>();

        tickets.forEach(t ->
                t.getPassengers().forEach(p -> seatNumbers.add(p.getSeatNumber()))
        );

        Assertions.assertEquals(seatNumbers.size(), tickets.size(), "Duplicate seats detected!");
    }

    private BookingRequest createRequest(int idx) {
        BookingRequest r = new BookingRequest();
        r.setTrainNumber(12138L);
        r.setUserEmail("user" + idx + "@gmail.com");
        r.setFrom(stationRepository.findByNameIgnoreCase("Gwalior Junction").orElseThrow());
        r.setFrom(stationRepository.findByNameIgnoreCase("Morena Junction").orElseThrow());
        r.setDate(LocalDateTime.now().plusDays(1));

        Passenger p = new Passenger();
        p.setName("User" + idx);
        p.setCoachType(CoachType.SLEEPER);

        r.setPassengers(List.of(p));
        return r;
    }
}
