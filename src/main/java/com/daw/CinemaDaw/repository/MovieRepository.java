package com.daw.CinemaDaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daw.CinemaDaw.domain.cinema.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
            SELECT DISTINCT s.movie
            FROM Screening s 
            WHERE s.screeningDateTime >= CURRENT_TIMESTAMP
            """)
    List<Movie> findMoviesWithFutureScreenings();

}