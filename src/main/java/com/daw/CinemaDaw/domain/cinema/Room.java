package com.daw.CinemaDaw.domain.cinema;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Room {


   

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="The  name is obligatory")
    @Size(min=2, max=100, message= "The name has to have beetwen 2 and 100 characters")
    @Column
    private String name;
    
    @NotNull(message="The capacity is obligatory")
    @Min(value=10, message="The minimum capacity is 10")
    @Max(value=999, message="The maximum capacity is 999")
    @Column
    private int capacity;
    @ManyToOne
    private Cinema cinema;

    @OneToMany(mappedBy="room", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Seat> seats = new ArrayList<>();
    
     public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    
    public Room(String name, int capacity) {
        
        this.name = name;
        this.capacity = capacity;
    }

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", capacity=").append(capacity);
        sb.append(", cinema=").append(cinema);
        sb.append(", seats=").append(seats);
        sb.append('}');
        return sb.toString();
    }

 
   

   
    
}
