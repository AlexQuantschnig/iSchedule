/**
 * AssistantRepository.java
 * Repository for the Assistant class
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {
    Optional<Assistant> findByEmailAndPassword(String email, String password);
    Assistant findByEmail(String email);
}
