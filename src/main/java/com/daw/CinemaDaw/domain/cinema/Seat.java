        package com.daw.CinemaDaw.domain.cinema;

        import jakarta.persistence.Column;
        import jakarta.persistence.Entity;
        import jakarta.persistence.EnumType;
        import jakarta.persistence.Enumerated;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.ManyToOne;

        @Entity
        public class Seat {
            @Id
            @GeneratedValue(strategy=GenerationType.IDENTITY)
            private Long id;

            @Column
        private int seatRow;      
        @ManyToOne        
            private Room room;
        @Column
        private int seatNumber;         
        @Column 
        private int posX;               
        @Column 
        private int posY;               
        @Column 
        @Enumerated(EnumType.STRING)
        private SeatType type = SeatType.STANDARD;      
        
        @Column 
        private boolean isActive;

        



        public int getSeatRow() {
            return seatRow;
        }
        public void setSeatRow(int seatRow) {
            this.seatRow = seatRow;
        }
        public Room getRoom() {
            return room;
        }
        public void setRoom(Room room) {
            this.room = room;
        }
    
        
        
        public Seat(int row, int seatNumber, int posX, int posY, SeatType type,
                boolean isActive) {
            this.seatRow = row;
            this.seatNumber = seatNumber;
            this.posX = posX;
            this.posY = posY;
            this.type = type;
            
            this.isActive = isActive;
        }
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public int getRow() {
            return seatRow;
        }
        public void setRow(int fila) {
            this.seatRow = fila;
        }
        public int getSeatNumber() {
            return seatNumber;
        }
        public void setSeatNumber(int seatNumber) {
            this.seatNumber = seatNumber;
        }
        public int getPosX() {
            return posX;
        }
        public void setPosX(int posX) {
            this.posX = posX;
        }
        public int getPosY() {
            return posY;
        }
        public void setPosY(int posY) {
            this.posY = posY;
        }
        public SeatType getType() {
            return type;
        }
        public void setType(SeatType type) {
            this.type = type;
        }
        public boolean isActive() {
            return isActive;
        }
        

        public Seat() {
        }
        public void setActive(boolean isActive) {
            this.isActive = isActive;
        }
        @Override
        public String toString() {
            return "Seat [id=" + id + ", seatRow=" + seatRow + "]";
        }
    

        
    }
