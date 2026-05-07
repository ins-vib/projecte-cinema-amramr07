package com.daw.CinemaDaw.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.CinemaDaw.domain.cinema.Genre;
import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.repository.GenreRepository;
import com.daw.CinemaDaw.repository.MovieRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;

import jakarta.validation.Valid;

@Controller
public class MovieController {

    private MovieRepository movieRepository;
    private ScreeningRepository screeningRepository;
    private GenreRepository genreRepository;

  

    public MovieController(MovieRepository movieRepository, ScreeningRepository screeningRepository,
            GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.genreRepository = genreRepository;
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
    addGenreAttributes(model, Set.of());
    return "movies/create-movie";
}

@PostMapping("/movies/create")
public String guardarMovie(@Valid @ModelAttribute Movie movie,
                           BindingResult result,
                           @RequestParam(name = "genreIds", required = false) List<Long> genreIds,
                           Model model) {
    Set<Genre> selectedGenres = loadSelectedGenres(genreIds);
    movie.setGenres(selectedGenres);
    movie.setGenre(movie.getGenresText());

    if (selectedGenres.isEmpty()) {
        result.rejectValue("genres", "movie.genres.required", "Has de seleccionar almenys un gènere.");
    }

    if (result.hasErrors()) {
        addGenreAttributes(model, selectedGenres);
        return "movies/create-movie";
    }
    movieRepository.save(movie);
    return "redirect:/movies"; 


}


    // Show movie edit form
    @GetMapping("/movies/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            model.addAttribute("movie", movie);
            addGenreAttributes(model, movie.getGenres());
            return "movies/edit-movie";
        }
        return "redirect:/movies"; 
    }

    // Update movie
   @PostMapping("/movies/edit")
public String updateMovie(@Valid @ModelAttribute Movie movie,
                          BindingResult result,
                          @RequestParam(name = "genreIds", required = false) List<Long> genreIds,
                          Model model) {
    Set<Genre> selectedGenres = loadSelectedGenres(genreIds);
    movie.setGenres(selectedGenres);
    movie.setGenre(movie.getGenresText());

    if (selectedGenres.isEmpty()) {
        result.rejectValue("genres", "movie.genres.required", "Has de seleccionar almenys un gènere.");
    }

    if(result.hasErrors()){
        addGenreAttributes(model, selectedGenres);
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
        // Primero borrar las sesiones asociadas
        screeningRepository.deleteAll(screeningRepository.findByMovieId(id));
        movieRepository.deleteById(id);
    }
    return "redirect:/movies";
}

private Set<Genre> loadSelectedGenres(List<Long> genreIds) {
    if (genreIds == null || genreIds.isEmpty()) {
        return Set.of();
    }
    return new HashSet<>(genreRepository.findAllById(genreIds));
}

private void addGenreAttributes(Model model, Set<Genre> selectedGenres) {
    model.addAttribute("allGenres", genreRepository.findAll());
    model.addAttribute("selectedGenreIds", selectedGenres.stream()
            .map(Genre::getId)
            .toList());
}
}
