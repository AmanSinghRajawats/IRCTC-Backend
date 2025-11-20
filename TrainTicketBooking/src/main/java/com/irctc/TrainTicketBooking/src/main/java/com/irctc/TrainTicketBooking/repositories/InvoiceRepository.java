package com.irctc.TrainTicketBooking.repositories;

import com.irctc.TrainTicketBooking.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
