package com.daw.CinemaDaw.DTO;

import java.time.LocalDateTime;

public class CheckoutLine {
    private final Long screeningId, seatId;
    private final String movieTitle, cinemaName, roomName;
    private final LocalDateTime screeningDateTime;
    private final int seatRow, seatNumber;
    private final double price;

    public CheckoutLine(Long screeningId, Long seatId, String movieTitle, String cinemaName, String roomName,
                        LocalDateTime screeningDateTime, int seatRow, int seatNumber, double price) {
        this.screeningId = screeningId;
        this.seatId = seatId;
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.roomName = roomName;
        this.screeningDateTime = screeningDateTime;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public Long getScreeningId() { return screeningId; }
    public Long getSeatId() { return seatId; }
    public String getMovieTitle() { return movieTitle; }
    public String getCinemaName() { return cinemaName; }
    public String getRoomName() { return roomName; }
    public LocalDateTime getScreeningDateTime() { return screeningDateTime; }
    public int getSeatRow() { return seatRow; }
    public int getSeatNumber() { return seatNumber; }
    public double getPrice() { return price; }
}
