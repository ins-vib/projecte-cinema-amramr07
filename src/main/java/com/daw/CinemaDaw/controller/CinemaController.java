package com.daw.CinemaDaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.CinemaDaw.DTO.ServicesListDTO;
import com.daw.CinemaDaw.domain.cinema.Cinema;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.repository.CinemaRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;

import jakarta.validation.Valid;

@Controller
public class CinemaController {

    private final CinemaRepository cinemaRepository;
    private final ScreeningRepository screeningRepository;

    public CinemaController(CinemaRepository cinemaRepository, ScreeningRepository screeningRepository) {
        this.cinemaRepository = cinemaRepository;
        this.screeningRepository = screeningRepository;
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
            model.addAttribute("cinema", optional.get());
            return "cinemes/cinemes-detall";
        }
        return "redirect:/cinemes";
    }

    // Borrar cine (borra primer els screenings de cada sala)
    @GetMapping("/cinema/delete/{id}")
    public String delete(@PathVariable Long id) {
        Optional<Cinema> optional = cinemaRepository.findById(id);
        if (optional.isPresent()) {
            Cinema cinema = optional.get();
            for (Room room : cinema.getRooms()) {
                screeningRepository.deleteAll(screeningRepository.findByRoomId(room.getId()));
            }
            cinemaRepository.delete(cinema);
        }
        return "redirect:/cinemes";
    }

    // Formulari crear cinema
    @GetMapping("/create/cinema")
    public String mostrarFormulariAlta(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "cinemes/create-cinema";
    }

    // Crear cinema
    @PostMapping("/cinema/create")
    public String guardarCinema(@Valid @ModelAttribute Cinema cinema, BindingResult result) {
        if (result.hasErrors()) {
            return "cinemes/create-cinema";
        }
        cinemaRepository.save(cinema);
        return "redirect:/cinemes";
    }

    // Formulari editar cinema
    @GetMapping("/cinema/edit/{id}")
    public String mostrarFormulariEditar(@PathVariable Long id, Model model) {
        Optional<Cinema> optional = cinemaRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("cinema", optional.get());
            return "cinemes/edit-cinema";
        }
        return "redirect:/cinemes";
    }

    // Editar cinema (solo actualiza campos, no toca las rooms)
    @PostMapping("/edit/cinema")
    public String editCinema(@Valid @ModelAttribute Cinema cinema, BindingResult result) {
        if (result.hasErrors()) {
            return "cinemes/edit-cinema";
        }
        Optional<Cinema> optional = cinemaRepository.findById(cinema.getId());
        if (optional.isPresent()) {
            Cinema existing = optional.get();
            existing.setName(cinema.getName());
            existing.setAdress(cinema.getAdress());
            existing.setCity(cinema.getCity());
            existing.setPostalCode(cinema.getPostalCode());
            cinemaRepository.save(existing);
        }
        return "redirect:/cinemes";
    }

    // Formulari serveis
    @GetMapping("/services")
    public String showForm(Model model) {
        model.addAttribute("servicesDTO", new ServicesListDTO());
        model.addAttribute("allServices", List.of(
            "crispetes",
            "parking",
            "begudes",
            "vip",
            "imax"
        ));
        return "cinemes/services-form";
    }

    @PostMapping("/services")
    public String save(@ModelAttribute ServicesListDTO dto) {
        System.out.println(dto.getServices());
        return "redirect:/";
    }
}