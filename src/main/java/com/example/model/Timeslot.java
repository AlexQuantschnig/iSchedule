/**
 * Timeslot.java
 * Purpose: Entity class for Timeslot
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Timeslot() {
    }

    public Timeslot(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Timeslot{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", course=" + course +
                ", room=" + room +
                '}';
    }
}