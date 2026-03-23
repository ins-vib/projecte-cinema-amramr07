package com.daw.CinemaDaw.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daw.CinemaDaw.domain.user.User;
import com.daw.CinemaDaw.repository.UserRepository;

// Indica que és un servei de Spring
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repositori per accedir a la BD
    private final UserRepository userRepository;

    // Injecció de dependències per constructor
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Mètode clau que Spring Security utilitza en el login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Busquem l'usuari a la base de dades
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat"));

        // Convertim el nostre User a un UserDetails de Spring
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()) // username
                .password(user.getPassword())     // password encriptat
                .roles(user.getRole().name())     // rol: ADMIN o CLIENT
                .build();
    }
}