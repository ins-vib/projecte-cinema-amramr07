package com.daw.CinemaDaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.CinemaDaw.domain.cinema.Cinema;
import com.daw.CinemaDaw.repository.CinemaRepository;

@Controller
public class CinemaController {


     private  CinemaRepository cinemaRepository;

    public CinemaController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

       


     // Listar cinemes
    @GetMapping("/cinemes")
    public String cinemes(Model model) {
        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);
        return "cinemes/cinemes";
    }

    // Detalle de un cine
    @GetMapping("/cinema/{id}")
    public String detall(@PathVariable Long id, Model model) {
        Optional<Cinema> optional = cinemaRepository.findById(id);
        if (optional.isPresent()) {
            Cinema cinema = optional.get();
            model.addAttribute("cinema", cinema);
            return "cinemes/cinemes-detall";
        }
        return "redirect:cinemes/cinemes";
    }

    // Borrar cine
    @GetMapping("/cinema/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        Optional<Cinema> optional = cinemaRepository.findById(id);
        optional.ifPresent(cinemaRepository::delete);
        return "redirect:cinemes/cinemes";
    }

   //Formulari crear cinema
    @GetMapping("/create/cinema")
    public String mostrarFormulariAlta(Model model) {
        Cinema cinema = new Cinema();
        //cinema.setCity("Tarragona");
        model.addAttribute("cinema",cinema);
       
        return "cinemes/create-cinema";
    }

    //Crear cinema
    @PostMapping("/cinema/create")
    public String guardarCinema(@ModelAttribute Cinema cinema) {
        cinemaRepository.save(cinema);
        
        return "redirect:cinemes/cinemes";
    }
    //Formulari ediatr cinema
    @GetMapping("/cinema/edit/{id}")
    public String mostrarFormulariEditarÇ(@PathVariable Long id, Model model){
        Optional<Cinema> optional = cinemaRepository.findById(id);
        if(optional.isPresent()){
            Cinema cinema = optional.get();
            model.addAttribute("cinema", cinema);
            return "cinemes/edit-cinema";
        }
        return "redirect:cinemes/cinemes";
        
    }

     //Editar cinema
     @PostMapping("/edit/cinema")
    public String editCinema(@ModelAttribute Cinema cinema) {
        cinemaRepository.save(cinema);
        
        return "redirect:cinemes/cinemes";
    }
}
    

