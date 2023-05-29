/**
 * StudentController.java
 * Purpose: Controller of the student.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.controller;
import com.example.model.*;
import com.example.repository.AdministratorRepository;
import com.example.repository.AssistantRepository;
import com.example.repository.TimeslotRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;
    private final AdministratorRepository administratorRepository;
    private final TimeslotRepository timeslotRepository;
    private final AssistantRepository assistantRepository;
    public StudentController(StudentRepository studentRepository, AdministratorRepository administratorRepository, TimeslotRepository timeslotRepository, AssistantRepository assistantRepository) {
        this.studentRepository = studentRepository;
        this.administratorRepository = administratorRepository;
        this.timeslotRepository = timeslotRepository;
        this.assistantRepository = assistantRepository;
    }

    /**
     * Method to get all students from the database and display them on the students page.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/students")
    public String getAllStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    /**
     * Method to create a student and add it to the database.
     * @param name The name of the student to be created.
     * @param email The email of the student to be created.
     * @param password The password of the student to be created.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/students")
    public String createStudent(@RequestParam("name") String name,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);
        if(students.stream().anyMatch(s -> s.getEmail().equals(email))){
            model.addAttribute("error", "Student already exists!");
            return "/students";
        }
        studentRepository.saveAndFlush(student);
        return "redirect:/students";
    }

    /**
     * Method to delete a student from the database.
     * @param id The id of the student to be deleted.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("studentID") Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }

    /**
     * Method to log out the user.
     * @param request The request used to get the session.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/logout")
        public String logout(HttpServletRequest request){
            request.getSession().invalidate();
            return "redirect:/login";
        }

    /**
     * Method to show the login page.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Method to log in the user.
     * Checks if the user is a student, administrator or assistant and redirects to the corresponding page.
     * @param email The email of the user to be logged in.
     * @param password The password of the user to be logged in.
     * @param model The model used to pass data to the view.
     * @param request The request used to get the session.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model, HttpServletRequest request) {

        Optional<Student> student = studentRepository.findByEmailAndPassword(email, password);
        Optional<Administrator> administrator = administratorRepository.findByEmailAndPassword(email, password);
        Optional<Assistant> assistant = assistantRepository.findByEmailAndPassword(email, password);
        if (student.isPresent()){
            request.getSession().setAttribute("email", email);
            return "redirect:/enrollments";
        } else if (administrator.isPresent()){
            request.getSession().setAttribute("email", email);
            return "redirect:/admin";
        } else if (assistant.isPresent()){
            request.getSession().setAttribute("email", email);
            return "redirect:/assistant";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    /**
     * Method to show the enrollments page.
     * @param model The model used to pass data to the view.
     * @param request The request used to get the session.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/enrollments")
    public String showEnrollments(Model model, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        List<Map<String, Object>> enrollments = getEnrollments(student);
        model.addAttribute("enrollments", enrollments);
        return "enrollments";
    }

    /**
     * Method to get the enrollments of a student.
     * @param student The student whose enrollments are to be returned.
     * @return A list of maps containing the id, startDateTime, endDateTime, courseName and room of the enrollments.
     */
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
        enrollments.sort((enrollment1, enrollment2) -> {
            LocalDateTime startDateTime1 = (LocalDateTime) enrollment1.get("startDateTime");
            LocalDateTime startDateTime2 = (LocalDateTime) enrollment2.get("startDateTime");
            return startDateTime1.compareTo(startDateTime2);
        });
        return enrollments;
    }


}
