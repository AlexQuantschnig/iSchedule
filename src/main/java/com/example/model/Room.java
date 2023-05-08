package com.example.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Timeslot> bookedTimeslots = new HashSet<>();

    public Room() {
    }

    public Room(Long id, String name, Set<Timeslot> bookedTimeslots) {
        this.id = id;
        this.name = name;
        this.bookedTimeslots = bookedTimeslots;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Timeslot> getBookedTimeslots() {
        return bookedTimeslots;
    }

    public void setBookedTimeslots(Set<Timeslot> bookedTimeslots) {
        this.bookedTimeslots = bookedTimeslots;
    }

    public void book(Timeslot timeSlot) {
        bookedTimeslots.add(timeSlot);
    }
}