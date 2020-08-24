package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.entities.EventDAO;
import com.barentzconnection.demo.entities.EventDTO;
import com.barentzconnection.demo.entities.LinkDAO;
import com.barentzconnection.demo.entities.UserDAO;
import com.barentzconnection.demo.repositories.IEventRepository;
import com.barentzconnection.demo.repositories.ILinksRepository;
import com.barentzconnection.demo.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AdminController {

    IEventRepository eventRepository;
    IUserRepository userRepository;
    private final String UPLOADED_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/img/events/";
    private final String IMG_PATH = "/img/events/";
    ILinksRepository linksRepository;

    @Autowired
    public AdminController(IEventRepository eventRepository, IUserRepository userRepository, ILinksRepository linksRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.linksRepository = linksRepository;
        preload();
    }

    private void preload(){
        if(linksRepository.findByName("OpeningCeremonyZoom").isEmpty()) {
            linksRepository.saveAndFlush(
                    new LinkDAO(
                            "OpeningCeremonyZoom",
                            "http://zoom.us"
                    )
            );
            linksRepository.saveAndFlush(
                    new LinkDAO(
                            "ConnectAndLearnChallenge",
                            "https://www.youtube.com/embed/dQw4w9WgXcQ"
                    )
            );
            linksRepository.saveAndFlush(
                    new LinkDAO(
                            "ConnectAndPlayMeeting",
                            "youtube.com"
                    )
            );
            linksRepository.saveAndFlush(
                    new LinkDAO(
                            "ConnectAndTravelNorway",
                            "youtube.com"
                    )
            );
            linksRepository.saveAndFlush(
                    new LinkDAO(
                            "ConnectAndTravelRussia",
                            "youtube.com"
                    )
            );
            linksRepository.saveAndFlush(
                    new LinkDAO(
                            "ClosingCeremonyZoom",
                            "zoom.us"
                    )
            );
        }
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

    @GetMapping(value = "/admin/links")
    public String getLinks(Model model){
        model.addAttribute("links", linksRepository.findAll());
        return "adminLinks";
    }

    @GetMapping(value = "/admin/links/{id}/edit")
    public String getLinkPage(@PathVariable Long id, Model model){
        model.addAttribute("link", linksRepository.findById(id).get());
        return "linkEditPage";
    }

    @PostMapping(value = "/admin/links/{id}/edit",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editLink(@PathVariable Long id, LinkDAO link){
        LinkDAO editedLink = linksRepository.findById(id).orElse(null);
        if(editedLink!=null){
            editedLink.setLink(link.getLink());
            editedLink.setName(link.getName());
            linksRepository.saveAndFlush(editedLink);
            return "redirect:/admin/links";
        }
        else {
            return "redirect:/admin/links";
        }
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
