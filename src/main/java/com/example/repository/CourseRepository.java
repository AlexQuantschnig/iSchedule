/**
 * CourseRepository.java
 * Repository for the Course class
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);
}
