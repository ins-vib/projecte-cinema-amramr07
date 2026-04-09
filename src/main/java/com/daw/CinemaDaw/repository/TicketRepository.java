package com.daw.CinemaDaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    boolean existsByScreeningIdAndSeatId(Long screeningId, Long seatId);
    
   
    List<Ticket> findByScreeningId(Long screeningId);
}
