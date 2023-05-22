package com.example.repository;

import com.example.model.Course;
import com.example.model.Room;
import com.example.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    Timeslot findByRoomAndStartDateTime(Room room, LocalDateTime startDateTime);

    Timeslot findByCourse_id(Long courseId);

    Timeslot findByCourseAndStartDateTime(Course course, LocalDateTime startDateTime);

    List<Timeslot> findAllByCourse_Id(Long courseId);
}
