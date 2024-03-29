package com.bot.springboottwitchbot.channels;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.channels.builder_utils.BotBuilderUtil;
import com.bot.springboottwitchbot.connections.ChannelConnection;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:twitchCredentials.properties")
public class TestChannel implements ChannelConnection {
//    @Value("${bot_channel_name}")
//    private String testChannelName;
//    @Value("${bot_channel_accessToken}")
//    private String testChannelToken;
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

//    public String getTestChannelName() {
//        return testChannelName;
//    }
//
//    public String getTestChannelToken() {
//        return testChannelToken;
//    }

    /*public OAuth2Credential getCredentialBot() {
        return credentialBot;
    }

    public TwitchClient getTwitchClientBot() {
        return twitchClientBot;
    }*/

    @Override
    public void Run() {
//        twitchClientBot.getChat().joinChannel(getTestChannelName());
        ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getChat().joinChannel(
                ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTestChannelName());
        EventManager eventManagerBot = ApplicationContextProvider.getApplicationContext().getBean(BotBuilderUtil.class).getTwitchClientBot().getEventManager();
        EventHandlerBot eventHandlerBot = new EventHandlerBot();
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerBot);
    }
}
