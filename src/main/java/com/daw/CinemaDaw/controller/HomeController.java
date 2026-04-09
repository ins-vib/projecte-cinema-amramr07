package com.daw.CinemaDaw.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.movie.New;
import com.daw.CinemaDaw.domain.movie.Screening;
import com.daw.CinemaDaw.domain.user.Role;
import com.daw.CinemaDaw.domain.user.User;
import com.daw.CinemaDaw.repository.MovieRepository;
import com.daw.CinemaDaw.repository.ScreeningRepository;
import com.daw.CinemaDaw.repository.TicketRepository;
import com.daw.CinemaDaw.repository.UserRepository;
import com.daw.CinemaDaw.service.NewsService;

@Controller
public class HomeController {

    private NewsService newsService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;
    private MovieRepository movieRepository;
    private ScreeningRepository screeningRepository;
    private TicketRepository ticketRepository;

   

    public HomeController(NewsService newsService, UserRepository userRepository, BCryptPasswordEncoder encoder,
            MovieRepository movieRepository, ScreeningRepository screeningRepository,
            TicketRepository ticketRepository) {
        this.newsService = newsService;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {
        ArrayList<New> llista = new ArrayList<>();
        try {
            llista = newsService.getNews();
        } catch (FileNotFoundException e) {
            System.out.println("No he pogut obrir el fitxer");
        }
        model.addAttribute("llista", llista);

       
   
        return "/landing";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/home";
    }

    // ✅ Ahora movieRepository no será null
    @GetMapping("/client")
    public String client(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "client/home";
    }

    @GetMapping("/client/movie/{id}")
    public String clientMovieDetail(@PathVariable Long id, Model model) {
        Optional<Movie> optional = movieRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("movie", optional.get());
            model.addAttribute("screenings", screeningRepository.findByMovieId(id));
            return "client/movie-detail";
        }
        return "redirect:/client";
    }

    @PostMapping("/client/reserve")
public String reserve(@RequestParam Long seatId,
                      @RequestParam Long screeningId) {

    System.out.println("Seat: " + seatId);
    System.out.println("Screening: " + screeningId);

    // aquí luego harás lógica real de reserva

    return "redirect:/client";
}

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute User user,
                             @RequestParam String confirmPassword,
                             Model model) {
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Les contrasenyes no coincideixen.");
            return "register";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.CLIENT);
        userRepository.save(user);
        return "redirect:/login?registered";
    }

@GetMapping("/landing")
public String landing(Model model) {
    ArrayList<New> llista = new ArrayList<>();
    try {
        llista = newsService.getNews();
    } catch (FileNotFoundException e) {
        System.out.println("No he pogut obrir el fitxer");
    }
    model.addAttribute("llista", llista);
    return "landing";
}

@GetMapping("/home")
public String Home(Model model) {
    ArrayList<New> llista = new ArrayList<>();
    try {
        llista = newsService.getNews();
    } catch (FileNotFoundException e) {
        System.out.println("No he pogut obrir el fitxer");
    }
    model.addAttribute("llista", llista);
    return "home";
}

@GetMapping("/client/screening/{id}")
public String viewScreening(@PathVariable Long id, Model model) {
    Optional<Screening> optional = screeningRepository.findById(id);

    if (optional.isPresent()) {
        Screening screening = optional.get();
        
        List<Seat> allSeats = screening.getRoom().getSeats();
        
       
        List<Seat> freeSeats = allSeats.stream()
            .filter(seat -> !ticketRepository.existsByScreeningIdAndSeatId(id, seat.getId()))
            .filter(Seat::isActive) 
            .toList();

        model.addAttribute("screening", screening);
        model.addAttribute("seats", freeSeats); 

        return "client/screening-detail";
    }
    return "redirect:/client";
}
}