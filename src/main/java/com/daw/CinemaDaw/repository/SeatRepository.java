package com.daw.CinemaDaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.CinemaDaw.domain.cinema.Seat;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{
    
}
