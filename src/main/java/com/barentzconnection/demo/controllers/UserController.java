package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.entities.EventDAO;
import com.barentzconnection.demo.entities.UserDAO;
import com.barentzconnection.demo.repositories.IEventRepository;
import com.barentzconnection.demo.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class UserController {
    IUserRepository userRepo;
    IEventRepository eventRepo;
    BCryptPasswordEncoder encoder;
    @Autowired
    public UserController(IUserRepository userRepo, IEventRepository eventRepo, BCryptPasswordEncoder encoder){
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.encoder = encoder;
        //Login – E-mail – Password – Country – Score – Logged in
        userRepo.saveAndFlush(
                new UserDAO(
                        "user",
                        "email",
                        encoder.encode("pass"),
                        "country",
                        100,
                        true
                )
        );
        userRepo.saveAndFlush(
                new UserDAO(
                        "user2",
                        "email2",
                        encoder.encode("pass2"),
                        "country2",
                        202,
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
                        "bf.jpg"
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
                        "bf2.jpg"
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
                        "bf.jpg"
                )
        );
    }

    @GetMapping(value = "/login")
    public String userPage(Model model){
        model.addAttribute("user", new UserDAO());
        model.addAttribute("password_check", new UserDAO());
        return "loginPage";
    }

    @GetMapping(value = "/leaders")
    public String leadersPage(Model model){
        model.addAttribute("users", userRepo.findAllByScoreAfterOrderByScoreDesc(0));
        return "leaderPage";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerUser(UserDAO user){
        if(userRepo.findUserDAOByLogin(user.getLogin()).isPresent()){
            return "redirect:/login?lg";
        }
        /*if(!user.getPassword().equals(pass.getPassword())){
            System.out.println("padfads");
            return "redirect:/login?pwd";
        }*/
        System.out.println(user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setScore(0);
        user.setIsAdmin(false);
        System.out.println(user);
        userRepo.saveAndFlush(user);
        return "redirect:/login?success";
    }

    /*
        TODO
            список мероприятий на которые ты подписан
            добавить кнопку подписаться на карточку
     */
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
