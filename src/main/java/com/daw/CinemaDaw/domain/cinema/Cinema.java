package com.daw.CinemaDaw.domain.cinema;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
public class Cinema {


    

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="The cinema name is obligatory")
    @Size(min=2, max=100, message= "The name has to have beetwen 2 and 100 characters")
    @Column
    private String name;
    
    
     @NotBlank(message="The address  is obligatory")
    @Size(min=2, max=100, message= "The adress has to have beetwen 2 and 100 characters")
    @Column
    private String adress;


     @NotBlank(message="The city  is obligatory")
    @Size(min=2, max=100, message= "The city has to have beetwen 2 and 100 characters")
    @Column
    private String city;


     @NotBlank(message="The postal code is obligatory")
    @Pattern (regexp="\\d{5}", message = "The postal code has to have 5 characters")
    @Column
    private String postalCode;

    @OneToMany(mappedBy="cinema", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Room> rooms = new ArrayList<>();


    public String getName() {
        return name;
    }
    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public Cinema() {
    }
    public Cinema(String name, String adress, String city, String postalCode) {
        this.name = name;
        this.adress = adress;
        this.city = city;
        this.postalCode = postalCode;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "Cinema [id=" + id + "]";
    }

  

    
    
}
