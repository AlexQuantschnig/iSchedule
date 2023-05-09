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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/courses")
    public String showAllCourses(Model model, HttpServletRequest request) {

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
}