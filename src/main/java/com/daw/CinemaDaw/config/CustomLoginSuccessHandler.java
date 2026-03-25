package com.daw.CinemaDaw.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Classe que gestiona què passa després d'un login correcte
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

   

    

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {

        // Llista per guardar els rols de l'usuari
        List<String> roles = new ArrayList<>();

        // Recorrem totes les autoritats (rols)
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        // Comprovem els rols i redirigim

        // Si és ADMIN → prioritat màxima
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");

        // Si és CLIENT
        } else if (roles.contains("ROLE_CLIENT")) {
            response.sendRedirect("/client");

        // Altres casos
        } else {
            response.sendRedirect("/landing");
        }
    }
}