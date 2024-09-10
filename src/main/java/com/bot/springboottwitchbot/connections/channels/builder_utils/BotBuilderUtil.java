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
public class BotBuilderUtil {

    TestChannelCredentialsUtil testChannelCredentialsUtil;

    @Autowired
    private BotBuilderUtil(TestChannelCredentialsUtil testChannelCredentialsUtil) {
        this.testChannelCredentialsUtil = testChannelCredentialsUtil;
    }

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

    public String getTestChannelId() {
        return testChannelCredentialsUtil.getTestChannelId();
    }

    public String getBotChannelId() {
        return testChannelCredentialsUtil.getBotChannelId();
    }

    public String getTestChannelToken() {
        return testChannelCredentialsUtil.getTestChannelToken();
    }

    public String getBotToken() {
        return testChannelCredentialsUtil.getBotToken();
    }

    public String getTestChannelName() {
        return testChannelCredentialsUtil.getTestChannelName();
    }

    public String getBotChannelName() {
        return testChannelCredentialsUtil.getBotChannelName();
    }

    public String getClient_id() {
        return testChannelCredentialsUtil.getClient_id();
    }

    public TwitchClient getTwitchClientBot() {
        return twitchClientBot;
    }

}
