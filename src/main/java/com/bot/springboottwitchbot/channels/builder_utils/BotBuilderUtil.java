package com.bot.springboottwitchbot.channels.builder_utils;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BotBuilderUtil {
    @Autowired
    TestChannelCredentialsUtil testChannelCredentialsUtil;
    public final OAuth2Credential credentialBot = new OAuth2Credential("twitch",
            ApplicationContextProvider.getApplicationContext().getBean(TestChannelCredentialsUtil.class).getBotToken());
    public final TwitchClient twitchClientBot =
            TwitchClientBuilder.builder()
                    .withEnableChat(true)
                    .withChatAccount(credentialBot)
                    .withEnableHelix(true)
                    .withEnablePubSub(true)
                    .withDefaultEventHandler(SimpleEventHandler.class)
                    .build();

    public String getBotToken() {
        return testChannelCredentialsUtil.getBotToken();
    }

    public TwitchClient getTwitchClientBot() {
        return twitchClientBot;
    }

    public String getTestChannelName () {
        return testChannelCredentialsUtil.getTestChannelName();
    }
}
