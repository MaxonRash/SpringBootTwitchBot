package com.bot.springboottwitchbot;

import com.bot.springboottwitchbot.connections.connection_runners.BotConnectionRunner;
import com.bot.springboottwitchbot.connections.connection_runners.MainConnectionRunner;
import com.bot.springboottwitchbot.connections.connection_runners.SecondConnectionRunner;
import com.bot.springboottwitchbot.config.BotProperties;
import com.bot.springboottwitchbot.quartz.CheckDOBRunner;
import com.bot.springboottwitchbot.quartz.RepeatMessageRunner;
import com.bot.springboottwitchbot.utilities.UtilityDOB;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
@PropertySource("classpath:databaseCredentials.properties")
public class SpringBootTwitchBotApplication {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws SchedulerException {
        context = SpringApplication.run(SpringBootTwitchBotApplication.class, args);

        // Which channels connect is driven by the bot.channels property (see application.properties).
        BotProperties botProperties = ApplicationContextProvider.getApplicationContext().getBean(BotProperties.class);
        if (botProperties.isChannelEnabled("test")) {
            ApplicationContextProvider.getApplicationContext().getBean(BotConnectionRunner.class).getChannelConnection().run();
        }
        if (botProperties.isChannelEnabled("main")) {
            ApplicationContextProvider.getApplicationContext().getBean(MainConnectionRunner.class).getChannelConnection().run();
        }
        if (botProperties.isChannelEnabled("second")) {
            ApplicationContextProvider.getApplicationContext().getBean(SecondConnectionRunner.class).getChannelConnection().run();
        }

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

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(SpringBootTwitchBotApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
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
