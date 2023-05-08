package com.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "assistant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssistantPreference> preferences = new ArrayList<>();

    public Assistant() {
    }

    public Assistant(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AssistantPreference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<AssistantPreference> preferences) {
        this.preferences = preferences;
    }

    public void addPreference(AssistantPreference preference) {
        preferences.add(preference);
        preference.setAssistant(this);
    }

    public void removePreference(AssistantPreference preference) {
        preferences.remove(preference);
        preference.setAssistant(null);
    }
}
