package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.entities.EventDAO;
import com.barentzconnection.demo.entities.EventDTO;
import com.barentzconnection.demo.entities.UserDAO;
import com.barentzconnection.demo.repositories.IEventRepository;
import com.barentzconnection.demo.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    IEventRepository eventRepository;
    IUserRepository userRepository;

    @Autowired
    public AdminController(IEventRepository eventRepository, IUserRepository userRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/admin")
    public String adminPage(){
        return "adminPage";
    }

    @GetMapping(value = "/admin/events")
    public String adminEvents(Model model){
        model.addAttribute("events", eventRepository.findAll());
        return "adminEvents";
    }

    @GetMapping(value = "/admin/events/add")
    public String addEvent(Model model){
        model.addAttribute("event", new EventDTO());
        return "eventAddPage";
    }

    @PostMapping(value = "/admin/events/add",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editEvent(EventDTO eventDTO){
        EventDAO event = new EventDAO();
        event.edit(eventDTO);
        eventRepository.saveAndFlush(event);
        Optional<EventDAO> eventDAO = eventRepository
                .findEventDAOByName(eventDTO.getName());
        if(eventDAO.isPresent()){
            return "redirect:/admin/events/" +
                eventDAO.get().getId();
        }
        else{
            return "redirect:/admin/events/";
        }
    }

    @GetMapping(value = "/admin/events/{id}")
    public String eventPage(@PathVariable Long id, Model model){
        Optional<EventDAO> eventDAO = eventRepository.findById(id);
        if(eventDAO.isPresent()){
            model.addAttribute("event", eventDAO.get());
            return "eventPage";
        }
        else
            return "redirect:/admin/events";
    }

    @GetMapping(value = "/admin/events/{id}/edit")
    public String adminEventPage(@PathVariable Long id, Model model){
        Optional<EventDAO> event = eventRepository.findById(id);
        if(event.isPresent()) {
            EventDTO eventDTO = new EventDTO();
            eventDTO.edit(event.get());
            model.addAttribute("event", eventDTO);
            return "eventEditPage";
        }
        else{
            return "redirect:/admin/events";
        }
    }

    @PostMapping(value = "/upload/{id}")
    public String singleFileUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        System.out.println(System.getProperty("user.dir"));
        System.out.println("hello");
        String UPLOADED_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/img/";
        if(!file.isEmpty()) {
            try {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                Optional<EventDAO> event = eventRepository.findById(id);
                if(event.isPresent()){
                    event.get().setImgPath(file.getOriginalFilename());
                    eventRepository.saveAndFlush(event.get());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/admin/events";
    }

    @PostMapping(value = "/admin/events/{id}/edit",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editEventPage(@PathVariable Long id, EventDTO editedEvent){
        EventDAO event = eventRepository.findById(id).orElse(null);
        if(event!=null){
            event.edit(editedEvent);
            eventRepository.saveAndFlush(event);
            return "redirect:/admin/events/" + id;
        }
        else {
            return "redirect:/admin/events";
        }
    }

    @GetMapping(value = "/admin/users")
    public String adminUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "adminUsers";
    }

    @GetMapping(value = "/admin/users/{id}")
    public String adminUserPage(@PathVariable Long id, Model model){
        Optional<UserDAO> userDAO = userRepository.findById(id);
        if(userDAO.isPresent()){
            model.addAttribute("user", userDAO.get());
            return "userPage";
        }
        else {
            return "redirect:/admin/users";
        }
    }

    @GetMapping(value = "/admin/users/{id}/edit")
    public String editUserPage(@PathVariable Long id, Model model){
        Optional<UserDAO> userDAO = userRepository.findById(id);
        if(userDAO.isPresent()){
            model.addAttribute("user", userDAO.get());
            return "userEditPage";
        }
        else {
            return "redirect:/admin/users";
        }
    }

    @PostMapping(value = "/admin/users/{id}/edit",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editUser(@PathVariable Long id, UserDAO editedUser){
        UserDAO user = userRepository.findById(id).orElse(null);
        if(user!=null){
            user.edit(editedUser);
            userRepository.saveAndFlush(user);
            return "redirect:/admin/users/" + id;
        }
        else {
            return "redirect:/admin/users";
        }
    }
}
