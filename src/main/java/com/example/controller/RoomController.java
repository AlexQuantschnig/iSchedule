package com.example.controller;
import com.example.model.Room;
import com.example.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostMapping("/addRoom")
    public String addRoom(@RequestParam String name){
        Room room = new Room();
        room.setName(name);
        roomRepository.saveAndFlush(room);
        return "redirect:/addCourse";
    }
}
