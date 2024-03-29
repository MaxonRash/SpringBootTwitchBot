package com.bot.springboottwitchbot.connection_runners;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.channels.TestChannel;
import com.bot.springboottwitchbot.connections.ChannelConnection;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BotConnectionRunner {
//    private final OAuth2Credential credentialBot = new OAuth2Credential("twitch",
//            ApplicationContextProvider.getApplicationContext().getBean(TestChannel.class).getTestChannelToken());
//    private final TwitchClient twitchClientBot =
//            TwitchClientBuilder.builder()
//                    .withEnableChat(true)
//                    .withChatAccount(credentialBot)
//                    .withEnableHelix(true)
//                    .withEnablePubSub(true)
//                    .withDefaultEventHandler(SimpleEventHandler.class)
//                    .build();

    private ChannelConnection channelConnection;
    @Autowired
    public BotConnectionRunner(@Qualifier("testChannel") ChannelConnection channelConnection) {
        this.channelConnection = channelConnection;
    }

    public ChannelConnection getChannelConnection() {
        return channelConnection;
    }
}
