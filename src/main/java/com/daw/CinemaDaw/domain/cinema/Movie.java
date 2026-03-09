package com.daw.CinemaDaw.domain.cinema;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(name="duration_minutes",nullable=false)
    private int duration; // minuts

    @Column(length=50)
    private String genre;

    @Column(columnDefinition="TEXT")
    private String sinopsis;

    @Column(name = "release_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate releaseDate;

    


    public Movie() {
    }

    public Movie(String title, int duration, String genre, String description, LocalDate releaseDate) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.sinopsis = description;
        this.releaseDate = releaseDate;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return sinopsis;
    }

    public void setDescription(String description) {
        this.sinopsis = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

  

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + "]";
    }
}
