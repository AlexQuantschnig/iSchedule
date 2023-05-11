package com.example.repository;

import com.example.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByEmailAndPassword(String email, String password);
    Administrator findByEmail(String email);
}
