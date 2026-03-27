package com.daw.CinemaDaw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cookies")
public class CookieController {

    // Mostrar formulari
    @GetMapping("/form-cookie")
    public String showForm() {
        return "cookies/form-cookie";
    }

    // Guardar cookie
    @PostMapping("/save-cookie")
    public String saveCookie(@RequestParam String username,
                             HttpServletResponse response) {

        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(60 * 60 * 24); // 1 dia
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/cookies/showCookie";
    }

    // Mostrar cookie
    @GetMapping("/showCookie")
    public String showCookie(
        @CookieValue(value = "username", required = false) String username,
        Model model) {

        model.addAttribute("username", username);
        return "cookies/show-cookie";
    }

    // Eliminar cookie
    @GetMapping("/delete-cookie")
    public String deleteCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie("username", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/cookies/showCookie";
    }
}
