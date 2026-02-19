package com.daw.CinemaDaw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.CinemaDaw.domain.cinema.cinema;
import com.daw.CinemaDaw.repository.CinemaRepository;

@Component
public class Proves implements CommandLineRunner{
    private CinemaRepository cinemaRepository;
 
 
 
    public Proves(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }
   
   
   
    @Override
    public void run (String... args) throws Exception{
        
       cinema cinema1 = new cinema("Ocine", "Gavarres, 46", "Tarragona", "43122");
       cinemaRepository.save(cinema1);
    }

   
    
}
