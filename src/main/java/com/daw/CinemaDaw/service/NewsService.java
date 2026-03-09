package com.daw.CinemaDaw.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.daw.CinemaDaw.domain.movie.New;

@Service
public class NewsService {

    public ArrayList<New> getNews() throws FileNotFoundException{

        ArrayList<New> newlist = new ArrayList<>();
         Scanner lectorFitxer = new Scanner(
        getClass().getClassLoader().getResourceAsStream("news.txt")
    );
        // Llegir un fitxer de text línia a línia
        File f = new File("news.txt");
        if (f.exists()) {
                // llegir l'arxiu
                
                String linia;
                while(lectorFitxer.hasNextLine()) {
                    linia = lectorFitxer.nextLine();
                    String[] camps = linia.split(":");

                    New n = new New(camps[0], camps[1]);
                   
                    newlist.add(n);

                 System.out.println(linia);
                }
        }
        else {
                System.out.println("No existeix l'arxiu");
        }
        return newlist;

    }
    
}
