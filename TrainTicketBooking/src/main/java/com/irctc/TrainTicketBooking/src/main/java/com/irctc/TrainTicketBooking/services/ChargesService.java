package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.entities.Charges;
import com.irctc.TrainTicketBooking.entities.Train;
import com.irctc.TrainTicketBooking.enums.CoachType;
import com.irctc.TrainTicketBooking.repositories.ChargesRepository;
import com.irctc.TrainTicketBooking.repositories.TrainRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargesService {

    @Autowired
    private ChargesRepository chargesRepository;

    @Autowired
    private TrainRepository trainRepository;

    // Save or Update Charges per CoachType
    public Charges saveCharges(Charges charges) {
        Train train = trainRepository.findById(charges.getTrain().getTrainId()).orElseThrow(()-> new RuntimeException("No train found with "+charges.getTrain().getTrainId()+" to saveCharges."));
        charges.setTrain(train);
        return chargesRepository.save(charges);
    }

    // Fetch charges by coach type
    public Charges getChargesByCoachType(CoachType type) {
        Charges charges = chargesRepository.findByCoachType(type).orElseThrow(() -> new RuntimeException("Charges not found for CoachType :" + type + " to getChargesByCoachType."));
        return charges;
    }
}
