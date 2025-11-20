package com.irctc.TrainTicketBooking.controllers;

import com.irctc.TrainTicketBooking.dtos.CoachResponseDTO;
import com.irctc.TrainTicketBooking.dtos.TrainResponseDTO;
import com.irctc.TrainTicketBooking.entities.Train;
import com.irctc.TrainTicketBooking.services.TrainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trains")
public class TrainController {

    private TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    //POST - create train
    @PostMapping({"/add-train", "/create-train"})
    public ResponseEntity<Train> createTrain(@RequestBody Train train) {
        return trainService.createTrain(train);
    }

    //POST - create bulk train
    @PostMapping({"/add-bulk-train", "/create-bulk-train"})
    public ResponseEntity<List<Train>> createBulkTrain(@RequestBody List<Train> train) {
        return trainService.createBulkTrain(train);
    }

    //PUT - update train
    @PutMapping("/{trainId}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long trainId, @RequestBody Train train) {
        return trainService.updateTrainByTrainId(trainId, train);
    }

    // GET - find all train
    @GetMapping({"", "/get-all-route-stops", "/all"})
    public ResponseEntity<List<Train>> getAllTrains() {
        return trainService.findAllTrains();
    }

  /*
  // GET - find train by trainId
    @GetMapping("/{trainId}")
    public ResponseEntity<Train> getTrainByTrainId(@PathVariable Long trainId){
        return trainService.findTrainByTrainId(trainId);
    }
   */

    // GET - find train by trainId
    @GetMapping("/{trainId}")
    public TrainResponseDTO getTrainByTrainId(@PathVariable Long trainId) {

        Train train = trainService.findTrainByTrainId(trainId).getBody();

        TrainResponseDTO dto = new TrainResponseDTO();
        dto.setTrainId(train.getTrainId());
        dto.setTrainNumber(train.getTrainNumber());
        dto.setTrainName(train.getTrainName());
        dto.setTrainType(train.getTrainType());
        dto.setRunningDays(train.getRunningDays());

        // convert coaches into DTO
        List<CoachResponseDTO> coachDTOs = train.getCoaches().stream().map(c -> {
            CoachResponseDTO cdto = new CoachResponseDTO();
            cdto.setCoachId(c.getCoachId());
            cdto.setCoachNumber(c.getCoachNumber());
            cdto.setCoachType(c.getCoachType());
            cdto.setTotalSeats(c.getTotalSeats());
            cdto.setAvailableSeats(c.getAvailableSeats());
            return cdto;
        }).toList();

        dto.setCoaches(coachDTOs);

        return dto;
    }
}
