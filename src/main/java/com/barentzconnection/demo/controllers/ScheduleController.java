package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.entities.EventDAO;
import com.barentzconnection.demo.entities.LinkDAO;
import com.barentzconnection.demo.repositories.IEventRepository;
import com.barentzconnection.demo.repositories.ILinksRepository;
import com.barentzconnection.demo.repositories.IUserRepository;
import com.barentzconnection.demo.services.AuthService;
import com.barentzconnection.demo.services.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Controller
public class ScheduleController {
    IEventRepository eventRepository;
    IUserRepository userRepository;
    ILinksRepository linksRepository;

    @Autowired
    public ScheduleController(IEventRepository eventRepository, IUserRepository userRepository, ILinksRepository linksRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.linksRepository = linksRepository;
        preload();
    }

    private void preload(){
        if(eventRepository.findEventDAOByName("Become a musician without a music instrument.").isEmpty()) {
            eventRepository.saveAndFlush(
                    new EventDAO(
                            "Become a musician without a music instrument.",
                            "Always wanted to learn how to play the guitar, but have neither the guitar nor time? Great! We will make your dream come true. All you need is a smartphone and Katja Merakerli’s workshop, where she will tell you how to create music with “Garageband” app.",
                            "Katja Marakelri",
                            LocalTime.of(18, 0),
                            "https://docs.google.com/forms/d/1wOXML1So2ASAD0v5ARyj04OS5GQm2WJATmA2djuHRzY/viewform?edit_requested=true"
                    )
            );
        }
        if(eventRepository.findEventDAOByName("How to improve your public speaking skills").isEmpty()) {
            eventRepository.saveAndFlush(
                    new EventDAO(
                            "How to improve your public speaking skills",
                            "Nervous when speaking in public? Always mumble, stutter or jabber while giving a talk? Don't know how to stand and where to put your hands? Maria Yurieva, an actress and director-teacher of the “Children’s theater school”, will teach you some techniques to develop a strong, vibrant speaking voice and reveal some secrets of how to reduce your fear of public speaking.",
                            "Maria Urieva",
                            LocalTime.of(19, 0),
                            "https://docs.google.com/forms/d/1wOXML1So2ASAD0v5ARyj04OS5GQm2WJATmA2djuHRzY/viewform?edit_requested=true"
                    )
            );
        }
        if(eventRepository.findEventDAOByName("There’s a bear in your room!").isEmpty()) {
            eventRepository.saveAndFlush(
                    new EventDAO(
                            "There’s a bear in your room!",
                            "The perfect way to get new skills in handicraft and reuse old magazines or newspapers is to take part in the challenge from Victoria Zaitseva. She works at the Center for Creativity and Social Adaptation and will be glad to teach you some new handicrafts and design skills. Are you ready? So, join us!",
                            "Victoria Zaytseva",
                            LocalTime.of(19, 0),
                            "https://docs.google.com/forms/d/1wOXML1So2ASAD0v5ARyj04OS5GQm2WJATmA2djuHRzY/viewform?edit_requested=true"
                    )
            );
        }
        if(eventRepository.findEventDAOByName("First step in returning your pre-self-isolation body").isEmpty()) {
            eventRepository.saveAndFlush(
                    new EventDAO(
                            "First step in returning your pre-self-isolation body",
                            "Fitness centers are just starting to open, YouTube persistently offers home workouts, while you fall asleep on the couch with a pack of chips? It’s time you take on the challenge from our speaker Vladislav Sapozhnikov, who is the world championship bronze medalist in ice swimming and the record holder in the international open water swimming festival “Gulf Stream”. If you want to remember where your abs are – join Vladislav’s challenge!",
                            "Vladislav Sapozhnikov",
                            LocalTime.now(),
                            "https://docs.google.com/forms/d/1wOXML1So2ASAD0v5ARyj04OS5GQm2WJATmA2djuHRzY/viewform?edit_requested=true"

                    )
            );
        }
        if(eventRepository.findEventDAOByName("Basic breaking").isEmpty()) {
            eventRepository.saveAndFlush(
                    new EventDAO(
                            "Basic breaking",
                            "If you can't sit still with the sound of groovy music, if \"Dirty Dancing\" and \"Step Forward\" are more than just movies for you, if you don’t dance, but with admiration watch break dancers spinning on their heads, the next workshop is just for you! Artem Ostanin, bboy, head of the “Dance Lab” and founder of the “Polar Universal” breaking team, will show you how to own the dance floor!",
                            "Artem Ostanin",
                            LocalTime.of(17, 0),
                            "https://docs.google.com/forms/d/1wOXML1So2ASAD0v5ARyj04OS5GQm2WJATmA2djuHRzY/viewform?edit_requested=true"
                    )
            );
        }
    }

    @GetMapping(value = "/schedule")
    public String getSchedule(Model model){
        //model.addAttribute("events", eventRepository.findAllByDay(1));
        model.addAttribute("day", DayService.getCurrentDay());
        if(AuthService.isAuthenticated()){
            model.addAttribute("auth", true);
            model.addAttribute("user",
                    userRepository.findUserDAOByLogin(AuthService.getUserLogin()).get());
        }
        else{
            model.addAttribute("auth", false);
        }
        List<LinkDAO> links = linksRepository.findAll();
        for (LinkDAO link:links){
            model.addAttribute(link.getName(),link.getLink());
        }
        return "1day";
    }

    @GetMapping(value = "/schedule/{day}")
    public String getEventsPerDay(@PathVariable int day, Model model){
        if(day==2){
            List<EventDAO> eventDAOList = eventRepository.findAllByIdIsNotNullOrderByIdAsc();
            model.addAttribute("event1", eventDAOList.get(0));
            model.addAttribute("event2", eventDAOList.get(1));
            model.addAttribute("event3", eventDAOList.get(2));
            model.addAttribute("event4", eventDAOList.get(3));
            model.addAttribute("event5", eventDAOList.get(4));
            LocalTime currentTime = LocalTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
            for (int i = 0; i<eventDAOList.size(); i++){
                EventDAO event = eventDAOList.get(i);
                StringBuilder builder = new StringBuilder("event").append(i+1).append("ready");
                if(event.getTime().isBefore(currentTime)){
                    model.addAttribute(builder.toString(), true);
                }
                else{
                    model.addAttribute(builder.toString(), false);
                }
            }
        }
        if(AuthService.isAuthenticated()){
            model.addAttribute("auth", true);
            model.addAttribute("user",
                    userRepository.findUserDAOByLogin(AuthService.getUserLogin()).get());
        }
        else{
            model.addAttribute("auth", false);
        }
        List<LinkDAO> links = linksRepository.findAll();
        for (LinkDAO link:links){
            model.addAttribute(link.getName(),link.getLink());
        }
        return day+"day :: loadArea";
    }
}
