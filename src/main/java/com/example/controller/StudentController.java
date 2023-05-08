package com.example.controller;

import com.example.model.Course;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.model.Student;
import com.example.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model, HttpServletRequest request) {

        // Find the student with the given email and password
        Optional<Student> student = studentRepository.findByEmailAndPassword(email, password);

        if (student.isPresent()) {
            // If the student is found, add them to the model and redirect to the homepage
            request.getSession().setAttribute("email", email);
            System.out.println(model);
            return "redirect:/enrollments";
        } else {
            // If the student is not found, add an error message to the model and return to the login page
            model.addAttribute("error", "Invalid email or password");
            System.out.println("Not found");
            return "login";
        }
    }
    @GetMapping("/enrollments")
    public String showEnrollments(Model model,HttpServletRequest request) {
        String email = (String)request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        model.addAttribute("enrollments", student.getEnrollments());
        return "enrollments";
    }


}
