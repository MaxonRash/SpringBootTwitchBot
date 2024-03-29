package com.bot.springboottwitchbot.connections;

import com.bot.springboottwitchbot.ApplicationContextProvider;
import com.bot.springboottwitchbot.channels.TestChannel;
import com.bot.springboottwitchbot.event_handlers.EventHandlerBot;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.stereotype.Component;
@Component
public interface ChannelConnection {
//    private final OAuth2Credential credentialBot = new OAuth2Credential("twitch", testChannelToken);
//    private final TwitchClient twitchClientBot =
//            TwitchClientBuilder.builder()
//                    .withEnableChat(true)
//                    .withChatAccount(credentialBot)
//                    .withEnableHelix(true)
//                    .withEnablePubSub(true)
//                    .withDefaultEventHandler(SimpleEventHandler.class)
//                    .build();

    public void Run(); /*{
        twitchClientBot.getChat().joinChannel(testChannelName);
//        twitchClientBot.getChat().joinChannel( ApplicationContextProvider.getApplicationContext().getBean(TestChannel.class).getTestChannelName());
        EventManager eventManagerBot = twitchClientBot.getEventManager();
        EventHandlerBot eventHandlerBot = new EventHandlerBot();
        eventManagerBot.getEventHandler(SimpleEventHandler.class).registerListener(eventHandlerBot);
    }*/


}
