package com.barentzconnection.demo.services;

import com.barentzconnection.demo.configuration.WebSecurityConfig;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public static String getUserLogin(){
        Object login = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(login instanceof User){
            return ((User) login).getUsername();
        }
        else{
            return (String) login;
        }
    }

    public static boolean isAuthenticated() {
        return !getUserLogin().equals(WebSecurityConfig.ANONYMOUS);
    }

}
