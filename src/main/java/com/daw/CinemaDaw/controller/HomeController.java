package com.daw.CinemaDaw.controller;



import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.CinemaDaw.domain.movie.New;
import com.daw.CinemaDaw.service.NewsService;

@Controller
public class HomeController {
    private NewsService newsService;

public HomeController(NewsService newsService){
    this.newsService = newsService;
}

   

    // Página principal
    @GetMapping("/")
    public String home(Model model) {
 ArrayList<New> llista = new ArrayList<>();
        
        try {
            llista=newsService.getNews();
          
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("No he pogut obrir el fitxer");
        }
        model.addAttribute("llista", llista);
        return "home";
    }

   
}