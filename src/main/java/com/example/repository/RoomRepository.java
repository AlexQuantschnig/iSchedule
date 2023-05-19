package com.example.repository;

import com.example.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findById(long id);
}
