package com.example.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Student{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   private String name;

    @Column(unique = true)
    private String email;
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "enrollments",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> enrollments = new HashSet<>();

    public Student() {

    }


    public Student(Long id, String name, String password, String email,Set<Course> enrollments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.enrollments = enrollments;
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

    public Set<Course> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Course> enrollments) {
        this.enrollments = enrollments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
