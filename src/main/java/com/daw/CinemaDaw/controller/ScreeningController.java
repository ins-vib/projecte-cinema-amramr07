package com.daw.CinemaDaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.domain.movie.Screening;
import com.daw.CinemaDaw.repository.MovieRepository;
import com.daw.CinemaDaw.repository.RoomRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;

@Controller
public class ScreeningController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ScreeningController(ScreeningRepository screeningRepository,
                               MovieRepository movieRepository,
                               RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    // Mostrar formulari crear sessió des d'una pel·lícula
    @GetMapping("/screening/create")
    public String showCreateForm(@RequestParam Long movieId, Model model) {
        Optional<Movie> optMovie = movieRepository.findById(movieId);
        if (optMovie.isPresent()) {
            List<Room> rooms = roomRepository.findAll();
            model.addAttribute("screening", new Screening());
            model.addAttribute("movie", optMovie.get());
            model.addAttribute("rooms", rooms);
            return "screenings/create-screening";
        }
        return "redirect:/movies";
    }

    // Guardar nova sessió
    @PostMapping("/screening/create")
    public String createScreening(@ModelAttribute Screening screening,
                                  @RequestParam Long movieId,
                                  @RequestParam Long roomId,
                                  Model model) {
        Optional<Movie> optMovie = movieRepository.findById(movieId);
        Optional<Room> optRoom = roomRepository.findById(roomId);

        if (optMovie.isPresent() && optRoom.isPresent()) {
            screening.setMovie(optMovie.get());
            screening.setRoom(optRoom.get());
            screeningRepository.save(screening);
            return "redirect:/movies/" + movieId;
        }
        return "redirect:/movies";
    }

    // Mostrar formulari editar sessió
    @GetMapping("/screening/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Screening> optional = screeningRepository.findById(id);
        if (optional.isPresent()) {
            List<Room> rooms = roomRepository.findAll();
            model.addAttribute("screening", optional.get());
            model.addAttribute("rooms", rooms);
            return "screenings/edit-screening";
        }
        return "redirect:/movies";
    }

    // Actualitzar sessió
    @PostMapping("/screening/edit")
    public String updateScreening(@ModelAttribute Screening screening,
                                  @RequestParam Long roomId) {
        Optional<Screening> optional = screeningRepository.findById(screening.getId());
        Optional<Room> optRoom = roomRepository.findById(roomId);

        if (optional.isPresent() && optRoom.isPresent()) {
            Screening existing = optional.get();
            existing.setScreeningDateTime(screening.getScreeningDateTime());
            existing.setPrice(screening.getPrice());
            existing.setRoom(optRoom.get());
            screeningRepository.save(existing);
            return "redirect:/movies/" + existing.getMovie().getId();
        }
        return "redirect:/movies";
    }

    // Esborrar sessió
    @GetMapping("/screening/delete/{id}")
    public String deleteScreening(@PathVariable Long id) {
        Optional<Screening> optional = screeningRepository.findById(id);
        if (optional.isPresent()) {
            Long movieId = optional.get().getMovie().getId();
            screeningRepository.delete(optional.get());
            return "redirect:/movies/" + movieId;
        }
        return "redirect:/movies";
    }
}