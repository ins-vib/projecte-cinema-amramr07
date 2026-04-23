package com.daw.CinemaDaw.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.CinemaDaw.DTO.CheckoutDTO;
import com.daw.CinemaDaw.DTO.CheckoutLine;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.movie.Screening;
import com.daw.CinemaDaw.domain.order.Order;
import com.daw.CinemaDaw.repository.OrderRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;
import com.daw.CinemaDaw.repository.SeatRepository;
import com.daw.CinemaDaw.service.SeatUnavailableException;
import com.daw.CinemaDaw.service.TicketService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final TicketService ticketService;

    public OrderController(OrderRepository orderRepository,
                           ScreeningRepository screeningRepository,
                           SeatRepository seatRepository,
                           TicketService ticketService) {
        this.orderRepository = orderRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.ticketService = ticketService;
    }

    @GetMapping({"/client/order/checkout"})
    public String showCheckout(HttpSession session, Model model) {
        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/client";
        }

        cleanupCart(cart);
        if (cart.isEmpty()) {
            session.removeAttribute("cart");
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
                Optional<Seat> optSeat = seatRepository.findById(seatId);
                if (optSeat.isEmpty()) continue;
                Seat seat = optSeat.get();

                lines.add(new CheckoutLine(
                    screeningId,
                    seatId,
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
            session.removeAttribute("cart");
            return "redirect:/client";
        }

        session.setAttribute("cart", cart);
        model.addAttribute("lines", lines);
        model.addAttribute("total", total);
        if (!model.containsAttribute("checkoutDTO")) {
            model.addAttribute("checkoutDTO", new CheckoutDTO());
        }
        return "client/checkout";
    }

    @PostMapping({"/client/order/cart/remove", "/order/cart/remove"})
    public String removeCartItem(@RequestParam Long screeningId,
                                 @RequestParam Long seatId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");

        if (cart != null) {
            List<Long> seatIds = cart.get(screeningId);
            if (seatIds != null) {
                seatIds.remove(seatId);
                if (seatIds.isEmpty()) {
                    cart.remove(screeningId);
                }
            }

            if (cart.isEmpty()) {
                session.removeAttribute("cart");
            } else {
                session.setAttribute("cart", cart);
            }
        }

        redirectAttributes.addFlashAttribute("cartMessage", "Entrada eliminada del carret.");
        return "redirect:/client/order/checkout";
    }

    @PostMapping({"/client/order/cart/clear", "/order/cart/clear"})
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("cartMessage", "S'ha buidat el carret.");
        return "redirect:/client";
    }

    @PostMapping({"/client/order/confirm", "/order/confirm"})
    public String confirmOrder(@Valid @ModelAttribute CheckoutDTO checkoutDTO,
                               BindingResult result,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("checkoutDTO", checkoutDTO);
            return showCheckout(session, model);
        }

        Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/client";

        try {
            Order order = ticketService.createOrderFromCart(cart, checkoutDTO);
            if (order == null) {
                redirectAttributes.addFlashAttribute(
                    "cartErrors",
                    List.of("No s'ha pogut confirmar la compra perquè el carret no té entrades vàlides.")
                );
                return "redirect:/client/order/checkout";
            }

            session.removeAttribute("cart");
            return "redirect:/client/order/confirmation/" + order.getId();
        } catch (SeatUnavailableException ex) {
            removeUnavailableSeatsFromCart(cart);
            if (cart.isEmpty()) {
                session.removeAttribute("cart");
            } else {
                session.setAttribute("cart", cart);
            }
            redirectAttributes.addFlashAttribute("cartErrors", ex.getConflicts());
            return "redirect:/client/order/checkout";
        }
    }

    private void removeUnavailableSeatsFromCart(Map<Long, List<Long>> cart) {
        List<String> unavailableSeats = ticketService.findUnavailableSeats(cart);
        if (unavailableSeats.isEmpty()) {
            return;
        }

        for (Map.Entry<Long, List<Long>> entry : cart.entrySet()) {
            Long screeningId = entry.getKey();
            List<Long> seatIds = entry.getValue();
            if (seatIds == null) continue;

            seatIds.removeIf(seatId -> !ticketService.findUnavailableSeats(Map.of(screeningId, List.of(seatId))).isEmpty());
        }

        cleanupCart(cart);
    }

    private void cleanupCart(Map<Long, List<Long>> cart) {
        cart.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isEmpty());
    }

    @GetMapping({"/client/order/confirmation/{id}", "/order/confirmation/{id}"})
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
    public String  orderDetail(@PathVariable Long id, Model model) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("order", optional.get());
            return "admin/order-detail";
        }
        return "redirect:/admin/orders";
    }
}
