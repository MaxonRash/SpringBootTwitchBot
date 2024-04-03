package com.bot.springboottwitchbot;

import com.bot.springboottwitchbot.connections.connection_runners.BotConnectionRunner;
import com.bot.springboottwitchbot.connections.connection_runners.MainConnectionRunner;
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

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTwitchBotApplication.class, args);

        //uncomment for running on test channel
        ApplicationContextProvider.getApplicationContext().getBean(BotConnectionRunner.class).getChannelConnection().Run();

        //uncomment for running on main channel
        ApplicationContextProvider.getApplicationContext().getBean(MainConnectionRunner.class).getChannelConnection().Run();
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
