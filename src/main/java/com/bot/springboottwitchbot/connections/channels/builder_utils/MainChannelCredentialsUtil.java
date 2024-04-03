package com.bot.springboottwitchbot.connections.channels.builder_utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:twitchCredentials.properties")
public class MainChannelCredentialsUtil {
    @Value("${main_channel_id}")
    private String mainChannelId;
    @Value("${main_channel_name}")
    private String mainChannelName;
    @Value("${main_channel_accessToken}")
    private String mainToken;
    @Value("${main_channel_clientId}")
    private String client_id;

    public String getMainChannelId() {
        return mainChannelId;
    }

    public String getMainChannelName() {
        return mainChannelName;
    }

    public String getMainToken() {
        return mainToken;
    }

    public String getClient_id() {
        return client_id;
    }
}
