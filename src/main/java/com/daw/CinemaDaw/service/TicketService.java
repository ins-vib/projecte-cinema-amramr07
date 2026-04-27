package com.daw.CinemaDaw.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.daw.CinemaDaw.DTO.CheckoutDTO;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.order.Coupon;
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
    private final CouponService couponService;

    public TicketService(ScreeningRepository screeningRepository,
                         SeatRepository seatRepository,
                         TicketRepository ticketRepository,
                         OrderRepository orderRepository,
                         CouponService couponService) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
        this.couponService = couponService;
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

        double subtotal = 0.0;
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
                subtotal += screening.getPrice();
            }
        }

        if (tickets.isEmpty()) return null;

        CouponService.CouponValidationResult couponValidation =
            couponService.validateCoupon(checkoutDTO.getCouponCode(), checkoutDTO.getClientEmail(), subtotal);

        double discountAmount = Math.min(couponValidation.getDiscountAmount(), subtotal);
        double total = Math.max(0.0, subtotal - discountAmount);

        order.setSubtotalAmount(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(total);
        order.setAppliedCouponCode(couponValidation.hasCoupon() ? couponValidation.getCoupon().getCode() : null);
        order.setTickets(tickets);

        try {
            Order savedOrder = orderRepository.saveAndFlush(order);

            if (couponValidation.hasCoupon()) {
                couponService.markAsUsed(couponValidation.getCoupon(), savedOrder);
            }

            if (subtotal >= CouponService.REWARD_THRESHOLD) {
                Coupon generatedCoupon = couponService.createRewardCoupon(savedOrder);
                savedOrder.setGeneratedCouponCode(generatedCoupon.getCode());
                savedOrder = orderRepository.save(savedOrder);
            }

            return savedOrder;
        } catch (DataIntegrityViolationException ex) {
            throw new SeatUnavailableException(
                List.of("Some seats were sold while you were confirming your purchase. Please select them again.")
            );
        }
    }

    public CheckoutSummary calculateCheckoutSummary(Map<Long, List<Long>> cart, String rawCouponCode, String clientEmail) {
        double subtotal = 0.0;

        if (cart == null || cart.isEmpty()) {
            return new CheckoutSummary(0.0, 0.0, 0.0, null, null);
        }

        for (Map.Entry<Long, List<Long>> entry : cart.entrySet()) {
            Long screeningId = entry.getKey();
            List<Long> seatIds = entry.getValue();
            if (seatIds == null || seatIds.isEmpty()) continue;

            Optional<Screening> optScreening = screeningRepository.findById(screeningId);
            if (optScreening.isEmpty()) continue;
            Screening screening = optScreening.get();

            subtotal += screening.getPrice() * seatIds.size();
        }

        String normalizedCode = couponService.normalizeCode(rawCouponCode);
        if (normalizedCode == null || clientEmail == null || clientEmail.isBlank()) {
            return new CheckoutSummary(subtotal, 0.0, subtotal, null, null);
        }

        try {
            CouponService.CouponValidationResult validation =
                couponService.validateCoupon(normalizedCode, clientEmail, subtotal);
            double discount = Math.min(validation.getDiscountAmount(), subtotal);
            return new CheckoutSummary(subtotal, discount, Math.max(0.0, subtotal - discount), validation.getCoupon().getCode(), null);
        } catch (IllegalArgumentException ex) {
            return new CheckoutSummary(subtotal, 0.0, subtotal, normalizedCode, ex.getMessage());
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
            + " | row "
            + seat.getSeatRow()
            + " seat "
            + seat.getSeatNumber();
    }

    public static class CheckoutSummary {
        private final double subtotal;
        private final double discountAmount;
        private final double total;
        private final String appliedCouponCode;
        private final String couponMessage;

        public CheckoutSummary(double subtotal, double discountAmount, double total, String appliedCouponCode, String couponMessage) {
            this.subtotal = subtotal;
            this.discountAmount = discountAmount;
            this.total = total;
            this.appliedCouponCode = appliedCouponCode;
            this.couponMessage = couponMessage;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }

        public double getTotal() {
            return total;
        }

        public String getAppliedCouponCode() {
            return appliedCouponCode;
        }

        public String getCouponMessage() {
            return couponMessage;
        }

        public boolean hasDiscount() {
            return discountAmount > 0.0;
        }

        public boolean hasCouponMessage() {
            return couponMessage != null && !couponMessage.isBlank();
        }

        public String getRewardDescription() {
            return "Starting at " + String.format(Locale.US, "%.2f", CouponService.REWARD_THRESHOLD)
                + " €, you receive a " + String.format(Locale.US, "%.2f", CouponService.REWARD_DISCOUNT)
                + " € coupon for your next purchase.";
        }
    }
}
