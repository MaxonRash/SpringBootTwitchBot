package com.bot.springboottwitchbot.quartz;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.models.User;
import com.bot.springboottwitchbot.services.UsersService;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CheckForDatesOfBirth implements Job {
    private static final Logger log = LoggerFactory.getLogger(CheckForDatesOfBirth.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
//        UtilityCommandsGlobal.listOfUsersWithDOB = Collections.singletonList("12.08.1994");
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
