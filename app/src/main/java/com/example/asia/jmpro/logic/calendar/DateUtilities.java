package com.example.asia.jmpro.logic.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by asia on 05/10/2017.
 */

public class DateUtilities {
    public static int currentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int currentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int currentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }
}
