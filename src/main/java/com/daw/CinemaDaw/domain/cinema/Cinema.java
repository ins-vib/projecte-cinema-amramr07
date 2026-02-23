package com.daw.CinemaDaw.domain.cinema;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class Cinema {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String adress;
    @Column
    private String city;
    @Column
    private String postalCode;

    @OneToMany(mappedBy="cinema")
    List<Room> rooms = new ArrayList<>();


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
        StringBuilder sb = new StringBuilder();
        sb.append("cinema{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", adress=").append(adress);
        sb.append(", city=").append(city);
        sb.append(", postalCode=").append(postalCode);
        sb.append('}');
        return sb.toString();
    }

    
    
}
