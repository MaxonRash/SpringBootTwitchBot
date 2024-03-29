package com.bot.springboottwitchbot;

import com.bot.springboottwitchbot.channels.TestChannel;
import com.bot.springboottwitchbot.connections.ChannelConnection;
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
    ChannelConnection channelConnection;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTwitchBotApplication.class, args);

//        System.out.println(ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getBotToken());

        ApplicationContextProvider.getApplicationContext().getBean(TestChannel.class).Run();
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


}
