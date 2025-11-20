package com.irctc.TrainTicketBooking.controllers;

import com.irctc.TrainTicketBooking.dtos.RouteStopDTO;
import com.irctc.TrainTicketBooking.entities.RouteStop;
import com.irctc.TrainTicketBooking.services.RouteStopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/route-stops")
public class RouteStopController {

    private RouteStopService routeStopService;

    public RouteStopController(RouteStopService routeStopService) {
        this.routeStopService = routeStopService;
    }

    //POST - create route-stop
    @PostMapping({"/add-route-stop","/create-route-stop"})
    public ResponseEntity<RouteStop> createRouteStop(@RequestBody RouteStop routeStop){
        return routeStopService.createRouteStop(routeStop);
    }

    // GET - find all route-stops
    @GetMapping({"","/get-all-route-stops","/all"})
    public ResponseEntity<List<RouteStopDTO>> getAllRouteStops(){
        return routeStopService.findAllRouteStops();
    }
}
