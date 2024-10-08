package com.bot.springboottwitchbot;

import com.bot.springboottwitchbot.connections.connection_runners.BotConnectionRunner;
import com.bot.springboottwitchbot.connections.connection_runners.MainConnectionRunner;
import com.bot.springboottwitchbot.connections.connection_runners.SecondConnectionRunner;
import com.bot.springboottwitchbot.quartz.CheckDOBRunner;
import com.bot.springboottwitchbot.quartz.RepeatMessageRunner;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
@PropertySource("classpath:databaseCredentials.properties")
public class SpringBootTwitchBotApplication {
    public static void main(String[] args) throws SchedulerException {
        SpringApplication.run(SpringBootTwitchBotApplication.class, args);

        //uncomment for running on test channel
        ApplicationContextProvider.getApplicationContext().getBean(BotConnectionRunner.class).getChannelConnection().Run();

        //uncomment for running on main channel
        ApplicationContextProvider.getApplicationContext().getBean(MainConnectionRunner.class).getChannelConnection().Run();

        //uncomment for running on second channel
//        ApplicationContextProvider.getApplicationContext().getBean(SecondConnectionRunner.class).getChannelConnection().Run();

//        CheckDOBRunner.runSimpleTriggerTest();
//        CheckDOBRunner.runCronTriggerTest();

        UtilityDOB.addDOBsToList(); // Adds today's users with DOB to list UtilityDOB.listOfUsersWithDOB at start
        CheckDOBRunner.runCronTriggerCheckDOBs(); // Adds today's users with DOB to list UtilityDOB.listOfUsersWithDOB at 00:05 every day
        CheckDOBRunner.runCronTriggerSendMessageAboutUsersWithDOBsToday(); // Sends message about users with DOB at 14:30 every day
        RepeatMessageRunner.runCronTriggerDOBAddingReminderMessage(); // Sends message to remind about adding DOB every hour between 14 and 23

    }

    //DataSource Config (postgres)
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    // publishing a test event
        /*
        SubscriptionData subscriptionData = new SubscriptionData();
        subscriptionData.setDisplayName("testUser");
        ChannelSubscribeEvent channelSubscribeEvent = new ChannelSubscribeEvent(subscriptionData);
        eventManagerHappa.publish(channelSubscribeEvent);
        */

//     twitchClientBot.getEventManager().onEvent(ChatModerationEvent.class, System.out::println);
}
