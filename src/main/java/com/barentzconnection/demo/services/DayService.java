package com.barentzconnection.demo.services;

import java.time.LocalDate;
import java.time.ZoneId;

public class DayService {
    public static int getCurrentDay(){
        LocalDate currentDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
        switch (currentDate.getDayOfMonth()){
            case 25: return 1;
            case 26: return 2;
            case 27: return 3;
            case 28: return 4;
            case 29: return 5;
            default: return 1;
        }
    }

}
