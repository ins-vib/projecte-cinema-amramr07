package com.daw.CinemaDaw.domain.order;

import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.movie.Screening;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tickets",
    uniqueConstraints = @UniqueConstraint(columnNames={"screening_id", "seat_id"}))

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable= false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable=false)
    private Screening screening;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable=false)
    private Seat seat;

    public Ticket() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Screening getScreening() { return screening; }
    public void setScreening(Screening screening) { this.screening = screening; }
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
}