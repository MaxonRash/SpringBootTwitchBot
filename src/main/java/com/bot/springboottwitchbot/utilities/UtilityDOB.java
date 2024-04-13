package com.bot.springboottwitchbot.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilityDOB {
    public static List<String> listOfUsersWithDOB /*= new ArrayList<>()*/;
    public static boolean CheckIfFollowIsMoreThan6Months(Date currentDate, Date dateOfFollowSince) {
        Calendar currentDateCalendar = Calendar.getInstance();
        currentDateCalendar.setTime(currentDate);
        Calendar followSinceDateCalendar = Calendar.getInstance();
        followSinceDateCalendar.setTime(dateOfFollowSince);
        currentDateCalendar.add(Calendar.MONTH, -6);
        return currentDateCalendar.after(followSinceDateCalendar);
    }
}
