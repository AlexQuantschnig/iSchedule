package com.example.repository;

import com.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long>{
    Optional<Student> findByEmailAndPassword(String email, String password);

    Student findByEmail(String email);
}
