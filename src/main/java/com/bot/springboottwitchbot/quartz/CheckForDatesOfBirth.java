package com.bot.springboottwitchbot.quartz;

import com.bot.springboottwitchbot.utilities.UtilityCommandsGlobal;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.Collections;

public class CheckForDatesOfBirth implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        UtilityCommandsGlobal.listOfUsersWithDOB = Collections.singletonList("12.08.1994");
        System.out.println(UtilityCommandsGlobal.listOfUsersWithDOB);
    }
}
