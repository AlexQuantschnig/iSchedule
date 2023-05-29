/**
 * AdministratorRepository.java
 * Repository for the Administrator class
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByEmailAndPassword(String email, String password);
}
