/**
 * CourseController.java
 * Purpose: Controller of the courses.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.controller;
import com.example.model.Course;
import com.example.model.Room;
import com.example.model.Student;
import com.example.repository.CourseRepository;
import com.example.repository.RoomRepository;
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
    private final RoomRepository roomRepository;


    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository, RoomRepository roomRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
    }


    /**
     * Method to get all courses from the database and display them on the administrator page.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/addCourse")
    public String showAddCoursePage(Model model) {
        List<Room> rooms = roomRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("rooms", rooms);
        return "addCourse";
    }

    /**
     * Method to add a course to the database.
     * If the course already exists, an error message is displayed.
     * @param model The model used to pass data to the view.
     * @param courseName The name of the course to be added.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/addCourse")
    public String addCourse(Model model, @RequestParam("name") String courseName) {
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        Course course = new Course();
        course.setName(courseName);
        if(courseRepository.findByName(courseName) != null){
            model.addAttribute("error", "Course already exists!");
            return "addCourse";
        }
        courseRepository.saveAndFlush(course);
        return "redirect:/admin";
    }

    /**
     * Method to get all the courses a student is not enrolled in.
     * @param model The model used to pass data to the view.
     * @param request The request object used to get the email of the student.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/courses")
    public String coursesNotEnrolled(Model model, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        List<Course> allCourses = courseRepository.findAll();
        List<Course> coursesNotEnrolled = allCourses.stream()
                .filter(course -> !student.getEnrollments().contains(course))
                .collect(Collectors.toList());
        model.addAttribute("courses", coursesNotEnrolled);
        return "courses";
    }

    /**
     * Method to add a course to the list of enrollments of a student.
     * @param courseId The id of the course to be added.
     * @param request The request object used to get the email of the student.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/enroll")
    public String addEnrollment(@RequestParam("courseId") Long courseId, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        Course course = courseRepository.findById(courseId).orElseThrow();
        student.getEnrollments().add(course);
        studentRepository.save(student);
        return "redirect:/enrollments";
    }

    /**
     * Method to delete a course from the list of enrollments of a student.
     * @param courseId The id of the course to be deleted.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("courseId") Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            student.getEnrollments().remove(course);
            studentRepository.save(student);
        }
        course.getStudents().clear();
        courseRepository.save(course);
        courseRepository.delete(course);

        return "redirect:/admin";
    }

}