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

import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.repository.MovieRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;

import jakarta.validation.Valid;

@Controller
public class MovieController {

    private MovieRepository movieRepository;
    private ScreeningRepository screeningRepository;

  

    public MovieController(MovieRepository movieRepository, ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping("/movies")
public String movies(Model model) {
    List<Movie> movies = movieRepository.findAll();
    model.addAttribute("movies", movies);
    return "movies/Movies";
}

@GetMapping("/movies/create")
public String mostrarFormulariCrear(Model model) {
    model.addAttribute("movie", new Movie());
    return "movies/create-movie";
}

@PostMapping("/movies/create")
public String guardarMovie(@ModelAttribute Movie movie) {
    movieRepository.save(movie);
    return "redirect:/movies"; 


}


    // Show movie edit form
    @GetMapping("/movies/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            model.addAttribute("movie", optionalMovie.get());
            return "movies/edit-movie";
        }
        return "redirect:/movies"; 
    }

    // Update movie
   @PostMapping("/movies/edit")
public String updateMovie(@Valid @ModelAttribute Movie movie,
                          BindingResult result) {

    if(result.hasErrors()){
        return "movies/edit-movie";
    }

    Optional<Movie> optionalMovie = movieRepository.findById(movie.getId());
    if (optionalMovie.isPresent()) {
        movieRepository.save(movie);
    }

    return "redirect:/movies";
}

    // View movie details
  @GetMapping("/movies/{id}")
public String viewMovie(@PathVariable Long id, Model model) {
    Optional<Movie> optionalMovie = movieRepository.findById(id);
    if (optionalMovie.isPresent()) {
        model.addAttribute("movie", optionalMovie.get());
        model.addAttribute("screenings", screeningRepository.findByMovieId(id)); 
        
        return "movies/movie-details";
    }
    return "redirect:/movies";
}

@PostMapping("/movies/delete/{id}")
public String deleteMovie(@PathVariable Long id) {
    Optional<Movie> optionalMovie = movieRepository.findById(id);
    if (optionalMovie.isPresent()) {
        movieRepository.deleteById(id);
    }
    return "redirect:/movies";
}

}


