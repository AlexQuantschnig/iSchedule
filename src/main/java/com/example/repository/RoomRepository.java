package com.example.repository;

import com.example.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Room findById(long id);
}
