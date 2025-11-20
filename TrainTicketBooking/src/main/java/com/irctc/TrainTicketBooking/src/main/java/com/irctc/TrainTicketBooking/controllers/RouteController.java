package com.irctc.TrainTicketBooking.controllers;

import com.irctc.TrainTicketBooking.dtos.RouteDTO;
import com.irctc.TrainTicketBooking.entities.Route;
import com.irctc.TrainTicketBooking.services.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    //POST - create route-stop
    @PostMapping({"/add-route","/create-route"})
    public ResponseEntity<Route> createRoute(@RequestBody Route route){
        return routeService.createRoute(route);
    }

    // GET - find route
    @GetMapping({"","/get-all-routes","/all"})
    public ResponseEntity<List<Route>> getAllRoutes(){
        return routeService.findAllRoutes();
    }

    // GET - find route by routeId
    @GetMapping({"/{routeId}"})
    public ResponseEntity<RouteDTO> getAllRoutes(@PathVariable Long routeId){
        return routeService.findRouteByRouteId(routeId);
    }
}
