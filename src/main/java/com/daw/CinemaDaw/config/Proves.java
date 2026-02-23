package com.daw.CinemaDaw.config;


import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.CinemaDaw.domain.cinema.Cinema;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.repository.CinemaRepository;
import com.daw.CinemaDaw.repository.RoomRepository;

@Component
public class Proves implements CommandLineRunner{
    private CinemaRepository cinemaRepository;
    private RoomRepository roomRepository;
 
 
 
    public Proves(CinemaRepository cinemaRepository, RoomRepository roomRepository) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
    }
   
   
   
    @Override
    public void run (String... args) throws Exception{
        
       Cinema cinema1 = new Cinema("Ocine", "Gavarres, 46", "Tarragona", "43122");
       Cinema cinema2 = new Cinema("Oscars", "Major,15", "Tarragona", "43100");
       cinemaRepository.save(cinema1);
       cinemaRepository.save(cinema2);

       Room room1 = new Room("Sala 1", 120);
       room1.setCinema(cinema1);
       roomRepository.save(room1);

       Room room2 = new Room("Sala 2", 80);
    room2.setCinema(cinema1); 
    roomRepository.save(room2);

    Room room3 = new Room("Sala 3", 30);
    room3.setCinema(cinema1); 
    roomRepository.save(room3);
    
    System.out.println(room3.getCinema().getName());

     Optional<Cinema> optionalCinema =cinemaRepository.findById(4L);
       if(optionalCinema.isPresent()){
        Cinema cinema = optionalCinema.get();
        List<Room> rooms = cinema.getRooms();

        for(Room room : rooms){
            System.out.println(room);
        }
      
       }
       else{
        System.out.println("No trobat");
       }
   

      /*  List<Cinema> llista = cinemaRepository.findAll();
       for (Cinema cinema  : llista) {
           System.out.println(cinema);
       }


       Optional<Cinema> optionalCinema =cinemaRepository.findById(4L);
       if(optionalCinema.isPresent()){
        Cinema cinema = optionalCinema.get();
        System.out.println(cinema);
        cinema.setCity("Reus");
        cinemaRepository.save(cinema);
       }
       else{
        System.out.println("No trobat");
       }

         List<Cinema> llista2 = cinemaRepository.findByCity("Tarragona");
       for (Cinema cinema  : llista2) {
           System.out.println(cinema);
       }

      

       llista = cinemaRepository.findAll();
       for (Cinema cinema  : llista) {
           System.out.println(cinema);
       }*/

       

    
    }

   
    
}
