package com.daw.CinemaDaw.controller;

import java.util.Comparator;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.CinemaDaw.domain.cinema.Cinema;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.repository.CinemaRepository;
import com.daw.CinemaDaw.repository.RoomRepository;

import jakarta.validation.Valid;

@Controller
public class RoomController {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public RoomController(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    // Veure detall sala
    @GetMapping("/room/{id}")
    public String detall(@PathVariable Long id, Model model) {
        Optional<Room> optional = roomRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("room", optional.get());
            return "rooms/room-details";
        }
        return "redirect:/cinemes";
    }

    // Mostrar formulari crear sala
    @GetMapping("/room/create")
    public String mostrarFormulariCrear(@RequestParam Long cinemaId, Model model) {
        Optional<Cinema> optional = cinemaRepository.findById(cinemaId);
        if (optional.isPresent()) {
            model.addAttribute("room", new Room());
            model.addAttribute("cinema", optional.get());
            return "rooms/create-room";
        }
        return "redirect:/cinemes";
    }

   @PostMapping("/room/create")
public String crearRoom(@Valid @ModelAttribute Room room,
                        BindingResult result,
                        @RequestParam Long cinemaId,
                        Model model) {

    if (result.hasErrors()) {
        model.addAttribute("cinemaId", cinemaId);
        cinemaRepository.findById(cinemaId).ifPresent(c -> model.addAttribute("cinema", c)); 
        return "rooms/create-room";
    }

    Optional<Cinema> optional = cinemaRepository.findById(cinemaId);
    if (optional.isPresent()) {
        room.setCinema(optional.get());
        roomRepository.save(room);
        return "redirect:/cinema/" + cinemaId;
    }

    return "redirect:/cinemes";
}

    // Mostrar formulari editar sala
    @GetMapping("/room/edit/{id}")
    public String mostrarFormulariEditar(@PathVariable Long id, Model model) {
        Optional<Room> optional = roomRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("room", optional.get());
            return "rooms/edit-room";
        }
        return "redirect:/cinemes";
    }

   @PostMapping("/room/edit")
public String editRoom(@Valid @ModelAttribute Room room,
                       BindingResult result,
                       Model model) {

    Optional<Room> optional = roomRepository.findById(room.getId());

    if (result.hasErrors()) {
        if (optional.isPresent()) {
            room.setCinema(optional.get().getCinema()); 
            model.addAttribute("cinema", optional.get().getCinema());
        }
        return "rooms/edit-room";
    }

    if (optional.isPresent()) {
        Room existing = optional.get();
        existing.setName(room.getName());
        existing.setCapacity(room.getCapacity());
        roomRepository.save(existing);
        return "redirect:/cinema/" + existing.getCinema().getId();
    }

    return "redirect:/cinemes";
}

    // Esborrar sala
    @GetMapping("/room/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        Optional<Room> optional = roomRepository.findById(id);
        if (optional.isPresent()) {
            Long cinemaId = optional.get().getCinema().getId();
            roomRepository.delete(optional.get());
            return "redirect:/cinema/" + cinemaId;
        }
        return "redirect:/cinemes";
    }

@GetMapping("/room/{id}/seats")
public String verSeients(@PathVariable Long id, Model model) {
    Optional<Room> optional = roomRepository.findById(id);
    if (optional.isPresent()) {
        Room room = optional.get();

        
        room.getSeats().sort(
            Comparator.comparingInt(Seat::getSeatRow)
                      .thenComparingInt(Seat::getSeatNumber)
        );

        model.addAttribute("room", room);
        return "rooms/room-seats";
    }
    return "redirect:/cinemes";
}
}