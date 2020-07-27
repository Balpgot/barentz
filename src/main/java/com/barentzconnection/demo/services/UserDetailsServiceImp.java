package com.barentzconnection.demo.services;

import com.barentzconnection.demo.configuration.WebSecurityConfig;
import com.barentzconnection.demo.entities.UserDAO;
import com.barentzconnection.demo.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {

    IUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImp(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Optional<UserDAO> optionalUserDAO = userRepository.findUserDAOByLogin(username);
        User.UserBuilder builder;
        if (optionalUserDAO.isPresent()) {
            System.out.println(optionalUserDAO.toString());
            UserDAO user = optionalUserDAO.get();
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            if (user.getIsAdmin()) {
                builder.roles(WebSecurityConfig.ADMIN);
                grantedAuthorities.add(new SimpleGrantedAuthority(WebSecurityConfig.ADMIN));
            } else {
                builder.roles(WebSecurityConfig.USER);
                grantedAuthorities.add(new SimpleGrantedAuthority(WebSecurityConfig.USER));
            }
            builder.authorities(grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }
}