package com.daw.CinemaDaw.DTO;

import java.util.List;

public class SeatsListDTO {

    List<Long> seats;

    public List<Long> getSeats(){
        return seats;
    }
    
    public void setSeats(List<Long> seats){
        this.seats=seats;
    }
}
