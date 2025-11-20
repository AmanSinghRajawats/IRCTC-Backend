package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.RouteDTO;
import com.irctc.TrainTicketBooking.dtos.RouteStopDTO;
import com.irctc.TrainTicketBooking.dtos.StationDTO;
import com.irctc.TrainTicketBooking.entities.Route;
import com.irctc.TrainTicketBooking.entities.Station;
import com.irctc.TrainTicketBooking.repositories.RouteRepository;
import com.irctc.TrainTicketBooking.repositories.RouteStopRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // create route
    public ResponseEntity<Route> createRoute(Route route) {
        routeRepository.save(route);
        return ResponseEntity.ok().body(route);
    }

    // find all routes
    public ResponseEntity<List<Route>> findAllRoutes() {
        List<Route> allRoutes = routeRepository.findAll();
        return ResponseEntity.ok().body(allRoutes);
    }

    // find route by id
    public ResponseEntity<RouteDTO> findRouteByRouteId(Long routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RuntimeException("Route not found by routeId : " + routeId + " to findRouteByRouteID"));

        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setRouteName(route.getRouteName());

        List<RouteStopDTO> routeStopDTOS = route.getStops().stream().map(stopage -> {

            RouteStopDTO routeStopDTO = new RouteStopDTO();

            routeStopDTO.setDistanceFromOrigin(stopage.getDistanceFromOrigin());
            routeStopDTO.setHaltDuration(stopage.getHaltDuration());
            routeStopDTO.setSequenceNumber(stopage.getSequenceNumber());
            routeStopDTO.setArrivalTime(stopage.getArrivalTime());
            routeStopDTO.setDepartureTime(stopage.getDepartureTime());

            StationDTO stationDTO = new StationDTO();

            stationDTO.setName(stopage.getStation().getName());
            stationDTO.setStationCode(stopage.getStation().getStationCode());
            stationDTO.setCity(stopage.getStation().getCity());
            stationDTO.setState(stopage.getStation().getState());

            routeStopDTO.setStationDTO(stationDTO);
            return routeStopDTO;

    }).toList();

        routeDTO.setRouteStopDTOS(routeStopDTOS);

        return ResponseEntity.ok().

    body(routeDTO);
}

// find station by stationId

// update station by stationId

// delete station by stationId

}
