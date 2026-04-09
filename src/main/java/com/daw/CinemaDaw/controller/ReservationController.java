package com.daw.CinemaDaw.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.CinemaDaw.domain.Order;
import com.daw.CinemaDaw.domain.Ticket;
import com.daw.CinemaDaw.repository.ScreeningRepository;
import com.daw.CinemaDaw.repository.SeatRepository;
import com.daw.CinemaDaw.repository.TicketRepository;

@Controller
@RequestMapping("/client")
public class ReservationController {

    private final SeatRepository seatRepository;
    private TicketRepository ticketRepository;
    private ScreeningRepository screeningRepository;

    public ReservationController(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

  @PostMapping("/client/reserve")
public String reserve(@RequestParam Long seatId, @RequestParam Long screeningId) {
    
    // 1. Verificar si ya se ha ocupado (seguridad extra por si dos personas pinchan a la vez)
    if (ticketRepository.existsByScreeningIdAndSeatId(screeningId, seatId)) {
        return "redirect:/client/screening/" + screeningId + "?error=already_taken";
    }

    // 2. Crear una orden simple (puedes ampliarla con el usuario logueado)
    Order order = new Order();
    order.setOrderDateTime(LocalDateTime.now());
    // orderRepository.save(order); // Necesitarás el OrderRepository

    // 3. Crear el Ticket
    Ticket ticket = new Ticket();
    ticket.setScreening(screeningRepository.findById(screeningId).get());
    ticket.setSeat(seatRepository.findById(seatId).get());
    ticket.setOrder(order);
    ticket.setPrice(ticket.getScreening().getPrice());

    ticketRepository.save(ticket);

    return "redirect:/client?success";
}
}