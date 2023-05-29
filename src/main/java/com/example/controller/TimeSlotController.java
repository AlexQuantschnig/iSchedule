/**
 * TimeSlotController.java
 * Purpose: Controller of the timeslot.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    /**
     * Method to add a timeslot to the database.
     * Checks if the timeslot is valid. If not, an error message is displayed.
     * @param model The model used to pass data to the view.
     * @param courseID The id of the course to be added.
     * @param roomID The id of the room to be added.
     * @param date The date of the timeslot to be added.
     * @param startTime The start time of the timeslot to be added.
     * @param endTime  The end time of the timeslot to be added.
     * @return The name of the view to be displayed.
     */
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

        // Build LocalDateTime objects from date and time
        LocalDateTime startDateTime = LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), getTime(startTime).toLocalTime().getHour(), getTime(startTime).toLocalTime().getMinute());
        LocalDateTime endDateTime = LocalDateTime.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), getTime(endTime).toLocalTime().getHour(), getTime(endTime).toLocalTime().getMinute());

        if (startTime.equals(endTime) || startDateTime.isAfter(endDateTime)) {
            model.addAttribute("error", "End time cannot be before start time!");
            return "addCourse";
        }
        timeslot.setStartDateTime(startDateTime);
        timeslot.setEndDateTime(endDateTime);

        // Check if room is already booked
        List<Timeslot> overlappingTimeslots = timeslotRepository.findOverlappingTimeslotsByRoom(room, startDateTime, endDateTime);
        if (!overlappingTimeslots.isEmpty()) {
            model.addAttribute("error", "Room is already booked!");
            return "addCourse";
        }
        // Check if course is already booked
        overlappingTimeslots = timeslotRepository.findOverlappingTimeslotsByCourse(course, startDateTime, endDateTime);
        if (!overlappingTimeslots.isEmpty()) {
            model.addAttribute("error", "Course is already booked!");
            return "addCourse";
        }

        timeslotRepository.saveAndFlush(timeslot);
        return "addCourse";
    }

    /**
     * Method to get a time object from a string.
     * @param time The string to be converted.
     * @return The time object.
     */
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

    /**
     * Method to get the configureTimeslot view.
     * @param courseId The id of the course to be configured.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/configureTimeslot")
    private String configureTimeslot(@RequestParam("courseId") Long courseId, Model model) {
        List<Timeslot> timeslots = timeslotRepository.findAllByCourse_Id(courseId);
        model.addAttribute("timeslots", timeslots);
        return "configureTimeslot";
    }

    /**
     * Method to delete a timeslot from the database.
     * @param timeslotId The id of the timeslot to be deleted.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/deleteTimeslot")
    private String deleteTimeslot(@RequestParam("timeslotId") Long timeslotId) {
        timeslotRepository.deleteById(timeslotId);
        return "redirect:/admin";
    }

    /**
     * Method to update a timeslot in the database.
     * @param redirectAttributes The redirectAttributes used to pass data to the view.
     * @param courseId The id of the course to be updated.
     * @param timeslotId The id of the timeslot to be updated.
     * @param startTime The start time of the timeslot to be updated.
     * @param endTime The end time of the timeslot to be updated.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/configureTimeslot")
    private String updateTimeslot(RedirectAttributes redirectAttributes, @RequestParam("courseId") Long courseId, @RequestParam("timeslotId") Long timeslotId, @RequestParam("startDate") LocalDateTime startTime, @RequestParam("endDate") LocalDateTime endTime, Model model) {
        List<Timeslot> timeslots = timeslotRepository.findAllByCourse_Id(courseId);
        model.addAttribute("timeslots", timeslots);
        redirectAttributes.addAttribute("courseId", courseId);
        Timeslot timeslot = timeslotRepository.findById(timeslotId).orElseThrow();
        if (startTime.equals(endTime) || startTime.isAfter(endTime)) {
            model.addAttribute("error", "End time cannot be before start time!");
            return "configureTimeslot";
        }
        LocalTime maxTime = LocalTime.of(23,0);
        LocalTime minTime = LocalTime.of(8,15);
        if (startTime.toLocalTime().isBefore(minTime) || endTime.toLocalTime().isBefore(minTime)) {
            model.addAttribute("error", "No course before 8:15");
            return "configureTimeslot";
        }
        if (startTime.toLocalTime().isAfter(maxTime) || endTime.toLocalTime().isAfter(maxTime)) {
            model.addAttribute("error", "No course after 23:00");
            return "configureTimeslot";
        }

        timeslot.setStartDateTime(startTime);
        timeslot.setEndDateTime(endTime);
        timeslotRepository.saveAndFlush(timeslot);
        return "redirect:/admin";
    }
}
