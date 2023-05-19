package com.example.repository;

import com.example.model.Room;
import com.example.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface TimeslotRepository extends JpaRepository<Timeslot, Integer> {
    Timeslot findByRoomAndStartDateTime(Room room, LocalDateTime startDateTime);
}
