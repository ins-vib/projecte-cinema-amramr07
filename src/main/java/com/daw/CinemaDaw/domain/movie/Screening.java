package com.daw.CinemaDaw.domain.movie;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.domain.cinema.Room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "screening_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime screeningDateTime;

    @Column
    private double price;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Room room;

    public Screening() {
    }

    public Screening(LocalDateTime screeningDateTime, double price, Movie movie, Room room) {
        this.screeningDateTime = screeningDateTime;
        this.price = price;
        this.movie = movie;
        this.room = room;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getScreeningDateTime() { return screeningDateTime; }
    public void setScreeningDateTime(LocalDateTime screeningDateTime) { this.screeningDateTime = screeningDateTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
}