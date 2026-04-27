package com.daw.CinemaDaw.domain.cinema;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {

    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotBlank(message="The movie title is obligatory")
@Size(min=2, max=100, message="The title has to have between 2 and 100 characters")
@Column(nullable = false)
private String title;

@NotNull(message="The movie duration is obligatory")
@Min(value = 1, message="The duration must be at least 1 minute")
@Max(value = 500, message="The duration must be at most 500 minutes")
@Column(name="duration_minutes", nullable=false)
private Integer duration; // Cambié de int a Integer

@NotBlank(message="The genre is obligatory")
@Size(min=2, max=100, message="The genre has to have between 2 and 100 characters")
@Column(length=100, nullable=false)
private String genre;

@Column(columnDefinition="TEXT")
private String sinopsis;

@Column(name = "poster_url")
private String posterUrl;

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

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
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

    public Integer getDuration() {
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
