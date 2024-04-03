package com.bot.springboottwitchbot.connections.channels.builder_utils;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainBuilderUtil {
    MainChannelCredentialsUtil mainChannelCredentialsUtil;

    @Autowired
    private MainBuilderUtil(MainChannelCredentialsUtil mainChannelCredentialsUtil) {
        this.mainChannelCredentialsUtil = mainChannelCredentialsUtil;
    }

    public final OAuth2Credential credentialMain = new OAuth2Credential("twitch",
            ApplicationContextProvider.getApplicationContext().getBean(MainChannelCredentialsUtil.class).getMainToken());
    public final TwitchClient twitchClientMain =
            TwitchClientBuilder.builder()
                    .withEnableChat(true)
                    .withChatAccount(credentialMain)
                    .withEnableHelix(true)
                    .withEnablePubSub(true)
                    .withDefaultEventHandler(SimpleEventHandler.class)
                    .build();

    public String getMainChannelId() {
        return mainChannelCredentialsUtil.getMainChannelId();
    }
    public String getMainChannelName() {
        return mainChannelCredentialsUtil.getMainChannelName();
    }

    public String getMainToken() {
        return mainChannelCredentialsUtil.getMainToken();
    }

    public String getClient_id() {
        return mainChannelCredentialsUtil.getClient_id();
    }

    public TwitchClient getTwitchClientMain() {
        return twitchClientMain;
    }

    public OAuth2Credential getCredentialMain() {
        return credentialMain;
    }
}
