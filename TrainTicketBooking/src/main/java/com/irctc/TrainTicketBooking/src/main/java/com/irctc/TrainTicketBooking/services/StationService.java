package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.StationDTO;
import com.irctc.TrainTicketBooking.entities.Station;
import com.irctc.TrainTicketBooking.exceptions.StationNotFoundException;
import com.irctc.TrainTicketBooking.repositories.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    // create station
    public ResponseEntity<Station> createStation(Station station){
        stationRepository.save(station);
        return ResponseEntity.ok().body(station);
    }

    // find all stations
    public ResponseEntity<List<StationDTO>> findAllStation(){
        List<Station> allStation = stationRepository.findAll();
        List<StationDTO> allStationDTO = allStation.stream().map(station -> {
            StationDTO stationDTO = new StationDTO();
            stationDTO.setName(station.getName());
            stationDTO.setStationCode(station.getStationCode());
            stationDTO.setState(station.getState());
            stationDTO.setCity(station.getCity());
            return stationDTO;
        }).toList();


        return ResponseEntity.ok().body(allStationDTO);
    }

    // find station by station name

    public ResponseEntity<Station> findStationByName(String stationName){
        Station station = stationRepository.findByNameIgnoreCase(stationName).orElseThrow(() -> new StationNotFoundException("Station not found with station name : "+stationName+" to findStationByName."));
        return ResponseEntity.ok().body(station);
    }

    // update station by stationId

    // delete station by stationId

}
