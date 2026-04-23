package com.daw.CinemaDaw.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
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

import jakarta.transaction.Transactional;

@Service
public class TicketService {

    private static final DateTimeFormatter SCREENING_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

    @Transactional
    public Order createOrderFromCart(Map<Long, List<Long>> cart, CheckoutDTO checkoutDTO) {
        List<String> conflicts = findUnavailableSeats(cart);
        if (!conflicts.isEmpty()) {
            throw new SeatUnavailableException(conflicts);
        }

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
        try {
            return orderRepository.saveAndFlush(order);
        } catch (DataIntegrityViolationException ex) {
            throw new SeatUnavailableException(
                List.of("Alguns seients s'acaben de vendre mentre confirmaves la compra. Torna a seleccionar-los.")
            );
        }
    }

    public List<String> findUnavailableSeats(Map<Long, List<Long>> cart) {
        List<String> conflicts = new ArrayList<>();

        if (cart == null || cart.isEmpty()) {
            return conflicts;
        }

        for (Map.Entry<Long, List<Long>> entry : cart.entrySet()) {
            Long screeningId = entry.getKey();
            List<Long> seatIds = entry.getValue();
            if (seatIds == null || seatIds.isEmpty()) continue;

            Optional<Screening> optScreening = screeningRepository.findById(screeningId);
            if (optScreening.isEmpty()) continue;
            Screening screening = optScreening.get();

            for (Long seatId : seatIds) {
                if (!ticketRepository.existsByScreeningIdAndSeatId(screeningId, seatId)) continue;

                Optional<Seat> optSeat = seatRepository.findById(seatId);
                if (optSeat.isEmpty()) continue;

                conflicts.add(formatSeatConflict(screening, optSeat.get()));
            }
        }

        return conflicts;
    }

    private String formatSeatConflict(Screening screening, Seat seat) {
        return screening.getMovie().getTitle()
            + " | "
            + screening.getScreeningDateTime().format(SCREENING_FORMATTER)
            + " | fila "
            + seat.getSeatRow()
            + " seient "
            + seat.getSeatNumber();
    }
}
