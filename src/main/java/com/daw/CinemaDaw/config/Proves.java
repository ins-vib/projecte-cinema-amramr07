package com.daw.CinemaDaw.config;


import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.CinemaDaw.domain.cinema.Cinema;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.cinema.SeatType;
import com.daw.CinemaDaw.repository.CinemaRepository;
import com.daw.CinemaDaw.repository.RoomRepository;
import com.daw.CinemaDaw.repository.SeatRepository;

import jakarta.transaction.Transactional;

@Component
public class Proves implements CommandLineRunner{
    private CinemaRepository cinemaRepository;
    private RoomRepository roomRepository;
    private SeatRepository seatRepository;
 
 
 
    public Proves(CinemaRepository cinemaRepository, RoomRepository roomRepository, SeatRepository seatRepository) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.seatRepository= seatRepository;
    }
   
   
   
    @Override
    @Transactional
    
    public void run (String... args) throws Exception{
        
       Cinema cinema1 = new Cinema("Ocine", "Gavarres, 46", "Tarragona", "43122");
       Cinema cinema2 = new Cinema("Oscars", "Major,15", "Tarragona", "43100");
       cinemaRepository.save(cinema1);
       cinemaRepository.save(cinema2);

       Room room1 = new Room("Sala 1", 120);
       room1.setCinema(cinema1);
       roomRepository.save(room1);

       Room room2 = new Room("Sala 2", 120);
    room2.setCinema(cinema1); 
    roomRepository.save(room2);

    Room room3 = new Room("Sala 3", 120);
    room3.setCinema(cinema1); 
    roomRepository.save(room3);

    Cinema cinema3D = new Cinema("Cinema3D", "Major,15", "Tarragona", "43100");
    Room room3D1 = new Room("Sala 3D 1", 500);
    room3D1.setCinema(cinema3D);
    cinema3D.getRooms().add(room3D1);
    cinemaRepository.save(cinema3D);

    
    
    

     

       List<Room> allRooms = roomRepository.findAll();

List<Room> allRooms1 = roomRepository.findAll();

for (Room room : allRooms) {
    for (int i = 0; i < 10; i++) {
        for(int j = 0; j<10; j++){
            Seat seat = new Seat(j, j, i, j, SeatType.STANDARD, true);
            seat.setRoom(room);
            seatRepository.save(seat);
        }
        
    }

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

       Optional<Cinema> optionalC = cinemaRepository.findById(1L);
       if (optionalC.isPresent()){
        Cinema c = optionalC.get();
        System.out.println(c);


       List<Room> sales2 =  c.getRooms();
       for(Room r: sales2){
        System.out.println(r);
        List<Seat> seients2 = r.getSeats();
        for(Seat s: seients2){
            System.out.println(s);
        }

       }
       }

    
    }

   
    
}
