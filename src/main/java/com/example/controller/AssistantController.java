package com.example.controller;

import com.example.model.Assistant;
import com.example.repository.AssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public class AssistantController {
    @Autowired
    private AssistantRepository assistantRepository;

    @GetMapping
    public Iterable<Assistant> getAllAssistants() {
        return assistantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assistant> getAssistantById(@PathVariable Long id) {
        Optional<Assistant> assistant = assistantRepository.findById(id);
        return assistant.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Assistant createAssistant(@RequestBody Assistant assistant) {
        return assistantRepository.save(assistant);
    }
}
