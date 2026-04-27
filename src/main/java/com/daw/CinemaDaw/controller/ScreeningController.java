package com.daw.CinemaDaw.controller;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.domain.movie.Screening;
import com.daw.CinemaDaw.repository.MovieRepository;
import com.daw.CinemaDaw.repository.RoomRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;

import jakarta.validation.Valid;

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
    public String showCreateForm(@RequestParam Long movieId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Optional<Movie> optMovie = movieRepository.findById(movieId);
        if (optMovie.isPresent()) {
            if (!model.containsAttribute("screening")) {
                model.addAttribute("screening", new Screening());
            }
            loadCreateForm(model, optMovie.get());
            return "screenings/create-screening";
        }
        redirectAttributes.addFlashAttribute("screeningError", "La pellicula indicada no existeix.");
        return "redirect:/movies";
    }

    // Guardar nova sessió
    @PostMapping("/screening/create")
    public String createScreening(@Valid @ModelAttribute Screening screening,
                                  BindingResult result,
                                  @RequestParam Long movieId,
                                  @RequestParam Long roomId,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Optional<Movie> optMovie = movieRepository.findById(movieId);
        if (optMovie.isEmpty()) {
            redirectAttributes.addFlashAttribute("screeningError", "La pellicula indicada no existeix.");
            return "redirect:/movies";
        }

        Optional<Room> optRoom = roomRepository.findById(roomId);
        if (optRoom.isEmpty()) {
            result.addError(new FieldError("screening", "room", "Has de seleccionar una sala valida."));
        }

        if (result.hasErrors()) {
            loadCreateForm(model, optMovie.get());
            model.addAttribute("roomError", getRoomError(result));
            return "screenings/create-screening";
        }

        try {
            screening.setMovie(optMovie.get());
            screening.setRoom(optRoom.get());
            screeningRepository.save(screening);
            redirectAttributes.addFlashAttribute("screeningMessage", "Sessio creada correctament.");
            return "redirect:/movies/" + movieId;
        } catch (DataIntegrityViolationException ex) {
            loadCreateForm(model, optMovie.get());
            model.addAttribute("screeningError", "No s'ha pogut desar la sessio. Revisa les dades.");
            return "screenings/create-screening";
        }
    }

    // Mostrar formulari editar sessió
    @GetMapping("/screening/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Optional<Screening> optional = screeningRepository.findById(id);
        if (optional.isPresent()) {
            if (!model.containsAttribute("screening")) {
                model.addAttribute("screening", optional.get());
            }
            loadEditForm(model, optional.get());
            return "screenings/edit-screening";
        }
        redirectAttributes.addFlashAttribute("screeningError", "La sessio que vols editar no existeix.");
        return "redirect:/movies";
    }

    // Actualitzar sessió
    @PostMapping("/screening/edit")
    public String updateScreening(@Valid @ModelAttribute Screening screening,
                                  BindingResult result,
                                  @RequestParam Long roomId,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Optional<Screening> optional = screeningRepository.findById(screening.getId());
        if (optional.isEmpty()) {
            redirectAttributes.addFlashAttribute("screeningError", "La sessio que vols editar no existeix.");
            return "redirect:/movies";
        }

        Screening existing = optional.get();
        Optional<Room> optRoom = roomRepository.findById(roomId);
        if (optRoom.isEmpty()) {
            result.addError(new FieldError("screening", "room", "Has de seleccionar una sala valida."));
        }

        if (result.hasErrors()) {
            screening.setMovie(existing.getMovie());
            screening.setRoom(existing.getRoom());
            loadEditForm(model, existing);
            model.addAttribute("screening", screening);
            model.addAttribute("roomError", getRoomError(result));
            return "screenings/edit-screening";
        }

        try {
            existing.setScreeningDateTime(screening.getScreeningDateTime());
            existing.setPrice(screening.getPrice());
            existing.setRoom(optRoom.get());
            screeningRepository.save(existing);
            redirectAttributes.addFlashAttribute("screeningMessage", "Sessio actualitzada correctament.");
            return "redirect:/movies/" + existing.getMovie().getId();
        } catch (DataIntegrityViolationException ex) {
            loadEditForm(model, existing);
            model.addAttribute("screening", screening);
            model.addAttribute("screeningError", "No s'ha pogut actualitzar la sessio. Revisa les dades.");
            model.addAttribute("roomError", null);
            return "screenings/edit-screening";
        }
    }

    // Esborrar sessió
    @PostMapping("/screening/delete/{id}")
    public String deleteScreening(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Screening> optional = screeningRepository.findById(id);
        if (optional.isPresent()) {
            Long movieId = optional.get().getMovie().getId();
            try {
                screeningRepository.delete(optional.get());
                redirectAttributes.addFlashAttribute("screeningMessage", "Sessio esborrada correctament.");
            } catch (DataIntegrityViolationException ex) {
                redirectAttributes.addFlashAttribute("screeningError", "No s'ha pogut esborrar la sessio perquè te dades relacionades.");
            }
            return "redirect:/movies/" + movieId;
        }
        redirectAttributes.addFlashAttribute("screeningError", "La sessio que vols esborrar no existeix.");
        return "redirect:/movies";
    }

    private void loadCreateForm(Model model, Movie movie) {
        model.addAttribute("movie", movie);
        model.addAttribute("rooms", roomRepository.findAll());
    }

    private void loadEditForm(Model model, Screening screening) {
        model.addAttribute("movie", screening.getMovie());
        model.addAttribute("rooms", roomRepository.findAll());
    }

    private String getRoomError(BindingResult result) {
        return result.getFieldErrors("room")
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(null);
    }
}
