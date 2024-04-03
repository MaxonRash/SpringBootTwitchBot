package com.bot.springboottwitchbot.channels.builder_utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:twitchCredentials.properties")
public class TestChannelCredentialsUtil {
    @Value("${test_channel_id}")
    private String testChannelId;
    @Value("${bot_channel_id}")
    private String botChannelId;
    @Value("${test_channel_name}")
    private String testChannelName;
    @Value("${test_channel_accessToken}")
    private String testChannelToken;
    @Value("${bot_channel_accessToken}")
    private String botToken;
    @Value("${bot_channel_clientId}")
    private String client_id;

    public String getTestChannelId() {
        return testChannelId;
    }

    public String getBotChannelId() {
        return botChannelId;
    }

    public String getTestChannelName() {
        return testChannelName;
    }

    public String getTestChannelToken() {
        return testChannelToken;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getClient_id() {
        return client_id;
    }
}
