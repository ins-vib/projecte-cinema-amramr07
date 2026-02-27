package com.daw.CinemaDaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.cinema.Seat;



public interface SeatRepository extends JpaRepository<Seat, Long>{
    
}
