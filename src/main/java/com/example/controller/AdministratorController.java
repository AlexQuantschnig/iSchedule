/**
 * AdministratorController.java
 * Purpose: Controller of the administrator.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.controller;
import com.example.model.Course;
import com.example.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdministratorController {
    private final CourseRepository courseRepository;

    /**
     * Constructor for the AdministratorController class.
     * @param courseRepository The CourseRepository object used to access course data.
     */
    public AdministratorController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Method to get all courses from the database and display them on the administrator page.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/admin")
    public String getAllCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "admin";
    }
}
