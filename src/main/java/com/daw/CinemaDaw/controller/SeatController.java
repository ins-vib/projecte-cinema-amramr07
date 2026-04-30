package com.daw.CinemaDaw.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.CinemaDaw.domain.cinema.Room;
import com.daw.CinemaDaw.domain.cinema.Seat;
import com.daw.CinemaDaw.domain.cinema.SeatType;
import com.daw.CinemaDaw.repository.RoomRepository;
import com.daw.CinemaDaw.repository.SeatRepository;

import jakarta.validation.Valid;

@Controller
public class SeatController {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public SeatController(SeatRepository seatRepository, RoomRepository roomRepository) {
        this.seatRepository = seatRepository;
        this.roomRepository = roomRepository;
    }

    // Ver detalles de un seat
    @GetMapping("/seat/{id}")
    public String viewSeat(@PathVariable Long id, Model model) {
        Optional<Seat> optional = seatRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("seat", optional.get());
            return "seats/seat-details";
        }
        return "redirect:/cinemes";
    }

    // Mostrar formulario para crear seat
    @GetMapping("/seat/create")
    public String showCreateForm(@RequestParam Long roomId, Model model) {
        Optional<Room> optional = roomRepository.findById(roomId);
        if (optional.isPresent()) {
            model.addAttribute("seat", new Seat());
            model.addAttribute("room", optional.get());
            return "seats/create-seat";
        }
        return "redirect:/cinemes";
    }

    // Guardar nuevo seat y actualizar capacidad
    @PostMapping("/seat/create")
    public String createSeat(@Valid @ModelAttribute Seat seat, BindingResult result,
                             @RequestParam Long roomId, Model model) {
        if (result.hasErrors()) {
            roomRepository.findById(roomId).ifPresent(r -> model.addAttribute("room", r));
            return "seats/create-seat";
        }

        Optional<Room> optional = roomRepository.findById(roomId);
        if (optional.isPresent()) {
            Room room = optional.get();
            seat.setRoom(room);
            seatRepository.save(seat);

            room.setCapacity(roomRepository.countSeatsByRoomId(roomId));
            roomRepository.save(room);

            return "redirect:/room/" + roomId + "/seats";
        }

        return "redirect:/cinemes";
    }

    // Mostrar formulario para editar seat
    @GetMapping("/seat/edit/{id}")
    public String editSeatForm(@PathVariable Long id, Model model) {
        Optional<Seat> optional = seatRepository.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("seat", optional.get());
            model.addAttribute("types", SeatType.values());
            return "seats/edit-seat";
        }
        return "redirect:/cinemes";
    }

    // Guardar cambios de seat 
    @PostMapping("/seat/edit")
    public String updateSeat(@Valid @ModelAttribute Seat seat, BindingResult result) {
        if (result.hasErrors()) {
            return "seats/edit-seat";
        }

        Optional<Seat> optional = seatRepository.findById(seat.getId());
        if (optional.isPresent()) {
            Seat existing = optional.get();
            existing.setSeatRow(seat.getSeatRow());
            existing.setSeatNumber(seat.getSeatNumber());
            existing.setPosX(seat.getPosX());
            existing.setPosY(seat.getPosY());
            existing.setType(seat.getType());
            existing.setActive(seat.isActive());
            seatRepository.save(existing);
            return "redirect:/room/" + existing.getRoom().getId() + "/seats";
        }

        return "redirect:/cinemes";
    }

    // Borrar seat y actualizar capacidad
   @PostMapping("/seat/delete/{id}")
public String deleteSeat(@PathVariable Long id) {
    Optional<Seat> optional = seatRepository.findById(id);
    if (optional.isPresent()) {
        Seat seat = optional.get();
        Room room = seat.getRoom();
        Long roomId = room.getId();
        seatRepository.delete(seat);

        room.setCapacity(roomRepository.countSeatsByRoomId(roomId));
        roomRepository.save(room);

        return "redirect:/room/" + roomId + "/seats";
    }
    return "redirect:/cinemes";
}
}
