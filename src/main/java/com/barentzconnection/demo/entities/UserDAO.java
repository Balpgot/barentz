package com.barentzconnection.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserDAO {

    //Login – E-mail – Password – Country – Score – Logged in
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String email;
    private String password;
    private String country;
    private Integer score;
    private Boolean isLogged;
    private Boolean isAdmin;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<EventDAO> userEvents;

    public UserDAO(String login, String email, String pass, String country, int i, boolean b) {
        this.login = login;
        this.email = email;
        this.password = pass;
        this.country = country;
        this.score = i;
        this.isAdmin = b;
    }

    public void edit(UserDAO editedUser){
        this.login = editedUser.getLogin();
        this.email = editedUser.getEmail();
        this.password = editedUser.getPassword();
        this.country = editedUser.getCountry();
        this.score = editedUser.getScore();
        this.isAdmin = editedUser.getIsAdmin();
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", score=" + score +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
