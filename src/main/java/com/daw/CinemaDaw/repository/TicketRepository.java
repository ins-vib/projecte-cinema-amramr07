package com.daw.CinemaDaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.order.Ticket;
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByScreeningId(Long screeningId);
    boolean existsByScreeningIdAndSeatId(Long screeningId, Long seatId);
}