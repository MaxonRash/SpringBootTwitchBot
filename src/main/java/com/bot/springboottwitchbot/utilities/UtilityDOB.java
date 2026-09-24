package com.bot.springboottwitchbot.utilities;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UtilityDOB {
    private static final Logger log = LoggerFactory.getLogger(UtilityDOB.class);
    public static List<String> listOfUsersWithDOB = new ArrayList<>();
    public static boolean CheckIfFollowIsMoreThan6Months(Date currentDate, Date dateOfFollowSince) {
        Calendar currentDateCalendar = Calendar.getInstance();
        currentDateCalendar.setTime(currentDate);
        Calendar followSinceDateCalendar = Calendar.getInstance();
        followSinceDateCalendar.setTime(dateOfFollowSince);
        currentDateCalendar.add(Calendar.MONTH, -6);
        return currentDateCalendar.after(followSinceDateCalendar);
    }

    public static void addDOBsToList () {
        UtilityDOB.listOfUsersWithDOB = new ArrayList<>();
        ArrayList<User> allUsersWithDOB = ApplicationContextProvider.getApplicationContext().getBean(UsersService.class).findAllUsersWithDOBIsToday();
        if (!allUsersWithDOB.isEmpty()) {
            for (User user : allUsersWithDOB) {
                UtilityDOB.listOfUsersWithDOB.add(user.getLogin());
            }
        }
        log.info("Добавлены ДР. Сегодня ДР у: {}", UtilityDOB.listOfUsersWithDOB);
    }
}
