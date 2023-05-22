package com.example.controller;
import com.example.model.Administrator;
import com.example.model.Course;
import com.example.model.Timeslot;
import com.example.repository.AdministratorRepository;
import com.example.repository.TimeslotRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.model.Student;
import com.example.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;
    private final AdministratorRepository administratorRepository;

    private final TimeslotRepository timeslotRepository;
    public StudentController(StudentRepository studentRepository, AdministratorRepository administratorRepository, TimeslotRepository timeslotRepository) {
        this.studentRepository = studentRepository;
        this.administratorRepository = administratorRepository;
        this.timeslotRepository = timeslotRepository;
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/logout")
        public String logout(HttpServletRequest request){
            request.getSession().invalidate();
            return "redirect:/login";
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
        Optional<Administrator> administrator = administratorRepository.findByEmailAndPassword(email, password);
        if (student.isPresent()){
            request.getSession().setAttribute("email", email);
            return "redirect:/enrollments";
        } else if (administrator.isPresent()){
            request.getSession().setAttribute("email", email);
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
    @GetMapping("/enrollments")
    public String showEnrollments(Model model, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        List<Map<String, Object>> enrollments = getEnrollments(student);
        model.addAttribute("enrollments", enrollments);
        return "enrollments";
    }
    public List<Map<String,Object>> getEnrollments(Student student){
        List<Map<String, Object>> enrollments = new ArrayList<>();
        List<Long> courseIds = new ArrayList<>();
        for (Course course : student.getEnrollments()) {
            courseIds.add(course.getId());
        }
        List<Timeslot> timeslots = timeslotRepository.findAll();
        for (Timeslot timeslot : timeslots) {
            if (courseIds.contains(timeslot.getCourse().getId())) {
                Map<String, Object> enrollment = new HashMap<>();
                enrollment.put("id", timeslot.getId().toString());
                enrollment.put("startDateTime", timeslot.getStartDateTime());
                enrollment.put("endDateTime", timeslot.getEndDateTime());
                enrollment.put("courseName", timeslot.getCourse().getName());
                enrollment.put("room",timeslot.getRoom().getName());
                enrollments.add(enrollment);
            }
        }
        return enrollments;
    }


}
