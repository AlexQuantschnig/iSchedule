/**
 * StudentRepository.java
 * Repository for the Student class
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long>{
    Optional<Student> findByEmailAndPassword(String email, String password);

    Student findByEmail(String email);
}
