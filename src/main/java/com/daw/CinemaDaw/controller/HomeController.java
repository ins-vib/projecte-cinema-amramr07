package com.daw.CinemaDaw.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.CinemaDaw.repository.CinemaRepository;

@Controller
public class HomeController {

    private  CinemaRepository cinemaRepository;

    public HomeController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    // Página principal
    @GetMapping("/")
    public String home() {
        return "home";
    }

   
}