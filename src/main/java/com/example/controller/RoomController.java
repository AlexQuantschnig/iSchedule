/**
 * RoomController.java
 * Purpose: Controller of the rooms.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.controller;
import com.example.model.Course;
import com.example.model.Room;
import com.example.repository.CourseRepository;
import com.example.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class RoomController {

    private final RoomRepository roomRepository;
    private final CourseRepository courseRepository;

    public RoomController(RoomRepository roomRepository, CourseRepository courseRepository) {
        this.roomRepository = roomRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Method to add a room to the database.
     * @param model The model used to pass data to the view.
     * @param name The name of the room to be added.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/addRoom")
    public String addRoom(Model model, @RequestParam String name){
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        Room room = new Room();
        room.setName(name);
        if (roomRepository.findByName(name) != null){
            model.addAttribute("error", "Room already exists");
            return "addCourse";
        }
        roomRepository.saveAndFlush(room);
        return "redirect:/addCourse";
    }
}
