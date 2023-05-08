package com.example.controller;

import com.example.model.Timeslot;
import com.example.repository.TimeslotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TImeSlotController {

    private final TimeslotRepository timeSlotRepository;

    public TImeSlotController(TimeslotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }


    @PostMapping
    public Timeslot createTimeSlot(@RequestBody Timeslot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    @GetMapping
    public Iterable<Timeslot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Timeslot> getTimeSlotById(@PathVariable(value = "id") Long timeSlotId) {
        Timeslot timeSlot = timeSlotRepository.findById(timeSlotId);

        return ResponseEntity.ok().body(timeSlot);
    }


}
