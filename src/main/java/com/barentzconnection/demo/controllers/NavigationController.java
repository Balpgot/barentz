package com.barentzconnection.demo.controllers;

import com.barentzconnection.demo.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    public NavigationController(){}

    @GetMapping(value = "/")
    public String mainPage(Model model){
        model.addAttribute("auth", AuthService.isAuthenticated());
        model.addAttribute("login", AuthService.getUserLogin());
        return "mainPage";
    }

    @GetMapping(value = "/lgn")
    public String loginPageRedirect(){
        return "redirect:/login";
    }

    @GetMapping(value = "/contacts")
    public String contactsPage(){
        return "contactsPage";
    }
}
