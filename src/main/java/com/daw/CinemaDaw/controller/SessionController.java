package com.daw.CinemaDaw.controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller

@RequestMapping("/session")

public class SessionController {

    // Establir dades a la sessió
    @GetMapping("/setSessionData")
    public String setSessionData(@RequestParam("username") String username, HttpSession session) {
        // Emmagatzemem el nom d'usuari a la sessió
        session.setAttribute("username", username);
        
        // Obtenim l'hora actual i la desem a la sessió
        String horaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        session.setAttribute("horaActual", horaActual);

        return "redirect:/session/showSessionData";  // Redirigim per mostrar les dades
    }

    // Mostrar dades de la sessió
    @GetMapping("/showSessionData")
    public String showSessionData(HttpSession session, Model model) {
        // Recuperem el nom d'usuari de la sessió
        String username = (String) session.getAttribute("username");
        // Afegim el nom d'usuari al model per mostrar-lo
        model.addAttribute("username", username);

        return "session/sessionData";  // Vista on es mostren les dades
    }

    // Ruta per a mostrar el formulari
    @GetMapping("/")
    public String index() {
        return "session/index";  // Vista per introduir el nom d'usuari
    }
}
    
