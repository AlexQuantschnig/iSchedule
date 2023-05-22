package com.example.controller;
import com.example.model.Course;
import com.example.model.Room;
import com.example.model.Student;
import com.example.model.Timeslot;
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

    @GetMapping("/admin")
    public String getAllCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "admin";
    }

    @GetMapping("/addCourse")
    public String showAddCoursePage(Model model) {
        List<Room> rooms = roomRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("rooms", rooms);
        return "addCourse";
    }

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

    @PostMapping("/enroll")
    public String addEnrollment(@RequestParam("courseId") Long courseId, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Student student = studentRepository.findByEmail(email);
        Course course = courseRepository.findById(courseId).orElseThrow();
        student.getEnrollments().add(course);
        studentRepository.save(student);
        return "redirect:/enrollments";
    }

    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam("courseId") Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();

        // Remove the course from all students' enrollments
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            student.getEnrollments().remove(course);
            studentRepository.save(student);
        }

        // Clear the enrollments of the course itself
        course.getStudents().clear();
        courseRepository.save(course);

        // Delete the course
        courseRepository.delete(course);

        return "redirect:/admin";
    }

}