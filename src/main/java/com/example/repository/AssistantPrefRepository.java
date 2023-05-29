/**
 * AssistantPrefRepository.java
 * Repository for the AssistantPreference class
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.repository;
import com.example.model.AssistantPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssistantPrefRepository extends JpaRepository<AssistantPreference,Long> {
    List<AssistantPreference> findByAssistant_Id(Long id);
}
