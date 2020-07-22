package com.barentzconnection.demo.entities;

import lombok.Data;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@Entity
@Table(name = "event")
public class EventDAO {
    //Name – Category – Description -  Date - Time – Link
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String name;
    private String category;
    private String description;
    private Integer day;
    private LocalDate date;
    private LocalTime time;
    private String link;
    private String imgPath;
    @ManyToMany(mappedBy = "userEvents")
    private Set<UserDAO> users;

    public EventDAO() {
    }

    public EventDAO(String name, String category, String description,
                    Integer day, LocalDate date, LocalTime time, String link, String imgPath) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.day = day;
        this.date = date;
        this.time = time;
        this.link = link;
        this.imgPath = imgPath;
    }

    public void edit(EventDAO editedEvent){
        this.name = editedEvent.getName();
        this.category = editedEvent.getCategory();
        this.description = editedEvent.getDescription();
        this.date = editedEvent.getDate();
        this.time = editedEvent.getTime();
        this.link = editedEvent.getLink();
        this.imgPath = editedEvent.getImgPath();
    }

    public String getDateAsString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(formatter);
    }

    public String getTimeAsString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDay() {
        return day;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLink() {
        return link;
    }

    public String getImgPath() {
        return imgPath;
    }
}
