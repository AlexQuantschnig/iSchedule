package com.example.controller;

import com.example.model.Course;
import com.example.model.Student;
import com.example.model.Timeslot;
import com.example.repository.CourseRepository;
import com.example.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;


@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/admin")
    public String getAllCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "admin";
    }

    @GetMapping("/courses")
    public String coursesNotEnrolled(Model model, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        List<Course> allCourses = courseRepository.findAll();
        List<Course> coursesNotEnrolled = allCourses.stream()
                .filter(course -> !student.getEnrollments().stream()
                        .map(Timeslot::getCourse)
                        .collect(Collectors.toSet())
                        .contains(course)).toList();
        model.addAttribute("courses", coursesNotEnrolled);
        return "courses";
    }

    @PostMapping("/enroll")
    public String addEnrollment(@RequestParam("courseId") Long courseId, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        Course course = courseRepository.findById(courseId).orElseThrow();
        Set<Timeslot> timeslots = course.getTimeslots();
        student.getEnrollments().addAll(timeslots);
        studentRepository.save(student);
        return "redirect:/enrollments";
    }
}