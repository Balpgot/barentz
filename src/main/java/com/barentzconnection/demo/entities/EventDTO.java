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
    private String description;
    private String day;
    private String date;
    private String time;
    private String link;
    private String imgPath;

    public int getDayOfMonth(){
        String [] data = date.split("\\.");
        return Integer.parseInt(data[0]);
    }

    public int getMonth(){
        String [] data = date.split("\\.");
        return Integer.parseInt(data[1]);
    }

    public void edit(EventDAO event){
        this.name = event.getName();
        this.category = event.getCategory();
        this.description = event.getDescription();
        this.day = String.valueOf(event.getDay());
        this.date = event.getDateAsString();
        this.time = event.getTimeAsString();
        this.link = event.getLink();
        this.imgPath = event.getImgPath();
    }

    public int getHour(){
        return Integer.parseInt(time.split(":")[0]);
    }

    public int getMinute(){
        return Integer.parseInt(time.split(":")[1]);
    }
}
