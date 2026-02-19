package com.daw.CinemaDaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.CinemaDaw.domain.cinema.cinema;




@Repository
public interface CinemaRepository extends JpaRepository<cinema, Long> {
    
}
