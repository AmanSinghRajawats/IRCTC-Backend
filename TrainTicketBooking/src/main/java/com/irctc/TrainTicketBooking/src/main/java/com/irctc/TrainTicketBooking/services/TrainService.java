package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.BookingResponse;
import com.irctc.TrainTicketBooking.entities.BookingRequest;
import com.irctc.TrainTicketBooking.entities.Train;
import com.irctc.TrainTicketBooking.exceptions.TrainNotFoundException;
import com.irctc.TrainTicketBooking.repositories.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private TrainRepository trainRepository;
    @Autowired
    @Lazy
    private CoachService coachService;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    // create train
    public ResponseEntity<Train> createTrain(Train train){
        trainRepository.save(train);
        return ResponseEntity.ok().body(train);
    }

    // create bulk train
    public ResponseEntity<List<Train>> createBulkTrain(List<Train> train){
        trainRepository.saveAll(train);
        return ResponseEntity.ok().body(train);
    }

    // find all stations
    public ResponseEntity<List<Train>> findAllTrains(){
        List<Train> allStation = trainRepository.findAll();
        return ResponseEntity.ok().body(allStation);
    }

    // find train by trainId
    public ResponseEntity<Train> findTrainByTrainId(Long trainId){
        Train train = trainRepository.findById(trainId).orElseThrow(() -> new TrainNotFoundException("Train not found with trainId : "+trainId+" for findTrainByTrainId."));
        return ResponseEntity.ok().body(train);
    }

    // update train by trainId
    public ResponseEntity<Train> updateTrainByTrainId(Long trainId, Train train){
        Train trainInDB = trainRepository.findById(trainId).orElseThrow(() -> new TrainNotFoundException("Train not found with trainId : " + trainId + " to update train."));
        trainInDB.setTrainName(train.getTrainName() != null ? train.getTrainName() : trainInDB.getTrainName());
        trainInDB.setTrainType(train.getTrainType() != null ? train.getTrainType() : trainInDB.getTrainType());
        trainInDB.setTrainNumber(train.getTrainNumber() != null ? train.getTrainNumber() : trainInDB.getTrainNumber());
        trainInDB.setRunningDays(train.getRunningDays() != null ? train.getRunningDays() : trainInDB.getRunningDays());
        trainInDB.setCoaches(train.getCoaches() != null ? train.getCoaches() : trainInDB.getCoaches());
        trainRepository.save(trainInDB);
        return ResponseEntity.ok().body(trainInDB);
    }

    // delete station by stationId

    // find train by trainNumber
    public Train findTrainByTrainNumber(Long trainNumber){
        Train train = trainRepository.findByTrainNumber(trainNumber).orElseThrow(() -> new TrainNotFoundException("Train not found with trainNumber : " + trainNumber + " for findTrainByTrainNumber."));
        return train;
    }


    // book ticket
    public ResponseEntity<BookingResponse> bookTicket(BookingRequest bookingRequest){
        return coachService.bookTicket(bookingRequest);
    }

    public ResponseEntity<BookingResponse> cancelTicket(String pnr) {
        return coachService.cancelTicket(pnr);
    }
}
