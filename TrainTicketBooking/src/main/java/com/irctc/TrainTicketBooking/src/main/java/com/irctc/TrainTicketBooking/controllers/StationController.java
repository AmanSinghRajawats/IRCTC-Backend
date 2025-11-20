package com.irctc.TrainTicketBooking.controllers;

import com.irctc.TrainTicketBooking.dtos.StationDTO;
import com.irctc.TrainTicketBooking.entities.Station;
import com.irctc.TrainTicketBooking.services.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stations")
public class StationController {

    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }


    // POST - create station
    @PostMapping("/create-station")
    public ResponseEntity<Station> addStation(@RequestBody Station station){
        return stationService.createStation(station);
    }

    // GET - find all station
    @GetMapping({"","/get-all-stations","/all"})
    public ResponseEntity<List<StationDTO>> getAllStations(){
        return stationService.findAllStation();
    }

    // GET - find all station
    @GetMapping("/{stationName}")
    public ResponseEntity<Station> getStationByName(@PathVariable String stationName){
        return stationService.findStationByName(stationName);
    }

}
