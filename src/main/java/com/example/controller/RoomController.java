package com.example.controller;

import com.example.model.Room;
import com.example.model.Timeslot;
import com.example.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @GetMapping
    public Iterable<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable(value = "id") Long roomId) {
      Room room = roomRepository.findById(roomId);
        return ResponseEntity.ok().body(room);
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<?> bookRoom(@PathVariable(value = "id") Long roomId, @RequestBody Timeslot timeSlot) {
        Room room = roomRepository.findById(roomId);
        room.book(timeSlot);
        roomRepository.save(room);
        return ResponseEntity.ok().build();
    }
}
