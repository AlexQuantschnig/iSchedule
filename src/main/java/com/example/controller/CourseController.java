package com.example.controller;

import com.example.model.Course;
import com.example.model.Room;
import com.example.model.Student;
import com.example.model.Timeslot;
import com.example.repository.CourseRepository;
import com.example.repository.RoomRepository;
import com.example.repository.StudentRepository;
import com.example.repository.TimeslotRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class CourseController {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final TimeslotRepository timeslotRepository;

    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository, RoomRepository roomRepository, TimeslotRepository timeslotRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.timeslotRepository = timeslotRepository;
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
        model.addAttribute("rooms", rooms);
        return "addCourse";
    }

    @PostMapping("/addCourse")
    public String addCourse(Model model, @RequestParam("name") String courseName, @RequestParam("room") Long roomID, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        Course course = new Course();
        course.setName(courseName);
        Timeslot timeslot = new Timeslot();
        Room room = roomRepository.findById(roomID).orElseThrow();
        timeslot.setRoom(room);
        timeslot.setCourse(course);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        LocalDateTime startDateTime = LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), getTime(startTime).toLocalTime().getHour(), getTime(startTime).toLocalTime().getMinute());
        LocalDateTime endDateTime = LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), getTime(endTime).toLocalTime().getHour(), getTime(endTime).toLocalTime().getMinute());

        if (startTime.equals(endTime) || startDateTime.isAfter(endDateTime)) {
            model.addAttribute("error", "End time cannot be before start time!");
            return "addCourse";
        }
        timeslot.setStartDateTime(startDateTime);
        timeslot.setEndDateTime(endDateTime);
        if (courseRepository.findByName(courseName) != null) {
            model.addAttribute("error", "Course already exists!");
            return "addCourse";
        } else if (timeslotRepository.findByRoomAndStartDateTime(room, startDateTime) != null) {
            model.addAttribute("error", "Room is already booked!");
            return "addCourse";
        }
        courseRepository.saveAndFlush(course);
        timeslotRepository.saveAndFlush(timeslot);
        return "redirect:/admin";
    }

    private Time getTime(String time) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date startTime;
        try {
            startTime = timeFormat.parse(time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Time(startTime.getTime());
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