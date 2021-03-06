package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.entities.UserDAO;
import com.barentzconnection.demo.repositories.IEventRepository;
import com.barentzconnection.demo.repositories.IUnregisteredUsersRepository;
import com.barentzconnection.demo.repositories.IUserRepository;
import com.barentzconnection.demo.services.AuthService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserController {
    IUserRepository userRepo;
    IUnregisteredUsersRepository unregisteredUsersRepo;
    IEventRepository eventRepo;
    BCryptPasswordEncoder encoder;
    JavaMailSenderImpl mailSender;
    private final String UPLOADED_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/img/avatars/";
    private final String AVATAR_PATH = "/img/avatars/";
    @Autowired
    public UserController(IUserRepository userRepo, IEventRepository eventRepo,
                          IUnregisteredUsersRepository unregisteredUsersRepo,
                          JavaMailSenderImpl mailSender,
                          BCryptPasswordEncoder encoder){
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.encoder = encoder;
        this.unregisteredUsersRepo = unregisteredUsersRepo;
        this.mailSender = mailSender;
        //Login – E-mail – Password – Country – Score – Logged in

        //Name – Category – Description -  Date - Time – Link
        preload();
    }

    private void preload(){
        UserDAO user;
        if(!userRepo.findUserDAOByLogin("Admin").isPresent()) {
            userRepo.saveAndFlush(
                    new UserDAO(
                            "Admin",
                            "barentsconnection@gmail.com",
                            encoder.encode("BC2020"),
                            "Russia",
                            "Murmansk",
                            0,
                            -1,
                            true
                    )
            );
            user = userRepo.findUserDAOByLogin("Admin").get();
            user.setAvatarPath(AVATAR_PATH + "basic.jpg");
            userRepo.saveAndFlush(user);
        }
        if(!userRepo.findUserDAOByLogin("Admin2").isPresent()) {
            userRepo.saveAndFlush(
                    new UserDAO(
                            "Admin2",
                            "barentsconnection@gmail.com",
                            encoder.encode("BC2020"),
                            "Russia",
                            "Murmansk",
                            0,
                            -1,
                            true
                    )
            );
            user = userRepo.findUserDAOByLogin("Admin2").get();
            user.setAvatarPath(AVATAR_PATH + "basic.jpg");
            userRepo.saveAndFlush(user);
        }
    }

    @GetMapping(value = "/")
    public String mainPage(Model model){
        if(AuthService.isAuthenticated()){
            model.addAttribute("auth", true);
            model.addAttribute("user",
                    userRepo.findUserDAOByLogin(AuthService.getUserLogin()).get());
        }
        else{
            model.addAttribute("auth", false);
        }
        return "homepage";
    }

    @GetMapping(value = "/login")
    public String userPage(Model model){
        model.addAttribute("user", new UserDAO());
        if(AuthService.isAuthenticated()){
            model.addAttribute("auth", true);
            model.addAttribute("user",
                    userRepo.findUserDAOByLogin(AuthService.getUserLogin()).get());
        }
        else{
            model.addAttribute("auth", false);
        }
        return "loginPage";
    }

    @GetMapping(value = "/leaders")
    public String leadersPage(Model model){
        model.addAttribute("users", userRepo.findAllByScoreAfterOrderByScoreDesc(-1));
        if(AuthService.isAuthenticated()){
            model.addAttribute("auth", true);
            model.addAttribute("user",
                    userRepo.findUserDAOByLogin(AuthService.getUserLogin()).get());
        }
        else{
            model.addAttribute("auth", false);
        }
        return "leaderPage";
    }

    @PostMapping(value = "/avatar/{id}")
    public String uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        if(!file.isEmpty()) {
            try {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                int dotPlace = file.getOriginalFilename().lastIndexOf(".");
                if(dotPlace>0) {
                    String extension = file.getOriginalFilename().substring(dotPlace);
                    Path path = Paths.get(UPLOADED_FOLDER + id + extension);
                    Files.write(path, bytes);
                    Optional<UserDAO> user = userRepo.findById(id);
                    if (user.isPresent()) {
                        user.get().setAvatarPath(AVATAR_PATH+id+extension);
                        userRepo.saveAndFlush(user.get());
                        return "redirect:/cabinet/" + user.get().getLogin();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
            TODO
                Добавить Request Param
         */
        return "redirect:/cabinet/?empty";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model){
        model.addAttribute("user", new UserDAO());
        if(AuthService.isAuthenticated()){
            model.addAttribute("auth", true);
            model.addAttribute("user",
                    userRepo.findUserDAOByLogin(AuthService.getUserLogin()).get());
        }
        else{
            model.addAttribute("auth", false);
        }
        return "register";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String registerUser(UserDAO user){
        if(userRepo.findUserDAOByLogin(user.getLogin()).isPresent()){
            return "redirect:/register?login";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setScore(0);
        user.setIsAdmin(false);
        user.setAvatarPath(AVATAR_PATH+"basic.jpg");
        user.setRegistrationToken(String.valueOf(user.hashCode()));
        unregisteredUsersRepo.saveAndFlush(user);
        sendConfirmationMessage(user);
        return "redirect:/login?confirm";
    }

    private void sendConfirmationMessage(UserDAO user){
        String confirmationLink = "barentsconnection.com/register/confirm/" + user.getRegistrationToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("BarentsConnection");
        message.setTo(user.getEmail());
        message.setSubject("E-mail confirmation");
        message.setText("Hi! Please, follow link below to confirm your email for Barents Connection.\n\n" +
                confirmationLink+"\n\nBest regards,\n\nBarents Connection team");
        mailSender.send(message);
    }

    @GetMapping(value = "/register/confirm/{token}")
    public String confirmUser(@PathVariable Long token){
        Optional<UserDAO> userOptional = unregisteredUsersRepo.findUserDAOByRegistrationToken(String.valueOf(token));
        if(userOptional.isEmpty()){
            return "redirect:/";
        }
        UserDAO user = userOptional.get();
        user.setRegistrationToken(Strings.EMPTY);
        user.setIsConfirmed(true);
        userRepo.saveAndFlush(user);
        return "redirect:/login?success";
    }
}
