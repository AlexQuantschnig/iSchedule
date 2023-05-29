/**
 * AssistantPreference
 * This class represents the assistant preferences.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AssistantPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime datetime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id")
    private Assistant assistant;

    public AssistantPreference() {
    }

    public AssistantPreference(Long id, LocalDateTime datetime, Room room, Assistant assistant) {
        this.id = id;
        this.datetime = datetime;
        this.room = room;
        this.assistant = assistant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}