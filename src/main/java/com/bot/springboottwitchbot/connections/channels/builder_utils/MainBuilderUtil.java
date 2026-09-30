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
public class MainBuilderUtil {
    private static final Logger log = LoggerFactory.getLogger(MainBuilderUtil.class);

    private final MainChannelCredentialsUtil mainChannelCredentialsUtil;
    private final OAuth2Credential credentialMain;
    private final TwitchClient twitchClientMain;

    @Autowired
    private MainBuilderUtil(MainChannelCredentialsUtil mainChannelCredentialsUtil) {
        this.mainChannelCredentialsUtil = mainChannelCredentialsUtil;
        this.credentialMain = new OAuth2Credential("twitch", mainChannelCredentialsUtil.getMainToken());
        this.twitchClientMain =
                TwitchClientBuilder.builder()
                        .withEnableChat(true)
                        .withChatAccount(credentialMain)
                        .withEnableHelix(true)
                        .withEnablePubSub(true)
                        .withDefaultEventHandler(SimpleEventHandler.class)
                        .build();
    }

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

    /**
     * Sends a chat message via the main account and logs it. Rarely used — most bot output
     * goes through {@link BotBuilderUtil#sendMessage(String, String)}.
     */
    public void sendMessage(String channel, String message) {
        log.info("[SEND (main acct) -> {}] {}", channel, message);
        twitchClientMain.getChat().sendMessage(channel, message);
    }
}
