/**
 * AssistantController.java
 * Purpose: Controller of the assistant.
 * Author: Alex Quantschnig
 * Date: 29.05.2023
 */
package com.example.controller;
import com.example.model.Assistant;
import com.example.model.AssistantPreference;
import com.example.model.Room;
import com.example.repository.AdministratorRepository;
import com.example.repository.AssistantPrefRepository;
import com.example.repository.AssistantRepository;
import com.example.repository.RoomRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class AssistantController {

    private final AssistantRepository assistantRepository;
    private final AssistantPrefRepository assistantPrefRepository;
    private final RoomRepository roomRepository;

    public AssistantController(AssistantRepository assistantRepository, AssistantPrefRepository assistantPrefRepository, RoomRepository roomRepository, AdministratorRepository administratorRepository) {
        this.assistantRepository = assistantRepository;
        this.assistantPrefRepository = assistantPrefRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Method to get all assistants from the database and display them on the assistant page.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/assistant")
    public String showPreferences(Model model, HttpServletRequest request) {
        String email = (String) request.getSession().getAttribute("email");
        Assistant assistant = assistantRepository.findByEmail(email);
        model.addAttribute("assID", assistant.getId());
        List<AssistantPreference> preferences = assistant.getPreferences();
        model.addAttribute("preferences", preferences);
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "assistant";
    }

    /**
     * Method to add an assistant to the database.
     * Is the email already in use, the assistant will not be added.
     * @param name The name of the assistant.
     * @param email The email of the assistant.
     * @param password The password of the assistant.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/addAssistant")
    public String addAssistant(@RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model){
        List<Assistant> assistants = assistantRepository.findAll();
        model.addAttribute("assistants", assistants);
        if(assistants.stream().anyMatch(s -> s.getEmail().equals(email))){
            model.addAttribute("error", "Assistant already exists!");
            return "allAssistants";
        }
        Assistant assistant = new Assistant();
        assistant.setName(name);
        assistant.setEmail(email);
        assistant.setPassword(password);
        assistantRepository.saveAndFlush(assistant);
        return "redirect:/allAssistants";

    }

    /**
     * Method to delete an assistant from the database.
     * @param id The id of the assistant to be deleted.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/deleteAssistant")
    public String deleteAssistant(@RequestParam("id") Long id){
        assistantRepository.deleteById(id);
        return "redirect:/allAssistants";
    }

    /**
     * Method to add a preference to an assistant.
     * If the time of the preference is not between 8:15 and 23:00, the preference will not be added.
     * @param request The request object used to get the email of the assistant.
     * @param model The model used to pass data to the view.
     * @param assistantID The id of the assistant.
     * @param dateTime The date and time of the preference.
     * @param roomID The id of the room.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/addPreferenceToAssistant")
    public String addPreferenceToAssistant(HttpServletRequest request,Model model,@RequestParam("assID") Long assistantID,@RequestParam("dateTime")LocalDateTime dateTime, @RequestParam("room") Long roomID){
        List<AssistantPreference> preferences = assistantPrefRepository.findByAssistant_Id(assistantID);
        model.addAttribute("preferences", preferences);
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms",rooms);
        model.addAttribute("assID", assistantID);
        LocalTime maxTime = LocalTime.of(23,0);
        LocalTime minTime = LocalTime.of(8,15);
        String email = (String) request.getSession().getAttribute("email");
        Assistant assistant = assistantRepository.findByEmail(email);

        if (dateTime.toLocalTime().isAfter(maxTime)){
            model.addAttribute("error", "No course after 23:00");
            if (assistant == null){
                return "preferences";
            }
           return "assistant";
        }
        if (dateTime.toLocalTime().isBefore(minTime)){
            model.addAttribute("error", "No course before 8:15");
            if (assistant == null){
                return "preferences";
            }
            return "assistant";
        }
        addPref(assistantID, dateTime, roomID);

        if(assistant == null){
            return "redirect:/allAssistants";
        }
        return "redirect:/assistant";
    }

    /**
     * Helper method to add a preference to an assistant.
     * @param assistantID The id of the assistant.
     * @param dateTime The date and time of the preference.
     * @param roomID The id of the room.
     */
    private void addPref(@RequestParam("assID") Long assistantID, @RequestParam("dateTime") LocalDateTime dateTime, @RequestParam("room") Long roomID) {
        AssistantPreference preference = new AssistantPreference();
        Room room = roomRepository.findById(roomID).orElseThrow();
        Assistant assistant = assistantRepository.findById(assistantID).orElseThrow();
        preference.setRoom(room);
        preference.setAssistant(assistant);
        preference.setDatetime(dateTime);
        assistantPrefRepository.saveAndFlush(preference);
    }

    /**
     * Method to get all assistants from the database and display them on the allAssistants page.
     * @param model The model used to pass data to the view.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/allAssistants")
    public String getAllAssistants(Model model) {
        List<Assistant> assistants = assistantRepository.findAll();
        model.addAttribute("assistants", assistants);
        return "allAssistants";
    }

    /**
     * Method to get all preferences of an assistant from the database and display them on the preferences page.
     *
     * @param model The model used to pass data to the view.
     * @param id The id of the assistant.
     * @return The name of the view to be displayed.
     */
    @GetMapping("/configurePreferences")
    public String getAllPreferences(Model model, @RequestParam("assID") Long id) {
        List<AssistantPreference> preferences = assistantPrefRepository.findByAssistant_Id(id);
        model.addAttribute("preferences", preferences);
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms",rooms);
        model.addAttribute("assID", id);
        return "preferences";
    }

    /**
     * Method to delete a preference from the database.
     * @param request The request object used to get the email of the assistant.
     * @param id The id of the preference to be deleted.
     * @return The name of the view to be displayed.
     */
    @PostMapping("/deletePreference")
    public String deletePreference(HttpServletRequest request,@RequestParam("id") Long id) {
        assistantPrefRepository.deleteById(id);
        String email = (String) request.getSession().getAttribute("email");
        Assistant assistant = assistantRepository.findByEmail(email);
        System.out.println(assistant);
        if (assistant == null) {
            return "redirect:/allAssistants";
        }
        return "redirect:/assistant";
    }

}
