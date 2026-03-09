package com.daw.CinemaDaw.controller;



import java.io.FileNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.CinemaDaw.service.NewsService;

@Controller
public class HomeController {

   

    // Página principal
    @GetMapping("/")
    public String home() {

        NewsService newsService = new NewsService();
        try {
            newsService.getNews();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("No he pogut obrir el fitxer");
        }
        return "home";
    }

   
}