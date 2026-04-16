package com.daw.CinemaDaw.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    public OrderController(OrderRepository orderRepository,
                           ScreeningRepository screeningRepository,
                           SeatRepository seatRepository,
                           TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/order/checkout")
    public String showCheckout(HttpSession session, Model model) {
        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/client";
        }

        List<CheckoutLine> lines = new ArrayList<>();
        double total = 0.0;

        for (Map.Entry<Long, List<Long>> entry : cart.entrySet()) {
            Long screeningId = entry.getKey();
            List<Long> seatIds = entry.getValue();
            if (seatIds == null || seatIds.isEmpty()) continue;

            Optional<Screening> optScreening = screeningRepository.findById(screeningId);
            if (optScreening.isEmpty()) continue;
            Screening screening = optScreening.get();

            for (Long seatId : seatIds) {
                if (ticketRepository.existsByScreeningIdAndSeatId(screeningId, seatId)) continue;

                Optional<Seat> optSeat = seatRepository.findById(seatId);
                if (optSeat.isEmpty()) continue;
                Seat seat = optSeat.get();

                lines.add(new CheckoutLine(
                    screening.getMovie().getTitle(),
                    screening.getRoom().getCinema().getName(),
                    screening.getRoom().getName(),
                    screening.getScreeningDateTime(),
                    seat.getSeatRow(),
                    seat.getSeatNumber(),
                    screening.getPrice()
                ));
                total += screening.getPrice();
            }
        }

        if (lines.isEmpty()) {
            return "redirect:/client";
        }

        model.addAttribute("lines", lines);
        model.addAttribute("total", total);
        model.addAttribute("checkoutDTO", new CheckoutDTO());
        return "client/checkout";
    }

    @PostMapping("/order/confirm")
    public String confirmOrder(@Valid @ModelAttribute CheckoutDTO checkoutDTO,
                               BindingResult result,
                               HttpSession session,
                               Model model) {
        if (result.hasErrors()) {
            return showCheckout(session, model);
        }

        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/client";

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

        if (tickets.isEmpty()) return "redirect:/client";

        order.setTotalAmount(total);
        order.setTickets(tickets);
        orderRepository.save(order);
        session.removeAttribute("cart");

        return "redirect:/order/confirmation/" + order.getId();
    }

    @GetMapping("/order/confirmation/{id}")
    public String showConfirmation(@PathVariable Long id, Model model) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("order", optional.get());
            return "client/order-confirmation";
        }
        return "redirect:/client";
    }

    @GetMapping("/admin/orders")
    public String listAllOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/orders";
    }

    @GetMapping("/admin/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("order", optional.get());
            return "admin/order-detail";
        }
        return "redirect:/admin/orders";
    }

    // DTO intern per la vista
    public static class CheckoutLine {
        private final String movieTitle, cinemaName, roomName;
        private final LocalDateTime screeningDateTime;
        private final int seatRow, seatNumber;
        private final double price;

        public CheckoutLine(String movieTitle, String cinemaName, String roomName,
                            LocalDateTime screeningDateTime, int seatRow, int seatNumber, double price) {
            this.movieTitle = movieTitle;
            this.cinemaName = cinemaName;
            this.roomName = roomName;
            this.screeningDateTime = screeningDateTime;
            this.seatRow = seatRow;
            this.seatNumber = seatNumber;
            this.price = price;
        }

        public String getMovieTitle() { return movieTitle; }
        public String getCinemaName() { return cinemaName; }
        public String getRoomName() { return roomName; }
        public LocalDateTime getScreeningDateTime() { return screeningDateTime; }
        public int getSeatRow() { return seatRow; }
        public int getSeatNumber() { return seatNumber; }
        public double getPrice() { return price; }
    }
}