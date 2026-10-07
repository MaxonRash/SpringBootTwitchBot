package com.bot.springboottwitchbot.connections.channels.builder_utils;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotBuilderUtil {

    private static final Logger log = LoggerFactory.getLogger(BotBuilderUtil.class);

    private final TestChannelCredentialsUtil testChannelCredentialsUtil;
    private final OAuth2Credential credentialBot;
    private final TwitchClient twitchClientBot;

    @Autowired
    private BotBuilderUtil(TestChannelCredentialsUtil testChannelCredentialsUtil) {
        this.testChannelCredentialsUtil = testChannelCredentialsUtil;
        this.credentialBot = new OAuth2Credential("twitch", testChannelCredentialsUtil.getBotToken());
        this.twitchClientBot =
                TwitchClientBuilder.builder()
                        .withEnableChat(true)
                        .withChatAccount(credentialBot)
                        .withEnableHelix(true)
                        .withEnablePubSub(true)
                        .withDefaultEventHandler(SimpleEventHandler.class)
                        .build();
    }

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

    /**
     * Sends a chat message via the bot account and logs it, so every outgoing bot message
     * (on whichever channel) is visible in the logs.
     */
    public void sendMessage(String channel, String message) {
        log.info("[SEND -> {}] {}", channel, message);
        com.bot.springboottwitchbot.utilities.ChatLog.out(channel, message);
        twitchClientBot.getChat().sendMessage(channel, message);
    }

}
