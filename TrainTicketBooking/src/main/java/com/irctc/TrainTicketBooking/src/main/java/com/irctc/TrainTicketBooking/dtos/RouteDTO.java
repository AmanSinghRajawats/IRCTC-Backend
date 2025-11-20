package com.irctc.TrainTicketBooking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RouteDTO {
    private String routeName;
    private List<RouteStopDTO> routeStopDTOS;
}
