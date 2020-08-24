package com.barentzconnection.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class UserDAO {
    //Login – E-mail – Password – Country – Score – Logged in
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String email;
    private String password;
    private String country;
    private String city;
    private Integer score;
    private Integer age;
    private Boolean isLogged;
    private Boolean isAdmin;
    private String avatarPath;
    private String registrationToken;
    private Boolean isConfirmed;

    public UserDAO(String login, String email, String pass, String country, String city, int age, int score, boolean isAdmin) {
        this.login = login;
        this.email = email;
        this.password = pass;
        this.country = country;
        this.city = city;
        this.age = age;
        this.score = score;
        this.isAdmin = isAdmin;
    }

    public String getLocation(){
        return country + ',' + city;
    }

    public void edit(UserDAO editedUser){
        this.login = editedUser.getLogin();
        this.email = editedUser.getEmail();
        this.password = editedUser.getPassword();
        this.country = editedUser.getCountry();
        this.city = editedUser.getCity();
        this.age = editedUser.getAge();
        this.score = editedUser.getScore();
        this.isAdmin = editedUser.getIsAdmin();
        this.avatarPath = editedUser.getAvatarPath();
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
