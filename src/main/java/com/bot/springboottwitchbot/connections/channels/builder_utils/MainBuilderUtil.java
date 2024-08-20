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
 * The idea of using ApplicationContextProvider is not good, but not to rewrite the whole structure here is the solution:
 * <a href="https://stackoverflow.com/questions/78871993/why-applicationcontextprovider-throws-nullpointerexception-on-another-system">Why @DependsOn is used here</a>
 */

@Component
@DependsOn({"applicationContextProvider"})
public class MainBuilderUtil {
    MainChannelCredentialsUtil mainChannelCredentialsUtil;
    public final OAuth2Credential credentialMain = new OAuth2Credential("twitch",
            ApplicationContextProvider.getApplicationContext().getBean(MainChannelCredentialsUtil.class).getMainToken());

    @Autowired
    private MainBuilderUtil(MainChannelCredentialsUtil mainChannelCredentialsUtil) {
        this.mainChannelCredentialsUtil = mainChannelCredentialsUtil;
    }

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
