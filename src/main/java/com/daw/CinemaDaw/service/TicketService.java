package com.daw.CinemaDaw.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.daw.CinemaDaw.DTO.CheckoutDTO;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.movie.Screening;
import com.daw.CinemaDaw.domain.order.Order;
import com.daw.CinemaDaw.domain.order.OrderStatus;
import com.daw.CinemaDaw.domain.order.Ticket;
import com.daw.CinemaDaw.repository.OrderRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;
import com.daw.CinemaDaw.repository.SeatRepository;
import com.daw.CinemaDaw.repository.TicketRepository;

@Service
public class TicketService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;

    public TicketService(ScreeningRepository screeningRepository,
                         SeatRepository seatRepository,
                         TicketRepository ticketRepository,
                         OrderRepository orderRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
    }

    public Order createOrderFromCart(Map<Long, List<Long>> cart, CheckoutDTO checkoutDTO) {
        // Creem la nova ordre a partir del DTO
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setClientName(checkoutDTO.getClientName());
        order.setClientEmail(checkoutDTO.getClientEmail());
        order.setStatus(OrderStatus.CONFIRMED);

        double total = 0.0;
        List<Ticket> tickets = new ArrayList<>();

        for (Map.Entry<Long, List<Long>> entry : cart.entrySet()) {
            Long screeningId = entry.getKey();
            List<Long> seatIds = entry.getValue();
            if (seatIds == null || seatIds.isEmpty()) continue;

            Optional<Screening> optScreening = screeningRepository.findById(screeningId);
            if (optScreening.isEmpty()) continue;
            Screening screening = optScreening.get();

            for (Long seatId : seatIds) {
                // Evitem duplicats: si ja existeix ticket per aquesta sessió i seient, saltem
                if (ticketRepository.existsByScreeningIdAndSeatId(screeningId, seatId)) continue;

                Optional<Seat> optSeat = seatRepository.findById(seatId);
                if (optSeat.isEmpty()) continue;

                Ticket ticket = new Ticket();
                ticket.setScreening(screening);
                ticket.setSeat(optSeat.get());
                ticket.setPrice(screening.getPrice());
                ticket.setOrder(order);
                tickets.add(ticket);
                total += screening.getPrice();
            }
        }

        if (tickets.isEmpty()) return null;

        order.setTotalAmount(total);
        order.setTickets(tickets);
        return orderRepository.save(order);
    }
}