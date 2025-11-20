package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.BookingResponse;
import com.irctc.TrainTicketBooking.entities.*;
import com.irctc.TrainTicketBooking.enums.BookingStatus;
import com.irctc.TrainTicketBooking.enums.RunningDays;
import com.irctc.TrainTicketBooking.exceptions.*;
import com.irctc.TrainTicketBooking.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CoachService {

    @Autowired
    private TrainService trainService;

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ChargesService chargesService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;


    @Transactional
    public ResponseEntity<BookingResponse> bookTicket(BookingRequest bookingRequest) {
        // find train is running on given date or not, for that convert date into running days
        RunningDays runningDays = RunningDays.valueOf(bookingRequest.getDate().getDayOfWeek().name());

        // arrival and departure time for BookingResponse
        LocalTime arrivalTime = null;
        LocalTime departureTime = null;

        BookingResponse bookingResponse = new BookingResponse();
        double totalFare = 0;
        Invoice invoice = new Invoice();

        Train train = trainService.findTrainByTrainNumber(bookingRequest.getTrainNumber());
        if (train.getRunningDays().contains(runningDays)) {
            Ticket ticket = new Ticket();

            User user = userRepository.findByEmail(bookingRequest.getUserEmail()).orElseThrow(() -> new UserNotFoundException("User not found with Email : " + bookingRequest.getUserEmail() + "  : CoachService.bookTicket()"));
            for (Passenger passenger : new ArrayList<>(bookingRequest.getPassengers())) {
                List<Coach> listOfCoaches = train.getCoaches()
                        .stream()
                        .filter(coach -> coach.getCoachType().equals(passenger.getCoachType()))
                        .toList();

                if (listOfCoaches == null || listOfCoaches.isEmpty()) {
                    throw new InvalidCoachTypeException(
                            "Invalid coach type '" + passenger.getCoachType() +
                                    "' for passenger '" + passenger.getName() +
                                    "'. Train '" + train.getTrainName() +
                                    "' does not have any '" + passenger.getCoachType() + "' coach."
                    );
                }
                boolean isTicketAvailable = true;

                for (Coach coachTemp : listOfCoaches) {

                    // applying PESSIMISTIC LOCK here
                    Coach coach = coachRepository.findCoachForUpdate(coachTemp.getCoachId());
                    if (coach == null) {
                        throw new InvalidCoachTypeException("No Coach available in train : " + train.getTrainName());
                    }

                    Integer availableSeats = coach.getAvailableSeats();
                    if (availableSeats > 0 && isTicketAvailable) {

                        // fetching stations
                        Station fromStation = stationRepository.findByNameIgnoreCase(bookingRequest.getFrom().getName()).orElseThrow(() -> new StationNotFoundException("From station not found  : CoachService.bookTicket()"));

                        Station toStation = stationRepository.findByNameIgnoreCase(bookingRequest.getTo().getName()).orElseThrow(() -> new StationNotFoundException("To station not found  : CoachService.bookTicket()"));

                        if (fromStation.getStationId() == null || toStation.getStationId() == null) {
                            throw new StationNotFoundException("Source / Destination not found : CoachService.bookTicket()");
                        }

                        // calculating distance
                        RouteStop fromRouteStop = fromStation.getRouteStops().stream().filter(rs -> rs.getStation().getName().equals(bookingRequest.getFrom().getName())).findFirst().orElseThrow(() -> new StationNotFoundException("From station not found in this train route : CoachService.bookTicket()"));

                        RouteStop toRouteStop = toStation.getRouteStops().stream().filter(rs -> rs.getStation().getName().equals(bookingRequest.getTo().getName())).findFirst().orElseThrow(() -> new StationNotFoundException("To station not found in this train route : CoachService.bookTicket()"));


                        // arrival and departure time for BookingResponse
                        arrivalTime = fromRouteStop.getArrivalTime();
                        departureTime = fromRouteStop.getDepartureTime();

                        if (fromRouteStop.getSequenceNumber() > toRouteStop.getSequenceNumber()) {
                            throw new RuntimeException("Invalid Station Order Sequence, Please enter the valid Station order sequence.");
                        }

                        double distance = toRouteStop.getDistanceFromOrigin() - fromRouteStop.getDistanceFromOrigin();

                        // setting-up charges in ticket
                        Charges charges = chargesService.getChargesByCoachType(passenger.getCoachType());
                        invoice.setDistance(distance);
                        invoice.setFixedCharges(charges.getFixedCharges());
                        invoice.setRatePerKM(charges.getRatePerKM());
                        invoice.setSuperfastCharges(charges.getSuperfastCharges());
                        invoice.setReservationCharges(charges.getReservationCharges());
//                        invoice.setTotalFare();  will set at the end, because we want to set total amount of multiple passenger's ticket.
                        totalFare += charges.getSuperfastCharges() + charges.getFixedCharges() + charges.getReservationCharges() + (charges.getRatePerKM() * distance);

                        System.out.println("charges : " + charges.toString());

                        // Generating seat number
                        String coachNumber = coach.getCoachNumber();
                        String seatNumber = coachNumber + "-" + coach.getNextAvailableSeat();
                        coach.setNextAvailableSeat(coach.getNextAvailableSeat() + 1); // AtomicInteger
                        coach.setAvailableSeats(availableSeats - 1);
                        //                    coach.setBookingStatus(BookingStatus.CONFIRMED);
                        // SAVE UPDATED COACH IMMEDIATELY - for multithreading
                        coachRepository.saveAndFlush(coach);
                        passenger.setBookingStatus(BookingStatus.CONFIRMED);
                        passenger.setSeatNumber(seatNumber);
                        passenger.setCoach(coach);
                        passengerRepository.save(passenger);
                        isTicketAvailable = false;
                        System.out.println("Hi " + passenger.getName() + ", You Booked the Ticket Successfully.");
                    } else {
                        // Waiting List  -- when writing cancellation logic, will update this waiting list again
                        List<Passenger> waitingList = coach.getWaitingList();
                        if (waitingList.isEmpty()) {
                            waitingList = new ArrayList<>();
//                            waitingList.add(passenger);
                        }
                        waitingList.add(passenger);
                        coach.setWaitingList(waitingList);
                        passenger.setBookingStatus(BookingStatus.WAITING_LIST);
                        System.out.println("WAITING LIST - Hi " + passenger.getName() + ", Your Waiting list number is WL: "+coach.getWaitingList().size() +". Thank you!");
                        passengerRepository.save(passenger);
                    }

                    // setting up ticket fields
                    ticket.setJourneyDate(bookingRequest.getDate());
                    ticket.setBookingDate(LocalDateTime.now());
                    ticket.setBoardingStation(bookingRequest.getFrom().getName());
                    ticket.setDestinationStation(bookingRequest.getTo().getName());
                    ticket.setPassengers(bookingRequest.getPassengers());
                    ticket.setTrain(train);  // saving Train
                    ticket.setUser(user);  // saving User
                    ticket.setPnr(ticket.generatePNR());  // setting up PNR
                    if (!isTicketAvailable) break;
                }
                if (listOfCoaches == null || listOfCoaches.isEmpty()) {
                    throw new InvalidCoachTypeException("No Coach available in train : " + train.getTrainName());
                }
            }
            invoice.setTotalFare(totalFare);
            ticket.setTotalFare(new BigDecimal(totalFare));

            // Sending email

            String emailBody = "Dear " + user.getFirstName() + ",\n\n" + "Your ticket has been booked successfully!\n\n" + "PNR Number: " + ticket.getPnr() + "\n" + "Train: " + train.getTrainName() + "\n" + "From: " + bookingRequest.getFrom().getName() + "\n" + "To: " + bookingRequest.getTo().getName() + "\n" + "Date: " + bookingRequest.getDate().toLocalDate() + "\n" + "Time : " + bookingRequest.getDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n" + "Total Fare: ₹" + ticket.getTotalFare() + "\n\n" + "Booking Status: "+BookingStatus.CONFIRMED + "\n\n" + "Happy Journey!" + "\n\n\n" + "Regards," + "\n" + "IRCTC";

            // creating kafka event
            NotificationEvent notificationEvent = new NotificationEvent();
            notificationEvent.setEmail(bookingRequest.getUserEmail());
            notificationEvent.setPnr(ticket.getPnr());
            notificationEvent.setSubject("Ticket Booking " + BookingStatus.CONFIRMED);
            notificationEvent.setBookingStatus(BookingStatus.CONFIRMED);
            notificationEvent.setMessage(emailBody);
            notificationEvent.setTrainNumber(train.getTrainNumber());
            kafkaTemplate.send("notifications", notificationEvent);

            /*

            // Sending Email before Kafka implementation -> Now using NotificationConsumer to send email
            emailService.sendTicketEmail(
                    bookingRequest.getUserEmail(),
                    "Ticket Booking "+BookingStatus.CONFIRMED,
                    emailBody
            );
             */

            Ticket savedTicket = ticketRepository.save(ticket);
            invoice.setTicket(savedTicket);

            Invoice savedInvoice = invoiceRepository.save(invoice);

            savedTicket.setInvoice(savedInvoice);
            bookingResponse.setFrom(ticket.getBoardingStation());
            bookingResponse.setTo(ticket.getDestinationStation());
            bookingResponse.setPnr(ticket.getPnr());
            bookingResponse.setTrainName(train.getTrainName());
            bookingResponse.setJourneyDate(ticket.getJourneyDate().toLocalDate());
            bookingResponse.setArrivalTime(arrivalTime);
            bookingResponse.setDepartureTime(departureTime);
            bookingResponse.setTotalFare(totalFare);
            bookingResponse.setBookingStatus(ticket.getPassengers().get(0).getBookingStatus());

            bookingResponse.setMessage("Hi " + user.getFirstName() + ", Your Ticket has been Booked for : [ " + bookingRequest.getPassengers().stream().map(p -> p.getName() + " (" + p.getBookingStatus() + ")").collect(Collectors.joining(", ")) + " ] Thank you!");

            //            System.out.println("Checking Booking Status for ticket.getPassengers().get(list.size()-1).getBookingStatus()): "+ticket.getPassengers().get(ticket.getPassengers().size()-1).getBookingStatus());
//            System.out.println("Checking Booking Status for ticket.getPassengers().get(0).getBookingStatus()): "+ticket.getPassengers().get(0).getBookingStatus());
            return ResponseEntity.ok(bookingResponse);
        } else {
            throw new TrainNotFoundException("No train running on " + bookingRequest.getDate() + ", day " + bookingRequest.getDate().getDayOfWeek());
        }
    }

    // cancel ticket

    @Transactional
    public ResponseEntity<BookingResponse> cancelTicket(String pnr) {

        BookingResponse bookingResponse = new BookingResponse();
        Ticket ticket = ticketRepository.findByPnr(pnr).orElseThrow(() -> new RuntimeException("No ticket found with PNR : " + pnr + " for coachService.cancelTicket()."));

        String userEmail = ticket.getUser().getEmail();

        List<Passenger> passengers = ticket.getPassengers();
        if (!passengers.isEmpty()) {
            Train train = ticket.getTrain();
            for (Passenger passenger : passengers) {
                if (passenger.getBookingStatus() == BookingStatus.CANCELLED)
                    throw new TicketCancellationException("Ticket cancellation failed because the ticket is already CANCELLED.");
                Coach passengersCoach = passenger.getCoach();
                train.getCoaches().stream().filter(coach -> coach.getCoachId().equals(passengersCoach.getCoachId())).forEach((coach) -> {
                    coach.setAvailableSeats(coach.getAvailableSeats() + 1);
                    coach.setNextAvailableSeat(coach.getNextAvailableSeat() - 1);
                });
                passenger.setBookingStatus(BookingStatus.CANCELLED);
                passengerRepository.save(passenger);
                coachRepository.save(passengersCoach);
            }

            // Sending Cancellation confirmation email

            String emailBody = "Dear " + userEmail + ",\n\n" + "Your ticket with PNR '" + pnr + "' has been CANCELLED successfully!\n\n" + "PNR Number: " + ticket.getPnr() + "\n" + "Train: " + train.getTrainName() + "\n" + "From: " + ticket.getBoardingStation() + "\n" + "To: " + ticket.getDestinationStation() + "\n" + "Date: " + ticket.getJourneyDate() + "\n" + "Total Fare: ₹" + ticket.getTotalFare() + "\n\n" + "Booking Status: "+BookingStatus.CANCELLED  + "\n\n" + "Thank you!" + "\n\n\n" + "Regards," + "\n" + "IRCTC";

            // creating kafka event
            NotificationEvent notificationEvent = new NotificationEvent();
            notificationEvent.setEmail(userEmail);
            notificationEvent.setPnr(ticket.getPnr());
            notificationEvent.setSubject("Ticket " + BookingStatus.CANCELLED + " Confirmation");
            notificationEvent.setBookingStatus(BookingStatus.CANCELLED);
            notificationEvent.setMessage(emailBody);
            notificationEvent.setTrainNumber(train.getTrainNumber());
            kafkaTemplate.send("notifications", notificationEvent);

            ticket.setRemarks("CANCELLED");
            ticketRepository.save(ticket);

            bookingResponse.setFrom(ticket.getBoardingStation());
            bookingResponse.setTo(ticket.getDestinationStation());
//            bookingResponse.setArrivalTime(ticket.getTrain().getRoutes().));
//            bookingResponse.setDepartureTime();

            LocalTime arrivalTime = ticket.getTrain().getRoutes()
                    .stream()
                    .flatMap(route -> route.getStops().stream())
                    .filter(stop -> stop.getStation().getName().equals(ticket.getBoardingStation()))
                    .map(RouteStop::getArrivalTime)
                    .findFirst()
                    .orElse(null);


            LocalTime departureTime = ticket.getTrain().getRoutes()
                    .stream()
                    .flatMap(route -> route.getStops().stream())
                    .filter(stop -> stop.getStation().getName().equals(ticket.getBoardingStation()))
                    .map(RouteStop::getDepartureTime)
                    .findFirst()
                    .orElse(null);


            bookingResponse.setPnr(ticket.getPnr());
            bookingResponse.setDepartureTime(departureTime);
            bookingResponse.setArrivalTime(arrivalTime);
            bookingResponse.setTrainName(train.getTrainName());
            bookingResponse.setJourneyDate(ticket.getJourneyDate().toLocalDate());
            bookingResponse.setTotalFare(ticket.getTotalFare().doubleValue());
            bookingResponse.setBookingStatus(ticket.getPassengers().get(0).getBookingStatus());
            bookingResponse.setMessage("Hi " + userEmail + ", Your Ticket has been CANCELLED Successfully for : [ " + ticket.getPassengers().stream().map(p -> p.getName() + " (" + p.getBookingStatus() + ")").collect(Collectors.joining(", ")) + " ] Thank you!");



            // Confirm waiting list ticket of same coach type

//            ticket.getPassengers().stream().filter(passenger -> passenger.getCoachType().equals(ticket.get))


            return ResponseEntity.ok(bookingResponse);
        } else {
            throw new RuntimeException("Passenger List is Empty in Ticket for CoachService.cancelTicket().");
        }
    }
}
