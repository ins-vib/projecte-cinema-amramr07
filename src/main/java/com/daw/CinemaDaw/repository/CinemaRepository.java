package com.daw.CinemaDaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.CinemaDaw.domain.cinema.Cinema;




@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findByCity(String city);

    
}
