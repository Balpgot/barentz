package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.entities.EventDAO;
import com.barentzconnection.demo.entities.UserDAO;
import com.barentzconnection.demo.repositories.IEventRepository;
import com.barentzconnection.demo.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping(value = "/admin/users")
    public String adminUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "adminUsers";
    }

    @GetMapping(value = "/admin/events")
    public String adminEvents(Model model){
        model.addAttribute("events", eventRepository.findAll());
        return "adminEvents";
    }

    @GetMapping(value = "/admin/events/{id}")
    public String adminEventPage(@PathVariable Long id, Model model){
        model.addAttribute(eventRepository.findById(id));
        return "eventAdminPage";
    }

    @PostMapping(value = "/admin/events/{id}")
    public String editEvent(@PathVariable Long id, EventDAO editedEvent){
        EventDAO event = eventRepository.findById(id).orElse(null);
        if(event!=null){
            event.edit(editedEvent);
            return "redirect:/admin/users/" + id;
        }
        else {
            return "redirect:/admin/users";
        }
    }

    @GetMapping(value = "/users/{id}")
    public String adminUserPage(@PathVariable Long id, Model model){
        model.addAttribute("user", userRepository.findById(id));
        return "userAdminPage";
    }

    @PostMapping(value = "/admin/users/{id}")
    public String editUser(@PathVariable Long id, UserDAO editedUser){
        UserDAO user = userRepository.findById(id).orElse(null);
        if(user!=null){
            user.edit(editedUser);
            return "redirect:/admin/users/" + id;
        }
        else {
            return "redirect:/admin/users";
        }
    }
}
