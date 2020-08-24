package com.barentzconnection.demo.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table
public class EventDAO {
    //Name – Category – Description -  Date - Time – Link
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition="varchar(1000)")
    private String description;
    private String speaker;
    private LocalTime time;
    private String link;

    public EventDAO() {
    }

    public EventDAO(String name, String description, String speaker,
                    LocalTime time, String link) {
        this.name = name;
        this.speaker = speaker;
        this.description = description;
        this.time = time;
        this.link = link;
    }

    public void edit(EventDTO editedEvent){
        this.name = editedEvent.getName();
        this.description = editedEvent.getDescription();
        this.speaker = editedEvent.getSpeaker();
        this.time = LocalTime.now().withHour(editedEvent.getHour()).withMinute(editedEvent.getMinute());
        this.link = editedEvent.getLink();
    }

    public String getTimeString(){
        return getTimeAsString()+" / "+getTimeAsStringNO();
    }

    public String getTimeAsString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter)+" RU";
    }

    public String getTimeAsStringNO(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime norwegian = time.minusHours(1);
        return norwegian.format(formatter) + " NO";
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "EventDAO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", link='" + link + '\'' +
                '}';
    }
}
