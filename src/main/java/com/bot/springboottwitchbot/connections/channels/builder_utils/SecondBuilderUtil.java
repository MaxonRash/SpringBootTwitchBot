package com.bot.springboottwitchbot.connections.channels.builder_utils;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * The idea of using ApplicationProvider is not good, but not to rewrite the whole structure here is the solution:
 * <a href="https://stackoverflow.com/questions/78871993/why-applicationcontextprovider-throws-nullpointerexception-on-another-system">Why @DependsOn is used here</a>
 */

@Component
@DependsOn({"applicationContextProvider"})
public class SecondBuilderUtil {
    SecondChannelCredentialsUtil secondChannelCredentialsUtil;

    @Autowired
    private SecondBuilderUtil(SecondChannelCredentialsUtil secondChannelCredentialsUtil) {
        this.secondChannelCredentialsUtil = secondChannelCredentialsUtil;
    }

    public final OAuth2Credential credentialSecond = new OAuth2Credential("twitch",
            ApplicationContextProvider.getApplicationContext().getBean(SecondChannelCredentialsUtil.class).getSecondChannelToken());
    public final TwitchClient twitchClientSecond =
            TwitchClientBuilder.builder()
                    .withEnableChat(true)
                    .withChatAccount(credentialSecond)
                    .withEnableHelix(true)
                    .withEnablePubSub(true)
                    .withDefaultEventHandler(SimpleEventHandler.class)
                    .build();

    public String getSecondChannelId() {
        return secondChannelCredentialsUtil.getSecondChannelId();
    }

    public String getSecondChannelToken() {
        return secondChannelCredentialsUtil.getSecondChannelToken();
    }

    public String getSecondChannelName() {
        return secondChannelCredentialsUtil.getSecondChannelName();
    }

    public String getClient_id() {
        return secondChannelCredentialsUtil.getClient_id();
    }

    public TwitchClient getTwitchClientSecond() {
        return twitchClientSecond;
    }

}

