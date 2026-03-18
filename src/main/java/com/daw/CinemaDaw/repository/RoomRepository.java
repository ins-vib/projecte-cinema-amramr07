package com.daw.CinemaDaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daw.CinemaDaw.domain.cinema.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
  @Query("SELECT COUNT(s) FROM Seat s WHERE s.room.id = :roomId")
    int countSeatsByRoomId(@Param("roomId") Long roomId);
    
}
