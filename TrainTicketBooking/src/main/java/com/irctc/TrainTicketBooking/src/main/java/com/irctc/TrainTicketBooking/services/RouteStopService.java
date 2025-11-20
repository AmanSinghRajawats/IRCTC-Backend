package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.RouteStopDTO;
import com.irctc.TrainTicketBooking.dtos.StationDTO;
import com.irctc.TrainTicketBooking.entities.RouteStop;
import com.irctc.TrainTicketBooking.repositories.RouteStopRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteStopService {

    private RouteStopRepository routeStopRepository;

    public RouteStopService(RouteStopRepository routeStopRepository) {
        this.routeStopRepository = routeStopRepository;
    }

    // create route-stop
    public ResponseEntity<RouteStop> createRouteStop(RouteStop routeStop){
        routeStopRepository.save(routeStop);
        return ResponseEntity.ok().body(routeStop);
    }

    // find all route-stops
    public ResponseEntity<List<RouteStopDTO>> findAllRouteStops(){
        List<RouteStop> allRouteStops = routeStopRepository.findAll();

        List<RouteStopDTO> resultRouteStopDTO = allRouteStops.stream().map(routeStop ->  {
            RouteStopDTO routeStopDTO = new RouteStopDTO();
            routeStopDTO.setArrivalTime(routeStop.getArrivalTime());
            routeStopDTO.setDepartureTime(routeStop.getDepartureTime());
            routeStopDTO.setHaltDuration(routeStop.getHaltDuration());
            routeStopDTO.setSequenceNumber(routeStop.getSequenceNumber());
            routeStopDTO.setDistanceFromOrigin(routeStop.getDistanceFromOrigin());

            StationDTO stationDTO = new StationDTO();

            stationDTO.setState(routeStop.getStation().getState());
            stationDTO.setName(routeStop.getStation().getName());
            stationDTO.setCity(routeStop.getStation().getCity());
            stationDTO.setStationCode(routeStop.getStation().getStationCode());
            routeStopDTO.setStationDTO(stationDTO);
            return routeStopDTO;
    }).toList();

        return ResponseEntity.ok().body(resultRouteStopDTO);
    }

    // find station by stationId

    // update station by stationId

    // delete station by stationId

}
