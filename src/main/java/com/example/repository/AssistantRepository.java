package com.example.repository;

import com.example.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {
}
