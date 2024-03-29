package com.bot.springboottwitchbot.channels.builder_utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:twitchCredentials.properties")
public class TestChannelCredentialsUtil {
    @Value("${test_channel_name}")
    public String testChannelName;
    @Value("${bot_channel_accessToken}")
    public String botToken;

    public String getTestChannelName() {
        return testChannelName;
    }

    public String getBotToken() {
        return botToken;
    }


}
