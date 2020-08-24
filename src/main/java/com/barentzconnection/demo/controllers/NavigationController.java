package com.barentzconnection.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NavigationController {

    public NavigationController(){
    }

    @GetMapping(value = "/lgn")
    public String loginPageRedirect(){
        return "redirect:/login";
    }

    @GetMapping(value = "/contacts")
    public String contactsPage(){
        return "contactsPage";
    }

    @GetMapping(value = "/contacts/{id}")
    public String loadContacts(@PathVariable Long id){
        if(id==2){
            return "team2 :: team";
        }
        else if(id==3){
            return "speakers :: speak";
        }
        else{
            return "contactsPage";
        }
    }
}
