package com.example.controller;
import com.example.model.Course;
import com.example.model.Room;
import com.example.model.Timeslot;
import com.example.repository.CourseRepository;
import com.example.repository.RoomRepository;
import com.example.repository.TimeslotRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class TimeSlotController {
    private final RoomRepository roomRepository;
    private final TimeslotRepository timeslotRepository;
    private final CourseRepository courseRepository;


    public TimeSlotController(RoomRepository roomRepository, TimeslotRepository timeslotRepository, CourseRepository courseRepository) {
        this.roomRepository = roomRepository;
        this.timeslotRepository = timeslotRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/addTimeslot")
    public String addTimeslot(Model model, @RequestParam("course") Long courseID, @RequestParam("room") Long roomID, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime ){
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        Room room = roomRepository.findById(roomID).orElseThrow();
        Course course = courseRepository.findById(courseID).orElseThrow();
        Timeslot timeslot = new Timeslot();
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
        if (timeslotRepository.findByRoomAndStartDateTime(room, startDateTime) != null) {
            model.addAttribute("error", "Room is already booked!");
            return "addCourse";
        }
        if (timeslotRepository.findByCourseAndStartDateTime(course, startDateTime) != null) {
            model.addAttribute("error", "Course is already booked!");
            return "addCourse";
        }
        timeslotRepository.saveAndFlush(timeslot);
        return "addCourse";
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
    @GetMapping("/configureTimeslot")
    private String configureTimeslot(@RequestParam("courseId") Long courseId, Model model) {
        System.out.println(courseId);
        List<Timeslot> timeslots = timeslotRepository.findAllByCourse_Id(courseId);
        System.out.println(timeslots);
        model.addAttribute("timeslots", timeslots);
        return "configureTimeslot";
    }
    @PostMapping("/deleteTimeslot")
    private String deleteTimeslot(@RequestParam("timeslotId") Long timeslotId) {
        timeslotRepository.deleteById(timeslotId);
        return "redirect:/admin";
    }
}
