package com.barentzconnection.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private String name;
    private String category;
    private String speaker;
    private String description;
    private String time;
    private String link;

    public void edit(EventDAO event){
        this.name = event.getName();
        this.description = event.getDescription();
        this.time = event.getTimeAsString().substring(0,event.getTimeAsString().indexOf(" "));
        this.speaker = event.getSpeaker();
    }

    public int getHour(){
        return Integer.parseInt(time.split(":")[0]);
    }

    public int getMinute(){
        return Integer.parseInt(time.split(":")[1]);
    }
}
