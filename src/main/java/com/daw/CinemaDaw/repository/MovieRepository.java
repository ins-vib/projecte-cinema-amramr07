package com.daw.CinemaDaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.cinema.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}