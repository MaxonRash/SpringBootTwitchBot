package com.bot.springboottwitchbot.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class RepeatMessageRunner {
    public static void runCronTriggerDOBAddingReminderMessage() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail job = JobBuilder.newJob(SendReminderMessageAboutDOB.class)
                .withIdentity("myJobReminderDOB1", "group1")
                .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTriggerDOBAddReminder1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 14-23 ? * *"))
                .forJob("myJobReminderDOB1", "group1")
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }
}