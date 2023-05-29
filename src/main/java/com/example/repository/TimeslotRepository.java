/**
 * TimeslotRepository.java
 * Purpose: interface for the TimeslotRepository
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.Course;
import com.example.model.Room;
import com.example.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    List<Timeslot> findAllByCourse_Id(Long courseId);
    @Query("SELECT t FROM Timeslot t WHERE t.room = :room AND ((t.startDateTime <= :endDateTime AND t.endDateTime >= :startDateTime) OR (t.startDateTime >= :startDateTime AND t.startDateTime < :endDateTime) OR (t.endDateTime > :startDateTime AND t.endDateTime <= :endDateTime))")
    List<Timeslot> findOverlappingTimeslotsByRoom(@Param("room") Room room, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
    @Query("SELECT t FROM Timeslot t WHERE t.course = :course AND ((t.startDateTime <= :endDateTime AND t.endDateTime >= :startDateTime) OR (t.startDateTime >= :startDateTime AND t.startDateTime < :endDateTime) OR (t.endDateTime > :startDateTime AND t.endDateTime <= :endDateTime))")
    List<Timeslot> findOverlappingTimeslotsByCourse(@Param("course") Course course, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

}
