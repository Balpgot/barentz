package com.barentzconnection.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    public NavigationController(){}



    @GetMapping(value = "/lgn")
    public String loginPageRedirect(){
        return "redirect:/login";
    }

    @GetMapping(value = "/contacts")
    public String contactsPage(){
        return "contactsPage";
    }
}
