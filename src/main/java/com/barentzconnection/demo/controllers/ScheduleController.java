package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.repositories.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScheduleController {
    IEventRepository eventRepository;

    @Autowired
    public ScheduleController(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "/schedule")
    public String getSchedule(Model model){
        model.addAttribute("events", eventRepository.findAllByDay(1));
        return "schedulePage";
    }

    @GetMapping(value = "/schedule/{day}")
    public String getEventsPerDay(@PathVariable int day, Model model){
        model.addAttribute("events", eventRepository.findAllByDay(day));
        return "results :: eventsPerDay";
    }
}
