package com.daw.CinemaDaw.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.daw.CinemaDaw.domain.movie.New;

@Service
public class NewsService {

    public ArrayList<New> getNews() throws FileNotFoundException {

        ArrayList<New> newlist = new ArrayList<>();
        
        var inputStream = getClass().getClassLoader().getResourceAsStream("news.txt");
        
        if (inputStream == null) {
            System.out.println("No existeix l'arxiu");
            return newlist;
        }

        Scanner lectorFitxer = new Scanner(inputStream);
        
        while (lectorFitxer.hasNextLine()) {
            String linia = lectorFitxer.nextLine();
            String[] camps = linia.split(":");
            
            if (camps.length >= 2) {  // evitar ArrayIndexOutOfBoundsException
                New n = new New(camps[0], camps[1]);
                newlist.add(n);
            }
        }
        
        
        return newlist;
    }
}