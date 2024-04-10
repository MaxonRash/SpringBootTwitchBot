package com.bot.springboottwitchbot.quartz;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CheckDOBRunner {
    public static void runSimpleTriggerTest() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail job = JobBuilder.newJob(CheckForDatesOfBirth.class)
                .withIdentity("myJob", "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
        scheduler.scheduleJob(job, trigger);

        scheduler.start();
    }

    public static void runCronTriggerTest() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail job = JobBuilder.newJob(CheckForDatesOfBirth.class)
                .withIdentity("myJob", "group1")
                .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * 04 ? * *"))
                .forJob("myJob", "group1")
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }
}
