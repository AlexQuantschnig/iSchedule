/**
 * RoomRepository.java
 * Repository for the Room class
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String name);
}
