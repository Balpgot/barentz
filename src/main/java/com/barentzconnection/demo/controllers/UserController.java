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

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class UserController {
    IUserRepository userRepo;
    IEventRepository eventRepo;
    @Autowired
    public UserController(IUserRepository userRepo, IEventRepository eventRepo){
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        //Login – E-mail – Password – Country – Score – Logged in
        userRepo.saveAndFlush(
                new UserDAO(
                        "user",
                        "email",
                        "pass",
                        "country",
                        100,
                        true
                )
        );
        //Name – Category – Description -  Date - Time – Link
        eventRepo.saveAndFlush(
                new EventDAO(
                        "name",
                        "category",
                        "desc",
                        1,
                        LocalDate.now(),
                        LocalTime.now(),
                        "link",
                        "./"
                )
        );
        eventRepo.saveAndFlush(
                new EventDAO(
                        "name2",
                        "category2",
                        "desc2",
                        1,
                        LocalDate.now(),
                        LocalTime.now(),
                        "link",
                        "./"
                )
        );
        eventRepo.saveAndFlush(
                new EventDAO(
                        "name2",
                        "category2",
                        "desc2",
                        2,
                        LocalDate.now(),
                        LocalTime.now(),
                        "link",
                        "./"
                )
        );
    }

    @GetMapping(value = "/login")
    public String userPage(){
        return "loginPage";
    }

    @GetMapping(value = "/cabinet/{login}")
    public String userPage(@PathVariable String login, Model model){
        UserDAO user = userRepo.findUserDAOByLogin(login).orElse(null);
        if(user == null){
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("events", user.getUserEvents());
        return "cabinet";
    }


}
