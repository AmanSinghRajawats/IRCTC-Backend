package com.irctc.TrainTicketBooking.controllers;

import com.irctc.TrainTicketBooking.entities.Charges;
import com.irctc.TrainTicketBooking.enums.CoachType;
import com.irctc.TrainTicketBooking.services.ChargesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/charges")
public class ChargesController {

    @Autowired
    private ChargesService chargesService;

    @PostMapping({"/add","/add-charges"})
    public Charges createCharges(@RequestBody Charges charges) {
        return chargesService.saveCharges(charges);
    }

    @GetMapping("/{coachType}")
    public Charges getCharges(@PathVariable CoachType coachType) {
        return chargesService.getChargesByCoachType(coachType);
    }

}
