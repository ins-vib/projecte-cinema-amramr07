package com.daw.CinemaDaw.domain.user;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    // Clau primària autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // Nom d'usuari únic i obligatori
    @Column(nullable = false, unique = true)
    public String username;

    // Contrasenya (s'hauria d'emmagatzemar encriptada)
    @Column(nullable = false)
    public String password;

    // Rol de l'usuari (es guarda com String a la BD)
    @Enumerated(EnumType.STRING)
    public Role role;

    // Constructor buit (obligatori per JPA)
    public User() {
    }

    // Getters i setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}