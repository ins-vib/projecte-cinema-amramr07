package com.daw.CinemaDaw.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.cinema.Movie;
import com.daw.CinemaDaw.domain.movie.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovieId(Long movieId);
    List<Screening> findByRoomId(Long roomId); 
    List<Screening> findByMovieAndScreeningDateTimeGreaterThanEqualOrderByScreeningDateTimeAsc(
        Movie movie,
        LocalDateTime datetime
    );
}