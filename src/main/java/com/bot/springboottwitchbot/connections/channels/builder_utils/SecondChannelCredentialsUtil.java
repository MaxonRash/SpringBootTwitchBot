package com.bot.springboottwitchbot.connections.channels.builder_utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:twitchCredentials.properties")
public class SecondChannelCredentialsUtil {
    @Value("${second_channel_id}")
    private String secondChannelId;
    @Value("${second_channel_name}")
    private String secondChannelName;
    @Value("${second_channel_accessToken}")
    private String secondToken;
    @Value("${second_channel_clientId}")
    private String client_id;

    public String getSecondChannelId() {
        return secondChannelId;
    }

    public String getSecondChannelName() {
        return secondChannelName;
    }

    public String getSecondChannelToken() {
        return secondToken;
    }

    public String getClient_id() {
        return client_id;
    }
}
