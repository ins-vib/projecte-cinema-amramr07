package com.daw.CinemaDaw.config;


import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.daw.CinemaDaw.domain.cinema.Cinema;
import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.cinema.SeatType;
import com.daw.CinemaDaw.domain.user.Role;
import com.daw.CinemaDaw.domain.user.User;
import com.daw.CinemaDaw.repository.CinemaRepository;
import com.daw.CinemaDaw.repository.RoomRepository;
import com.daw.CinemaDaw.repository.SeatRepository;
import com.daw.CinemaDaw.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class Proves implements CommandLineRunner{
    private final CinemaRepository cinemaRepository;
    private RoomRepository roomRepository;
    private SeatRepository seatRepository;
    private UserRepository userRepository;
    BCryptPasswordEncoder encoder;

 
 
 
  
   
   
   
  







    public Proves(CinemaRepository cinemaRepository, RoomRepository roomRepository, SeatRepository seatRepository,
            UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }







    @Override
    @Transactional
    
    public void run (String... args) throws Exception{

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("1234"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        
        User client = new User();
        client.setUsername("client");
        client.setPassword(encoder.encode("1234"));
        client.setRole(Role.CLIENT);
        userRepository.save(client);
        
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

for (Room room : allRooms) {
    int cols = 10; // número de asientos por fila (puedes cambiar)
    for (int i = 0; i < room.getCapacity(); i++) {
        int row = i / cols + 1;
        int number = i % cols + 1;
        Seat seat = new Seat(row, number, row, number, SeatType.STANDARD, true);
        seat.setRoom(room);
        seatRepository.save(seat);
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
