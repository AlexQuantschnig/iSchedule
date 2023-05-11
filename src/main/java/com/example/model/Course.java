package com.example.model;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Timeslot> timeslots = new HashSet<>();

    public Course() {
    }

    public Course(Long id, String name, Set<Timeslot> timeslots) {
        this.id = id;
        this.name = name;
        this.timeslots = timeslots;
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

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Set<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

}
