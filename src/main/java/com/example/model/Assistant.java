/**
 * Assistant
 * This class represents an assistant.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
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
    @Column(unique = true)
    private String email;
    private String password;
    @OneToMany(mappedBy = "assistant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssistantPreference> preferences = new ArrayList<>();

    public Assistant() {
    }

    public Assistant(Long id, String name, String email, String password, List<AssistantPreference> preferences) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.preferences = preferences;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
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

}
