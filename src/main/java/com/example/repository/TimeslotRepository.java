package com.example.repository;

import com.example.model.Timeslot;
import org.springframework.data.repository.CrudRepository;

public interface TimeslotRepository extends CrudRepository<Timeslot, Integer> {
    Timeslot findById(Long timeSlotId);
}
