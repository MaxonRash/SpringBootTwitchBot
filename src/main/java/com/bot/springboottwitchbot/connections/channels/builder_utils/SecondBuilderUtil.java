package com.bot.springboottwitchbot.connections.channels.builder_utils;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecondBuilderUtil {
    private final SecondChannelCredentialsUtil secondChannelCredentialsUtil;
    private final OAuth2Credential credentialSecond;
    private final TwitchClient twitchClientSecond;

    @Autowired
    private SecondBuilderUtil(SecondChannelCredentialsUtil secondChannelCredentialsUtil) {
        this.secondChannelCredentialsUtil = secondChannelCredentialsUtil;
        this.credentialSecond = new OAuth2Credential("twitch", secondChannelCredentialsUtil.getSecondChannelToken());
        this.twitchClientSecond =
                TwitchClientBuilder.builder()
                        .withEnableChat(true)
                        .withChatAccount(credentialSecond)
                        .withEnableHelix(true)
                        .withEnablePubSub(true)
                        .withDefaultEventHandler(SimpleEventHandler.class)
                        .build();
    }

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

